package web.mvc.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.dto.*;
import web.mvc.repository.ProductRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Configuration
@WebListener
public class ApiDataCofig implements ServletContextListener {

    @Autowired
    private ProductRepository productRepository;

    private List<row> dataList = new ArrayList<>();


    @Override
    public void contextInitialized(ServletContextEvent e) {
        List<GarakTotalCost> dto=null;
        ServletContext app=e.getServletContext();
        try {
            dto= calcGarakAvg();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        app.setAttribute("garakData",dto);
    }


    public GarakStructList Test(String start, String end) throws IOException {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strYesterdayDate = yesterday.format(formatter);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode("5a776b4843776f6436364e49554347", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("GarakAuctionRslt", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(start, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(end, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("%20/"+strYesterdayDate, "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // JSON 파싱
        try {
            JSONObject jsonObject = new JSONObject(new JSONTokener(sb.toString()));
            JSONObject response = jsonObject.optJSONObject("GarakAuctionRslt");

            if (response != null) {
                GarakAuctionRslt dto = new GarakAuctionRslt();
                dto.setList_total_count(response.optInt("list_total_count", 0));

                JSONObject resultObject = response.optJSONObject("RESULT");
                if (resultObject != null) {
                    RESULT result = new RESULT();
                    result.setCODE(resultObject.optString("CODE", ""));
                    result.setMESSAGE(resultObject.optString("MESSAGE", ""));
                    dto.setRESULT(result);
                }

                JSONArray rows = response.optJSONArray("row");
                Set<String> strSet= new HashSet<>();

                if (rows != null) {
                    List<row> rowList = new ArrayList<>();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject rowObject = rows.optJSONObject(i);
                        if (rowObject != null) {
                            row rowItem = new row();
                            rowItem.setPUM_NAME(rowObject.optString("PUM_NAME", ""));
                            strSet.add(rowObject.optString("PUM_NAME", ""));
                            double kg= Double.parseDouble(rowObject.optString("UUN", "").split("k")[0]);
                            rowItem.setUUN(kg);

                            rowItem.setDDD(rowObject.optString("DDD", ""));
                            rowItem.setPPRICE(rowObject.optLong("PPRICE", 1000));
                            rowItem.setSSANGI(rowObject.optString("SSANGI", ""));
                            rowItem.setINJUNG_GUBUN(rowObject.optString("INJUNG_GUBUN", ""));
                            rowItem.setADJ_DT(rowObject.optString("ADJ_DT", ""));
                            rowList.add(rowItem);
                        }
                    }
                    dto.setRow(rowList);

                }

                GarakStructList list=new GarakStructList(strSet,dto);
                return list;
            } else {
                System.out.println("GarakAuctionRslt 데이터가 없습니다.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("JSON 데이터 파싱 중 오류가 발생했습니다.");
        }
        return null;
        }

    public GarakAvgPrice calcGarak() throws Exception{

        GarakStructList dto= Test("1", "1000");
        List<row> rList= new ArrayList<>();

        Set<String>nameList= dto.getGarakNameList();
        rList=dto.getGarakAuctionRslt().getRow();


        if(dto!=null){
            int num = dto.getGarakAuctionRslt().getList_total_count()/1000;
            int num2 =  dto.getGarakAuctionRslt().getList_total_count()%1000;

            if(num<2 && num2>1){
                dto=Test("1001",(1000+num2)+"");
                for(row r:dto.getGarakAuctionRslt().getRow()){
                    nameList.add(r.getPUM_NAME());
                    rList.add(r);
                }

            }
            else if(num>=2){
                for(int i=1; i<num; i++){
                    dto=Test(((i*1000)+1)+"",((i+1)*1000)+"");
                    for(row r:dto.getGarakAuctionRslt().getRow()){
                        nameList.add(r.getPUM_NAME());
                        rList.add(r);
                    }

                }
            }
        }//if-end
        System.out.println(nameList.toString());
        return new GarakAvgPrice(nameList,rList);
    }//calcGarak-end

    public int findSet(String name){
        List<String> mealSet= Arrays.asList(new String[]{"감자","고구마","버섯","호박","벼","보리","조"});
        List<String> yupVeg= Arrays.asList(new String[]{"부추","갓","기타엽체","고추","피망","오이","쑥갓","시금치","깻잎","상추","배추","아욱","적채"});
        List<String> gwaSet= Arrays.asList(new String[]{"모과","토마토","수박","파인애플","딸기"});
        List<String> gunSet=Arrays.asList(new String[]{"양파","파","무","당근","비트","생강","마늘"});
        List<String> yangSet=Arrays.asList(new String[]{"브로커리","파세리","세러리","기타양채","양상추","양배추","빈스","칼라후라워"});
        List<String> fruitSet=Arrays.asList(new String[]{"대추","감","아보카도","감귤","포도","사과","블루베리",
                "람부탄","메론","오렌지","배","복숭아","레몬","기타과일","바나나","곶감","용과","유자","키위"});

        if(mealSet.contains(name)){
         return 1;
        }else if(yupVeg.contains(name)){
            return 2;
        } else if (gwaSet.contains(name)) {
            return 3;
        } else if (gunSet.contains(name)) {
            return 4;
        } else if (yangSet.contains(name)) {
            return 5;
        } else if (fruitSet.contains(name)) {
            return 6;
        }

        return 7;
    }

    public List<GarakTotalCost> calcGarakAvg() throws Exception {
        GarakAvgPrice gp=calcGarak();
        List<row> rList= gp.getRowList();
        Set<String> nameList= gp.getNameList();
        List<GarakDTO> garakDTOList= new ArrayList<>();


        for(String name:nameList){
            int cat = findSet(name);

            garakDTOList.add(new GarakDTO(name,0,0,"일반","특(1등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","상(2등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","중(3등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","4등",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","5등",cat));
            //garakDTOList.add(new GarakDTO(name,0,0,"일반","6등",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","9등(등외)",cat));

            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","특(1등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","상(2등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","중(3등)",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","4등",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","5등",cat));
            //garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","6등",cat));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","9등(등외)",cat));

        }

        for(row r:rList){
            for (GarakDTO g:garakDTOList){
                if(r.getPUM_NAME().equals(g.getGarak_name())
                        &&r.getDDD().equals(g.getGarak_grade())
                        &&r.getINJUNG_GUBUN().equals(g.getGarak_type())){
                    g.setGarak_price((g.getGarak_price()+(r.getPPRICE()/r.getUUN())));
                    g.setGarak_count(g.getGarak_count()+1);
                    break;
                }
            }//inner -for-end
        }//outer -for-end
        InsertProduct(garakDTOList);
        return InsertProduct(garakDTOList);
    }//calcGarakAvg-end

    @Transactional
    public List<GarakTotalCost> InsertProduct(List<GarakDTO> garakDTOList) {
        List<GarakTotalCost> garakTotalList = new ArrayList<>();
        List<Product> ProductList = new ArrayList<>();
        List<String> productNameList=new ArrayList<>();

        ProductList=productRepository.findAll();
        for (Product p:ProductList ){
            productNameList.add(p.getProductName());
        }

        for (int i=0; i<garakDTOList.size();i+=12) {
            GarakDTO dto = garakDTOList.get(i);
            // Product 저장
            if(!productNameList.contains(dto.getGarak_name())){
                productRepository.save(new Product(
                        dto.getGarak_name(),
                        new ProductCategory(dto.getGarak_category())
                ));
            }

            if (dto.getGarak_count() > 0) {
                garakTotalList.add(
                        new GarakTotalCost(
                                dto.getGarak_name(),
                                (int) (dto.getGarak_price() / dto.getGarak_count()),
                                dto.getGarak_type(),
                                dto.getGarak_grade(),
                                dto.getGarak_category()
                        )
                );
            } else {
                garakTotalList.add(
                        new GarakTotalCost(
                                dto.getGarak_name(),
                                0,
                                dto.getGarak_type(),
                                dto.getGarak_grade(),
                                dto.getGarak_category()
                        )
                );
            }
        }
        return garakTotalList;
    }
}
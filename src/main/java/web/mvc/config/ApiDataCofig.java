package web.mvc.config;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.context.annotation.Configuration;
import web.mvc.dto.*;

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
public class ApiDataCofig {

    private List<row> dataList = new ArrayList<>();

    public static GarakStructList Test(String start, String end) throws IOException {

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
                            System.out.println(rowItem.toString());
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

    public static GarakAvgPrice calcGarak() throws Exception{

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
        return new GarakAvgPrice(nameList,rList);
    }//calcGarak-end

    public static List<GarakDTO> calcGarakAvg() throws Exception {
        GarakAvgPrice gp=calcGarak();
        List<row> rList= gp.getRowList();
        Set<String> nameList= gp.getNameList();
        List<GarakDTO> garakDTOList= new ArrayList<>();

        for(String name:nameList){
            garakDTOList.add(new GarakDTO(name,0,0,"일반","특(1등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","상(2등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","중(3등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","4등"));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","5등"));
            //garakDTOList.add(new GarakDTO(name,0,0,"일반","6등"));
            garakDTOList.add(new GarakDTO(name,0,0,"일반","9등(등외)"));

            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","특(1등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","상(2등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","중(3등)"));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","4등"));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","5등"));
            //garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","6등"));
            garakDTOList.add(new GarakDTO(name,0,0,"우수농산물","9등(등외)"));

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
        return garakDTOList;
    }//calcGarakAvg-end
}



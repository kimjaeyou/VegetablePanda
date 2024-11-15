package web.mvc.config;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import web.mvc.dto.GarakAuctionRslt;
import web.mvc.dto.RESULT;
import web.mvc.dto.row;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
public class ApiDataCofig {

    private List<row> dataList = new ArrayList<>();

    public static GarakAuctionRslt Test(String start, String end) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode("5a776b4843776f6436364e49554347", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("GarakAuctionRslt", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(start, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(end, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("%20/20241101", "UTF-8"));
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
        System.out.println(sb.toString());

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

                            rowItem.setUUN(rowObject.optString("UUN", ""));
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

                // dto에 파싱된 데이터가 들어간 상태
                System.out.println(dto);
                System.out.println(dto.getRow());
                System.out.println("sys:::::::::::::"+strSet.toString());


                return dto;
            } else {
                System.out.println("GarakAuctionRslt 데이터가 없습니다.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("JSON 데이터 파싱 중 오류가 발생했습니다.");
        }
        return null;
        }

    public static void calcGarak() throws Exception{

        GarakAuctionRslt dto= Test("1", "1000");


        if(dto!=null){
            int num = dto.getList_total_count()/1000;
            int num2 =  dto.getList_total_count()%1000;
            if(num<2 && num2>1){
                Test("1001",num2+"");
            }
            else if(num>=2){

            }
        }
    }
}



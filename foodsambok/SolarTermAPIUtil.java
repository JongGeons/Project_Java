package pub2504.foodsambok;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class SolarTermAPIUtil {

    // 실제 발급받은 서비스 키 (이미 인코딩된 상태)
    private static final String SERVICE_KEY = "AZF6RMCP5b6qU2ptwa0x%2B0j2D%2Bj4lpkg0%2FHDeiKTs%2B9J2pRrLLMbm9QFAByrNoVqdMsdU%2BpYx3wt4sRn1PZoGw%3D%3D";
    private static final String API_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/get24DivisionsInfo";

    /**
     * 한국천문연구원_절기 정보 API에서 특정 연도의 절기 정보를 가져옵니다.
     * @param year 조회할 연도
     * @return 절기명과 날짜 매핑 (예: {"하지" = 2025-07-15})
     */
    public static Map<String, LocalDate> getSolarTerms(int year) {
        Map<String, LocalDate> solarTerms = new HashMap<>();

        try {
            // URL 구성 (SERVICE_KEY는 인코딩된 상태로 그대로 사용)
            String urlStr = API_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&solYear=" + year +
                    "&pageNo=1" +
                    "&numOfRows=30";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            // 응답 받기
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
                            ? conn.getInputStream()
                            : conn.getErrorStream(), "UTF-8")
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            // 응답 출력 (디버깅 용도)
          /*  System.out.println("▶ API 응답 원문:\n" + sb.toString()) */

            // XML → JSON 변환
            JSONObject jsonResponse = XML.toJSONObject(sb.toString());

            JSONObject response = jsonResponse.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");

            if (items.has("item")) {
                JSONArray itemArray;
                Object itemObj = items.get("item");

                if (itemObj instanceof JSONObject) {
                    itemArray = new JSONArray();
                    itemArray.put((JSONObject) itemObj);
                } else {
                    itemArray = (JSONArray) itemObj;
                }

                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject item = itemArray.getJSONObject(i);
                    String dateName = item.getString("dateName");
                    String solLocdate = item.get("locdate").toString();  // 수정된 부분

                    // 하지 또는 입추만 수집
                    if (dateName.equals("하지") || dateName.equals("입추")) {
                        LocalDate date = LocalDate.of(
                                Integer.parseInt(solLocdate.substring(0, 4)),
                                Integer.parseInt(solLocdate.substring(4, 6)),
                                Integer.parseInt(solLocdate.substring(6, 8))
                        );
                        solarTerms.put(dateName, date);
                    }
                }
            } else {
                System.err.println("▶ items 항목이 존재하지 않음. 응답 확인 필요.");
            }

        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return solarTerms;
    }
}

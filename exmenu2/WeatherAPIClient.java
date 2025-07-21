package exmenu2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class WeatherAPIClient {

    public static class WeatherInfo {
        public double temperature;
        public int rainProbability;

        public WeatherInfo(double temperature, int rainProbability) {
            this.temperature = temperature;
            this.rainProbability = rainProbability;
        }
    }

    public static WeatherInfo getWeatherInfo(String nx, String ny, String serviceKey) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        
        // 최근 5시간의 데이터를 확인
        for (int i = 0; i < 5; i++) {
            LocalDateTime targetTime = now.minusHours(i);
            String baseDate = targetTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = getBaseTime(targetTime.getHour());

            // API 호출 URL
            String urlBuilder = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
                    "?serviceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) +
                    "&numOfRows=100" +
                    "&pageNo=1" +
                    "&dataType=JSON" +
                    "&base_date=" + baseDate +
                    "&base_time=" + baseTime +
                    "&nx=" + nx +
                    "&ny=" + ny;
            
            URL url = new URL(urlBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("API 호출 실패. 응답 코드: " + responseCode);
                continue; // 다음 시간대로 넘어감
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = rd.lines().collect(Collectors.joining());
            rd.close();
            conn.disconnect();

            try {
                JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                JsonObject response = jsonObject.getAsJsonObject("response");
                JsonObject header = response.getAsJsonObject("header");

                if (!"00".equals(header.get("resultCode").getAsString())) {
                    System.err.println("API 오류: " + header.get("resultMsg").getAsString() + " - 기준 시간: " + baseTime);
                    continue; // 다음 시간대로 넘어감
                }
                
                if (!response.has("body")) {
                     System.err.println("API 응답에 'body'가 없습니다. - 기준 시간: " + baseTime);
                     continue; // 다음 시간대로 넘어감
                }

                JsonObject body = response.getAsJsonObject("body");
                JsonObject items = body.getAsJsonObject("items");
                JsonArray item = items.getAsJsonArray("item");

                double temperature = 0.0;
                int rainProbability = 0;

                for (JsonElement element : item) {
                    JsonObject obj = element.getAsJsonObject();
                    String category = obj.get("category").getAsString();
                    
                    if ("TMP".equals(category)) {
                        temperature = obj.get("fcstValue").getAsDouble();
                    } else if ("POP".equals(category)) {
                        rainProbability = obj.get("fcstValue").getAsInt();
                    }
                }

                System.out.println("날씨 정보를 성공적으로 가져왔습니다. 기준 시간: " + baseTime);
                return new WeatherInfo(temperature, rainProbability);

            } catch (JsonSyntaxException | NullPointerException e) {
                System.err.println("JSON 파싱 오류: " + e.getMessage());
                System.err.println("API 응답 내용: " + result);
                continue; // 다음 시간대로 넘어감
            }
        }
        
        // 5번의 시도에도 데이터를 찾지 못한 경우
        throw new RuntimeException("최근 5시간 동안 유효한 날씨 데이터를 찾을 수 없습니다.");
    }

    private static String getBaseTime(int hour) {
        if (hour < 2 || hour >= 23) {
            return "2300";
        } else {
            return String.format("%02d00", hour - 1);
        }
    }
}
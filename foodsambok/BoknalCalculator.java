package pub2504.foodsambok;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BoknalCalculator {

    /**
     * 특정 연도의 초복 날짜를 계산하여 반환합니다.
     * 하지를 기준으로 세 번째 경일
     * @param year 계산할 연도
     * @return 초복 날짜 (LocalDate), 절기 정보를 가져오지 못하면 null
     */
    public static LocalDate getChobok(int year) {
        Map<String, LocalDate> solarTerms = SolarTermAPIUtil.getSolarTerms(year);
        LocalDate haji = solarTerms.get("하지");
        
        if (haji == null) {
            System.err.println(year + "년 하지 정보를 가져오지 못했습니다. API 응답 확인 또는 서비스 키를 확인하세요.");
            return null; // 절기 데이터가 없는 경우
        }

        LocalDate currentDate = haji;
        int gyeongilCount = 0;

        // 하지 이후부터 경일을 찾습니다.
        while (gyeongilCount < 3) { // 세 번째 경일
            if (GanjiUtil.isGyeongil(currentDate)) {
                gyeongilCount++;
            }
            if (gyeongilCount < 3) { // 마지막 경일은 증가시키지 않음
                currentDate = currentDate.plusDays(1);
            }
        }
        return currentDate;
    }

    /**
     * 특정 연도의 중복 날짜를 계산하여 반환합니다.
     * 하지를 기준으로 네 번째 경일 (또는 윤달이 끼면 다섯 번째 경일)
     * 이 로직은 하지 이후 네 번째 경일을 찾습니다. 윤달로 인한 특이 사항은 고려되지 않았습니다.
     * @param year 계산할 연도
     * @return 중복 날짜 (LocalDate), 절기 정보를 가져오지 못하면 null
     */
    public static LocalDate getJungbok(int year) {
        Map<String, LocalDate> solarTerms = SolarTermAPIUtil.getSolarTerms(year);
        LocalDate haji = solarTerms.get("하지");

        if (haji == null) {
            System.err.println(year + "년 하지 정보를 가져오지 못했습니다. API 응답 확인 또는 서비스 키를 확인하세요.");
            return null;
        }

        LocalDate currentDate = haji;
        int gyeongilCount = 0;

        // 하지 이후부터 경일을 찾습니다.
        while (gyeongilCount < 4) { // 네 번째 경일
            if (GanjiUtil.isGyeongil(currentDate)) {
                gyeongilCount++;
            }
            if (gyeongilCount < 4) {
                currentDate = currentDate.plusDays(1);
            }
        }
        return currentDate;
    }
    
    

    /**
     * 특정 연도의 말복 날짜를 계산하여 반환합니다.
     * 입추를 기준으로 첫 번째 경일
     * @param year 계산할 연도
     * @return 말복 날짜 (LocalDate), 절기 정보를 가져오지 못하면 null
     */
    public static LocalDate getMalbok(int year) {
        Map<String, LocalDate> solarTerms = SolarTermAPIUtil.getSolarTerms(year);
        LocalDate ipchu = solarTerms.get("입추");

        if (ipchu == null) {
            System.err.println(year + "년 입추 정보를 가져오지 못했습니다. API 응답 확인 또는 서비스 키를 확인하세요.");
            return null;
        }

        LocalDate currentDate = ipchu;
        
        // 입추 당일이 경일인지 확인하고, 아니면 다음 날로 넘어갑니다.
        while (!GanjiUtil.isGyeongil(currentDate)) {
            currentDate = currentDate.plusDays(1);
        }
        return currentDate;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)");
        int currentYear = LocalDate.now().getYear(); // 현재 연도

        System.out.println("--- 올해 복날 (" + currentYear + "년) ---");
        LocalDate chobok = getChobok(currentYear);
        LocalDate jungbok = getJungbok(currentYear);
        LocalDate malbok = getMalbok(currentYear);

        if (chobok != null) {
            System.out.println("초복: " + chobok.format(formatter));
        }
        if (jungbok != null) {
            System.out.println("중복: " + jungbok.format(formatter));
        }
        if (malbok != null) {
            System.out.println("말복: " + malbok.format(formatter));
        }

        System.out.println("\n--- 내년 복날 (" + (currentYear + 1) + "년) ---");
        LocalDate chobokNextYear = getChobok(currentYear + 1);
        LocalDate jungbokNextYear = getJungbok(currentYear + 1);
        LocalDate malbokNextYear = getMalbok(currentYear + 1);
        
        if (chobokNextYear != null) {
            System.out.println("초복: " + chobokNextYear.format(formatter));
        }
        if (jungbokNextYear != null) {
            System.out.println("중복: " + jungbokNextYear.format(formatter));
        }
        if (malbokNextYear != null) {
            System.out.println("말복: " + malbokNextYear.format(formatter));
        }
    }
}
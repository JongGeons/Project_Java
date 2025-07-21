package pub2504.foodsambok;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class GanjiUtil {

    private static final String[] CHEONGAN = {"갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"};
    private static final String[] JIJI = {"자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"};

    // 기준 날짜 (1900년 1월 1일은 갑자일)
    private static final LocalDate BASE_DATE = LocalDate.of(1900, 1, 1);
    private static final int BASE_CHEONGAN_INDEX = 0; // 갑
    private static final int BASE_JIJI_INDEX = 0;    // 자

    /**
     * 특정 날짜의 천간을 반환합니다.
     * @param date 기준 날짜
     * @return 천간 문자열 (예: "갑", "경")
     */
    public static String getCheongan(LocalDate date) {
        long daysDiff = ChronoUnit.DAYS.between(BASE_DATE, date);
        int cheonganIndex = (int) ((BASE_CHEONGAN_INDEX + daysDiff) % 10);
        return CHEONGAN[cheonganIndex];
    }

    /**
     * 특정 날짜의 지지를 반환합니다.
     * @param date 기준 날짜
     * @return 지지 문자열 (예: "자", "오")
     */
    public static String getJiji(LocalDate date) {
        long daysDiff = ChronoUnit.DAYS.between(BASE_DATE, date);
        int jijiIndex = (int) ((BASE_JIJI_INDEX + daysDiff) % 12);
        return JIJI[jijiIndex];
    }

    /**
     * 특정 날짜가 경일(庚日)인지 여부를 반환합니다.
     * @param date 확인할 날짜
     * @return 경일이면 true, 아니면 false
     */
    public static boolean isGyeongil(LocalDate date) {
        return "경".equals(getCheongan(date));
    }
}
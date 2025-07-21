package exmenu2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class WeatherController {

    private WeatherGUI gui;

    private static final String SERVICE_KEY = "AZF6RMCP5b6qU2ptwa0x+0j2D+j4lpkg0/HDeiKTs+9J2pRrLLMbm9QFAByrNoVqdMsdU+pYx3wt4sRn1PZoGw==";
    private static final String NX = "63";
    private static final String NY = "125";
    private static final String IMAGE_PATH = "C:/Users/st042/OneDrive/바탕 화면/날씨사진/";

    public WeatherController(WeatherGUI gui) {
        this.gui = gui;
        setupEvents();
    }

    private void setupEvents() {
        gui.getRefreshBtn().addActionListener(e -> {
            try {
                WeatherAPIClient.WeatherInfo weather = WeatherAPIClient.getWeatherInfo(NX, NY, SERVICE_KEY);

                if (weather == null) {
                    JOptionPane.showMessageDialog(gui, "날씨 정보가 아직 업데이트되지 않았습니다.\n잠시 후 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    // 모든 라벨과 이미지를 초기화
                    gui.getDateLabel().setText("");
                    gui.getTempLabel().setText("온도: ");
                    gui.getRainLabel().setText("강수확률: ");
                    gui.getPhotoLabel().setIcon(null);
                    gui.getPhotoLabel().setText("날씨 사진 없음");
                    return; // 메서드 종료
                }

                // 날짜 설정
                gui.getDateLabel().setText("날짜: " + LocalDate.now());

                // 온도 및 강수확률 설정
                gui.getTempLabel().setText("온도: " + weather.temperature + "°C");
                gui.getRainLabel().setText("강수확률: " + weather.rainProbability + "%");

                // 이미지 파일명 결정
                String filename;
                if (weather.rainProbability >= 70) {
                    filename = "비.jpg";
                } else if (weather.temperature >= 30) {
                    filename = "맑음.jpg";
                } else {
                    filename = "구름.jpg";
                }

                // 이미지 파일 존재 여부 확인 및 적용
                File imageFile = new File(IMAGE_PATH + filename);
                if (imageFile.exists()) {
                    ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(400, 240, Image.SCALE_SMOOTH);
                    gui.getPhotoLabel().setIcon(new ImageIcon(img));
                    gui.getPhotoLabel().setText("");
                } else {
                    JOptionPane.showMessageDialog(gui, "이미지 파일이 존재하지 않습니다:\n" + imageFile.getAbsolutePath(), "에러", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(gui, "날씨 정보를 불러오는 중 오류 발생: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
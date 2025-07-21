package exmenu2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherGUI extends JFrame {

    private JLabel dateLabel;
    private JLabel photoLabel;
    private JLabel tempLabel;
    private JLabel rainLabel;
    private JButton refreshBtn;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.E요일 HH:mm:ss");

    public WeatherGUI() {
        setTitle("날씨 정보 프로그램");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("메뉴", new JPanel());
        tabbedPane.addTab("날씨", createWeatherPanel());
        tabbedPane.addTab("추천", new JPanel());
        tabbedPane.addTab("메뉴추가", new JPanel());

        add(tabbedPane);

        startDateTimeUpdate();
    }

    private JPanel createWeatherPanel() {
        // 이 패널은 GridBagLayout을 사용하여 내부 컨텐츠를 중앙에 배치합니다.
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        // 날씨 정보를 담을 컨테이너 패널 (세로 정렬)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // 날짜 패널
        JPanel datePanel = new JPanel();
        datePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "날짜", TitledBorder.LEFT, TitledBorder.TOP));
        datePanel.setPreferredSize(new Dimension(400, 55));
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        datePanel.add(dateLabel);
        datePanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // 사진 패널
        JPanel photoPanel = new JPanel();
        photoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "날씨 사진", TitledBorder.LEFT, TitledBorder.TOP));
        photoPanel.setPreferredSize(new Dimension(400, 200)); // 기존 크기 유지
        photoLabel = new JLabel("날씨 사진 없음", SwingConstants.CENTER);
        // photoLabel.setPreferredSize(new Dimension(380, 180)); // 이 부분 제거
        photoPanel.add(photoLabel);
        photoPanel.setLayout(new BorderLayout()); // BorderLayout으로 변경
        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // 온도 패널
        JPanel tempPanel = new JPanel();
        tempPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "온도", TitledBorder.LEFT, TitledBorder.TOP));
        tempPanel.setPreferredSize(new Dimension(400, 50));
        tempLabel = new JLabel("온도: ", SwingConstants.CENTER);
        tempLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        tempPanel.add(tempLabel);
        tempPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // 강수량 패널
        JPanel rainPanel = new JPanel();
        rainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "강수확률", TitledBorder.LEFT, TitledBorder.TOP));
        rainPanel.setPreferredSize(new Dimension(400, 50));
        rainLabel = new JLabel("강수확률: ", SwingConstants.CENTER);
        rainLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        rainPanel.add(rainLabel);
        rainPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // 새로고침 버튼
        refreshBtn = new JButton("날씨 불러오기");
        refreshBtn.setMaximumSize(new Dimension(200, 50));
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // contentPanel에 컴포넌트 추가
        contentPanel.add(datePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(photoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(tempPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(rainPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(refreshBtn);

        // contentPanel을 mainPanel의 정중앙에 배치
        mainPanel.add(contentPanel); 
        
        return mainPanel;
    }

    private void startDateTimeUpdate() {
        Timer timer = new Timer(1000, e -> {
            String now = sdf.format(new Date());
            dateLabel.setText(now);
        });
        timer.start();
    }

    public void showGUI() {
        setVisible(true);
    }

    // Getters
    public JLabel getDateLabel() { return dateLabel; }
    public JLabel getPhotoLabel() { return photoLabel; }
    public JLabel getTempLabel() { return tempLabel; }
    public JLabel getRainLabel() { return rainLabel; }
    public JButton getRefreshBtn() { return refreshBtn; }
}
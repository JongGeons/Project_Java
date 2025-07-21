package exmenu2;

public class WeatherMain {
    public static void main(String[] args) {
        WeatherGUI gui = new WeatherGUI();
        WeatherController controller = new WeatherController(gui);
        gui.showGUI();
    }
}
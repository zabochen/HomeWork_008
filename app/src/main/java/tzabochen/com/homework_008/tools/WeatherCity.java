package tzabochen.com.homework_008.tools;

public class WeatherCity
{
    private static final String START_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";

    // List of city ID - http://bulk.openweathermap.org/sample/
    public static String city = "Cherkasy";

    // Kelvin is used by default, Fahrenheit use units=imperial, Celsius use units=metric
    private static final String UNIT_FORMAT = "&units=metric";

    private static final String API_ID = "&appid=03385a242c9e1c316b7c9fd1e70679c8";

    public static String getURL()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(START_URL);
        stringBuilder.append(city);
        stringBuilder.append(UNIT_FORMAT);
        stringBuilder.append(API_ID);

        return stringBuilder.toString();
    }
}

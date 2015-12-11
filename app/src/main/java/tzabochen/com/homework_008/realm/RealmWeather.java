package tzabochen.com.homework_008.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmWeather extends RealmObject
{
    @PrimaryKey

    // DATE
    private long dt;
    private String dtTxt;

    // TEMPERATURE
    private double temp;
    private double tempMin;
    private double tempMax;

    // ICON
    private String icon;
    private String iconDescription;

    // WIND SPEED
    private double windSpeed;

    // CLOUDINESS
    private double clouds;

    // HUMIDITY
    private double humidity;

    // PRESSURE
    private double pressure;


    // GET/SET

    public long getDt()
    {
        return dt;
    }

    public void setDt(long dt)
    {
        this.dt = dt;
    }

    public String getDtTxt()
    {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt)
    {
        this.dtTxt = dtTxt;
    }

    public double getTemp()
    {
        return temp;
    }

    public void setTemp(double temp)
    {
        this.temp = temp;
    }

    public double getTempMin()
    {
        return tempMin;
    }

    public void setTempMin(double tempMin)
    {
        this.tempMin = tempMin;
    }

    public double getTempMax()
    {
        return tempMax;
    }

    public void setTempMax(double tempMax)
    {
        this.tempMax = tempMax;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIconDescription()
    {
        return iconDescription;
    }

    public void setIconDescription(String iconDescription)
    {
        this.iconDescription = iconDescription;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public double getClouds()
    {
        return clouds;
    }

    public void setClouds(double clouds)
    {
        this.clouds = clouds;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(double humidity)
    {
        this.humidity = humidity;
    }

    public double getPressure()
    {
        return pressure;
    }

    public void setPressure(double pressure)
    {
        this.pressure = pressure;
    }
}
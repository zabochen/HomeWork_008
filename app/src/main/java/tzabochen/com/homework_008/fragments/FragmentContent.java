package tzabochen.com.homework_008.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;
import tzabochen.com.homework_008.R;
import tzabochen.com.homework_008.realm.RealmWeather;
import tzabochen.com.homework_008.tools.WeatherDate;

public class FragmentContent extends Fragment
{
    // VALUE'S
    private Realm realm;

    public static FragmentContent newInstance(int itemPosition)
    {
        Bundle args = new Bundle();
        args.putInt("itemPosition", itemPosition);
        FragmentContent fragmentContent = new FragmentContent();
        fragmentContent.setArguments(args);
        return fragmentContent;
    }

    public int getPosition()
    {
        return getArguments().getInt("itemPosition", 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentContentView = inflater.inflate(R.layout.fragment_content, container, false);

        // REALM INSTANCE
        realm = Realm.getInstance(getContext());
        RealmResults<RealmWeather> realmResults = realm.where(RealmWeather.class).findAll();
        RealmWeather selectedRealmWeather = realmResults.get(getPosition());

        // DAY & MONTH
        TextView dayMonth = (TextView) fragmentContentView.findViewById(R.id.item_day_month);
        WeatherDate weatherDate = new WeatherDate(selectedRealmWeather.getDtTxt());
        StringBuilder sbDayMonth = new StringBuilder();
        sbDayMonth.append(weatherDate.getDayOfMonth()).append(" ")
                .append(weatherDate.getMonth()).append(", ")
                .append(weatherDate.getDayOfWeek());
        dayMonth.setText(sbDayMonth.toString());

        // ICON
        ImageView imageView = (ImageView) fragmentContentView.findViewById(R.id.item_icon);
        StringBuilder sbIcon = new StringBuilder();
        sbIcon.append("http://openweathermap.org/img/w/").append(selectedRealmWeather.getIcon()).append(".png");
        Picasso.with(getActivity()).load(sbIcon.toString()).into(imageView);

        // ICON DESCRIPTION
        TextView iconDescription = (TextView) fragmentContentView.findViewById(R.id.item_icon_description);
        StringBuilder sbIconDescription = new StringBuilder();
        sbIconDescription.append(weatherDate.getTime()).append(", ").append(selectedRealmWeather.getIconDescription());
        iconDescription.setText(sbIconDescription.toString());

        // TEMP MIN
        TextView tempMin = (TextView) fragmentContentView.findViewById(R.id.item_temp_min);
        StringBuilder sbTempMin = new StringBuilder();
        sbTempMin.append("Temp Min: ").append(String.valueOf((int) selectedRealmWeather.getTempMin())).append("°C");
        tempMin.setText(sbTempMin.toString());

        // TEMP MAX
        TextView tempMax = (TextView) fragmentContentView.findViewById(R.id.item_temp_max);
        StringBuilder sbTempMax = new StringBuilder();
        sbTempMax.append("Temp Max: ").append(String.valueOf((int) selectedRealmWeather.getTempMax())).append("°C");
        tempMax.setText(sbTempMax.toString());

        // WIND SPEED
        TextView windSpeed = (TextView) fragmentContentView.findViewById(R.id.item_wind_speed);
        StringBuilder sbWindSpeed = new StringBuilder();
        sbWindSpeed.append("Wind Speed: ").append((int) selectedRealmWeather.getWindSpeed()).append(" meter/sec");
        windSpeed.setText(sbWindSpeed.toString());

        // CLOUDS
        TextView cloudsAll = (TextView) fragmentContentView.findViewById(R.id.item_clouds_all);
        StringBuilder sbClouds = new StringBuilder();
        sbClouds.append("Cloudiness: ").append((int) selectedRealmWeather.getClouds()).append("%");
        cloudsAll.setText(sbClouds.toString());

        // HUMIDITY
        TextView humidity = (TextView) fragmentContentView.findViewById(R.id.item_humidity);
        StringBuilder sbHumidity = new StringBuilder();
        sbHumidity.append("Humidity: ").append((int) selectedRealmWeather.getHumidity()).append("%");
        humidity.setText(sbHumidity.toString());

        // PRESSURE
        TextView pressure = (TextView) fragmentContentView.findViewById(R.id.item_pressure);
        StringBuilder sbPressure = new StringBuilder();
        sbPressure.append("Pressure: ").append((int) selectedRealmWeather.getPressure()).append(" hPa");
        pressure.setText(sbPressure.toString());

        return fragmentContentView;
    }
}
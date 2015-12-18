package tzabochen.com.homework_008.realm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import tzabochen.com.homework_008.R;
import tzabochen.com.homework_008.tools.WeatherDate;

public class MyRealmBaseAdapter extends RealmBaseAdapter<RealmWeather>
{
    private Context context;

    public MyRealmBaseAdapter(Context context, RealmResults<RealmWeather> realmResults, boolean automaticUpdate)
    {
        super(context, realmResults, automaticUpdate);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_items, null);

            viewHolder = new ViewHolder();

            // DATE
            viewHolder.dayOfWeekAndMonth = (TextView) convertView.findViewById(R.id.list_day_of_week_and_month);
            viewHolder.month = (TextView) convertView.findViewById(R.id.list_month);

            // ICON
            viewHolder.weatherIcon = (ImageView) convertView.findViewById(R.id.list_icon);

            // TEMPERATURE
            viewHolder.temp = (TextView) convertView.findViewById(R.id.list_temp);

            // TIME
            viewHolder.time = (TextView) convertView.findViewById(R.id.list_time);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RealmWeather realmWeather = realmResults.get(position);

        if (realmWeather != null)
        {
            // DATE
            WeatherDate weatherDate = new WeatherDate(realmWeather.getDtTxt());

            StringBuilder getDayOfWeekAndMonth = new StringBuilder()
                    .append(weatherDate.getDayOfWeekShort())
                    .append(", ")
                    .append(weatherDate.getDayOfMonth());

            viewHolder.dayOfWeekAndMonth.setText(getDayOfWeekAndMonth.toString());
            viewHolder.month.setText(weatherDate.getMonth());

            // ICON
            StringBuilder getWeatherIcon = new StringBuilder()
                    .append("http://openweathermap.org/img/w/")
                    .append(realmWeather.getIcon())
                    .append(".png");

            Picasso.with(context).load(getWeatherIcon.toString()).into(viewHolder.weatherIcon);

            // TEMPERATURE
            double temp = realmWeather.getTemp();

            StringBuilder getTemp = new StringBuilder()
                    .append(String.valueOf((int) temp))
                    .append("°C");

            viewHolder.temp.setText(getTemp.toString());

            // TIME
            viewHolder.time.setText(weatherDate.getTime());
        }

        return convertView;
    }
}
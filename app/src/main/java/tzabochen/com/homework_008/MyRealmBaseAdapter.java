package tzabochen.com.homework_008;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class MyRealmBaseAdapter extends RealmBaseAdapter<RealmWeather> implements ListAdapter
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
            viewHolder.dayOfWeek = (TextView) convertView.findViewById(R.id.list_day_of_week);
            viewHolder.dayOfMonth = (TextView) convertView.findViewById(R.id.list_day_of_month);
            viewHolder.month = (TextView) convertView.findViewById(R.id.list_month);

            // ICON
            viewHolder.weatherIcon = (ImageView) convertView.findViewById(R.id.list_icon);

            // TEMPERATURE
            viewHolder.temp = (TextView) convertView.findViewById(R.id.list_temp);

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
            viewHolder.dayOfWeek.setText(weatherDate.getDayOfWeek());
            viewHolder.dayOfMonth.setText(weatherDate.getDayOfMonth());
            viewHolder.month.setText(weatherDate.getMonth());

            // TEMPERATURE
            double temp = realmWeather.getTemp();
            viewHolder.temp.setText(String.valueOf((int)temp) + "°C");

            // ICON
            String iconUrl = "http://openweathermap.org/img/w/" + realmWeather.getIcon() + ".png";
            Picasso.with(context).load(iconUrl).into(viewHolder.weatherIcon);
        }

        return convertView;
    }
}
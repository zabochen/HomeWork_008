package tzabochen.com.homework_008;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentItems extends ListFragment
{
    // VALUE'S
    private Realm realm;
    private ItemSelectedListener itemSelectedListener;
    private MyRealmBaseAdapter adapter;
    private static final String BASE_URL = WeatherCity.getURL();

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        itemSelectedListener = (ItemSelectedListener) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // REALM INSTANCE
        realm = Realm.getInstance(getContext());
        RealmResults<RealmWeather> realmResults = realm.where(RealmWeather.class).findAll();

        // ASYNC TASK -> LOAD & PARSE & ADD
        new GetWeatherDate().execute();

        // ADAPTER
        adapter = new MyRealmBaseAdapter(getActivity(), realmResults, true);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // SEND SELECTED POSITION
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        itemSelectedListener.itemSelected(position);
    }

    // ASYNC TASK
    public class GetWeatherDate extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            // VALUE'S
            Realm realm = Realm.getInstance(getContext());
            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try
            {
                // CONNECTION
                URL baseUrl = new URL(BASE_URL);
                connection = (HttpURLConnection) baseUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // BUFFER
                inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // GET JSON STRING
                StringBuilder stringBuilder = new StringBuilder();
                String getDataString;

                while ((getDataString = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(getDataString);
                }

                // PROCESSING JSON
                JSONObject rootJsonObject = new JSONObject(stringBuilder.toString());
                JSONArray rootJsonArray = rootJsonObject.getJSONArray("list");

                for (int i = 0; i < rootJsonArray.length(); i++)
                {
                    JSONObject weatherJsonObject = rootJsonArray.getJSONObject(i);
                    RealmWeather realmWeather = new RealmWeather();

                    // DATE
                    String dtTxt = weatherJsonObject.getString("dt_txt");
                    realmWeather.setDtTxt(dtTxt);

                    // TEMPERATURE
                    double temp = weatherJsonObject.getJSONObject("main").getDouble("temp");
                    realmWeather.setTemp(temp);

                    double tempMin = weatherJsonObject.getJSONObject("main").getDouble("temp_min");
                    realmWeather.setTempMin(tempMin);

                    double tempMax = weatherJsonObject.getJSONObject("main").getDouble("temp_max");
                    realmWeather.setTempMax(tempMax);

                    // ICON
                    String weatherIcon = weatherJsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                    realmWeather.setIcon(weatherIcon);

                    // ICON DESCRIPTION
                    String iconDescription = weatherJsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    realmWeather.setIconDescription(iconDescription);

                    // WIND SPEED
                    double windSpeed = weatherJsonObject.getJSONObject("wind").getDouble("speed");
                    realmWeather.setWindSpeed(windSpeed);

                    // CLOUDINESS
                    double clouds = weatherJsonObject.getJSONObject("clouds").getDouble("all");
                    realmWeather.setClouds(clouds);

                    // HUMIDITY
                    double humidity = weatherJsonObject.getJSONObject("main").getDouble("humidity");
                    realmWeather.setHumidity(humidity);

                    // PRESSURE
                    double pressure = weatherJsonObject.getJSONObject("main").getDouble("pressure");
                    realmWeather.setPressure(pressure);

                    // COMMIT
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(realmWeather);
                    realm.commitTransaction();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }

                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        // REFRESH
        @Override
        protected void onPostExecute(Void aVoid)
        {
            adapter.notifyDataSetChanged();
        }
    }
}
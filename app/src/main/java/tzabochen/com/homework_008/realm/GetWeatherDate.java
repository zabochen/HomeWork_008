package tzabochen.com.homework_008.realm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
import tzabochen.com.homework_008.fragments.FragmentItems;
import tzabochen.com.homework_008.tools.WeatherCity;

public class GetWeatherDate extends AsyncTask<Context, Void, Void>
{
    @Override
    protected Void doInBackground(Context... params)
    {
        // VALUE'S
        Realm realm = Realm.getInstance(params[0]);
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try
        {
            // GET CITY FROM PREFERENCES
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(params[0]);
            WeatherCity.city = sharedPreferences.getString("preferences_cities", "Cherkasy");

            // CONNECTION
            URL baseUrl = new URL(WeatherCity.getURL());
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
                long dt = weatherJsonObject.getLong("dt");
                realmWeather.setDt(dt);

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

            // CLEAR DATA
            realm.beginTransaction();
            RealmResults<RealmWeather> realmResults = realm.where(RealmWeather.class).findAll();
            realmResults.sort("dt");

            int dataSize = realmResults.size() - 40;

            for (int i = 0; i < dataSize; i++)
            {
                realmResults.remove(0);
            }

            realm.commitTransaction();
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
        FragmentItems.adapter.notifyDataSetChanged();
    }
}
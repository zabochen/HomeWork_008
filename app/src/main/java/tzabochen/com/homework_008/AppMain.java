package tzabochen.com.homework_008;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tzabochen.com.homework_008.fragments.ActivityContent;
import tzabochen.com.homework_008.fragments.FragmentContent;
import tzabochen.com.homework_008.preferences.ActivityPreferences;
import tzabochen.com.homework_008.realm.GetWeatherDate;
import tzabochen.com.homework_008.services.NotificationService;
import tzabochen.com.homework_008.tools.ItemSelectedListener;
import tzabochen.com.homework_008.tools.WeatherCity;

public class AppMain extends AppCompatActivity implements ItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    // VALUE'S
    private int itemPosition = 0;                       // ITEM SELECTED BY DEFAULT
    private boolean withContent = true;                 // SHOW TWO-PANE LAYOUT
    private SharedPreferences sharedPreferences;        // PREFERENCES

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // SERVICE
        if (savedInstanceState == null && connectionStatus())
        {
            startService(new Intent(this, NotificationService.class));
        }

        // PREFERENCES
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // CONNECTION
        if (!connectionStatus())
        {
            offlineModeMessage();
        }

        // TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(WeatherCity.city);
        }

        // LOAD SAVED ITEM POSITION
        if (savedInstanceState != null)
        {
            this.itemPosition = savedInstanceState.getInt("itemPosition");
        }

        // SEARCH FRAGMENT WITH CONTENT
        this.withContent = (findViewById(R.id.main_fragment_content) != null);

        if (withContent)
        {
            showContent(itemPosition);
        }
    }

    // SAVE ITEM POSITION
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("itemPosition", itemPosition);
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // GET ITEM POSITION WITH ITEM FRAGMENT
    @Override
    public void itemSelected(int position)
    {
        this.itemPosition = position;
        showContent(position);
    }

    private void showContent(int position)
    {
        if (withContent)
        {
            // TWO-PANE LAYOUT
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentContent fragmentContent = (FragmentContent) fragmentManager.findFragmentById(R.id.main_fragment_content);

            if (fragmentContent == null || fragmentContent.getPosition() != position)
            {
                fragmentContent = FragmentContent.newInstance(position);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_content, fragmentContent);
                fragmentTransaction.commit();
            }
        }
        else
        {
            // ONE-PANE LAYOUT
            startActivity(new Intent(this, ActivityContent.class).putExtra("itemPosition", position));
        }
    }

    // CONNECTION STATUS
    private boolean connectionStatus()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void offlineModeMessage()
    {
        Toast.makeText(this, R.string.offline_mode, Toast.LENGTH_SHORT).show();
    }

    // TOOLBAR MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.menu_item_preferences:
                Intent intent = new Intent(this, ActivityPreferences.class);
                startActivity(intent);
                break;

            case R.id.menu_item_refresh:
                new GetWeatherDate().execute(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals("preferences_updates"))
        {
            if (sharedPreferences.getBoolean("preferences_updates", true))
            {
                startService(new Intent(this, NotificationService.class));
            }
            else
            {
                stopService(new Intent(this, NotificationService.class));
            }
        }
    }
}
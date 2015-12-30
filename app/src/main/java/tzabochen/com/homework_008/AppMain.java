package tzabochen.com.homework_008;

import android.content.Intent;
import android.content.SharedPreferences;
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
import tzabochen.com.homework_008.realm.GetWeatherDateProgress;
import tzabochen.com.homework_008.receivers.NotificationReceiver;
import tzabochen.com.homework_008.services.NotificationService;
import tzabochen.com.homework_008.tools.ConnectionStatus;
import tzabochen.com.homework_008.tools.ItemSelectedListener;
import tzabochen.com.homework_008.tools.WeatherCity;

public class AppMain extends AppCompatActivity implements ItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{
    // VALUE'S
    private int itemPosition = 0;                       // ITEM SELECTED BY DEFAULT
    private boolean withContent = true;                 // SHOW TWO-PANE LAYOUT
    private SharedPreferences sharedPreferences;        // PREFERENCES
    private ConnectionStatus connectionStatus;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // CONNECTION
        connectionStatus = new ConnectionStatus(this);

        // SERVICE
        if (savedInstanceState == null && connectionStatus.getStatus())
        {
            startService(new Intent(this, NotificationService.class));
        }
        else if (!connectionStatus.getStatus())
        {
            stopService(new Intent(this, NotificationService.class));
        }

        // PREFERENCES
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // CONNECTION
        if (!connectionStatus.getStatus())
        {
            offlineModeMessage();
        }

        // TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        if (actionBar != null)
        {
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
            case R.id.menu_item_preferences:
                Intent intent = new Intent(this, ActivityPreferences.class);
                startActivity(intent);
                break;

            case R.id.menu_item_refresh:
                if (connectionStatus.getStatus())
                {
                    new GetWeatherDateProgress(this).execute(this);
                }
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
        else if (key.equals("preferences_cities"))
        {
            sendBroadcast(new Intent(NotificationReceiver.ACTIONBAR_SUBTITLE));
        }
    }
}
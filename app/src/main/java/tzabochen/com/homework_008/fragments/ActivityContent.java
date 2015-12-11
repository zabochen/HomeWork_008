package tzabochen.com.homework_008.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tzabochen.com.homework_008.R;
import tzabochen.com.homework_008.tools.WeatherCity;

public class ActivityContent extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(WeatherCity.city);
        }

        // GET DISPLAY OPTIONS
        boolean landscapeOrientation = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        boolean displayLargeSize = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= (Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (landscapeOrientation && displayLargeSize)
        {
            finish();
            return;
        }

        if (savedInstanceState == null)
        {
            int itemPosition = getIntent().getIntExtra("itemPosition", 0);
            FragmentContent fragmentContent = FragmentContent.newInstance(itemPosition);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(android.R.id.content, fragmentContent).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

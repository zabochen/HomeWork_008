package tzabochen.com.homework_008.preferences;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tzabochen.com.homework_008.R;
import tzabochen.com.homework_008.WeatherCity;

public class ActivityPreferences extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        // ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(WeatherCity.city);
        }

        FragmentPreferences fragmentPreferences = new FragmentPreferences();
        getFragmentManager().beginTransaction()
                .replace(R.id.preferences_fragment_container, fragmentPreferences)
                .commit();
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

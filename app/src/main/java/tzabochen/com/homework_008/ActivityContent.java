package tzabochen.com.homework_008;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ActivityContent extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
}

package tzabochen.com.homework_008;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class AppMain extends AppCompatActivity implements ItemSelectedListener
{
    // VALUE'S
    private int itemPosition = 0;           // ITEM SELECTED BY DEFAULT
    private boolean withContent = true;     // SHOW TWO-PANE LAYOUT

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
        Toast.makeText(this, R.string.offline_mode, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home: this.finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}
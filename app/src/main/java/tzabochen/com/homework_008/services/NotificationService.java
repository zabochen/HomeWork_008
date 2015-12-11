package tzabochen.com.homework_008.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;

public class NotificationService extends Service
{
    // VALUE'S
    public static final long TIME_INTERVAL = 10 * 1000; // 10 sec
    private Timer timer = null;
    private SharedPreferences sharedPreferences;

    public NotificationService()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (timer == null && sharedPreferences.getBoolean("preferences_updates", true))
        {
            timer = new Timer();
            timer.schedule(new NotificationTimerTask(getApplicationContext()), TIME_INTERVAL, TIME_INTERVAL);
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}

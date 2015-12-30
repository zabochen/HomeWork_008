package tzabochen.com.homework_008.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import tzabochen.com.homework_008.AppMain;
import tzabochen.com.homework_008.realm.GetWeatherDate;
import tzabochen.com.homework_008.realm.GetWeatherDateNotify;
import tzabochen.com.homework_008.services.NotificationTimerTask;

public class NotificationReceiver extends BroadcastReceiver
{
    // VALUE'S
    public static final String NOTIFICATION_UPDATE = "tzabochen.com.notification.update";
    public static final String NOTIFICATION_CANCEL = "tzabochen.com.notification.cancel";
    public static final String ACTIONBAR_SUBTITLE = "tzabochen.com.actionbar.subtitle";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String getIntentAction = intent.getAction();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (getIntentAction.equals(ACTIONBAR_SUBTITLE))
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            AppMain.actionBar.setSubtitle(sharedPreferences.getString("preferences_cities", ""));

            // START BACKGROUND UPDATE
            new GetWeatherDate().execute(context);
        }
        else if (getIntentAction.equals(NOTIFICATION_UPDATE))
        {
            // START BACKGROUND UPDATE
            new GetWeatherDateNotify(context).execute(context);

            // CANCEL NOTIFICATION & CLOSE STATUS BAR
            notificationManager.cancel(NotificationTimerTask.NOTIFICATION_ID);
            context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
        else if (getIntentAction.equals(NOTIFICATION_CANCEL))
        {
            // CANCEL NOTIFICATION & CLOSE STATUS BAR
            notificationManager.cancel(NotificationTimerTask.NOTIFICATION_ID);
            context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }
}

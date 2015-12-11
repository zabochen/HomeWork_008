package tzabochen.com.homework_008.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.TimerTask;

import tzabochen.com.homework_008.AppMain;
import tzabochen.com.homework_008.R;

public class NotificationTimerTask extends TimerTask
{
    // VALUE'S
    private static final int NOTIFICATION_ID = 10;
    private Context context;
    private static int counter = 0;

    private Handler handler = new Handler();

    public NotificationTimerTask(Context context)
    {
        this.context = context;
    }

    @Override
    public void run()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(context, AppMain.class);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addParentStack(AppMain.class);
                taskStackBuilder.addNextIntent(intent);

                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
                notificationBuilder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_weather_app_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.notification_content_text))
                        .addAction(R.mipmap.ic_check_white_24dp, context.getString(R.string.notification_action_update), pendingIntent)
                        .setNumber(++counter)
                        .setAutoCancel(true);

                Notification notification = notificationBuilder.build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        });
    }
}

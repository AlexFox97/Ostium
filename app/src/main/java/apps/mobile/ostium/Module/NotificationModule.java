package apps.mobile.ostium.Module;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import apps.mobile.ostium.MainActivity;
import apps.mobile.ostium.R;

public class NotificationModule
{
    private NotificationManager manager;
    private String notificationChannelId;
    private Activity activity;

    public NotificationModule(NotificationManager notificationManager, Activity activity)
    {
        manager = notificationManager;
        this.activity = activity;
        notificationChannelId = createNotificationChannel(activity.getApplicationContext());
    }

    public void pushNotification(String title, String message)
    {
        // TODO
        // made this work for R26 > but for R26 < needs to take a channelId in the builder
        // but doesn't seem to work for me
        // Bean

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity.getApplicationContext(), notificationChannelId);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);

        // our apps main image as the notification image can change later if you want...
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        notificationBuilder.setAutoCancel(true);

        // what to do when you click on the notification
        // this just take the user back to the app
        Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
        notificationBuilder.setContentIntent(pendingIntent);

        manager.notify(0, notificationBuilder.build());
    }

    private String createNotificationChannel(Context context)
    {
        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "channel_Main_Ostium";
            CharSequence channelName = "Main Ostium";
            String channelDescription = "Main Ostium Description";

            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = false;
            int channelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        }
        else
        {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
}

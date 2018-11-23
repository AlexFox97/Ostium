package apps.mobile.ostium;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import Objects.GetLocationRequest;

public class MainActivity extends AppCompatActivity
{
    private static final int PermissionCorrect = 1;
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 0;

    private TextView t;

    private GPSModule GPS;
    private NotificationManager notificationManager;
    private String notificationChannelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        t = findViewById(R.id.textView);

        initializeGPS();
        initializeNotifications();
        //initializeCal();
    }

    private void initializeNotifications()
    {
        // setup a new channel for R26 <
        notificationChannelId = createNotificationChannel(this.getApplicationContext());
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
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

    private void pushNotification(String title, String message)
    {
        // TODO
        // made this work for R26 > but for R26 < needs to take a channelId in the builder
        // but doesn't seem to work for me
        // Bean

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext());
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        // our apps main image as the notification image can change later if you want...
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent =  PendingIntent.getActivities(this, 0, new Intent[] {notificationIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void initializeCal()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, PermissionCorrect);
        }
    }

    private void initializeGPS()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener listener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s)
            {
                // if gps isn't enable at all send user too setting to enable it
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        GPS = new GPSModule(locationManager, listener);
        checkPermissions();
    }

    private void checkPermissions()
    {
        // check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // if below ver 23 don't need to ask for permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
            }
            return;
        }
        // then get the location
        GPS.StartLocationUpdates(GPSPingTime, GPSDistance);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // TODO
        // this may break when you try to get the calender permissions,
        // unless we get all permissions at the same time

        // if it returns expected request code permissions are good
        if(requestCode == PermissionCorrect)
        {
            GPS.StartLocationUpdates(GPSPingTime, GPSDistance);
        }
    }

    // when you press the onscreen button
    public void GetLocationNow(View view)
    {
        GetLocationRequest l = GPS.GetLocationNow();

        switch (l.result)
        {
            case Success:
                String message = "long: " +l.location.getLongitude() + " Lat: " + l.location.getLatitude();
                t.append("\n " + message);
                pushNotification("Got A Location", message );
                break;
            case Failed:
                Toast.makeText(getApplicationContext(), "Error getting last known location!!!", Toast.LENGTH_SHORT);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Problem, an unknown problem has occurred", Toast.LENGTH_SHORT);
        }

    }
}
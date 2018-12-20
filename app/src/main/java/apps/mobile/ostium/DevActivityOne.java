package apps.mobile.ostium;

import android.app.NotificationManager;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import Objects.Request.GetLocationRequest;
import apps.mobile.ostium.Module.GPSModule;
import apps.mobile.ostium.Module.NotificationModule;

public class DevActivityOne extends AppCompatActivity
{
    private static final int PermissionCorrect = 1;
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 0;

    private TextView t;
    private GPSModule GPS;

    private NotificationModule notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_one);
        
        t = findViewById(R.id.textView);

        initializeGPS();
        notification = new NotificationModule((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE), this);
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
                notification.pushNotification("New Location!!!", location.getLongitude() + " " + location.getLatitude());
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
                t.append("\n " + l.location.getLongitude() + " " + l.location.getLatitude());
                break;
            case Failed:
                Toast.makeText(getApplicationContext(), "Error getting last known location!!!", Toast.LENGTH_SHORT);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Problem, an unknown problem has occurred", Toast.LENGTH_SHORT);
        }

    }
}
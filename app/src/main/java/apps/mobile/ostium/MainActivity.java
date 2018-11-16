package apps.mobile.ostium;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import TransferObjects.GetLocationRequest;

public class MainActivity extends AppCompatActivity
{
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 0;

    private TextView t;
    private GPSModule GPS;


//Check permissions in activities before any calls are made
/*
    private static final int MY_CAL_REQ = 1;
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
    }
*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        t = findViewById(R.id.textView);

        initializeGPS();

    }

    private class CalendaresultReceiver extends ResultReceiver {

        public CalendaresultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //TODO: Handle data received from service
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
            // if below ver 23 don't need to sak for permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            return;
        }
        // then get the location
        GPS.StartLocationUpdates(GPSPingTime, GPSDistance);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // if it returns expected request code permissions are good
        if(requestCode == 1)
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
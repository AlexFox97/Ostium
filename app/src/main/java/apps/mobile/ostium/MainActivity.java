package apps.mobile.ostium;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button b;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.textView);
        b = findViewById(R.id.button);

        initializeGPS();
    }

    private void initializeGPS()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                displayLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                // if gps isnt enable at all send user too setting to enable it
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        checkPermissions();
    }

    private void checkPermissions()
    {
        // check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // then get the location
        GetLocation(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // if it returns expected request code permissions are good
        if(requestCode == 10)
        {
            GetLocation(null);
        }
    }

    @SuppressLint("MissingPermission")
    public void GetLoctionNow(View view)
    {
        Location l = locationManager.getLastKnownLocation("gps");
        if(l != null)
        {
            displayLocation(l);
        }
    }

    @SuppressLint("MissingPermission")
    public void GetLocation(View view)
    {
        locationManager.requestLocationUpdates("gps", 2000, 0, listener);
    }

    public void displayLocation(Location l)
    {
        t.append("\n " + l.getLongitude() + " " + l.getLatitude());
    }
}
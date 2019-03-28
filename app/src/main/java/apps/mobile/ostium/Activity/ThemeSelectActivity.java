package apps.mobile.ostium.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import apps.mobile.ostium.Objects.Request.GetLocationRequest;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


import apps.mobile.ostium.Module.GPSModule;
import apps.mobile.ostium.Module.NotificationModule;
import apps.mobile.ostium.R;

public class ThemeSelectActivity extends AppCompatActivity
{
    private static final int PermissionCorrect = 1;
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 0;

    private TextView t;
    private TextView s;
    private GPSModule GPS;

    private NotificationModule notification;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLOUR = "colour";

    private int col;
    private Button applyTextButton;
    public int colourPickerColour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_one);

        t = findViewById(R.id.textView);
        s = findViewById(R.id.colourTextview);

        initializeGPS();
        notification = new NotificationModule((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE), this);
        //initializeCal();

    }

    //Dialogue
    public void colourPickerDialogue(View view) {
        ThemeActivity r = new ThemeActivity();
        setContentView(R.layout.activity_theme);
    }

    public void buttonColourPickerDialogue(View view) {

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose Colour")
                .initialColor(R.style.Ostium_Light_2)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .showColorPreview(true)
                .showColorEdit(true)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        setTitle(selectedColor);
                        changeBackgroundColour(selectedColor);
                    }
                })
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int i) {
                        setTitle(i);
                        changeBackgroundColour(i);
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeBackgroundColour(selectedColor);
                        colourPickerColour = selectedColor;
                        saveInfo();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();

    }

    private void changeBackgroundColour(int selectedColour)
    {
        Toast.makeText(getApplicationContext(), "Selected Colour: "+selectedColour, Toast.LENGTH_SHORT);
        //setTheme(R.style.Theme_Ostium_Dark);

        Button r = (Button) findViewById(R.id.button);
        r.setBackgroundColor(selectedColour);

        Button d = (Button) findViewById(R.id.button1);
        d.setBackgroundColor(selectedColour);

        //TextView q = (TextView) findViewById(R.id.nav_dev_two_toolbar);
        //q.setBackgroundColor(selectedColour);

        s.append("\n " + "Selected Colour: "+selectedColour);

    }

    public void saveInfo() {
        saveData();
    }

    //Shared Preferences Code
    public void saveData() {
        //No other app can change our shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(COLOUR, colourPickerColour);

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        col = sharedPreferences.getInt(COLOUR, 0);
    }

    public void updateViews(){
        s = findViewById(R.id.colourTextview);
        s.setText(col);
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
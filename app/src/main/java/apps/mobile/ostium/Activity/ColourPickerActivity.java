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
import android.support.v7.widget.Toolbar;
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

public class ColourPickerActivity extends AppCompatActivity
{
    private static final int PermissionCorrect = 1;
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 20;

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
        setContentView(R.layout.activity_colour);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void initializeCal()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, PermissionCorrect);
        }
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
}

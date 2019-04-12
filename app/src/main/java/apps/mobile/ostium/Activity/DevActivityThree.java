package apps.mobile.ostium.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import apps.mobile.ostium.R;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class DevActivityThree extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLOUR = "colour";

    private int col;
    private TextView t;
    private TextView s;
    private Button applyTextButton;
    public int colourPickerColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_three);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t = findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_location_toolbar, menu);
        return true;
    }

    public void colourPickerDialogue(View view) {
        ThemeActivity r = new ThemeActivity();
        setContentView(R.layout.activity_theme);
    }

    public void buttonColourPickerDialogue(View view) {

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose Colour")
                .initialColor(R.style.page_background_red)
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
                        //saveInfo();
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

        Button d = (Button) findViewById(R.id.button1);
        d.setBackgroundColor(selectedColour);

        //TextView q = (TextView) findViewById(R.id.nav_dev_two_toolbar);
        //q.setBackgroundColor(selectedColour);

        s.append("\n " + "Selected Colour: "+selectedColour);

    }


}
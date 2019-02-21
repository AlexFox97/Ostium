package apps.mobile.ostium;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

public class DevActivityThree extends AppCompatActivity {

    String selectedColour;
    TextView e = findViewById(R.id.txtColourPickerValue);
    ColorPickerView z = findViewById(R.id.color_picker_view);

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COLOUR = "colour";

    private int col;
    private Button applyTextButton;
    public int colourPickerColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_three);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        selectedColour = z.getSelectedColor()+"";

        e.setText("Selected Colour: "+selectedColour);

        z.getSelectedColor();

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColour = z.getSelectedColor()+"";
                e.setText(selectedColour);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_location_toolbar, menu);
        return true;
    }

    //Shared Preferences Code
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
}
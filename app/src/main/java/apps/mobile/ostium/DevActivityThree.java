package apps.mobile.ostium;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;

import org.w3c.dom.Text;

public class DevActivityThree extends AppCompatActivity {

    String selectedColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_three);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ColorPickerView s = findViewById(R.id.color_picker_view);

        selectedColour = s.getSelectedColor()+"";
        TextView e = findViewById(R.id.colourTextview);
        s.getSelectedColor();

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerView z = findViewById(R.id.color_picker_view);
                selectedColour = z.getSelectedColor()+"";
                TextView e = findViewById(R.id.colourTextview);
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
    public void saveData() {
        //No other app can change our shared preferences
        /*SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, textView.getText().toString());
        editor.putBoolean(SWITCH1, switch_1.isChecked());

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT);*/
    }

    public void loadData(){
        /*SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);*/
    }

    public void updateViews(){
        /*textView.setText(text);
        switch_1.setChecked(switchOnOff);*/
    }
}
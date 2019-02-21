package apps.mobile.ostium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements recyclerViewAdapter.ItemClickListener {

    recyclerViewAdapter adapter;

    SwitchCompat switch_1, switch_2, switch_3, switch_4;
    boolean stateSwitch1, stateSwitch2, stateSwitch3, stateSwitch4;
    SharedPreferences preferences;
    TextView textView;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";

    private String text;
    private boolean switchOnOff;
    private Button applyTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //region Layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion Layout

        //region Main
        // data to populate the RecyclerView with
        ArrayList<String> listData = new ArrayList<>();
        listData.add("Home");
        listData.add("Work");
        listData.add("Gym");
        listData.add("Uni");
        listData.add("Shop");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.SettingsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recyclerViewAdapter(this, listData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //endregion Main

        /*preferences = getSharedPreferences("PREFS", 0);
        stateSwitch1 = preferences.getBoolean("switch1", false);
        stateSwitch2 = preferences.getBoolean("switch2", false);
        stateSwitch3 = preferences.getBoolean("switch3", false);
        stateSwitch4 = preferences.getBoolean("switch4", false);*/

        //switch_1 = (SwitchCompat) findViewById(R.id.switch_1);
        //switch_2= (SwitchCompat) findViewById(R.id.switch_2);
        //switch_3 = (SwitchCompat) findViewById(R.id.switch_3);
        //switch_4= (SwitchCompat) findViewById(R.id.switch_24;

        /*switch_1.setChecked(stateSwitch1);
        switch_2.setChecked(stateSwitch2);
        switch_3.setChecked(stateSwitch3);
        switch_4.setChecked(stateSwitch4);

        switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch1 = !stateSwitch1;
                switch_1.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", stateSwitch1);
                editor.apply();
            }
        });

        switch_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch2 = !stateSwitch2;
                switch_2.setChecked(stateSwitch2);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2", stateSwitch2);
                editor.apply();
            }
        });

        switch_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch3 = !stateSwitch3;
                switch_3.setChecked(stateSwitch3);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch3", stateSwitch3);
                editor.apply();
            }
        });

        switch_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch4 = !stateSwitch4;
                switch_4.setChecked(stateSwitch4);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch4", stateSwitch4);
                editor.apply();
            }
        });*/

        applyTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textView.setText("Hello");
               // saveData();
               // loadData();
            }
        });

        loadData();
        updateViews();
    }

    //region Layout Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_settings_toolbar, menu);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
    //endregion Layout

    public void btnSettings_onClick(View view)
    {
        Intent intent=new Intent(this,DevActivityThree.class);
        startActivity(intent);
    }

    public void btnRead_onClick(View view) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        String sSettings = prefs.getString("example_text", "xxx"); //need to get key for text preference field
        Toast.makeText(this, sSettings, Toast.LENGTH_LONG).show();
    }

    public void saveData() {
        //No other app can change our shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, textView.getText().toString());
        editor.putBoolean(SWITCH1, switch_1.isChecked());

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    public void updateViews(){
        textView.setText(text);
        switch_1.setChecked(switchOnOff);
    }

}



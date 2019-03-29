package apps.mobile.ostium.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import apps.mobile.ostium.R;
import apps.mobile.ostium.Objects.ThemeApplication;
import apps.mobile.ostium.Utils;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity
{
    private Spinner spThemes;
    public static ArrayList<String> selectedCalendars;

    // Here we set the theme for the activity
    // Note `Utils.onActivityCreateSetTheme` must be called before `setContentView`
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_theme);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSpinnerItemSelection();
    }

    private void setupSpinnerItemSelection()
    {
        spThemes = (Spinner) findViewById(R.id.spThemes);
        spThemes.setSelection(ThemeApplication.currentPosition);
        ThemeApplication.currentPosition = spThemes.getSelectedItemPosition();

        spThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (ThemeApplication.currentPosition != position) {
                    Utils.changeToTheme(ThemeActivity.this, position);
                }
                ThemeApplication.currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
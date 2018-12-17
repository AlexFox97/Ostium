package apps.mobile.ostium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import apps.mobile.ostium.Module.CalendarHandler;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {
    private Spinner spThemes;

    public static ArrayList<String> selectedCalendars;

    // Here we set the theme for the activity
    // Note `Utils.onActivityCreateSetTheme` must be called before `setContentView`
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MUST BE SET BEFORE setContentView
        Utils.onActivityCreateSetTheme(this);
        // AFTER SETTING THEME
        setContentView(R.layout.activity_theme);
        setupSpinnerItemSelection();
    }

    private void setupSpinnerItemSelection() {
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

    void calendarSelection()
    {
        //TODO: pass back calendarID rather than displayName

        // where we will store or remove selected items
        selectedCalendars = new ArrayList<String>();

        //Create DialogBuilder and set title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        //Retrieve list of calendars and convert to pass to AlertDialog
        ArrayList tempCal =  CalendarHandler.getCalendarList(getApplicationContext());
        final CharSequence[] calendars = (CharSequence[]) tempCal.toArray(new CharSequence[tempCal.size()]);


        builder.setTitle("Please select your used calendars:");

        builder.setMultiChoiceItems(calendars,null ,new DialogInterface.OnMultiChoiceClickListener()
        {
            public void onClick(DialogInterface dialog, int item, boolean isChecked)
            {
                //Handle clicked calendars
                if (isChecked) {
                    // if the user checked the item, add it to the selected items
                    selectedCalendars.add("");
                }

                else if (selectedCalendars.contains(item)) {
                    // else if the item is already in the array, remove it
                    selectedCalendars.remove(Integer.valueOf(item));
                }
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Save mSelectedItems somewhere
            }
        });

        //Set cancel button so it cancels
        builder.setNegativeButton("Cancel", null);

        //Show AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
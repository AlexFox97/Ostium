package apps.mobile.ostium.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import apps.mobile.ostium.Adapter.RecyclerViewAdapter;
import apps.mobile.ostium.Module.CalendarHandler;
import apps.mobile.ostium.R;

import java.io.FileOutputStream;
import java.util.ArrayList;

import static apps.mobile.ostium.Activity.MainActivity.calendarID;

public class SettingActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // data to populate the RecyclerView with
        ArrayList<String> listData = new ArrayList<>();
        listData.add("Select Calendar");

        RecyclerView recyclerView = findViewById(R.id.SettingsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(listData, this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_settings_toolbar, menu);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

        if(adapter.getItem(position).equals("Select Calendar"))
            calendarSelection();

//        Intent myIntent = new Intent(SettingActivity.this, ThemeActivity.class);
//        SettingActivity.this.startActivity(myIntent);
    }

    public void selectCalendars(View view)
    {
        calendarSelection();
    }

    private void calendarSelection()
    {
        //TODO: pass back calendarID rather than displayName

        //Create DialogBuilder and set title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<Integer> selectedItems = new ArrayList<>();

        //Retrieve list of calendars and convert to pass to AlertDialog
        ArrayList tempCal =  CalendarHandler.getCalendarList(getApplicationContext());
        final CharSequence[] calendars = (CharSequence[]) tempCal.toArray(new CharSequence[0]);

        builder.setTitle("Please select your calendars:");
        builder.setMultiChoiceItems(calendars,null ,new DialogInterface.OnMultiChoiceClickListener()
        {
            public void onClick(DialogInterface dialog, int item, boolean isChecked)
            {
                //Handle clicked calendars
                if (isChecked) {
                    // if the user checked the item, add it to the selected items
                    selectedItems.add(item);
                }

            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                for(int i = 0; i <= calendarID.size()-1; i++)
                {
                    calendarID.remove(i);
                }

                for(Integer item : selectedItems)
                {
                    calendarID.add(Integer.parseInt(calendars[item].toString().split(":")[0]));
                }
            }
        });

        //Set cancel button so it cancels
        builder.setNegativeButton("Cancel", null);

        //Show AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        Toast.makeText(getApplicationContext(), "Previously selected calendars have been cleared.", Toast.LENGTH_SHORT).show();
    }
}
package apps.mobile.ostium;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity implements recyclerViewAdapter.ItemClickListener {

    recyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //region Layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion Layout

        //region Main
        // data to populate the RecyclerView with
        ArrayList<String> locationSampleNames = new ArrayList<>();
        locationSampleNames.add("Home");
        locationSampleNames.add("Work");
        locationSampleNames.add("Gym");
        locationSampleNames.add("Uni");
        locationSampleNames.add("Shop");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.LocationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recyclerViewAdapter(this, locationSampleNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        //endregion Main

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

}



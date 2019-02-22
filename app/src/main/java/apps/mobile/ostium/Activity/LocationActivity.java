package apps.mobile.ostium.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import apps.mobile.ostium.R;
import apps.mobile.ostium.Adapter.RecyclerViewAdapter;

import static apps.mobile.ostium.Activity.MainActivity.savedLocations;

public class LocationActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.LocationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, savedLocations);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    //region Layout Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_settings_toolbar, menu);
        return true;
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
    //endregion Layout

}



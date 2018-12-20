package apps.mobile.ostium;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import apps.mobile.ostium.Module.CalendarProviderIntentService;
import apps.mobile.ostium.Module.CardObject;
import apps.mobile.ostium.Module.LocationObject;
import apps.mobile.ostium.Module.eventGeneric;

import java.util.ArrayList;
import java.util.List;

//        Main Activity
//        Home Activity
//        Settings Activity
//        Location Activity
//        Map Activity
//        Dev Activity One
//        Dev Activity Two
//        Dev Activity Three


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LogTagClass = MainActivity.class.getSimpleName();
    private static final int PermissionCorrect = 1;
    private static final String TAG = "main activity";
    public static eventGeneric selectedEvent;
    public static ArrayList<eventGeneric> userSelectedEvents = new ArrayList<>();
    public static ArrayList<Integer> calendarID = new ArrayList<>();
    public static ArrayList<LocationObject> savedLocations = new ArrayList<>();
    public ArrayList<eventGeneric> userCalendarEvents = new ArrayList<>();
    CalendarResultReceiver calendarResultHandler;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        selectedEvent = null;

        // region CardRecycler - onCreate
        RecyclerView recCardList = (RecyclerView) findViewById(R.id.cardList);
        recCardList.setHasFixedSize(true);
        LinearLayoutManager llmCard = new LinearLayoutManager(this);
        llmCard.setOrientation(LinearLayoutManager.VERTICAL);
        recCardList.setLayoutManager(llmCard);

        CardAdapter ca = new CardAdapter(createCardList(8));
        recCardList.setAdapter(ca);

        // region ListRecycler - onCreate
//        RecyclerView recTagList = (RecyclerView) recCardList.findViewById(R.id.tagList);
//        recTagList.setHasFixedSize(true);
//        LinearLayoutManager llmTag = new LinearLayoutManager(this);
//        llmTag.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recTagList.setLayoutManager(llmTag);
//
//        TagAdapter ta = new TagAdapter(createTagList(8));
//        recTagList.setAdapter(ta);

        // endregion Recycler

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    //region Drawer Methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void goToHome(View view) {
        Log.d(LogTagClass, "Button Home clicked!");
        startActivity(new Intent(this, MainActivity.class));
    }

    public void goToSettings(View view) {
        Log.d(LogTagClass, "Button Settings clicked!");
        startActivity(new Intent(this, SettingActivity.class));
    }

    public void goToLocation(View view) {
        Log.d(LogTagClass, "Button Location clicked!");
        startActivity(new Intent(this, LocationActivity.class));
    }

    public void goToMap(View view) {
        Log.d(LogTagClass, "Button Map clicked!");
        startActivity(new Intent(this, MapActivity.class));
    }

    public void goToDevOne(View view) {
        Log.d(LogTagClass, "Button Dev One clicked!");
        startActivity(new Intent(this, DevActivityOne.class));
    }

    public void goToDevTwo(View view) {
        Log.d(LogTagClass, "Button Dev Two clicked!");
        startActivity(new Intent(this, DevActivityTwo.class));
    }

    public void goToDevThree(View view) {
        Log.d(LogTagClass, "Button Dev Three clicked!");
        startActivity(new Intent(this, DevActivityThree.class));
    }
    //endregion Drawer


    private List<CardObject> createCardList(int size) {

        List<CardObject> result = new ArrayList<CardObject>();
        for (eventGeneric item : userSelectedEvents) {
            CardObject ci = new CardObject();
//            ci.name = (CardInfo.NAME_PREFIX) + " title " + i;
//            ci.surname = CardInfo.SURNAME_PREFIX + ""+ i;
//            ci.email = CardInfo.EMAIL_PREFIX + "other "+i + "@test.com";

            ci.title = item.getTitle();
            ci.details = item.getDescription();

            ci.date = item.getStartTime();
            result.add(ci);

        }

        return result;
    }

    protected void onResume() {
        super.onResume();
        getEventList();
    }


    private List<TagInfo> createTagList(int size) {

        List<TagInfo> result = new ArrayList<TagInfo>();
        for (int i = 1; i <= size; i++) {
            TagInfo ci = new TagInfo();
            ci.locationName = "Shops";
            result.add(ci);
        }
        return result;
    }

    public void addEvent(View v) {
        //On click of text in main activity
        //TODO: Show AlertDialog to select an event and then set location and return


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Please select an event:");

        ArrayList<String> eventTitlesTemp = new ArrayList<>();


        for (eventGeneric event : userCalendarEvents) {
            eventTitlesTemp.add(event.getTitle());
        }

        String[] eventsTitles = GetStringArray(eventTitlesTemp);

        builder.setSingleChoiceItems(eventsTitles, 0, null);

        builder.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedEvent = userCalendarEvents.get(which);
                dialog.dismiss();

                //TODO: Handle selected event

            }
        });

        AlertDialog addEventAlert = builder.create();
        addEventAlert.show();
    }

    private String[] GetStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    public void getEventList() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, PermissionCorrect);
        }

        calendarResultHandler = new CalendarResultReceiver(new Handler());

        Intent startIntent = new Intent(this, CalendarProviderIntentService.class);
        startIntent.putExtra("receiver", calendarResultHandler);
        startIntent.putExtra("calendars", calendarID);
        startService(startIntent);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            goToLocation(navigationView);
        } else if (id == R.id.nav_map) {
            goToMap(navigationView);
        } else if (id == R.id.nav_settings) {
            goToSettings(navigationView);
        } else if (id == R.id.nav_dev_one) {
            goToDevOne(navigationView);
        } else if (id == R.id.nav_dev_two) {
            goToDevTwo(navigationView);
        } else if (id == R.id.nav_dev_three) {
            goToDevThree(navigationView);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "App destroyed");

        super.onDestroy();
    }

    private class CalendarResultReceiver extends ResultReceiver {
        public CalendarResultReceiver(Handler handler) {
            super(handler);
        }

        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case CalendarProviderIntentService.RETRIEVE_SUCCESS:


                    if (resultData != null)
                        userCalendarEvents = ((ArrayList) resultData.getSerializable("events"));

                    break;

                case CalendarProviderIntentService.RETRIEVE_ERROR:
                    //TODO: Handle failure

            }
            super.onReceiveResult(resultCode, resultData);
        }


    }
}

package apps.mobile.ostium.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import apps.mobile.ostium.*;
import apps.mobile.ostium.Adapter.CardAdapter;
import apps.mobile.ostium.Module.*;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

//        Main Activity
//        Home Activity
//        Settings Activity
//        Location Activity
//        Map Activity
//        Dev Activity One
//        Dev Activity Two
//        Dev Activity Three


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String LogTagClass = MainActivity.class.getSimpleName();
    private static final int PermissionCorrect = 1;
    private static CardAdapter ca;
    private static final String TAG = "main activity";
    public static final String calendarSettingsFileName = "cal_settings";
    public static final String locationSettingsFileName = "loc_settings";
    public static final String eventSettingsFileName = "evn_settings";

    public static EventGeneric selectedEvent;

    public static ArrayList<EventGeneric> userEvents = new ArrayList<>();
    public static ArrayList<Integer> calendarID = new ArrayList<>();
    public static ArrayList<LocationObject> savedLocations = new ArrayList<>();

    public static ArrayList<CardObject> cardList = new ArrayList<>();

    public ArrayList<EventGeneric> userCalendarEvents = new ArrayList<>();
    public CalendarResultReceiver calendarResultHandler;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private GoogleApiClient googleApiClient;
    NotificationModule Notification;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        //region Sample Cards
        /*
        if (userSelectedEvents.size() == 0)
        {
            userSelectedEvents.add(new EventGeneric("Sample1", "Sample1"));
            userSelectedEvents.add(new EventGeneric("Sample2", "Sample2"));
            userSelectedEvents.add(new EventGeneric("Sample3", "Sample3"));
        }
        */
        
        if (userEvents.size() == 0) {
            userEvents.add(new EventGeneric("No items found.", "No items found."));
        }

        // region CardRecycler - onCreate
        RecyclerView recCardList = (RecyclerView) findViewById(R.id.cardList);
        recCardList.setHasFixedSize(true);
        LinearLayoutManager llmCard = new LinearLayoutManager(this);
        llmCard.setOrientation(LinearLayoutManager.VERTICAL);
        recCardList.setLayoutManager(llmCard);

        cardList = createCardList();
        ca = new CardAdapter(cardList);
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
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        LocationObject cantorBuilding = new LocationObject("Cantor", 53.3769219, -1.4677611345050374, "Work");
        LocationObject aldiSheffield = new LocationObject("Aldi Sheffield", 53.372670, -1.475285, "Shop");
        LocationObject tescoExpress = new LocationObject("Tesco Express", 53.379121, -1.467388, "Shop");
        LocationObject asdaQueensRoad = new LocationObject("Asda Queens Road", 53.368411, -1.463179, "Shop");
        LocationObject moorMarket = new LocationObject("Moor Market", 53.375677, -1.472894, "Shop");
        LocationObject owenBuilding = new LocationObject("Owen Building", 53.379564, -1.465743, "Place");

        savedLocations.add(aldiSheffield);
        savedLocations.add(cantorBuilding);
        savedLocations.add(tescoExpress);
        savedLocations.add(moorMarket);
        savedLocations.add(owenBuilding);
        savedLocations.add(asdaQueensRoad);
        loadCalendarId();
        GetPermissions();
        SetupNotifications();
    }

    private void GetPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                                                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                                                                  Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                  Manifest.permission.ACCESS_NETWORK_STATE,
                                                                  Manifest.permission.INTERNET,
                                                                  Manifest.permission.GET_ACCOUNTS,
                                                                  Manifest.permission.READ_CALENDAR
                                                    }, 111);
            }
        }
    }

    private void SetupNotifications()
    {
        // get the user to sign into there google account
        GoogleSignInOptions SIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, SIO)
                .build();

        // notification stuff
        Notification = new NotificationModule(getSystemService(NotificationManager.class),this);
        final TaskModule task = new TaskModule(this, googleApiClient);
        task.GetAllTasks();

        LocationListener listener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                for(int i = 0; i < savedLocations.size(); i++)
                {
                    if(location.getLongitude() + 0.01 == savedLocations.get(i).getLongt() || location.getLongitude() - 0.01 == savedLocations.get(i).getLongt())
                    {
                        if (location.getLatitude() + 0.01 == savedLocations.get(i).getLat() || location.getLatitude() - 0.01 == savedLocations.get(i).getLat())
                        {
                            // were in a known location make a notification
                            if(task.allTasks.size() > 0)
                            {
                                Notification.pushNotification(task.allTasks.get(0).getTitle(), task.allTasks.get(0).getNotes());
                            }
                        }
                    }

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        GPSModule gps = new GPSModule((LocationManager)getSystemService(LOCATION_SERVICE), listener);
        gps.StartLocationUpdates(1000, 10);        
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

    private ArrayList<CardObject> createCardList() {

        cardList = new ArrayList<CardObject>();
        for (EventGeneric item : userEvents)
        {
            CardObject ci = new CardObject(item);
            ci.title = item.getTitle();
            ci.details = item.getDescription();
            ci.date = item.getStartTime();

            cardList.add(ci);
        }
        return cardList;
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
        //Add calendar event from list of events from selected characters

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please select an event:");

        ArrayList<String> eventTitlesTemp = new ArrayList<>();

        for (EventGeneric event : userCalendarEvents)
        {
            eventTitlesTemp.add(event.getTitle());
        }

        String[] eventsTitles = GetStringArray(eventTitlesTemp);

        builder.setSingleChoiceItems(eventsTitles, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedEvent = userCalendarEvents.get(abs(which));
                userEvents.add(0, selectedEvent);
                cardList.add(0, new CardObject(selectedEvent));
                ca.notifyItemInserted(0);
                selectedEvent = null;

                dialog.dismiss();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 111) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveCalendarId();
    }

    private void saveCalendarId()
    {
        try {
            FileOutputStream fos = openFileOutput(MainActivity.calendarSettingsFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(calendarID);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCalendarId()
    {
         try {
            FileInputStream fis = openFileInput(MainActivity.calendarSettingsFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            calendarID = (ArrayList) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveLocations()
    {
        try {
            FileOutputStream fos = openFileOutput(MainActivity.locationSettingsFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(savedLocations);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLocations()
    {
        try {
            FileInputStream fis = openFileInput(MainActivity.locationSettingsFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            savedLocations = (ArrayList) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveEvents()
    {
        try {
            FileOutputStream fos = openFileOutput(MainActivity.eventSettingsFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(userEvents);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadEvents()
    {
        try {
            FileInputStream fis = openFileInput(MainActivity.eventSettingsFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            userEvents = (ArrayList) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

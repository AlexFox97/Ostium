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

import android.provider.Settings;
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

import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import apps.mobile.ostium.Adapter.CardAdapter;
import apps.mobile.ostium.Module.*;
import apps.mobile.ostium.R;
import apps.mobile.ostium.Objects.TagInfo;

import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import apps.mobile.ostium.Objects.CardObject;
import apps.mobile.ostium.Objects.LocationObject;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String LogTagClass = MainActivity.class.getSimpleName();
    private static final int PermissionCorrect = 1;
    private static CardAdapter ca;
    private static final String TAG = "main activity";
    public static final String calendarSettingsFileName = "cal_settings";
    public static final String locationSettingsFileName = "loc_settings";
    public static final String eventSettingsFileName = "evn_settings";

    private final int GPSPingTime = 2000;
    private final int GPSDistance = 20;

    private String m_Text = "";

    public static EventGeneric selectedEvent;

    public static ArrayList<EventGeneric> userEvents = new ArrayList<>();
    public static ArrayList<Integer> calendarID = new ArrayList<>();
    public static ArrayList<LocationObject> savedLocations = new ArrayList<>();
    public static ArrayList<CardObject> cardList = new ArrayList<>();

    public ArrayList<EventGeneric> userCalendarEvents = new ArrayList<>();
    public CalendarResultReceiver calendarResultHandler;

    public static int themeVar = 0x7f100170;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RecyclerView recCardList;

    private GoogleApiClient googleApiClient;
    NotificationModule Notification;

    private EventGeneric newEvent;

    private GPSModule GPS;
    private LocationObject lastLoc;

    public Boolean permisson = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTheme(themeVar);
        selectedEvent = null;

        // try to load stuff
        if(savedLocations.size() == 0)
        {
            loadLocations();
        }

        if(userEvents.size() == 0)
        {
            loadEvents();
        }

        if(calendarID.size() == 0)
        {
            loadCalendarId();
        }

        // check/get permission and setup stuff
        if(!permisson)
        {
            GetPermissions();
            SetupNotifications();
            permisson = true;
        }

        LocationObject cantorBuilding = new LocationObject("Cantor", 53.3769219, -1.4677611345050374, "Work");
        LocationObject aldiSheffield = new LocationObject("Aldi Sheffield", 53.372670, -1.475285, "Shop");
        LocationObject tescoExpress = new LocationObject("Tesco Express", 53.379121, -1.467388, "Shop");
        LocationObject asdaQueensRoad = new LocationObject("Asda Queens Road", 53.368411, -1.463179, "Shop");
        LocationObject moorMarket = new LocationObject("Moor Market", 53.375677, -1.472894, "Shop");
        LocationObject owenBuilding = new LocationObject("Owen Building", 53.379564, -1.465743, "Place");

        if(savedLocations.size() == 0)
        {
            savedLocations.add(aldiSheffield);
            savedLocations.add(cantorBuilding);
            savedLocations.add(tescoExpress);
            savedLocations.add(moorMarket);
            savedLocations.add(owenBuilding);
            savedLocations.add(asdaQueensRoad);
        }

        //default data if nothing in save files
        if (userEvents.size() == 0)
        {
            userEvents.add(new EventGeneric("Finish uni assignment", "Work", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Get food for tonight", "Shops", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Meeting", "Places", cantorBuilding, "Go over work done in last tutorial"));
            userEvents.add(new EventGeneric("Get present for Mother's Day", "Shops", asdaQueensRoad, "Stuff to do"));
            userEvents.add(new EventGeneric("Meeting with supervisor", "Work", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Do x y z", "Places", owenBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Post Parcel", "Places", moorMarket, "Go to post office near moor market"));
            userEvents.add(new EventGeneric("Mobile Apps Tutorial", "Work", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Mobile Apps Deadline", "Work", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Finish uni assignment", "Work", cantorBuilding, "Stuff to do"));
            userEvents.add(new EventGeneric("Get food for tonight", "Shops", tescoExpress, "Remember to get bread and milk!"));
            userEvents.add(new EventGeneric("Buy laundry detergent", "Places", aldiSheffield, "Go to Aldi to get this"));
        }

        // region CardRecycler - onCreate
        recCardList = (RecyclerView) findViewById(R.id.cardList);
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


        //getEventList();

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void GetPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
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
                                                                  Manifest.permission.READ_CALENDAR,
                                                                  Manifest.permission.READ_EXTERNAL_STORAGE,
                                                                  Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                    }, 111);
            }
        }
    }

    private void SetupNotifications()
    {
        if(googleApiClient != null)
        {
            return ;
        }
        //else setup the notifications
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
                for(int i = 0; i < userEvents.size(); i++)
                {
                    if(userEvents.get(i).locationTags != null) {
                        if (userEvents.get(i).locationTags.size() > 0) {
                            for (LocationObject loc : userEvents.get(i).locationTags) {
                                if (location.getLongitude() + 0.01 > loc.getLongt() && location.getLongitude() - 0.01 < loc.getLongt()) {
                                    if (location.getLatitude() + 0.01 > loc.getLat() && location.getLatitude() - 0.01 < loc.getLat()) {
                                        // we're in a known location make a notification
                                        if(lastLoc != null)
                                        {
                                            // if still in same place dont spam notifications
                                            if(loc.title != lastLoc.title)
                                            {
                                                Notification.pushNotification(userEvents.get(i).title, userEvents.get(i).description);
                                                break;
                                            }
                                        }
                                        else {
                                            lastLoc = loc;
                                            Notification.pushNotification(userEvents.get(i).title, userEvents.get(i).description);
                                            break;
                                        }
                                    }
                                }
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

        GPS = new GPSModule((LocationManager)getSystemService(LOCATION_SERVICE), listener);
        GPS.StartLocationUpdates(GPSPingTime, GPSDistance);
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
        Intent go = new Intent(this, MapActivity.class);
        go.putExtra("locations", savedLocations);
        startActivity(go);
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
*/
    private ArrayList<CardObject> createCardList() {

        cardList = new ArrayList<>();
        for (EventGeneric item : userEvents)
        {
            CardObject ci = new CardObject(item);
            ci.title = item.getTitle();
            ci.details = item.getDescription();
            ci.date = item.getStartTime();
            ArrayList<LocationObject> locations = new ArrayList<>();

            if(item.locationTags.size() == 0)
            {
                locations.add(item.location);
            }
            else
            {
                locations = item.locationTags;
            }

            ci.Locations = locations;
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

    private int choice = 0;
    public void updateEvent(View v)
    {
        final Context context = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Please What you Would Like to Do:");

        String[] choices = new String[2];
        choices[0] = "Add";
        choices[1] = "Delete";
        final List<String> choiceList = Arrays.asList(choices);

        builder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice = i;
            }
        });

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(choice == 0)
                {
                    addEvent();
                }
                else
                {
                    deleteEvent();
                    choice = 0;
                }
            }
        });

        builder.show();
    }

    public void deleteEvent() {
        ArrayList<String> tmp = new ArrayList<>();

        final Boolean[] events = new Boolean[userEvents.size()];
        Arrays.fill(events, false);

        for (EventGeneric event : userEvents)
        {
            tmp.add(event.getTitle());
        }

        final String[] eventTitles = GetStringArray(tmp);
        final List<String> eventList = Arrays.asList(eventTitles);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        DialogInterface.OnMultiChoiceClickListener multiListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                events[which] = isChecked;
            }
        };

        builder.setMultiChoiceItems(eventTitles, null, multiListener);
        builder.setCancelable(true);

        // Set a title for alert dialog
        builder.setTitle("Please select an option:");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try
                {
                    for (int i = 0; i < userEvents.size(); i++)
                    {
                        if (events[i])
                        {
                            userEvents.remove(i);
                            cardList.remove(i);
                        }
                    }
                }
                catch (Exception e)
                {

                }

                ca.notifyDataSetChanged();
                saveEvents();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog addEventAlert = builder.create();
        addEventAlert.show();
    }

    public void addEvent() {
        //Add calendar event from list of events from selected characters

        final Boolean checkedLocations[];
        ArrayList<String> locationTitlesTemp = new ArrayList<>();
        newEvent = new EventGeneric();

        checkedLocations = new Boolean[savedLocations.size()];
        Arrays.fill(checkedLocations, false);
        for (LocationObject location : savedLocations)
        {
            locationTitlesTemp.add(location.getTitle());
        }

        final String[] locationTitles = GetStringArray(locationTitlesTemp);
        final List<String> locationList = Arrays.asList(locationTitles);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        DialogInterface.OnMultiChoiceClickListener multiListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedLocations[which] = isChecked;

                // Get the current focused item
                String currentItem = locationList.get(which);
                for (int i = 0; i< savedLocations.size(); i++)
                {
                    if(savedLocations.get(i).title == currentItem)
                    {
                        newEvent.locationTags.add(savedLocations.get(i));
                        newEvent.location = savedLocations.get(i);
                        break;
                    }
                }
            }
        };

        builder.setMultiChoiceItems(locationTitles, null, multiListener);

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Please select a location:");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Current card
                addCalanderEvent();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog addEventAlert = builder.create();
        addEventAlert.show();
    }

    public void addCalanderEvent()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please select an event:");
        ArrayList<String> eventTitlesTemp = new ArrayList<>();

        for (EventGeneric event : userCalendarEvents)
        {
            eventTitlesTemp.add(event.getTitle());
        }

        final String[] eventsTitles = GetStringArray(eventTitlesTemp);

        builder.setSingleChoiceItems(eventsTitles, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedEvent = userCalendarEvents.get(abs(which));
                newEvent.calendarEvent = selectedEvent;
                dialog.dismiss();
                addDate();
            }
        });

        builder.show();
    }

    public void addDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker picker = new DatePicker(this);
        picker.setCalendarViewShown(false);

        builder.setTitle("Please select a date:");
        builder.setView(picker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String date = Integer.toString(picker.getDayOfMonth()) + "/" + Integer.toString(picker.getMonth()) +"/" + Integer.toString(picker.getYear());
                newEvent.startTime = date;
                addTime();
            }
        });

        builder.show();
    }

    public void addTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final TimePicker picker = new TimePicker(this);
        picker.setIs24HourView(true);
        picker.setEnabled(true);
               // picker.setCalendarViewShown(false);

        builder.setTitle("Please select a time:");
        builder.setView(picker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dateTime = newEvent.startTime + " " + picker.getHour() + ":" +picker.getMinute() ;
                newEvent.startTime = dateTime;
                addTask();
            }
        });

        builder.show();
    }

    public void addTask() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter task title:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as text, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                newEvent.title = m_Text;
                addTaskDescription();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addTaskDescription() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter task description:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as text, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                newEvent.description = m_Text;
                addTaskType();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addTaskType()
    {
        final Context context = this;
        final Boolean checkedLocationTypes[];
        ArrayList<String> locationTypeTitlesTemp = new ArrayList<>();

        checkedLocationTypes = new Boolean[2];
        Arrays.fill(checkedLocationTypes, false);

        locationTypeTitlesTemp.add("Shops");
        locationTypeTitlesTemp.add("Work");
        locationTypeTitlesTemp.add("Places");

        final String[] locationTypeTitles = GetStringArray(locationTypeTitlesTemp);
        final List<String> locationTypeList = Arrays.asList(locationTypeTitles);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnMultiChoiceClickListener multiListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedLocationTypes[which] = isChecked;

                // Get the current focused item
                String currentItem = locationTypeList.get(which);
                newEvent.eventType = currentItem;

                // Notify the current action
                Toast.makeText(context, currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
            }
        };

        builder.setMultiChoiceItems(locationTypeTitles, null, multiListener);

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Please select a location type:");
        builder.setPositiveButton("Create new task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addCard();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog addEventAlert = builder.create();
        addEventAlert.show();
    }

    private void addCard()
    {
        try
        {
            int index = userEvents.size();
            userEvents.add(newEvent);

            CardObject ci = new CardObject(newEvent);
            ci.title = newEvent.getTitle();
            ci.details = newEvent.getDescription();
            ci.date = newEvent.getStartTime();
            ci.Locations = newEvent.locationTags;

            newEvent = new EventGeneric();
            cardList.add(ci);

            ca.notifyDataSetChanged();
            ca.notifyItemInserted(index);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String[] GetStringArray(ArrayList<String> arr)
    {
        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    public void getEventList()
    {
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
        } else
        if (id == R.id.nav_map) {
            goToMap(navigationView);
        } else if (id == R.id.nav_settings) {
            goToSettings(navigationView);
        } else if (id == R.id.nav_dev_one) {
            goToDevOne(navigationView);
        }
        /*else if (id == R.id.nav_dev_two) {
            goToDevTwo(navigationView);
        } else if (id == R.id.nav_dev_three) {
            goToDevThree(navigationView);
        }*/

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

    @Override
    protected void onStop() {
        saveCalendarId();
        saveLocations();
        saveEvents();
        super.onStop();
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
            savedLocations = (ArrayList<LocationObject>) is.readObject();
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
            userEvents = (ArrayList<EventGeneric>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CalendarResultReceiver extends ResultReceiver
    {
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
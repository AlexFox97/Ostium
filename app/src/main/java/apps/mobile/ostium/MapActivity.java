package apps.mobile.ostium;

import Objects.Request.GetLocationRequest;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import apps.mobile.ostium.Module.GPSModule;
import apps.mobile.ostium.Module.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int GPSPingTime = 2000;
    private final int GPSDistance = 0;
    ArrayList<Place> places;
    boolean showSavedPlaces = false;
    boolean showShops = false;
    boolean showWork = false;
    boolean search = false;
    private GoogleMap mMap;
    private GPSModule GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //region Layout
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        //endregion Layout

        //region Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        places = new ArrayList<>();

        EditText edit_txt = (EditText) findViewById(R.id.TF_location);

        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSearch(v);
                    return true;
                }
                return false;
            }
        });
        //endregion Map
    }

    //region Layout Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_map_toolbar, menu);
        return true;
    }
    //endregion

    // region Map
    private void intializeGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                GetLocationRequest l = GPS.GetLocationNow();
                //String locationStats = "Latitude: " + l.location.getLatitude() + " Longitude: " + l.location.getLongitude();
                LatLng currentLocation = new LatLng(l.location.getLatitude(), l.location.getLongitude());

                String s = getCompleteAddressString(l.location.getLatitude(), l.location.getLongitude()); //new

                if (showSavedPlaces == true) {
                    //iterate through places array where type is savedPlace
                    for (int i = 0; i < places.size(); i++) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                    }
                }

                if (showShops == true) {
                    //iterate through places array where type is Shop
                    for (int i = 0; i < places.size(); i++) {
                        if (places.get(i).getPlaceType().equals("Shop")) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                        }
                    }
                }

                if (showWork == true) {
                    //iterate through places array where type is work
                    for (int i = 0; i < places.size(); i++) {
                        if (places.get(i).getPlaceType().equals("Work")) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                        }
                    }
                }


                if (search == true) {
                    //iterate through places array where type is house
                    EditText location_tf = findViewById(R.id.TF_location);
                    String searchLoc = location_tf.getText().toString();
                    for (int i = 0; i < places.size(); i++) {
                        if (places.get(i).getTitle().contains(searchLoc)) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                        }
                    }
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation)); //currentLocation
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                // if GPS isn't enabled at all send user too setting to enable it
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        GPS = new GPSModule(locationManager, listener);
        checkPermissions();
    }

    public void changeType(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void checkPermissions() {
        // check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // if below ver 23 don't need to ask for permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            }
            return;
        }
        // then get the location
        GPS.StartLocationUpdates(GPSPingTime, GPSDistance);
    }

    //This will be updated when "adding a location" functionality is completed
    public void setUpPlacesList() {
        //Places to add to list
        Place cantorBuilding = new Place("Cantor", 53.3769219, -1.4677611345050374, "Work");
        Place aldiSheffield = new Place("Aldi Sheffield", 53.372670, -1.475285, "Shop");
        Place tescoExpress = new Place("Tesco Express", 53.379121, -1.467388, "Shop");
        Place asdaQueensRoad = new Place("Asda Queens Road", 53.368411, -1.463179, "Shop");
        Place moorMarket = new Place("Moor Market", 53.375677, -1.472894, "Shop");
        Place owenBuilding = new Place("Owen Building", 53.379564, -1.465743, "Place");

        places.add(0, cantorBuilding);
        places.add(1, aldiSheffield);
        places.add(2, tescoExpress);
        places.add(3, moorMarket);
        places.add(4, owenBuilding);
        places.add(5, asdaQueensRoad);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpPlacesList();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        GetLocationRequest l = GPS.GetLocationNow();
        LatLng currentLocation = new LatLng(l.location.getLatitude(), l.location.getLongitude());
        String s = getCompleteAddressString(l.location.getLatitude(), l.location.getLongitude()); //new

        mMap.addMarker(new MarkerOptions().position(currentLocation).title(s));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation)); //currentLocation
    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Current loc address", strReturnedAddress.toString());
            } else {
                Log.w("Current loc address", "No address returned!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loc address", "Cannot get Address!");
        }
        return strAdd;
    }

    public void onSearch(View view) {
        if (!search) {

            EditText location_tf = findViewById(R.id.TF_location);
            String location = location_tf.getText().toString();
            Toast.makeText(MapActivity.this, location, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < places.size(); i++) {
                if (places.get(i).getTitle().contains(location)) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                }
            }
            Toast.makeText(MapActivity.this, "Showing Searched Place", Toast.LENGTH_SHORT).show();
            search = true;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            search = false;
            Toast.makeText(MapActivity.this, "Hiding Searched Place", Toast.LENGTH_SHORT).show();
            mMap.clear();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.B_search:
                EditText tf_location = findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList;

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                LatLng latlng = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latlng);
                                markerOptions.title(location);
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.B_places:
                if (!showSavedPlaces) {

                    for (int i = 0; i < places.size(); i++) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                    }
                    Toast.makeText(MapActivity.this, "Showing Nearby Saved Places", Toast.LENGTH_SHORT).show();
                    showSavedPlaces = true;
                } else {
                    Toast.makeText(MapActivity.this, "Hiding Nearby Saved Places", Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    showSavedPlaces = false;
                }
                break;
            case R.id.B_work:
                mMap.clear();
                if (!showWork) {
                    for (int i = 0; i < places.size(); i++) {
                        if (places.get(i).getPlaceType().equals("Work")) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                        }
                    }
                    Toast.makeText(MapActivity.this, "Showing Work", Toast.LENGTH_SHORT).show();
                    showWork = true;
                } else {
                    Toast.makeText(MapActivity.this, "Hiding Work", Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    showWork = false;
                }
                break;
            case R.id.B_shops:
                mMap.clear();
                if (!showShops) {
                    for (int i = 0; i < places.size(); i++) {
                        if (places.get(i).getPlaceType().equals("Shop")) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(places.get(i).getLat(), places.get(i).getLongt())).title(places.get(i).getTitle()));
                        }
                    }
                    Toast.makeText(MapActivity.this, "Showing Nearby Shops", Toast.LENGTH_SHORT).show();
                    showShops = true;
                } else {
                    Toast.makeText(MapActivity.this, "Hiding Nearby Shops", Toast.LENGTH_SHORT).show();
                    mMap.clear();
                    showShops = false;
                }
                break;
        }
    }

    // endregion Map

}

package apps.mobile.ostium.Module;

import java.io.Serializable;

import static apps.mobile.ostium.MainActivity.savedLocations;

public class LocationObject implements Serializable {
    double latitude;
    double longitude;
    String title;
    String placeType; //savedPlace, shop, work

    public LocationObject(String t, double lat, double longt, String pType)
    {
        title = t;
        latitude = lat;
        longitude = longt;
        placeType = pType;
        int id = savedLocations.size();
    }

    public LocationObject() {
    }

    public void setPlace(double lat, double longi)
    {
        latitude = lat;
        longitude = longi;
    }

    public void setTitle(double lat, double longi) {
        latitude = lat;
        longitude = longi;
    }

    public void setpType(String place) {
        placeType = place;
    }

    public double getLat()
    {
        return latitude;
    }

    public double getLongt()
    {
        return longitude;
    }

    public String getPlaceType()
    {
        return placeType;
    }

    public String getTitle() { return title; }
}


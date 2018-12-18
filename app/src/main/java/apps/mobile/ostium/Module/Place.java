package apps.mobile.ostium.Module;

import java.io.Serializable;

public class Place implements Serializable{
    double latitude;
    double longitude;
    String title;
    String placeType; //savedPlace, shop, work

    public Place(String t, double lat, double longt, String pType)
    {
        title = t;
        latitude = lat;
        longitude = longt;
        placeType = pType;
    }

    public Place(){}

    public void setPlace(double lat, double longi)
    {
        latitude = lat;
        longitude = longi;
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


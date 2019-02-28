package apps.mobile.ostium.Module;

import java.io.Serializable;

public class LocationObject implements Serializable
{
    public double latitude;
    public double longitude;
    public String title;
    public String placeType; //savedPlace, shop, work

    public LocationObject(String title, double lat, double longt, String pType)
    {
        this.title = title;
        latitude = lat;
        longitude = longt;
        placeType = pType;
    }

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


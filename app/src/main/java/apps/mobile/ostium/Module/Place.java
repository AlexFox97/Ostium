package apps.mobile.ostium.Module;

public class Place {
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


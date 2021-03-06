package apps.mobile.ostium.Objects;

import apps.mobile.ostium.Module.EventGeneric;
import apps.mobile.ostium.Objects.LocationObject;

import java.util.ArrayList;

public class CardObject
{
    public int cardID;
    public String title;
    public String details;
    public String date;
    public String sourceName = "Source Name";
    public String sourceColour = "#FFFFFF";
    public ArrayList<LocationObject> Locations;

    public CardObject(int cardID, String title, String details, String date, String sourceName, String sourceColour, ArrayList<LocationObject> locations)
    {
        this.cardID = cardID;
        this.title = title;
        this.details = details;
        this.date = date;
        this.sourceName = sourceName;
        this.sourceColour = sourceColour;
        this.Locations = locations;
    }

    public CardObject(EventGeneric event)
    {
//        this.cardID = event.cardID;
        this.title = event.getTitle();
        this.details = event.getDescription();
        this.date = event.getStartTime();
//        this.sourceName = event.getSourceName;
//        this.sourceColour = event.getSourceColour;
        this.Locations = event.getLocationTags();
    }

    public String getLocationsToString()
    {
        StringBuilder s = new StringBuilder();
        if (Locations != null)
        {
            for (LocationObject loc : Locations)
            {
                s.append(loc.getTitle() + "     ");
            }

            return s.toString();
        }
        else
        {
            return "";
        }
    }
}

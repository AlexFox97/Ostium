package apps.mobile.ostium.Module;

import java.util.ArrayList;

public class CardObject {
    public int cardID;
    public String title;
    public String details;
    public String date;
    public String sourceName = "Source Name";
    public String sourceColour = "#FFFFFF";
    public ArrayList<LocationObject> Locations;

    public CardObject(int cardID, String title, String details, String date, String sourceName, String sourceColour, ArrayList<LocationObject> locations) {
        this.cardID = cardID;
        this.title = title;
        this.details = details;
        this.date = date;
        this.sourceName = sourceName;
        this.sourceColour = sourceColour;
        this.Locations = locations;
    }

    public CardObject(EventGeneric event) {
//        this.cardID = event.cardID;
        this.title = event.getTitle();
        this.details = event.getDescription();
        this.date = event.getStartTime();
//        this.sourceName = event.getSourceName;
//        this.sourceColour = event.getSourceColour;
        this.Locations = event.getLocationTags();
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceColour() {
        return sourceColour;
    }

    public void setSourceColour(String sourceColour) {
        this.sourceColour = sourceColour;
    }

    public ArrayList<LocationObject> getLocations() {
        return Locations;
    }

    public void setLocations(ArrayList<LocationObject> locations) {
        Locations = locations;
    }

    public void addLocation(LocationObject location) {
        Locations.add(location);
    }

    public String getLocationsToString() {
        StringBuilder s = new StringBuilder();
        if (Locations != null) {
            for (LocationObject loc : Locations) {
                s.append(loc.getTitle() + "     ");
            }
            return s.toString();
        } else
            return "error";
    }
}

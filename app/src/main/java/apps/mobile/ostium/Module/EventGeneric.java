package apps.mobile.ostium.Module;

import java.io.Serializable;
import java.util.ArrayList;

public class EventGeneric implements Serializable
{
    public ArrayList<LocationObject> locationTags;
    private String title;
    private String eventType;
    private String description;
    private String startTime;
    private String endTime;
    private String calendarName;
    private LocationObject location;

    public EventGeneric(String title, String eventType)
    {
        this.title = title;
        this.eventType = eventType;
    }

    public ArrayList<LocationObject> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(ArrayList<LocationObject> locationTags) {
        this.locationTags = locationTags;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public void setLocation(double lati, double longi) {
        //Set location in place
        location.setPlace(lati, longi);
    }

    public String getTitle() {
        return title;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

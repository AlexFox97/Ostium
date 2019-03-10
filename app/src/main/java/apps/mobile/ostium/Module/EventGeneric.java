package apps.mobile.ostium.Module;

import apps.mobile.ostium.Objects.LocationObject;

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

    public EventGeneric(String title, String eventType, LocationObject taskLocation, String taskDescription)
    {
        this.title = title;
        this.eventType = eventType;
        this.startTime = "05/10/18 11:00";
        this.endTime = "05/10/18 16:00";
        this.location = taskLocation; //new LocationObject("Uni", 53.3769219, -1.4677611345050374, "Work");
        this.description = taskDescription; // "Generic Description of what I'm doing";
        this.calendarName = "CalendarName";
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

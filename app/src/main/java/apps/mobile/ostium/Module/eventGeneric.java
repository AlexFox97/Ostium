package apps.mobile.ostium.Module;

import java.io.Serializable;

public class eventGeneric implements Serializable{
    private String title;
    private String eventType;
    private String description;
    private String startTime;
    private String endTime;
    private Place location;

    public eventGeneric(String title, String eventType)
    {
        this.title = title;
        this.eventType = eventType;
    }

    public void setLocation(double lati,double longi)
    {
        //Set location in place
        location.setPlace(lati, longi);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}

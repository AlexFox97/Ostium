package apps.mobile.ostium.Module;

public class eventGeneric {
    private String title;
    private String eventType;
    private String description;
    private Integer startTime;
    private Integer endTime;
    private Float longitude;
    private Float latitude;

    public eventGeneric(String title, String eventType)
    {
        this.title = title;
        this.eventType = eventType;
    }

    public void setLocation(Float longi,Float lati)
    {
        longitude = longi;
        latitude = lati;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Integer endTime) {
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

    public Integer getStartTime() {
        return startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }
}

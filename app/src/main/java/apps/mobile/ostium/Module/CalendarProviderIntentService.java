package apps.mobile.ostium.Module;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.CalendarContract;
import apps.mobile.ostium.Objects.LocationObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarProviderIntentService extends IntentService
{
    public static final int RETRIEVE_SUCCESS = 2;
    public static final int RETRIEVE_ERROR = 3;

    public CalendarProviderIntentService(String name)
    {
        super(name);
    }

    public CalendarProviderIntentService()
    {
        super("CalendarProviderIntentService");
    }

    // Invoked on Intent Service Start
    @Override
    protected void onHandleIntent(Intent intent)
    {
        // Get ResultReceiver from the Intent passed to service
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        final ArrayList<Integer> calendars = (ArrayList<Integer>) intent.getSerializableExtra("calendars");

        // Create bundle to store data to send back
        Bundle bundle = new Bundle();
        ArrayList<EventGeneric> events = getEvents(calendars);
        bundle.putSerializable("events", events);

        /*
         * Send data back to UI
         * TODO: Implement success and fail cases for receiver.send
         * */
        receiver.send(RETRIEVE_SUCCESS, bundle);

    }

    @SuppressLint("MissingPermission")
    private ArrayList<EventGeneric> getEvents(ArrayList<Integer> calendarIDs)
    {
        ArrayList<EventGeneric> returnList = new ArrayList<>();
        Cursor cur;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.CALENDAR_ID,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND
                };

        Uri uri = CalendarContract.Events.CONTENT_URI;

        for(Integer item : calendarIDs)
        {
            //Suppressing check for permissions here, all permissions should be granted before this function is called
            cur = cr.query(uri, mProjection, CalendarContract.Events.CALENDAR_ID + " =  '" + item + "'", null, null);

            while (cur.moveToNext())
            {
                String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
                String accountType = cur.getString(cur.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
                String desc = cur.getString(cur.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                String start = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART));
                String end = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTEND));

                LocationObject cantorBuilding = new LocationObject("Cantor", 53.3769219, -1.4677611345050374, "Work");

                EventGeneric event = new EventGeneric(title, accountType, cantorBuilding, desc);
                event.setDescription(desc);
                event.setStartTime(start);
                event.setEndTime(end);

                returnList.add(event);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        Date d = new Date();
        double timeNow = d.getTime();

        for (EventGeneric item : new ArrayList<>(returnList))
        {
            if( Double.parseDouble( item.getStartTime()) < timeNow)
                returnList.remove(item);
            else {
                item.setStartTime(dateFormat.format(new Date(Long.parseLong(item.getStartTime()))));
                item.setEndTime(dateFormat.format(new Date(Long.parseLong(item.getEndTime()))));
            }
        }

        return returnList;
    }
}
package apps.mobile.ostium;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.CalendarContract;

public class androidCalendarHandle extends IntentService
{
    public androidCalendarHandle(String name) {
        super(name);
    }

    public androidCalendarHandle() {
        super("androidCalendarHandle");
    }

    // Invoked on Intent Service Start
    @Override
    protected void onHandleIntent(Intent intent)
    {
        // Get ResultReceiver from the Intent passed to service
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        // Create bundle to store data to send back
        Bundle bundle = new Bundle();

        /*
        * Send data back to UI
        * TODO: Implement success and fail cases for receiver.send
        * */
        receiver.send(1, bundle);
    }

    public void getDataFromEventTable()
    {
        Cursor cur;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
            {
                "_id",
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
            };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Events.EVENT_LOCATION + " = ? ";
        String[] selectionArgs = new String[]{"London"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext())
        {
            // EXAMPLE: Get title
            // String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));

            //TODO: Handle data
        }
    }
}
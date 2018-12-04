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

public class androidCalendarHandle extends IntentService
{
    public static final int RETRIEVE_SUCCESS = 2;
    public static final int RETRIEVE_ERROR = 3;

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

        bundle = getDataFromEventTable(bundle);

        /*
         * Send data back to UI
         * TODO: Implement success and fail cases for receiver.send
         * */
        receiver.send(RETRIEVE_SUCCESS, bundle);
    }

    @SuppressLint("MissingPermission")
    public Bundle getDataFromEventTable(Bundle bundle)
    {
        Cursor cur;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,

                };

        Uri uri = CalendarContract.Events.CONTENT_URI;

        //Suppressing check for permissions here, all permissions should be granted before this function is called
        cur = cr.query(uri, mProjection, null, null, null);

        /*
            TODO: Filter results to only relevant events e.g. No national holidays
            Get Calendar IDs:
                Get list
                Display Alert Dialog
                Handle input

            Select from events
        */

        int count = 1;

        while (cur.moveToNext())
        {

            String key = Integer.toString(count);
            bundle.putString(key ,cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE)));
            count++;
        }

        bundle.putInt("eventCount", count);

        return bundle;
    }
}
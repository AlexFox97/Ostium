package apps.mobile.ostium.Module;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;

public class CalendarHandler {

    @SuppressLint("MissingPermission")
    public static ArrayList getCalendarList(Context context)
    {
        Cursor cur;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME

                };

        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        //Suppressing check for permissions here, all permissions should be granted before this function is called
        cur = cr.query(uri, mProjection, null, null, null);

        ArrayList calendars = new ArrayList<String>();

        while (cur.moveToNext())
        {
            String value = cur.getString(cur.getColumnIndex(CalendarContract.Calendars._ID)) + ": " + cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
            if(!calendars.contains(value))
            {
                calendars.add(value);
            }
        }

        return calendars;
    }


}

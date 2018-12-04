package apps.mobile.ostium.Module;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class CalendarProviderIntentService extends IntentService
{
    public static final int RETRIEVE_SUCCESS = 2;
    public static final int RETRIEVE_ERROR = 3;

    public CalendarProviderIntentService(String name) {
        super(name);
    }

    public CalendarProviderIntentService() {
        super("CalendarProviderIntentService");
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
        receiver.send(RETRIEVE_SUCCESS, bundle);
    }


}
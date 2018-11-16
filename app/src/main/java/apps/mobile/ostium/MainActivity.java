package apps.mobile.ostium;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;


public class MainActivity extends Activity {


//Check permissions in activities before any calls are made
/*
    private static final int MY_CAL_REQ = 1;
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private class CalendaresultReceiver extends ResultReceiver {

        public CalendaresultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //TODO: Handle data received from service
        }

    }
}

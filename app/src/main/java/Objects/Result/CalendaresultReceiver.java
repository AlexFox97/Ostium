package Objects.Result;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

//Check permissions in activities before any calls are made
public class CalendaresultReceiver extends ResultReceiver
{
    public CalendaresultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData)
    {
        //TODO: Handle data received from service
    }
}
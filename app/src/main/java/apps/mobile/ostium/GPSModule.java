package apps.mobile.ostium;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import TransferObjects.GetLocationRequest;
import TransferObjects.ResultStatus;

public class GPSModule
{
    private LocationManager manager;
    private LocationListener listener;

    public GPSModule(LocationManager manager, LocationListener listener)
    {
        this.manager = manager;
        this.listener = listener;
    }

    @SuppressLint("MissingPermission")
    public GetLocationRequest GetLocationNow()
    {
        Location l = manager.getLastKnownLocation("gps");
        if(l != null)
        {
            return new GetLocationRequest(ResultStatus.Success, l);
        }

        return  new GetLocationRequest(ResultStatus.Failed);
    }

    @SuppressLint("MissingPermission")
    public void StartLocationUpdates(int TimePerPing, int distance)
    {
        manager.requestLocationUpdates("gps", TimePerPing, distance, listener);
    }
}
package apps.mobile.ostium.Module;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import apps.mobile.ostium.Objects.Request.GetLocationRequest;
import apps.mobile.ostium.Objects.Result.ResultStatus;

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
        try
        {
            manager.requestLocationUpdates("gps", TimePerPing, distance, listener);
        }
        catch (Exception e)
        {
        }
    }
}
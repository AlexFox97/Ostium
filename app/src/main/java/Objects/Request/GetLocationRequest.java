package Objects.Request;

import android.location.Location;

import Objects.Result.BaseResult;
import Objects.Result.ResultStatus;

public class GetLocationRequest extends BaseResult
{
    public Location location;

    public GetLocationRequest(ResultStatus result)
    {
        super(result);
    }

    public GetLocationRequest(ResultStatus result, Exception e)
    {
        super(result, e);
    }

    public GetLocationRequest(ResultStatus result, Location location)
    {
        super(result);
        this.location = location;
    }
}
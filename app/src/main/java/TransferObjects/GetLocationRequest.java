package TransferObjects;

public class GetLocationRequest extends BaseResult
{
    public double longitude;
    public double latitude;

    public GetLocationRequest(ResultStatus result)
    {
        super(result);
    }

    public GetLocationRequest(ResultStatus result, double longitude, double latitude)
    {
        super(result);
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

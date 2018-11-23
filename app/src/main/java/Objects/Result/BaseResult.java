package Objects.Result;

public class BaseResult
{
    public ResultStatus result;
    public Exception exception;

    public BaseResult(ResultStatus result, Exception exception)
    {
        this.result = result;
        this.exception = exception;
    }

    public BaseResult(ResultStatus result)
    {
        this.result = result;
    }
}
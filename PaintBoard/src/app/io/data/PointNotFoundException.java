package app.io.data;


public class PointNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PointNotFoundException(long time_stemp)
	{
		super("RecordAfter"+time_stemp+"NotExist");
	}
	public PointNotFoundException()
	{
		super("NoRecordExist");
	}
}

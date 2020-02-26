package Info;

public class AppInfo
{
	public static String getRunningPath()
	{
		return System.getProperty("java.class.path");
	}

}

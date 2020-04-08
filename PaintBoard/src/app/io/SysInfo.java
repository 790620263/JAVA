package app.io;

import java.io.File;

public class SysInfo {

	public static String getSavePath()
	{
		return System.getProperty("user.dir") + File.separator + "record.txt";
	}
}


import org.junit.Test;

import com.atcraft.NovelWebAccessor.log.MyLogger;

public class MyLogTest
{

	@Test
	public void test()
	{
		MyLogger logger=MyLogger.getLogger(MyLogger.LogType.HTML);
		logger.logDebug("DebugMessage");
	}

}

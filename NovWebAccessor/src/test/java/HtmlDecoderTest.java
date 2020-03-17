import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import com.atcraft.NovelWebAccessor.dataPack.NovInfo;
import com.atcraft.NovelWebAccessor.web.HtmlDecoder;

public class HtmlDecoderTest
{

	@Test
	public void testGetContext()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testExtractChapter() 
	{
		fail("Not yet implemented");
	}

//	@Test
	public void testExtractBookInfo()
	{
		NovInfo info;
		try
		{
			info=HtmlDecoder.extractBookInfo(HtmlDecoder.getContext(new File("A:\\下载_IDM\\诡秘\\0_1\\0_1.html")));
			System.out.println(info.getName());
			System.out.println(info.getWritter());
			System.out.println(info.getUpdateTime());
			System.out.println(info.getIntroduction());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}


}

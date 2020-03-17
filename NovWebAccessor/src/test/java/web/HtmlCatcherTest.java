package web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.atcraft.NovelWebAccessor.web.HtmlCatcher;

public class HtmlCatcherTest
{

	@Test
	public void test()
	{
		try
		{
			System.out.println(HtmlCatcher.getHtml(new URL("https://www.cnoz.org/0_1/")));
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

}

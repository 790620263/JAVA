package web;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.Test;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.dataPack.NovInfo;
import com.atcraft.NovelWebAccessor.web.HtmlCatcher;
import com.atcraft.NovelWebAccessor.web.HtmlDecoder;

public class HtmlDecoderTest
{

//	@Test
	public void testGetContext()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testExtractChapter() 
	{
		URL url;
		try
		{
			url = new URL("https://www.cnoz.org/0_1/");
			LinkedList<URL> ChapterList=HtmlDecoder.extractChapterList_Remote(url);
			for(URL aChapterLink:ChapterList)
			{
				NovChapter chapter=HtmlDecoder.extractChapter(HtmlCatcher.getDoc(aChapterLink));
				System.out.println(chapter.getContent());
				break;
			}
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

//	@Test
	public void testGetChapterList_Remote() 
	{
		String url="https://www.cnoz.org/0_1/" ;
		try
		{
			LinkedList<URL> list = HtmlDecoder.extractChapterList_Remote(new URL(url));
			System.out.println(list);
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}

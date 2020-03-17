package com.atcraft.NovelWebAccessor;


import java.io.IOException;

import org.junit.Test;

import com.atcraft.NovelWebAccessor.web.BookDownloadTask;

public class DownloadTaskTest
{

//	@Test
	public void testDownload()
	{
		try
		{
			BookDownloadTask task=new BookDownloadTask("https://www.cnoz.org/0_1/");
			task.run();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	@Test
	public void testMultiDownload()
	{
//		String regex="第[1-9][0-9]{0,3}章.*\\S*\\s*";
//		System.out.println(Pattern.matches(regex,"第1章   序 黑色安息日"));

		
	}

}

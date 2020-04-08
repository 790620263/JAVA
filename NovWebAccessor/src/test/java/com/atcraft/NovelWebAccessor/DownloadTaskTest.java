package com.atcraft.NovelWebAccessor;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
			FileOutputStream fos=new FileOutputStream("C:\\Users\\AtCraft\\Desktop\\1.txt");
			task.downloadTo(fos, 1);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testDBDownload()
	{
//		String regex="第[1-9][0-9]{0,3}章.*\\S*\\s*";
//		System.out.println(Pattern.matches(regex,"第1章   序 黑色安息日"));

		try
		{
			BookDownloadTask task=new BookDownloadTask("https://www.cnoz.org/0_1/");
			task.downloadToDB();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

}

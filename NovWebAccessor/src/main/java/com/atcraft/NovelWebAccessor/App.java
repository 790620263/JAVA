package com.atcraft.NovelWebAccessor;

import java.io.IOException;

import com.atcraft.NovelWebAccessor.web.BookDownloadTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
    	dl();
    }
    /**
     * 更新我的书籍
     */
    static void dl()
    {
		try
		{
			//揭棺起驾，诡秘之主
			String[] myBooks= {"https://www.biduo.cc/biquge/53_53835/"
					,"https://www.cnoz.org/0_1/"
			};
			for(String str:myBooks)
			{
				BookDownloadTask task=new BookDownloadTask(str);
//				task.multi_download_since(task.url,1);
				task.multi_download_since_last();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
    }
}

package com.atcraft.NovelWebAccessor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.atcraft.NovelWebAccessor.db.NovInfoDBAccessor;
import com.atcraft.NovelWebAccessor.web.BookDownloadTask;

public class BookManager
{
	ArrayList<String> bookList;
	NovInfoDBAccessor db;
	public BookManager(ArrayList<String> list)
	{
		bookList=new ArrayList<String>();
		try
		{
			bookList.addAll(list);
			init();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	void init() throws SQLException
	{
		db=new NovInfoDBAccessor(null);
//		for(String url:db.getAllBookUrl())
//		{
//			bookList.add(url);
//		}
	}
	 /**
	    * 更新我的书籍
	    */
	   public void updateBooks()
	   {
		   ExecutorService pool=Executors.newCachedThreadPool();
		   for(String url:bookList)
		   {
			   try
			{
				BookDownloadTask task=new BookDownloadTask(url);
				task.multi_download_since_last();
//				pool.submit(task);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		   }
	   }
	   public void addBookToDB(String[] urls)
	   {
		   ExecutorService pool=Executors.newCachedThreadPool();
		   try
		{
			pool.awaitTermination(100,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		   ArrayList<Future<Boolean>> stateList=new ArrayList<Future<Boolean>>();
		   for(String url:urls)
		   {
			   try
			{
				
				BookDownloadTask task=new BookDownloadTask(url);
				stateList.add(pool.submit(task));
				
				
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		   }
		   while(true)
			{
				for(Future<Boolean> f:stateList)
				{
					try
					{
						System.out.println(f.get());
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					} catch (ExecutionException e)
					{
						e.printStackTrace();
					}
				}
			}
	   }
	   void getBookFromDB()
	   {
		   
	   }
	   
}

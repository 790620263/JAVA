package download;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import download.web.BookDownloadTask;




public class BookManager 
{
	ArrayList<String> bookList;
	String savePath;
	public BookManager(ArrayList<String> list,String savePath)
	{
		bookList=new ArrayList<String>();
		bookList.addAll(list);
		this.savePath=savePath;
	}
	
    int retry=0;
    void retry(){
        System.out.println("Retrying  time= "+retry);
        shutdown();
        dlBook();
    }
    
	ExecutorService pool;
	   public void dlBook()
	   {
		   pool=Executors.newCachedThreadPool();
		   for(String url:bookList)
		   {
			try {
				BookDownloadTask task= new BookDownloadTask(url,savePath);
				pool.submit(task);
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		   }
	   }
	   public void  shutdown()
	   {
		   pool.shutdownNow();
		   System.out.println("TASK HAN'S SHUTDOWN");
	   }
	
	   
}

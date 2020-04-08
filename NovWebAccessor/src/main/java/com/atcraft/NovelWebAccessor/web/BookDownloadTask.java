package com.atcraft.NovelWebAccessor.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.dataPack.NovInfo;
import com.atcraft.NovelWebAccessor.db.ChapterDBAccessor;
import com.atcraft.NovelWebAccessor.db.NovInfoDBAccessor;

import tool.CharsetDectector;

public class BookDownloadTask implements Callable<Boolean>{
	Logger log;
	protected String novSavePath="A:\\库\\novSavePath";
	URL url;//小说的首页
	
	PrintWriter localFileWriter;
	
	/**
	 * 
	 * @param url 小说的首页链接
	 * @throws IOException
	 */
	public BookDownloadTask(String url) throws IOException
	{
		log=Logger.getLogger("DownloadTask");
		this.url=new URL(url);
	}
	public BookDownloadTask(String url,String bookSavePath) throws IOException
	{
		this(url);
		this.novSavePath=bookSavePath;
	}
	
	/**
	 * 单线程下载该书籍
	 * @param url 书籍目录页
	 */
	public void download()
	{
		try
		{
			NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
			
			//创建本地写入流
			localFileWriter=new PrintWriter(new File(novSavePath+File.separator+info.getName()+".txt"));
			
			write(info);
			
			LinkedList<URL> ChapterList=HtmlDecoder.extractChapterList_Remote(url);
			int index=1;
			for(URL aChapterLink:ChapterList)
			{
				NovChapter chapter=HtmlDecoder.extractChapter(HtmlCatcher.getDoc(aChapterLink));
				write(chapter,index);
				index++;
			}
			
			localFileWriter.close();
		}  catch (IOException e)
		{
			localFileWriter.close();
			log.error(e);
		}
	}
	
	/**
	 * 
	 * @param outStream 输出Object：1.图书信息，2.第sinceIndexNum章节信息...
	 * @param sinceIndexNum 从第几章读取？
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean downloadTo(OutputStream outStream,int sinceIndexNum) throws MalformedURLException, IOException, InterruptedException, ExecutionException
	{
		try
		{
			NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
			
			//创建写入流
			ObjectOutputStream out=new ObjectOutputStream(outStream);
			
			//读取章节列表
			LinkedList<URL> ChapterAddressList=HtmlDecoder.extractChapterList_Remote(url);
			
			if(sinceIndexNum>ChapterAddressList.size())throw new IndexOutOfBoundsException("指定的起始章节数不合法");
			
			ExecutorService pool=Executors.newFixedThreadPool(10);
			LinkedList<Future<NovChapter>> chapterList=new LinkedList<Future<NovChapter>>();
			
			//第几章节
			int index=1;
			for(URL aChapterLink:ChapterAddressList)
			{
				if(index<sinceIndexNum)
				{
					index++;
					continue;
				}
				
				int i=(int)(Math.random()*500);////做一个随机延时，防止网站屏蔽
				Thread.sleep(i);
				
				ChapterDownloadTask task=new ChapterDownloadTask(aChapterLink);
				Future<NovChapter> future=pool.submit(task);
				chapterList.add(future);
				
				Future<NovChapter> f=chapterList.peek();
				if(f.isDone())
				{
	log.info("listItemNum="+chapterList.size()+"；章节数"+index+"："+f.get().getTitle());
					out.writeObject(f.get());
					chapterList.remove();
					index++;
				}
			}
			
			while(chapterList.size()>0)
			{
				Future<NovChapter> future=chapterList.poll();
				out.writeObject(future.get());
log.debug("listItemNum="+chapterList.size()+"；章节数"+index+"："+future.get().getTitle());
				index++;
			}
			localFileWriter.close();
		}  catch (IOException e)
		{
			localFileWriter.close();
			log.error(e);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		return true;
		
		
	}
	
	
	NovInfoDBAccessor db_info;ChapterDBAccessor db_ch;
	private void initDB() throws SQLException, MalformedURLException, IOException {
	NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
	
	//创建数据库访问接口
	db_info=new NovInfoDBAccessor(info);
	db_ch=new ChapterDBAccessor(info.getName());}
	public boolean downloadToDB()
	{
		try
		{
			initDB();
			
			int sinceIndexNum=0;
			if(db_info.isBookExist()) {
				sinceIndexNum=db_info.getExistChapterNum();
			}else
			{
				db_info.createBook(url.toString());
				
			}
			//读取章节列表
			LinkedList<URL> ChapterAddressList=HtmlDecoder.extractChapterList_Remote(url);
			
			if(ChapterAddressList.size()<=sinceIndexNum)return true;
			
			ExecutorService pool=Executors.newFixedThreadPool(10);
			LinkedList<Future<NovChapter>> chapterList=new LinkedList<Future<NovChapter>>();
			
			//第几章节
			int index=1;
			for(URL aChapterLink:ChapterAddressList)
			{
				if(index<sinceIndexNum)
				{
					index++;
					continue;
				}
				if(index==sinceIndexNum)
				{
					db_ch.deleteChapter(index);
				}
				
				int i=(int)(Math.random()*500);////做一个随机延时，防止网站屏蔽
				Thread.sleep(i);
				
				ChapterDownloadTask task=new ChapterDownloadTask(aChapterLink);
				Future<NovChapter> future=pool.submit(task);
				chapterList.add(future);
				
				Future<NovChapter> f=chapterList.peek();
				if(f.isDone())
				{
	log.info("listItemNum="+chapterList.size()+"；章节数"+index+"："+f.get().getTitle());
					db_ch.insertChapter(index, f.get());
					chapterList.remove();
					index++;
				}
			}
			
			while(chapterList.size()>0)
			{
				Future<NovChapter> future=chapterList.poll();
				db_ch.insertChapter(index, future.get());
log.debug("listItemNum="+chapterList.size()+"；章节数"+index+"："+future.get().getTitle());
				index++;
			}
			localFileWriter.close();
		}  catch (IOException e)
		{
			log.error(e);
		} catch (InterruptedException e)
		{
			log.error(e);
		} catch (ExecutionException e)
		{
			log.error(e);
		} catch (SQLException e)
		{
			log.error(e);
		}
		return true;
	}
	
	/**
	 * @return 是否读取了新章节
	 * @param url
	 * @param sinceIndexNum 从这一章开始下载
	 * @param line 从这一行开始写入
	 */
	public boolean multi_download_since(int sinceChapterIndex,long line)
	{
		
		try
		{
			NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
			
			//创建本地文件追加s写入流
			File file=new File(novSavePath+File.separator+info.getName()+".txt");
			file.getParentFile().mkdirs();//防止路径不存在
			
			//读出前line行
			LinkedList<String> lines=new LinkedList<String>();
			BufferedReader reader=new BufferedReader(new FileReader(file));
			for(long nowLine=1;nowLine<=line;nowLine++) {
				lines.add(reader.readLine());
			}
			reader.close();
			
			file.delete();
			
			localFileWriter=new PrintWriter(new FileOutputStream(file,true),true);
			
			//将前line行写回，（即将后面的行删除）
			for(long nowLine=1;nowLine<=line;nowLine++) {
				localFileWriter.println(lines.poll());
			}
			
			LinkedList<URL> ChapterAddressList=HtmlDecoder.extractChapterList_Remote(url);
			
			if(sinceChapterIndex>ChapterAddressList.size())throw new IndexOutOfBoundsException("指定的起始章节数不合法");
			
			ExecutorService pool=Executors.newFixedThreadPool(10);
			LinkedList<Future<NovChapter>> chapterList=new LinkedList<Future<NovChapter>>();
			
			//第几章节
			int index=1;
			
			for(URL aChapterLink:ChapterAddressList)
			{
				if(index<sinceChapterIndex)
				{
					index++;
					continue;
				}
				
				int i=(int)(Math.random()*500);////做一个随机延时，防止网站屏蔽
				Thread.sleep(i);
				
				ChapterDownloadTask task=new ChapterDownloadTask(aChapterLink);
				Future<NovChapter> future=pool.submit(task);
				chapterList.add(future);
				
				Future<NovChapter> f=chapterList.peek();
				if(f.isDone())
				{
	log.info("listItemNum="+chapterList.size()+"；章节数"+index+"："+f.get().getTitle());
					write(f.get(),index);
					chapterList.remove();
					index++;
				}
			}
			
			while(chapterList.size()>0)
			{
				Future<NovChapter> future=chapterList.poll();
				write(future.get(),index);
//				chapterList.remove();
log.debug("listItemNum="+chapterList.size()+"；章节数"+index+"："+future.get().getTitle());
				index++;
			}
			localFileWriter.close();
		}  catch (IOException e)
		{
			localFileWriter.close();
			log.error(e);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	
	
	/**
	 * @return 是否成功
	 * @param url
	 * @param sinceIndexNum 从这一章开始下载
	 */
	public boolean multi_download_since(int sinceIndexNum)
	{
		
		try
		{
			NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
			
			//创建本地文件追加s写入流
			File file=new File(novSavePath+File.separator+info.getName()+".txt");
			file.getParentFile().mkdirs();//防止路径不存在
			localFileWriter=new PrintWriter(new FileOutputStream(file,true),false, Charset.forName("UTF-8"));
			
			if(!(file.length()>0))write(info);//非空的话就不写
			
			LinkedList<URL> ChapterAddressList=HtmlDecoder.extractChapterList_Remote(url);
			
//			if(sinceIndexNum==ChapterAddressList.size()+1)return false;//已经为最新了，无需更新
			if(sinceIndexNum>ChapterAddressList.size())throw new IndexOutOfBoundsException("指定的起始章节数不合法");
			
			ExecutorService pool=Executors.newFixedThreadPool(10);
			LinkedList<Future<NovChapter>> chapterList=new LinkedList<Future<NovChapter>>();
			
			//第几章节
			int index=1;
			for(URL aChapterLink:ChapterAddressList)
			{
				if(index<sinceIndexNum)
				{
					index++;
					continue;
				}
				
				int i=(int)(Math.random()*500);////做一个随机延时，防止网站屏蔽
				Thread.sleep(i);
				
				ChapterDownloadTask task=new ChapterDownloadTask(aChapterLink);
				Future<NovChapter> future=pool.submit(task);
				chapterList.add(future);
				
				Future<NovChapter> f=chapterList.peek();
				if(f.isDone())
				{
	log.info("listItemNum="+chapterList.size()+"；章节数"+index+"："+f.get().getTitle());
					write(f.get(),index);
					chapterList.remove();
					index++;
				}
			}
			
			while(chapterList.size()>0)
			{
				Future<NovChapter> future=chapterList.poll();
				write(future.get(),index);
//				chapterList.remove();
log.debug("listItemNum="+chapterList.size()+"；章节数"+index+"："+future.get().getTitle());
				index++;
			}
			localFileWriter.close();
		}  catch (IOException e)
		{
			localFileWriter.close();
			log.error(e);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * @param url
	 * @return boolean是否读取了新章节
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public boolean multi_download_since_last() throws MalformedURLException, IOException {
		
		long[] returnData=getLastLocalChapterIndexAndLine();
		int chapterNum=(int) returnData[0];
		long line=returnData[1];
		return multi_download_since( chapterNum,line-1);
		
	}
	protected long[] getLastLocalChapterIndexAndLine() throws IOException
	{
		NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
		File f=new File(novSavePath+File.separator+info.getName()+".txt");
		
//		if(!f.exists())multi_download();
//-------------------------确定本地文件的最新章节数及相应文本行数------------------------------------
		String charset=CharsetDectector.getEncode(f);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
		// 章节数
		int chapterNum = 0;
		long lineindex=0,tmp=0;
		
		String content = null;
		String regex="第[1-9][0-9]{0,3}章.*";
		// 创建标题匹配模式
		Pattern pattern = Pattern.compile(regex);
		
		while ((content = reader.readLine()) != null)
		{
			tmp++;
			
			boolean matchAny = false;
			Matcher matcher = pattern.matcher(content);
			matchAny =matcher.matches();

			if (content.length() > 0)
			{
				if (matchAny)// 是章节名
				{
					chapterNum++;
					lineindex=tmp;
				} else
				{
					continue;
				}
			}
		}
		reader.close();

		return new long[]{chapterNum,lineindex};
	}

	
	
	/**
	 * 多线程下载该书籍
	 * @param url 书籍目录页
	 */
	public void multi_download()
	{
		//从第一章下载
		multi_download_since(1);
	}

	
	/**
	 * 将章节写入本地
	 * @param str
	 * @throws IOException 
	 */
	
	void write(NovInfo info) throws IOException
	{
		localFileWriter.println("书名："+info.getName());
		localFileWriter.println("作者："+info.getWritter());
		localFileWriter.println("最后更新："+info.getUpdateTime());
		localFileWriter.println("简介："+info.getIntroduction());
		localFileWriter.flush();
	}
	void write(NovChapter chapter,int index) throws IOException
	{
//System.out.println(chapter.getTitle());	
		localFileWriter.println();
		localFileWriter.print("第"+index+"章   ");
		localFileWriter.println(chapter.getTitle());
		localFileWriter.println();
		localFileWriter.println(chapter.getContent());
		localFileWriter.flush();
	}
	

	public Boolean call() throws IOException
	{
//		boolean isSuccess=multi_download_since_last();
		boolean isSuccess=downloadToDB();
System.out.println("Download State="+isSuccess);
		return isSuccess;
	}
}

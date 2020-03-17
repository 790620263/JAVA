package com.atcraft.NovelWebAccessor.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.dataPack.NovInfo;

import tool.CharsetDectector;

public class BookDownloadTask implements Runnable
{
	Logger log;
	protected static final String novSavePath="A:\\库\\novSavePath"; 
	URL url;
	
	PrintWriter localFileWriter;
	public BookDownloadTask(String url) throws IOException
	{
		log=Logger.getLogger("DownloadTask");
		this.url=new URL(url);
	}
	/**
	 * 下载该书籍
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
	 * @return 是否读取了新章节
	 * @param url
	 * @param sinceIndexNum 从这一章开始下载
	 */
	public boolean multi_download_since(int sinceIndexNum)
	{
		if(sinceIndexNum<1)throw new IndexOutOfBoundsException("指定的起始章节数不合法");
		try
		{
			NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
			
			//创建本地文件追加s写入流
			File file=new File(novSavePath+File.separator+info.getName()+".txt");
			file.getParentFile().mkdirs();//防止路径不存在
			localFileWriter=new PrintWriter(new FileOutputStream(file,true),false, Charset.forName("UTF-8"));
			
			if(!(file.length()>0))write(info);//非空的话就不写
			
			LinkedList<URL> ChapterAddressList=HtmlDecoder.extractChapterList_Remote(url);
			
			if(sinceIndexNum==ChapterAddressList.size()+1)return false;//已经为最新了，无需更新
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
			// TODO Auto-generated catch block
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
		NovInfo info=HtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url.toString()));
		File f=new File(novSavePath+File.separator+info.getName()+".txt");
		
		if(!f.exists())multi_download();
//-------------------------确定本地最新章节数------------------------------------
		String charset=CharsetDectector.getEncode(f);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
		// 章节数
		int chapterNum = 0;
		
		String content = null;
		String regex="第[1-9][0-9]{0,3}章.*";
		// 创建标题匹配模式
		Pattern pattern = Pattern.compile(regex);
		
		while ((content = reader.readLine()) != null)
		{
			boolean matchAny = false;
			Matcher matcher = pattern.matcher(content);
			matchAny =matcher.matches();

			if (content.length() > 0)
			{
				if (matchAny)// 是章节名
				{
					chapterNum++;
				} else
				{
					continue;
				}
			}
		}
		reader.close();
//----------------------------------------------------------------------------------
		return multi_download_since( chapterNum+1);
		
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
	
	public void run()
	{
//		download(url);
//		multi_download();
	}
}

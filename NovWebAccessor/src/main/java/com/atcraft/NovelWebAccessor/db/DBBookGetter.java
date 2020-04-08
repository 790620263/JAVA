package com.atcraft.NovelWebAccessor.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.dataPack.NovInfo;

public class DBBookGetter
{
	NovInfoDBAccessor db_n;
	ChapterDBAccessor db_c;
	String bookName;
	private String novSavePath="A:\\库\\novSavePath";
	void init(String bookName) throws SQLException
	{
		db_n=new NovInfoDBAccessor(null);
		db_c=new ChapterDBAccessor(bookName);
		this.bookName=bookName;
	}
	void initFile()
	{
		NovInfo info;
		try
		{
			info = db_n.getInfo(bookName);
			
			//创建本地文件写入流
			File file=new File(novSavePath+File.separator+info.getName()+".txt");
			file.getParentFile().mkdirs();//防止路径不存在
			if(file.exists())file.delete();
			
			localFileWriter=new PrintWriter(new FileOutputStream(file,true),true,Charset.forName("UTF-8"));
			
			
			
			for(int i=1;i<=db_n.getExistChapterNum();i++)
			{
				write(db_c.getChapter(i),i);
			}
			
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	PrintWriter localFileWriter;
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
}

package com.atcraft.NovelWebAccessor.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHander
{

	final static int COMPRESSION_LEVEL=9;//最高压缩级别
	public static void main(String[] args)
	{
		try
		{
//			zip("D:\\Software\\LEGUI GLOBAL(日文游戏乱码转换器) V2.4.0 绿色版");
			unzip(new File("D:\\Software\\LEGUI GLOBAL(日文游戏乱码转换器) V2.4.0 绿色版.zip"),"D:\\Software\\new");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finish");

	}
	/**
	 * 
	 * @param targetPath 要压缩的目录
	 * @param savePath 压缩包保存的目录
	 * @param Zname 压缩包名字
	 * @param comment 压缩包描述
	 * @throws IOException
	 */
	public static void zip(String targetPath,String savePath,String ZipName) throws IOException
	{
		ZipOutputStream zout=new ZipOutputStream(
				new FileOutputStream(savePath+File.separator+ZipName), Charset.forName("UTF-8"));
	    zout.setLevel(COMPRESSION_LEVEL);
	    
	    BufferedInputStream in;
	    LinkedList<File> list= Scaner.getfilelist(new File(targetPath));
	    String pathinZip;
	    byte[] data=new byte[1024];
	    //
	    for(File f:list)
	    {
	    	System.out.println("Compress "+f.getAbsolutePath());
	    	
	    	in=new BufferedInputStream(new FileInputStream(f));
	    	pathinZip=Scaner.getRelitivePath(targetPath, f.getAbsolutePath());
	    	zout.putNextEntry(new ZipEntry(pathinZip));
	    	
	    	//写入一个文件
	    	int len=0;
	    	while((len=in.read(data))>0)
	    	{
	    		zout.write(data,0,len);
	    		zout.flush();
	    	}
	    	in.close();
	    	
	    }
	    
	    zout.close();
	}
	/**
	 * 压缩包保存在当前目录（和要压缩的目录在同一个目录）
	 * @param targetPath 要压缩的目录
	 * @throws IOException 
	 */
	public static void zip(String targetPath) throws IOException
	{
		File target=new File(targetPath);
		zip(targetPath,target.getParent(),target.getName()+".zip");
	}

	
	
	
	public static void unzip(File zipFile ,String savePath) throws IOException
	{
		ZipInputStream in=new ZipInputStream(new FileInputStream(zipFile),Charset.forName("UTF-8"));
		new File(savePath).mkdirs();
		
		BufferedOutputStream out=null;
		ZipEntry entry;
		String relativePath=null;

		while((entry=in.getNextEntry())!=null)
		{
			if(entry.isDirectory())
			{
				relativePath=entry.getName();
			}else
			{
				String foutPath=savePath+File.separator+relativePath+entry.getName();
				
				//确保父文件夹存在
				File fout=new File(foutPath);
				fout.getParentFile().mkdirs();
				
				System.out.println(foutPath);
				out=new BufferedOutputStream(new FileOutputStream(foutPath));
				
		    	//读一个文件
				byte[] data=new byte[1024];
		    	int len=0;
		    	while((len=in.read(data))>0)
		    	{
		    		out.write(data,0,len);
		    		out.flush();
		    	}
		    	out.close();
			}
		}
		in.close();
		
		
	}
}

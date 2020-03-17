
package com.atcraft.NovelWebAccessor.txt_tool;

import java.util.LinkedList;

import tool.CharsetDectector;

import java.io.*;

public class ADRemover
{

	public static LinkedList<String> adList;

	public ADRemover()
	{
		adList = new LinkedList<String>();
//		adList.add("<a href=\"http://www.mianhuatang.cc\" target=\"_blank\">棉花糖小说网WWW.Mianhuatang.CC</a>");
//		adList.add("（wwW.80txt.com 无弹窗广告）");
//		adList.add("[www.qiushu.cc 超多好看小说]");
//		adList.add(" <a href=http://www.qidian.com>起点中文网www.qidian.com欢迎广大书友光临阅读，最新、最快、最火的连载作品尽在起点原创！</a>");
//		adList.add("[www.qiushu.cc 超多好看小说]");

		String appPath=new File("").getAbsolutePath();
		System.out.println(appPath);
		File info=new File(appPath+File.separator+"ADinfo.ini");
		System.out.println(info.getAbsolutePath());
		
		try
		{
			if(info.exists())
			{
				BufferedReader reader=new BufferedReader(new FileReader(info));
				String ad=null;
				while((ad=reader.readLine())!=null)
				{
					adList.add(ad);
				}
				reader.close();
			}else {
			info.createNewFile();
			}
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 此方法将dir下的所有txt文档的匹配字符串全部删除
	 * 
	 * @param dir
	 * @param saveRoot
	 */
	public void handleAllTxt(File dir, String saveRoot)
	{

		saveRoot = saveRoot + File.separator + dir.getName() + "_NoAD";

		circle(dir, saveRoot);
	}

	private static void circle(File dir, String saveRoot)
	{
		File[] children = dir.listFiles();
		File thisFile;

		for (int index = 0; index < children.length; index++)
		{
			String savePath = saveRoot;
			new File(savePath).mkdirs();
			thisFile = children[index];
			if (thisFile.isDirectory())
			{
				savePath = savePath + File.separator + thisFile.getName();
				circle(thisFile, savePath);
			} else
			{
				if (thisFile.getName().toLowerCase().endsWith(".txt"))
				{
					try
					{
						delAD(thisFile, savePath);
						System.out.println("DEL  :  "+thisFile.getName());
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

	}

	private static void delAD(File theOld,String savePath) throws IOException
	{
		File theNew=new File(savePath+File.separator+theOld.getName());
		FileOutputStream fos=new FileOutputStream(theNew);
		OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
		BufferedWriter out=new BufferedWriter(osw);
		FileInputStream fis=new FileInputStream(theOld);
		InputStreamReader isr=new InputStreamReader(fis,CharsetDectector.getEncode(theOld));
		BufferedReader in=new BufferedReader(isr);

		StringBuffer buf=new StringBuffer();
		//读取文本内容
		String temp=null;
		while((temp=in.readLine())!=null)
		{
			buf.append(temp);
			buf.append("\n");
		}

		String str=buf.toString();
		for(String ad:adList)
		{
			str.replaceAll(ad, "");
		}
		out.write(str);out.newLine();

		out.flush();
		out.close();
		in.close();
	}

}

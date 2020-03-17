package com.atcraft.NovelWebAccessor.file;

/**
*获取目录大小
*将文件（夹）按A⇒Z/大小顺序/时间顺序排列
*/

import java.io.*;
import java.util.*;

public class FileSort
{
	
	//获得一个目录的大小
	static private long getDirSize(String path)
	{
		long size=0;

		File f=new File(path);
		File[] fs=f.listFiles();

		for (File tmp:fs)
		{
			if (tmp.isDirectory())
			{
				size = size + getDirSize(tmp.getAbsolutePath());
			}
			else//是文件
			{
				size = size + tmp.length();
			}
		}
		return size;
	}
	
	static public long getSize(File f)
	{
		if(f.isDirectory())
		{
			return getDirSize(f.getAbsolutePath());
		}
		else
		{
			return f.length();
		}
	}

	
	
	
	//按字母顺序（A⇒Z）排列（文件与文件夹不分先后）
	static public File[] SortInWord(File[] fs)
	{
		Arrays.sort(fs);
		return fs ;
	}
	//按字母顺序（A⇒Z）排列（排完文件夹排文件）
		static public File[] SortInWord_DirPriority(File[] fs)
		{
			int num=fs.length;

			File f=null;File tmp=null;
			int j=0;
			for (int i=0;i < num;i++)
			{
				f = fs[i];
				if (f.isDirectory())
				{
					//交换fs[i],fs[j]
					tmp = fs[j];
					fs[j] = f;
					fs[i] = tmp;
					j++;
				}
			}
			//排序文件夹
			Arrays.sort(fs, 0, j);
			//排序文件
			Arrays.sort(fs, j, num - 1);

			return  fs;
		}

	
	 //按文件大小排列（基于插入排序算法实现）
	 public static File[] SortInSize_BigToSmall(File[] fs)
	 {
		 int num=fs.length;
		 File temp;
		 
		long[] fSize =new long[num];
		 
		for(int i=0;i<num;i++)
		{
			fSize[i]=FileSort.getSize(fs[i]);
		}
		
		 int i=0;
		 long tempSize=0;
		 //下标由0至i的元素已排序，将下标为j的插入
		 for(int j=1;j<=num-1;j++)
		 {
			 i=j-1;
			 
			 temp=fs[j];
			 tempSize=fSize[j];
			 
			 while(i>=0 && fSize[i]<tempSize)
			 {
				 fSize[i+1]=fSize[i];
				 fs[i+1]=fs[i];
				 
				 i--;
			 }
			 fSize[i+1]=tempSize;
			 fs[i+1]=temp;
		 }
		 
		 return fs;
	 }
	 
	//按文件大小排列（基于插入排序算法实现）
	public static File[] SortInSize_SmallToBig(File[] fs)
	{
		int num=fs.length;
		File temp;

		long[] fSize =new long[num];

		for(int i=0;i<num;i++)
		{
			fSize[i]=FileSort.getSize(fs[i]);
		}

		int i=0;
		long tempSize=0;
		//下标由0至i的元素已排序，将下标为j的插入
		for(int j=1;j<=num-1;j++)
		{
			i=j-1;

			temp=fs[j];
			tempSize=fSize[j];

			while(i>=0 && fSize[i]>tempSize)
			{
				fSize[i+1]=fSize[i];
				fs[i+1]=fs[i];

				i--;
			}
			fSize[i+1]=tempSize;
			fs[i+1]=temp;
		}

		return fs;
	}
	 
	 
}


package download;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import download.BookManager;
 
/**
 * 
 *https://www.cnoz.org/0_1/
 *https://www.biduo.cc/biquge/53_53835/
 **/
public class AndroidApp 
{
	protected static String configPath;
    protected static String savePath="/storage/emulated/0/BaiduNetdisk/小说";
    static ArrayList<String> toDownload;
	
	public static void main(String[] args)
    {
		try
		{
			loadConfig();
			
			BookManager m=new BookManager(toDownload,savePath);
	    	m.dlBook();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
    	
    }
   
	static void loadConfig() throws FileNotFoundException
	{
		configPath="/storage/emulated/0/AppProjects/NovCatcher/src/config.ini";
		System.out.println("ConfigPath="+configPath);
		toDownload=new ArrayList<String>();
		BufferedReader r=new BufferedReader(new FileReader(configPath));
		String str;
		try
		{
			while((str =r.readLine())!=null)
			{
				//跳过开头标记有#的书
				if(str.startsWith("#"))continue;
				//@后面是书名注释
				str=str.split("@")[0];
//System.out.println(str);
				toDownload.add(str);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
}

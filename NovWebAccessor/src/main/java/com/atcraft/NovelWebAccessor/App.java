package com.atcraft.NovelWebAccessor;
import java.util.ArrayList;

/**
 * 
 *https://www.cnoz.org/0_1/
 *https://www.biduo.cc/biquge/53_53835/
 **/
public class App 
{
	protected static String configPath;
    static ArrayList<String> toDownload;
	
	public static void main(String[] args)
    {
			toDownload=new ArrayList<String>();
//			toDownload.add("http://www.biquge.tv/10_10711/");//
//			toDownload.add("http://www.biquge.tv/34_34762/");
//			toDownload.add("https://www.cnoz.org/0_1/");
			toDownload.add("https://www.biduo.cc/biquge/53_53835/");
			
			
			
			BookManager m=new BookManager(toDownload);
	    	m.updateBooks();
    	
    }
   
//	static void loadConfig() throws FileNotFoundException
//	{
//		configPath=System.getProperty("java.class.path").split(";")[0]+File.separator+"config.ini";
//		System.out.println("ConfigPath="+configPath);
//		toDownload=new ArrayList<String>();
//		BufferedReader r=new BufferedReader(new FileReader(configPath));
//		String str;
//		try
//		{
//			while((str =r.readLine())!=null)
//			{
//				toDownload.add(str);
//			}
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
}

package Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ShellHandle
{
//Process exec(String command)   
//    在单独的进程中执行指定的字符串命令。  
//Process exec(String command, String[] envp)   
//    在指定环境的单独进程中执行指定的字符串命令。  
//Process exec(String command, String[] envp, File dir)   
//    在有指定环境和工作目录的独立进程中执行指定的字符串命令。  
//Process exec(String[] cmdarray)   
//    在单独的进程中执行指定命令和变量。   
//Process exec(String[] cmdarray, String[] envp)   
//    在指定环境的独立进程中执行指定命令和变量。   
//Process exec(String[] cmdarray, String[] envp, File dir)   
//    在指定环境和工作目录的独立进程中执行指定的命令和变量。

	/**
	 * 
	 * @param cmd
	 * @return String[0]=outStreamReturn,String[1]=ErrStreamReturn
	 * @throws IOException
	 */
	public static String[] shell(String cmd) throws IOException
	{
		Process p = Runtime.getRuntime().exec(cmd);

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
		StringBuilder OutStrBuilder = new StringBuilder();
		@SuppressWarnings("unused")
		int len=0;
		char[] data = new char[512];
		while ((len = reader.read(data)) > 0)
		{
			OutStrBuilder.append(data);
		}

		StringBuilder ErrStrBuilder = new StringBuilder();
		reader = new BufferedReader(new InputStreamReader(p.getErrorStream(), Charset.forName("GBK")));
		while ((len = reader.read(data)) > 0)
		{
			ErrStrBuilder.append(data);
		}

		p.destroy();
		String[] result = new String[] { OutStrBuilder.toString(), ErrStrBuilder.toString() };
		return result;
	}

	/**
	 * 速度较快，无返回值
	 * @param cmd
	 * @throws IOException
	 */
	public static void shellRapid(String cmd) throws IOException
	{
		Runtime.getRuntime().exec(cmd);
	}
	public static boolean shellShutdown(int seconds)
	{
		String[] result;
		try
		{
			result = shell("shutdown -s -t 3600");
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		System.out.println(result[1]);
		if (result[1] == "")
		{
			return true;
		} else
		{
			return false;
		}
	}


	public static void main(String[] strs)
	{
		shellShutdown(3600);
//		try
//		{
//			System.out.println(shell("ping 127.0.0.1"));
//			
//			System.out.println("Finish.");
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}
}

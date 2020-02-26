package txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NovelCutter
{

	/**
	 * 
	 * @param filePath 要切割的TXT文件路径
	 * @param regex    正则表达式（章节名）匹配模式
	 * @throws IOException
	 */
	public static void cut(String filePath, String regex,String charSet) throws IOException
	{
		// 创建保存目录
		String saveDir = new File(filePath).getParent() + File.separator + new File(filePath).getName() + "_cutted";
		new File(saveDir).mkdirs();
		System.out.println(saveDir);
		
		//创建读写流
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),charSet));
		String savePath = null;
		BufferedWriter out = null;

		// 创建标题匹配模式
		Pattern pattern = Pattern.compile(regex);
		String content = null;

		while ((content = reader.readLine()) != null)
		{
			Matcher matcher = pattern.matcher(content);
			boolean isMatch = matcher.matches();
			if (content.length() > 0)
			{
				if (isMatch)
				{
					String cutPartName =content.stripLeading();//去除前导空格
					savePath = saveDir + File.separator + cutPartName+".txt";
		System.out.println(savePath);
		
					//防止出现重复章节致错
					File f=new File(savePath);
					if(f.exists()) f.delete();
					
					//关闭先前章节的文件流
					if(out!=null)out.close();
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath),charSet));
				} else
				{
					out.write(content);
					out.write("\n");
					out.flush();
				}
			}
		}

	}
}

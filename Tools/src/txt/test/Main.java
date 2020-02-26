package txt.test;
import java.io.File;

import txt.CharSetTransfer;


public class Main
{

	//GBK转UTF-8
	public static void main(String[] args) {
		//todo:选定要转换的文件所属目录
		File dir=new File("A:\\库\\cm");
		CharSetTransfer.handleAllTxt(dir, dir.getParent());
		System.out.println("finish");
	}
	
//	public static void main(String[] args)
//	{
//		//\s匹配任意不可见字符，.匹配任意非换行符字符，*匹配尽可能多次
//		//String regex=".*第.*章.*\\s*";
//		String regex="催眠短篇集.*\\s*";
//		String path="A:\\库\\cm\\催眠萝莉小说合集\\《催眠类系列精品集V1》TXT\\《催眠短篇集》作者：多人.txt";
//		String charSet="GBK";
//		try
//		{		
//			NovelCutter.cut(path, regex,charSet);
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
////		一个测试
////		Pattern pattern = Pattern.compile(regex);
////		Matcher matcher = pattern.matcher("催眠眼镜 第01章 表姐佳佳\n");
////		boolean isMatch = matcher.matches();
////		System.out.println(isMatch);
//	}
	

}

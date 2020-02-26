
package txt;

import java.io.*;



public class CharSetTransfer {





	/**
	 * 此方法将dir下的所有txt文档全部转为UTF-8编码
	 * @param dir
	 * @param saveRoot
	 */
	public static void handleAllTxt(File dir,String saveRoot)
	{

		saveRoot=saveRoot+File.separator+dir.getName()+"_UTF-8";

		circle(dir,saveRoot);
	}

	private static void circle(File dir, String saveRoot) {
		File[] children=dir.listFiles();
		File thisFile;

		for(int index=0;index<children.length;index++)
		{
			String savePath=saveRoot;
			new File(savePath).mkdirs();
			thisFile=children[index];
			if(thisFile.isDirectory())
			{
				savePath=savePath+File.separator+thisFile.getName();
				circle(thisFile,savePath);
			}
			else
			{
				if(thisFile.getName().toLowerCase().endsWith(".txt"))
				{
					try {
						toUTF8(thisFile,savePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}




	private static void toUTF8(File theOld,String savePath) throws IOException 
	{
		File theNew=new File(savePath+File.separator+theOld.getName());
		FileOutputStream fos=new FileOutputStream(theNew);
		OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
		BufferedWriter out=new BufferedWriter(osw);
		
		FileInputStream fis=new FileInputStream(theOld);
		String charSet=EncodeDectector.getEncode(theOld);
		
		InputStreamReader isr=null;
		try
		{
			isr = new InputStreamReader(fis,charSet);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			System.out.println(charSet);
		}
		BufferedReader in=new BufferedReader(isr);

		//读取文本内容
		String temp=null;
		while((temp=in.readLine())!=null)
		{
			out.write(temp);
			out.newLine();
		}

		out.write(" ");out.newLine();

		out.flush();
		out.close();
		in.close();
	}


}

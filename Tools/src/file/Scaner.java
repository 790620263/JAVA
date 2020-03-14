package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;




public class Scaner {
	
	/**
	 * 
	 * 获得某目录下的全部文件
	 * @param dir
	 * @return LinkedList_文件列表
	 */
	public static LinkedList<File> getfilelist(File dir)
	{
		LinkedList<File> array=new LinkedList<File>();
				
		for(File tmp:dir.listFiles())
		{
			if(tmp.isDirectory())
			{
				array.addAll(getfilelist(tmp));
			}
			else {
				array.add(tmp);
			}
		}

		return array;
	}
	
	/**
	 * 在dir目录下查找文件名含有name的文件，返回文件路径
	 * @param dir
	 * @param name
	 * @return 文件路径
	 * @throws FileNotFoundException
	 */
	public static String getSingleFileByNameInDir(File dir,String name) throws FileNotFoundException
	{
		for(File tmp:dir.listFiles())
		{
			if(tmp.isDirectory())
			{
				return getSingleFileByNameInDir(tmp,name);
			}
			else {
				if(tmp.getName().contains(name))
				{
					return tmp.getAbsolutePath();
				}
			}
		}
		throw new FileNotFoundException();
	}
	
	
	/**
	 * 获得文件相对于根目录的路径
	 * @param root
	 * @param absPath
	 * @return
	 */
	public static String getRelitivePath(String root,String absPath) {
		String path=null;
		if(root.endsWith(File.separator))
		{
			root=root.substring(0,root.length()-1);
		}
		
		path=new File(absPath).getAbsolutePath().replace(root,"");
		path=path.substring(1);//去除相对路径开头的\
		return path;
	}


}

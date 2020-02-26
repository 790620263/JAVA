package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import file.Scaner;

public class DBAccessor
{
	protected static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	protected static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";

	private static final String USER = "root";
	private static final String PASSWORD = "789000123";
	private Connection conn = null;
	private Statement stmt = null;

	public DBAccessor(String dbName) throws SQLException
	{
		connectDB(dbName);
	}
	private void connectDB(String dbName) throws SQLException
	{

		// 注册 JDBC 驱动
		try
		{
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		// 打开链接
		System.out.println("连接数据库...");
		conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

		// 执行查询
		System.out.println(" 实例化Statement对象...");
		stmt = conn.createStatement();

		stmt.execute("use "+dbName.toLowerCase());
		
		System.out.println("Connect Success");
	}

	private void disconnenct() throws SQLException
	{
		stmt.close();
		conn.close();
	}

	public ResultSet select(String sql) throws SQLException
	{
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
	public boolean execSQL(String sql) throws SQLException
	{
		return stmt.execute(sql);
		
	}
	public void insertEmo(String tag,String filePath) throws IOException, SQLException
	{
		byte[] data=new byte[4096];
		FileInputStream fin=new FileInputStream(filePath);
		fin.read(data);
		fin.close();
		String sql="INSERT INTO `pictdb`.`emotions`(`tags`) VALUES ('"+tag + "')"
	+"`pictdb`.`emotions`(`data`) VALUES ('"+data+");";
		stmt.execute(sql);
	}
	private void insertAll(String path) throws IOException, SQLException
	{
		LinkedList<File> list=Scaner.getfilelist(new File(path));
		for(File f:list)
		{
			insertEmo(f.getParentFile().getName(),f.getAbsolutePath());
		}
	}
	public static void main(String[] args)
	{
		try
		{
			DBAccessor ac=new DBAccessor("pictdb");
			ac.execSQL("CREATE TABLE emotions tags ");
//			ac.execSQL("")
			ac.insertAll("D:\\Pict");
			
			ac.disconnenct();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

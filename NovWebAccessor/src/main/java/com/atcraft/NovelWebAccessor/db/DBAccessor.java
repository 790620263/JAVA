package com.atcraft.NovelWebAccessor.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.log.MyLogger;

public class DBAccessor
{
	MyLogger log;
	String bookName;

	protected static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	protected static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";

	private static final String USER = "root";
	private static final String PASSWORD = "789000123";
	private Connection conn = null;
	private Statement stmt = null;

	/**
	 * 创建一个连接对应书的数据库
	 * 
	 * @param bookName
	 * @throws SQLException
	 */
	public DBAccessor(String bookName)
	{
		bookName=bookName.replaceAll(" ", "");//删除空白，否则SQL错误
		this.bookName = bookName;
	}

	public void init() throws SQLException
	{
		log=MyLogger.getLogger(MyLogger.LogType.DB);
		connectDB();
		if (!isBookExist())
		{
			createBook();
			System.out.println("CREATE BOOK : " + bookName);
		}
	}

	private void connectDB() throws SQLException
	{
		final String dbName = "novel";
		// 注册 JDBC 驱动
		try
		{
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		// 打开链接
		log.logInfo("连接数据库...");
		conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

		// 执行查询
		log.logInfo(" 实例化Statement对象...");
		stmt = conn.createStatement();

		stmt.execute("use " + dbName.toLowerCase());

		log.logInfo("Connect Success");
	}

//	private void disconnenct() throws SQLException
//	{
//		stmt.close();
//		conn.close();
//	}

	/**
	 * 检查这本书是否已经存在
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected boolean isBookExist() throws SQLException
	{
		String sql = "select COUNT(*) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='novel' and TABLE_NAME='"
				+ bookName + "' ;";
//		 int tableNum = jdbcTemplate.queryForInt(sql);
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{
			if (rs.getInt(1) > 0)
				return true;
		}
		return false;
	}

	/**
	 * 创建一本书，一本书就是一张表，有三列 第几章 章节名 章节内容
	 * 
	 * @param tableName
	 * @throws SQLException
	 */
	public void createBook() throws SQLException
	{
		String sql = "CREATE TABLE " + bookName
				+ "  ( `chapterIndex` int(10) NOT NULL, `chapterName` varchar(255) NOT NULL,`chapterContent` longtext CHARACTER SET utf8 NOT NULL,PRIMARY KEY (`chapterIndex`));";
		stmt.execute(sql);
	}

	/**
	 * 删除这本书（如果存在）
	 * 
	 * @throws SQLException
	 */
	public void deleteBookIfExist() throws SQLException
	{
		String sql = "drop table if exists " + bookName + ";";
		stmt.execute(sql);
	}

	/**
	 * 插入章节
	 * 
	 * @param tableName
	 * @param chapterIndex
	 * @param chapterName
	 * @param chapterContent
	 * @throws SQLException
	 */
	protected void insertChapter(int chapterIndex, String chapterName, String chapterContent) throws SQLException
	{
		String sql = "INSERT INTO `novel`.`" + bookName + "`(`chapterIndex`, `chapterName`, `chapterContent`) VALUES ("
				+ chapterIndex + ", \"" + chapterName + "\", \"" + chapterContent + "\")";
		stmt.execute(sql);
	}

	public void insertChapter(int chapterIndex, NovChapter chapter) throws SQLException
	{
		insertChapter(chapterIndex, chapter.getTitle(), chapter.getContent());
	}
	public NovChapter getChapter(int chapterIndex) throws SQLException
	{
		String sql="SELECT chapterName,chapterContent FROM novel."+bookName+" WHERE chapterIndex="+chapterIndex;
		ResultSet result=stmt.executeQuery(sql);
		
		String chapterName=null,chapterContent = null;
		while(result.next())
		{
			chapterName=result.getString("chapterName");
			chapterContent=result.getString("chapterContent");
		}
		//数据都读取到了
		if(chapterContent!=null)
		{
			return new NovChapter(chapterName, chapterContent);
		}else
		{
			throw new SQLException("Can't find the chapter");
		}
	}

}

package com.atcraft.NovelWebAccessor.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public abstract class DBAccessor
{
	protected Logger log;

	protected static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	protected static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";

	private static final String USER = "root";
	private static final String PASSWORD = "789000123";
	protected Connection conn = null;
	protected Statement stmt = null;
	/**
	 * 创建一个连接对应书的数据库
	 * 
	 * @param bookName
	 * @throws SQLException
	 */
	protected DBAccessor() throws SQLException
	{
		log=Logger.getLogger("DBAccessor");
		connectDB();
	}

	protected void connectDB() throws SQLException
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
//		log.info("连接数据库...");
		conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

		// 执行查询
//log.info(" 实例化Statement对象...");
		stmt = conn.createStatement();

		stmt.execute("use " + dbName);

		log.info("Connect Success");
	}

//	private void disconnenct() throws SQLException
//	{
//		stmt.close();
//		conn.close();
//	}


}

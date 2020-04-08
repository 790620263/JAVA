package com.atcraft.NovelWebAccessor.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.atcraft.NovelWebAccessor.dataPack.NovInfo;

public class NovInfoDBAccessor extends DBAccessor
{
	NovInfo info;
	/**
	 * 
	 * @param bookName
	 * @throws SQLException 连接失败
	 */
	public NovInfoDBAccessor(NovInfo info) throws SQLException
	{
		super();
		this.info=info;
	}
	
	/**
	 * 检查这本书是否已经存在
	 * 
	 * @return
	 * @throws SQLException
	 */
	public  boolean isBookExist() throws SQLException
	{
		String sql = "select COUNT(*) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='novel' and TABLE_NAME='"
				+info.getName()+"' ;";
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
	public void createBook(String url) throws SQLException
	{
		String sql = "CREATE TABLE " + info.getName()
				+ "  ( `chapterIndex` int(10) NOT NULL, `chapterName` varchar(255) NOT NULL,`chapterContent` longtext CHARACTER SET utf8 NOT NULL,PRIMARY KEY (`chapterIndex`));";
		stmt.execute(sql);
		
		insertBookInfo(url);
	}
	protected int getExistBookNum() throws SQLException
	{
		String sql = "select COUNT(*) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='novel' ;";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{
			return rs.getInt(1);
		}
		return 0;
	}
	public int getExistChapterNum() 
	{
		String sql = "select count(chapterIndex) from "+info.getName()+";";
		
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e)
		{
//			e.printStackTrace();
			log.info("book not exist");
		}
		
		return 0;
	}
	public ArrayList<String> getAllBookName()
	{
		ArrayList<String> list=new ArrayList<String>();
		String sql="select 书名 from bookinformation ;";
		
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				list.add(rs.getString("书名"));
			}
		} catch (SQLException e)
		{
			log.info(e);
		}
		return list;
				
	}
	public ArrayList<String> getAllBookUrl()
	{
		ArrayList<String> list=new ArrayList<String>();
		String sql="select 链接地址 from bookinformation ;";
		
		try
		{
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				list.add(rs.getString("链接地址"));
			}
		} catch (SQLException e)
		{
			log.info(e);
		}
		return list;
				
	}
	
	protected void insertBookInfo(String url) throws SQLException
	{
		String sql="INSERT INTO `novel`.`bookinformation`(`ID`, `书名`, `作者`, `介绍`, `更新时间`,`链接地址`) "
				+ "VALUES ("+getExistBookNum()+", '"+info.getName()+"', '"+info.getWritter()+"'"
				+ ", '"+info.getIntroduction()+"', '"+info.getUpdateTime().toString()+"', '"+url+"')";
		stmt.execute(sql);
	}
	protected void deleteBookInfo() throws SQLException
	{
		String sql="DELETE FROM `novel`.`bookinformation` WHERE `书名` = '"+info.getName()+"' AND `作者` = '"+info.getWritter()+"' LIMIT 1";
		stmt.execute(sql);
	
	}
	public void alterInfo(NovInfo info,String url) throws SQLException
	{
		deleteBookInfo();
		this.info=info;
		insertBookInfo(url);
	}
	/**
	 * 删除这本书（如果存在）
	 * 
	 * @throws SQLException
	 */
	public void deleteBookIfExist() throws SQLException
	{
		String sql = "drop table if exists " + info.getName() + ";";
		stmt.execute(sql);
		
		deleteBookInfo();
	}
	public NovInfo getInfo(int id) throws SQLException
	{
		String name = null,introduction = null,updateTime = null,writter = null;
		String sql="SELECT bookinformation.`书名`,bookinformation.`作者`,bookinformation.`介绍`,bookinformation.`更新时间` FROM bookinformation WHERE bookinformation.ID="
		+id+";";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		
		if (rs.next())
		{
			name=rs.getString(1);
			writter=rs.getString(2);
			introduction=rs.getString(3);
			updateTime=rs.getString(4);
			
			
		}else {
			throw new SQLException("CAN NOT FIND THIS BOOK");
		}
		NovInfo novInfo=new NovInfo(name, introduction, LocalDateTime.parse(updateTime), writter);
		return novInfo;
		
	}
	public NovInfo getInfo(String bookName) throws SQLException
	{
		String name = null,introduction = null,updateTime = null,writter = null;
		String sql="SELECT bookinformation.`书名`,bookinformation.`作者`,bookinformation.`介绍`,bookinformation.`更新时间` FROM bookinformation WHERE bookinformation.书名='"
		+bookName+"';";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		if (rs.next())
		{
			name=rs.getString(1);
			writter=rs.getString(2);
			introduction=rs.getString(3);
			updateTime=rs.getString(4);
			
			
		}else {
			throw new SQLException("CAN NOT FIND THIS BOOK");
		}
		NovInfo novInfo=new NovInfo(name, introduction, LocalDateTime.parse(updateTime), writter);
		return novInfo;
		
	}

}

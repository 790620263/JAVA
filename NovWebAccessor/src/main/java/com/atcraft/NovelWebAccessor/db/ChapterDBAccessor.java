package com.atcraft.NovelWebAccessor.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;

public class ChapterDBAccessor extends DBAccessor
{

	protected String bookName;
	/**
	 * 
	 * @param bookName
	 * @throws SQLException 连接失败
	 */
	public ChapterDBAccessor(String bookName) throws SQLException
	{
		super();
		this.bookName=bookName;
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
	public void deleteChapter(int chapterIndex) throws SQLException
	{
		String sql="DELETE FROM `novel`.`"+bookName+"` WHERE `chapterIndex` ="+chapterIndex+" LIMIT 1;";
		stmt.execute(sql);
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

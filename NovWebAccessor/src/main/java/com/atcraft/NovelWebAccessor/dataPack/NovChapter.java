package com.atcraft.NovelWebAccessor.dataPack;

import java.io.Serializable;

public class NovChapter implements Serializable
{
	private static final long serialVersionUID = 3563768763104517513L;

	protected String title,content;
	/**
	 * 
	 * @return String 章节名
	 */
	public String getTitle()
	{
		return title;
	}
	/**
	 * 
	 * @return String 章节内容
	 */
	public String getContent()
	{
		return content;
	}
	
	public NovChapter(String title,String content)
	{
		this.title=title;
		this.content=content;
	}
}

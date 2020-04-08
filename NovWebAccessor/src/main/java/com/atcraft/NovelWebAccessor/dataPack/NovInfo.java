package com.atcraft.NovelWebAccessor.dataPack;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NovInfo implements Serializable
{
	private static final long serialVersionUID = 9160833553728897501L;
	protected String name,introduction,writter,charset;
	LocalDateTime updateTime;
	
	
	public NovInfo(String name, String introduction, LocalDateTime updateTime,String writter,String charset)
	{
		this.name = name;
		this.introduction = introduction;
		this.updateTime = updateTime;
		this.writter=writter;
		this.charset=charset;
	}
	public NovInfo(String name)
	{
		this(name,"Unknown",LocalDateTime.parse("1970-01-01T01:01:01"),"Unknown","UTF-8");
	}
	public NovInfo(String name, String introduction, LocalDateTime updateTime,String writter)
	{
		this.name = name;
		this.introduction = introduction;
		this.updateTime = updateTime;
		this.writter=writter;
		this.charset="UTF-8";
	}
	/**
	 * 
	 * @return String 作者
	 */
	public String getWritter()
	{
		return writter;
	}
	/**
	 * 
	 * @return 简介
	 */
	public String getIntroduction()
	{
		return introduction;
	}
	/**
	 * 
	 * @return String 最后更新时间
	 */
	public LocalDateTime getUpdateTime()
	{
		return updateTime;
	}
	/**
	 * 
	 * @return String 书名
	 */
	public String getName()
	{
		return name;
	}
	public String getCharset()
	{
		return charset;
	}
}

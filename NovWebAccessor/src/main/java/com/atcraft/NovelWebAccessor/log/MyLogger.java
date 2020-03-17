package com.atcraft.NovelWebAccessor.log;

import org.apache.log4j.Logger;

public class MyLogger
{
	public enum LogType{DB,TXT,HTML};
	protected Logger logger;
	protected MyLogger(String name)
	{
		logger=Logger.getLogger(name);
	}
	public static MyLogger getLogger(LogType type)
	{
		return new MyLogger(type.toString());
	}
	public void logErr(Object msg)
	{
		logger.error(logger.getName()+" ："+msg);
	}
	public void logDebug(String msg)
	{
		logger.debug(logger.getName()+" ："+msg);
	}
	public void logInfo(String msg)
	{
		logger.info(logger.getName()+" ："+msg);
	}
	
}

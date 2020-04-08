package com.atcraft.NovelWebAccessor.db;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.Test;

import com.atcraft.NovelWebAccessor.dataPack.NovInfo;

public class NovDBAccessorTest
{

	@Test
	public void test()
	{
		LocalDateTime.now();
		try
		{
			NovInfoDBAccessor db=new NovInfoDBAccessor(null);
			NovInfo info=db.getInfo("地方大");
			System.out.println(info.getName());
		} catch (SQLException e)
		{
			
			e.printStackTrace();
		}
		
		
	}

}

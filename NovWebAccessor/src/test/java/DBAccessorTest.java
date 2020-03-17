

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.Test;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.db.DBAccessor;

public class DBAccessorTest
{
	
//	@Test
	public void TestInit()
	{
		DBAccessor dbac=new DBAccessor("诡秘之主");
		try
		{
			dbac.init();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
//	@Test
	public void TestInsert()
	{
		DBAccessor dbac=new DBAccessor("诡秘之主");
		try
		{
			dbac.init();
			dbac.insertChapter(0, new NovChapter("test1", "a test"));
		} catch (SQLException e)
		{
			
		}
	}
	@Test
	public void Test()
	{
		System.out.println(LocalDateTime.parse("2020-03-13T19:02:56"));
	}
	
	
}

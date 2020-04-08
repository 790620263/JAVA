package app.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import app.io.data.PointData;
import app.io.data.PointNotFoundException;

public class PointInputStream extends DataInputStream {

	public PointInputStream(InputStream in) {
		super(in);
	}

	/**
	 * 尝试从流中读取一个点坐标
	 * @return PointData
	 * @throws IOException
	 * @throws PointNotFoundException 点坐标不存在
	 */
	public PointData readPoint() throws IOException, PointNotFoundException
	{
		PointData p=null;
		int x,y;
		long time_stemp;
		
		if(available()>0)
		{
			time_stemp=readLong();
			skipBytes(1);
			x=readInt();
			skipBytes(1);
			y=readInt();
			skipBytes(1);
			p=new PointData(x, y,time_stemp);
		}else
		{
			throw new PointNotFoundException();
		}
		return p;
	}
	public PointData readPointAfter(long time_stemp) throws PointNotFoundException, IOException 
	{
		PointData p=null;
		do {
			p=readPoint();
		}while(p.getTime_stemp()<time_stemp);
		
		return p;
	}
	/**
	 * 从流中读取尽可能多的点坐标
	 * @return List (可能为空）
	 * @throws IOException
	 */
	public List<PointData> readPointAll() throws IOException
	{
		LinkedList<PointData> points=new LinkedList<PointData>();
		try
		{
			points.add(readPoint());
		}catch(PointNotFoundException e) {}
		
		return points;
	}
}

package app.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import app.io.data.PointData;

/**
 * 以时间 x y\n的格式输出点坐标
 * @author AtCraft
 *
 */
public class PointOutputStream extends FileWriter{

	
	public PointOutputStream(String path) throws IOException {
		super(path);
	}

	public void writePoint(PointData data) throws IOException
	{
		write(String.valueOf(data.getTime_stemp()));
		write('\t');
		write(String.valueOf(data.getX()));
		write('\t');
		write(String.valueOf(data.getY()));
		write('\n');
		flush();
	}
	public void writePointAll(List<PointData> pointList2) throws IOException
	{
		for(PointData p:pointList2)
		{
			writePoint(p);
		}
	}
}

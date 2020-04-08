package app.io.data;

import java.io.Serializable;
import java.util.Date;

public class PointData implements Serializable {
	private static final long serialVersionUID = 7257065400475037443L;

	protected int x;
	protected int y;
	
	protected long time_stemp;
	
	public PointData(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		time_stemp=new Date().getTime();
	}
	public PointData(int x, int y,long time_stemp) {
		super();
		this.x = x;
		this.y = y;
		this.time_stemp=time_stemp;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getTime_stemp() {
		return time_stemp;
	}
	
//	public String toString() {
//		long sec=time_stemp/1000;
//		LocalDateTime time=LocalDateTime.ofEpochSecond(sec,0,ZoneOffset.of("+8"));
//		return time.toString()+" X="+x+" Y="+y;
//	}
	public String toString() {
		return time_stemp+" X="+x+" Y="+y;
	}
}

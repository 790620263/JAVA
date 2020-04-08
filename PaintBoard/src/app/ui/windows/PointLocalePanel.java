package app.ui.windows;

import app.io.data.PointData;

public class PointLocalePanel extends TextPanel{
	private static final long serialVersionUID = 5336167423633977135L;
	public void println(PointData p)
	{
		super.println(p.toString());
	}
	public PointLocalePanel(String title)
	{
		super(title);
	}

}

package app.ui.windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;



/**
 * 单击鼠标开始绘制,再次单击取消
 * @author AtCraft
 *
 */
public class PaintPanel extends Panel{
	private static final long serialVersionUID = -5141456821951184472L;
//	private static final int LINE_WIDTH=3;
	volatile private Color FORECOLOR=Color.BLACK;
	private static final Color BACKGROUND_COLOR=Color.GREEN; 
	
	private Graphics g;
	
	volatile public boolean isStartPaint=false;
	public PaintPanel()
	{
		setBackground(BACKGROUND_COLOR);
		
//		clean();
	}
	
	volatile public int x1=-1,x2=-1,y1=-1,y2=-1;
//	public void paint(Graphics g) {
//		if(g2==null)
//		{
//			this.g2 = (Graphics2D) g;
//			g2.setColor(FORECOLOR);
//		    g2.setStroke(new BasicStroke(LINE_WIDTH,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
//		}
//		g2.drawLine(x1, y1, x2, y2);
//	}
	
	public void drawPoint(int x,int y)
	{
		if(g==null)
		{
			g=this.getGraphics();
			g.setColor(FORECOLOR);
		}
		if(x1==-1) {
			x1=x;y1=y;
			x2=x;y2=y;
		}else
		{
			x1=x2;y1=y2;
			x2=x;y2=y;
		}
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y2, x1, y1);
	}
	public void clean()
	{
		validate();
		repaint(); 
	}
}

package app.ui.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RelativeFrame extends JFrame {
	private static final long serialVersionUID = -2113561570304690375L;
 
	int oriW,oriH;
	JPanel panel;
	public RelativeFrame(String title)
	{
		super(title);
		this.setLayout(null);
		panel=new JPanel();
		panel.setLayout(null);
		this.getRootPane().getContentPane().add(panel);
		
		setSize(800, 600);
		
		setResizable(false);
		
		
//		setProportion();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.toFront();
	}

//	public Component add(Component comp) {
//		return panel.add(comp);
//	}
	

//	private void setProportion()
//	{
//		oriW =this.getWidth();//frame的宽
//		oriH = this.getHeight();//frame的高
//		
//		this.addWindowListener(new WindowListener() {
//			@Override
//			public void windowOpened(WindowEvent e) {
//				// TODO Auto-generated method stub
//				modifyComponentSize(oriW,oriH);
//			}
//
//			@Override
//			public void windowClosing(WindowEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void windowClosed(WindowEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void windowIconified(WindowEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void windowDeiconified(WindowEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void windowActivated(WindowEvent e) {
//				// TODO Auto-generated method stub
//				modifyComponentSize(oriW,oriH);
//			}
//
//			@Override
//			public void windowDeactivated(WindowEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
//
///**
//	 * frame中的控件自适应frame大小：改变大小位置和字体
//	 * @param frame 要控制的窗体
//	 * @param proportion 当前和原始的比例
//	 */
//
//	public void modifyComponentSize(int oriW,int oriH){
//		try 
//		{
//			Component[] components =this.getRootPane().getContentPane().getComponents();
//
//			float proportion=oriW/this.getWidth();
//			for(Component co:components)
//			{
//				float locX = co.getX() *proportion;
//				float locY = co.getY() * proportion;
//				co.setLocation((int)locX, (int)locY);
//
//				float width = co.getWidth() * proportion;
//				float height = co.getHeight() * proportion;
//				co.setSize((int)width, (int)height);
//				int size = (int)(co.getFont().getSize() * proportion);
//
//				Font font = new Font(co.getFont().getFontName(), co.getFont().getStyle(), size);
//				co.setFont(font);
//			}
//			
//
//		} 
//
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//	}

}

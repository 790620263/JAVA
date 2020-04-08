package app.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import app.io.PointOutputStream;
import app.io.SysInfo;
import app.io.data.PointData;
import app.ui.button.CssButton;
import app.ui.windows.PaintPanel;
import app.ui.windows.PointLocalePanel;
import app.ui.windows.RelativeFrame;
import app.ui.windows.StatePanel;

public class UIMain {

	public static File getPath_By_ChooseDiag(String defaultPath) {
		JFileChooser c=new JFileChooser(new File(defaultPath));
		c.setFileFilter(new FileFilter() {
			public String getDescription() {
				return null;
			}
			public boolean accept(File f) {
				return f.isDirectory()||f.getName().endsWith(".txt");
			}
		});
		c.showSaveDialog(new JFrame());
	
		return c.getSelectedFile();
	}
	private RelativeFrame fr;
	private StatePanel sp;
	private PaintPanel pp;
	private PointLocalePanel plp;
	private CssButton b_choose;
	private CssButton b_save;
	private CssButton b_clean;
	private CssButton b_print;
	private LinkedList<PointData> pointList=new LinkedList<PointData>();

	synchronized public void initGui() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				fr = new RelativeFrame("Painter");

				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					SwingUtilities.updateComponentTreeUI(fr);
					fr.pack();
				} catch (Exception e) {
					e.printStackTrace();
				}

				pp = new PaintPanel();
				sp = new StatePanel("状态栏");
				plp = new PointLocalePanel("坐标信息");
				

				b_choose = new CssButton("路径");
				b_save = new CssButton("保存");
				b_clean = new CssButton("清除");
				b_print=new CssButton("绘制");

				
				
				initStream();
				b_choose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						savePath=getPath_By_ChooseDiag(savePath).getAbsolutePath();
						initStream();
					}
				});
				b_save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						logPointsToFile(pointList);
					}
				});
				b_clean.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pp.clean();
						plp.clean();
						pointList.clear();
						printlnState("画布已清除");
					}
				});

				MouseAdapter paintPanelMouseAdapter = new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1)// 左键单击
						{
							pp.isStartPaint = !pp.isStartPaint;

							pp.x1 = -1;
							pp.x2 = -1;
							pp.y1 = -1;
							pp.y2 = -1;
							if (pp.isStartPaint)
								printlnState("开始画图");
							else
								printlnState("画图结束");
						}

					}

					public void mouseMoved(MouseEvent e) {
						if (pp.isStartPaint) {
							PointData pd = new PointData(e.getX(), e.getY());
							pp.drawPoint(e.getX(), e.getY());
							paintPointLocale(pd);
							pointList.add(pd);
						}
					}
				};

				pp.addMouseListener(paintPanelMouseAdapter);
				pp.addMouseMotionListener(paintPanelMouseAdapter);

				fr.add(b_clean);
				fr.add(b_save);
				fr.add(b_choose);
				fr.add(sp);
				fr.add(plp);
				fr.add(pp);
				fr.add(b_print);
				fr.setSize(810, 630);

				plp.setBounds(20, 20, 210, 400);
				sp.setBounds(20, 440, 500, 140);
				pp.setBounds(240, 20, 540, 400);

				b_choose.setBounds(540, 440, 110, 60);
				b_save.setBounds(670, 440, 110, 60);
				b_clean.setBounds(540, 520, 110, 60);
				b_print.setBounds(670, 520, 110, 60);

//				fr.setBackground(Color.gray);
//				fr.repaint();
				fr.setVisible(true);

			}
		});

	}

	private static PointOutputStream out;
//	private static PointInputStream in;

	synchronized public void initStream() {
		File record = new File(savePath);
		if (!record.exists()) {
			record.getParentFile().mkdirs();
		}

		try {
			out = new PointOutputStream(record.getAbsolutePath());
			printlnState("输出流初始化完成");
			printlnState("坐标数据文件保存路径：" + record.getAbsolutePath());
		} catch (FileNotFoundException e) {
			printlnState(e);
		} catch (IOException e) {
			printlnState(e);
		}

	}

	private static String savePath = SysInfo.getSavePath();

//	synchronized public void loadPointRecords() {
//		File record;
//		record = UIMain.getPath_By_ChooseDiag(savePath);
//		if (record != null) {
//			savePath = record.getParent();
//			try {
//				in = new PointInputStream(new FileInputStream(record));
//			} catch (FileNotFoundException e1) {
//				printlnState(e1);
//			}
//			printlnState("载入坐标数据...");
//			List<PointData> list;
//			try {
//				list = in.readPointAll();
//
//				SwingUtilities.invokeLater(new Runnable() {
//					public void run() {
//						for (PointData p : list) {
//							paintPointLocale(p);
//							paintPoint(p);
//						}
//					}
//				});
//				printlnState("载入坐标数据完成");
//			} catch (IOException e) {
//				printlnState(e);
//			}
//		}
//
//	}

	synchronized public void logPointsToFile(LinkedList<PointData> pointList) {
		printlnState("写入坐标数据...");
		try {
			out.writePointAll(pointList);
//			out.write(plp.getText());
			printlnState("写入坐标数据完成");
		} catch (Exception e) {
			printlnState("写入异常：" + e);
		}
	}

//	synchronized public void logPointsToFile(String ptext) {
//		printlnState("写入坐标数据...");
//		try {
//			out.writeUTF(ptext);
//			printlnState("写入坐标数据完成");
//		} catch (IOException e) {
//			printlnState("写入异常：" + e);
//		}
//	}

	synchronized public void paintPointLocale(PointData p) {
		// TODO
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				plp.println(p);
			}
		});
	}

	synchronized public void paintPoint(PointData p) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pp.drawPoint(p.getX(), p.getY());
			}
		});
	}

	synchronized public void paintPoint(int x, int y) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pp.drawPoint(x, y);
			}
		});
	}

	synchronized public void printlnState(String state) {
		sp.println(state);
	}

	synchronized public void printlnState(Throwable e) {
		printlnState(e.getMessage());
	}
}

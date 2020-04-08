package app.ui.windows;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import app.ui.FontGenerator;

public class TextPanel extends JScrollPane{
	private static final long serialVersionUID = -3739999820227445194L;

	private JTextArea area;
	public TextPanel(String title)
	{
		super(new JTextArea(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	              JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		area=(JTextArea) this.getViewport().getView();
		TitledBorder b=new TitledBorder(title);
		this.setBorder(b);
		this.setFont(FontGenerator.getYAHE(14));
		area.setEditable(false);
		area.setBackground(Color.WHITE);
	}
	
	public void println(String text)
	{
		area.append(text+"\n");
	}
	public void clean() {
		area.setText("");
		
	}
	public String getText()
	{
		return area.getText();
	}
	public void append(String str) {
		area.append(str);
	}
	
}
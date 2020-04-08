package app.ui.button;

import javax.swing.JButton;

import app.ui.FontGenerator;

public class CssButton extends JButton {
	private static final long serialVersionUID = -7962572311468456849L;

	public CssButton(String text)
	{
		super(text);
		this.setFont(FontGenerator.getYAHE(24));
	}
}

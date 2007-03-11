package visidia.gui.presentation;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8133682274589127227L;

	JTextArea textArea;

	JFrame owner;

	public HelpDialog(JFrame parent, String title) {
		super(parent, title);
		this.owner = parent;
		// Create a text area.
		this.textArea = new JTextArea();
		this.textArea.setFont(new Font("Courier", Font.TRUETYPE_FONT, 13));
		this.textArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(this.textArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(400, 300));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createTitledBorder(title), BorderFactory
						.createEmptyBorder(5, 5, 5, 5)), areaScrollPane
						.getBorder()));
		this.textArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), BorderFactory
						.createCompoundBorder(BorderFactory
								.createLoweredBevelBorder(), BorderFactory
								.createEmptyBorder(5, 5, 5, 5))));

		this.getContentPane().add(areaScrollPane);
		this.pack();
		this.setVisible(false);
	}

	public void setVisible(boolean b) {
		Dimension ownerDim = this.owner.getPreferredSize();
		this.setLocation(
				(int) (this.owner.getX() + ownerDim.getWidth() / 2 - this
						.getWidth() / 2), (int) (this.owner.getY() + ownerDim
						.getHeight() / 5));
		super.setVisible(b);
	}

	public void setText(String txt) {
		this.textArea.setText(txt);
	}

	public String getText() {
		return this.textArea.getText();
	}

	public void setEditable(boolean b) {
		this.textArea.setEditable(b);
	}
}

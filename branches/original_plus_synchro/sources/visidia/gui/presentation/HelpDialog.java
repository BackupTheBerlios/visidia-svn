package visidia.gui.presentation;

import java.awt.*;
import javax.swing.*;

public class HelpDialog extends JDialog {

    JTextArea textArea;
    JFrame owner;

    public HelpDialog(JFrame parent, String title) {
	super(parent, title);
	owner = parent;
	//Create a text area.
        textArea = new JTextArea();
	textArea.setFont(new Font("Courier", Font.TRUETYPE_FONT, 13));
        textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(400, 300));
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
		                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));
	textArea.setBorder (BorderFactory.createCompoundBorder 
			       (BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createCompoundBorder(
				   BorderFactory.createLoweredBevelBorder (), 
                                   BorderFactory.createEmptyBorder(5,5,5,5))));
	
	getContentPane().add(areaScrollPane);
	pack();
	setVisible(false);
    }

    public void setVisible(boolean b) {
	Dimension ownerDim = owner.getPreferredSize ();
	setLocation ((int) (owner.getX () + 
			    ownerDim.getWidth () / 2 - getWidth () / 2),
		     (int) (owner.getY () + ownerDim.getHeight () / 5));
	super.setVisible(b);
    }

    public void setText(String txt) {
	textArea.setText(txt);
    }
 
    public String getText() {
	return textArea.getText();
    }

    public void setEditable(boolean b) {
	textArea.setEditable(b);
    }
}

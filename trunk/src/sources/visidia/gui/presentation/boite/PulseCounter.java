package visidia.gui.presentation.boite;

import java.io.Serializable;
import java.awt.event.*;
import javax.swing.*;


public class PulseCounter extends JLabel implements ActionListener, Serializable {

    int pulse;
    
    public PulseCounter() {
	super();
	initState();
    }
    

    public void initState() {
	pulse = 0;
	setPulse(pulse);
	setEnabled(false);
    }
    
    public void setPulse(int pulse) {
	if(!isEnabled())
	    setEnabled(true);
	setText("Pulse Counter : " + String.valueOf(pulse));
    }

    public void actionPerformed(ActionEvent e) {}


}

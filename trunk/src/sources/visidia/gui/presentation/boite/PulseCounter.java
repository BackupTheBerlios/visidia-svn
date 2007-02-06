package visidia.gui.presentation.boite;

import java.io.Serializable;
import java.awt.event.*;
import javax.swing.*;


public class PulseCounter extends JLabel implements ActionListener, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8189330111132793753L;
	int pulse;
    
    public PulseCounter() {
	super();
	this.initState();
    }
    

    public void initState() {
	this.pulse = 0;
	this.setPulse(this.pulse);
	this.setEnabled(false);
    }
    
    public void setPulse(int pulse) {
	if(!this.isEnabled())
	    this.setEnabled(true);
	this.setText("Pulse Counter : " + String.valueOf(pulse));
    }

    public void actionPerformed(ActionEvent e) {}


}

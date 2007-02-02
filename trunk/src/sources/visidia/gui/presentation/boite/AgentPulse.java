package visidia.gui.presentation.boite;

import java.io.Serializable;
//import visidia.gui.donnees.TableImages;
//import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.event.*;


public class AgentPulse extends JLabel implements ActionListener, Serializable {

    int pulse;
    
    public AgentPulse() {
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

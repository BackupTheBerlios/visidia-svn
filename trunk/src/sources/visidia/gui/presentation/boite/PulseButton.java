package visidia.gui.presentation.boite;

import java.io.Serializable;
import visidia.gui.donnees.TableImages;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * Cette classe représente le bouton de l'horloge globale dans la
 * fenêtre de simulation
 *
 */


public class PulseButton extends JButton implements ActionListener, Serializable {

    
    private PulseFrame pulseFrame;
    private int pulseValue;
    
    private final ImageIcon globalClock = new ImageIcon(TableImages.getImage("globalClock"));
    private final ImageIcon noGlobalClock = new ImageIcon(TableImages.getImage("noGlobalClock"));
    
    
    /**
     * indique l'état de l'affichage de l'horloge globale
     * 0 : asynchrone
     * 1 : synchrone
     * 2 : synchrne avec affichage des pulse
     *
     **/
    private int state;
    
    /*
    public PulseButton() {
	super();
	addActionListener(this);
	setAlignmentY(CENTER_ALIGNMENT);
	initState();
    }
    */


    
    public PulseButton() {
	super();
	addActionListener(this);
	setAlignmentY(CENTER_ALIGNMENT);
	initState();
    }
    

    public void initState() {
	if (state != 0) {
	    pulseFrame.setVisible(false);
	    pulseFrame = null;
	    pulseValue = 0;
	}
	state=0;
	pulseValue=0;
	setEnabled(false);
	setIcon(noGlobalClock);
	setToolTipText("No Global clock Detected");
    }

    public void setPulse(int pulse) {
	if(state == 0) {
	    actionLeavingState0To1();
	}
	
	pulseValue = pulse;
	pulseFrame.setPulse();
    }
    
    public void actionPerformed(ActionEvent e) {
	if(state == 1) {
	    actionEnterStateTwo();
	} else if(state == 2) {
	    actionLeavingState2To1();
	}
    }

    
    private void actionLeavingState0To1() {
	pulseFrame = new PulseFrame();
	setEnabled(true);
	setIcon(globalClock);
	setToolTipText("Click to view time units");
	state = 1;
    }

    private void actionEnterStateTwo() {
	setToolTipText("Click to hide time units");
	pulseFrame.setVisible(true);
	state=2;
    }
    
    private void actionLeavingState2To1 () {
	setToolTipText("Click to view time units");
	pulseFrame.setVisible(false);
	state=1;
    }
    
    /**
     * Fenetre d'affiche de la valeur de l'horloge globale
     *
     **/

    private class PulseFrame extends JFrame {

	private JLabel pulseLabel;

	PulseFrame() {
	    super("Time");
	    this.getContentPane().setLayout(new BorderLayout());
	    
	    pulseLabel = new JLabel();
	    
	    pulseLabel.setForeground(Color.green);
	    pulseLabel.setBackground(Color.black);
	    pulseLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    pulseLabel.setOpaque(true);
	    
	    JLabel text = new JLabel("*_*  Time Units  *_*");
	    
	    this.setPulse();
	    
	    getContentPane().add(text, BorderLayout.NORTH);
	    getContentPane().add(pulseLabel, BorderLayout.CENTER);
	    //setSize(50,30);
	    pack();
	}
	
	
	public void setPulse() {
	    pulseLabel.setText(String.valueOf(pulseValue));
	    pulseLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	}
    }
}

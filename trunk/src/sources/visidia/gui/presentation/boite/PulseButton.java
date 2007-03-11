package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import visidia.gui.donnees.TableImages;

/**
 * Cette classe représente le bouton de l'horloge globale dans la fenêtre de
 * simulation
 * 
 */

public class PulseButton extends JButton implements ActionListener,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1047302737303937239L;

	private PulseFrame pulseFrame;

	private int pulseValue;

	private final ImageIcon globalClock = new ImageIcon(TableImages
			.getImage("globalClock"));

	private final ImageIcon noGlobalClock = new ImageIcon(TableImages
			.getImage("noGlobalClock"));

	/**
	 * indique l'état de l'affichage de l'horloge globale 0 : asynchrone 1 :
	 * synchrone 2 : synchrne avec affichage des pulse
	 * 
	 */
	private int state;

	/*
	 * public PulseButton() { super(); addActionListener(this);
	 * setAlignmentY(CENTER_ALIGNMENT); initState(); }
	 */

	public PulseButton() {
		super();
		this.addActionListener(this);
		this.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.initState();
	}

	public void initState() {
		if (this.state != 0) {
			this.pulseFrame.setVisible(false);
			this.pulseFrame = null;
			this.pulseValue = 0;
		}
		this.state = 0;
		this.pulseValue = 0;
		this.setEnabled(false);
		this.setIcon(this.noGlobalClock);
		this.setToolTipText("No Global clock Detected");
	}

	public void setPulse(int pulse) {
		if (this.state == 0) {
			this.actionLeavingState0To1();
		}

		this.pulseValue = pulse;
		this.pulseFrame.setPulse();
	}

	public void actionPerformed(ActionEvent e) {
		if (this.state == 1) {
			this.actionEnterStateTwo();
		} else if (this.state == 2) {
			this.actionLeavingState2To1();
		}
	}

	private void actionLeavingState0To1() {
		this.pulseFrame = new PulseFrame();
		this.setEnabled(true);
		this.setIcon(this.globalClock);
		this.setToolTipText("Click to view time units");
		this.state = 1;
	}

	private void actionEnterStateTwo() {
		this.setToolTipText("Click to hide time units");
		this.pulseFrame.setVisible(true);
		this.state = 2;
	}

	private void actionLeavingState2To1() {
		this.setToolTipText("Click to view time units");
		this.pulseFrame.setVisible(false);
		this.state = 1;
	}

	/**
	 * Fenetre d'affiche de la valeur de l'horloge globale
	 * 
	 */

	private class PulseFrame extends JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1600670420358598702L;

		private JLabel pulseLabel;

		PulseFrame() {
			super("Time");
			this.getContentPane().setLayout(new BorderLayout());

			this.pulseLabel = new JLabel();

			this.pulseLabel.setForeground(Color.green);
			this.pulseLabel.setBackground(Color.black);
			this.pulseLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			this.pulseLabel.setOpaque(true);

			JLabel text = new JLabel("*_*  Time Units  *_*");

			this.setPulse();

			this.getContentPane().add(text, BorderLayout.NORTH);
			this.getContentPane().add(this.pulseLabel, BorderLayout.CENTER);
			// setSize(50,30);
			this.pack();
		}

		public void setPulse() {
			this.pulseLabel
					.setText(String.valueOf(PulseButton.this.pulseValue));
			this.pulseLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		}
	}
}

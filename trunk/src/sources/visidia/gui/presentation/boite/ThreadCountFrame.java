package visidia.gui.presentation.boite;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class ThreadCountFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -832261894060995853L;
	private JLabel label;
    private ThreadGroup initialThreadGroup;
    private Timer timer;

    public ThreadCountFrame(ThreadGroup threadGroup){
	super("VISIDIA thread counts");
	this.initialThreadGroup = threadGroup;
	
	this.label = new JLabel("                                      ");
	this.label.setForeground(Color.green);
	this.label.setBackground(Color.black);
	this.label.setOpaque(true);
	this.getContentPane().add(this.label, BorderLayout.CENTER);

	ActionListener timerListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    ThreadCountFrame.this.label.setText("active thread count : "+ThreadCountFrame.this.initialThreadGroup.activeCount()+"    ");
		}
	    };
	this.timer = new Timer(100, timerListener);

	WindowListener windowListener = new java.awt.event.WindowAdapter() {
		public void windowOpened(java.awt.event.WindowEvent e) {
		    ThreadCountFrame.this.timer.start();
		    //System.out.println(" timer started");
		}

		public void windowClosed(java.awt.event.WindowEvent e) {
		    ThreadCountFrame.this.timer.stop();
		    //System.out.println(" timer stoped");
		}
	    };
	this.addWindowListener( windowListener );
    }
}
	
	


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
	
	label = new JLabel("                                      ");
	label.setForeground(Color.green);
	label.setBackground(Color.black);
	label.setOpaque(true);
	getContentPane().add(label, BorderLayout.CENTER);

	ActionListener timerListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    label.setText("active thread count : "+initialThreadGroup.activeCount()+"    ");
		}
	    };
	timer = new Timer(100, timerListener);

	WindowListener windowListener = new java.awt.event.WindowAdapter() {
		public void windowOpened(java.awt.event.WindowEvent e) {
		    timer.start();
		    //System.out.println(" timer started");
		}

		public void windowClosed(java.awt.event.WindowEvent e) {
		    timer.stop();
		    //System.out.println(" timer stoped");
		}
	    };
	addWindowListener( windowListener );
    }
}
	
	


/*
 * OpenHelpDist.java.java
 *
 * Created on 5/02/2002
 */

package visidia.gui.metier.inputOutput;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Help for setting up the distributed simulation (configuration steps).
 *
 * @author DERBEL bilel
 * @version 1.0
 */
public class OpenHelpDist extends JDialog implements ActionListener, WindowListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8528289126080696814L;
	private JEditorPane helpLocalNode = new JEditorPane();
    private JEditorPane helpNodeLocation = new JEditorPane();
    private JEditorPane helpRegistry = new JEditorPane();
    //private JButton buttonOk = new JButton("Ok");

    /**
     * Build a failure detector configuration window.
     */
    public OpenHelpDist(String title) {
        // set parameters
        this.setTitle(title);
        // build gui
	this.addWindowListener(this);
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Local Node specification", this.buildLocalNode());
        jTabbedPane.addTab("Distirbuting the Nodes", this.buildNodeLocation());
	jTabbedPane.addTab("Configuring the registry",this.buildRegistry());
	this.getContentPane().add(jTabbedPane);
    }

    private Component buildRegistry() {
	this.helpRegistry.setContentType("text/html");
        this.helpRegistry.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    this.helpRegistry.setPage(file.toURI().toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            this.helpRegistry.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(this.helpRegistry);
        return jScrollPane;
    }

    private Component buildNodeLocation() {
	this.helpNodeLocation.setContentType("text/html");
        this.helpNodeLocation.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    this.helpNodeLocation.setPage(file.toURI().toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            this.helpNodeLocation.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(this.helpNodeLocation);
        return jScrollPane;
    }

    private Component buildLocalNode() {
	this.helpLocalNode.setContentType("text/html");
        this.helpLocalNode.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    this.helpLocalNode.setPage(file.toURI().toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            this.helpLocalNode.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(this.helpLocalNode);
        return jScrollPane;
    }

    public void actionPerformed(ActionEvent e) {
    }

    /*
     * WindowListener methods.
     */
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    /**
     * Show dialog window.
     */
    public void show() {
        this.setSize(600, 400);
	super.setVisible(true);
    }
}

/*
 * OpenHelpDist.java.java
 *
 * Created on 5/02/2002
 */

package visidia.gui.metier.inputOutput;

import java.awt.*;
import javax.swing.*;
//import java.util.*;
import java.awt.event.*;
//import visidia.gui.donnees.*;
import java.io.*;

/**
 * Help for setting up the distributed simulation (configuration steps).
 *
 * @author DERBEL bilel
 * @version 1.0
 */
public class OpenHelpDist extends JDialog implements ActionListener, WindowListener {

    private JEditorPane helpLocalNode = new JEditorPane();
    private JEditorPane helpNodeLocation = new JEditorPane();
    private JEditorPane helpRegistry = new JEditorPane();
    private JButton buttonOk = new JButton("Ok");

    /**
     * Build a failure detector configuration window.
     */
    public OpenHelpDist(String title) {
        // set parameters
        setTitle(title);
        // build gui
	addWindowListener(this);
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Local Node specification", buildLocalNode());
        jTabbedPane.addTab("Distirbuting the Nodes", buildNodeLocation());
	jTabbedPane.addTab("Configuring the registry",buildRegistry());
	getContentPane().add(jTabbedPane);
    }

    private Component buildRegistry() {
	helpRegistry.setContentType("text/html");
        helpRegistry.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    helpRegistry.setPage(file.toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            helpRegistry.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(helpRegistry);
        return jScrollPane;
    }

    private Component buildNodeLocation() {
	helpNodeLocation.setContentType("text/html");
        helpNodeLocation.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    helpNodeLocation.setPage(file.toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            helpNodeLocation.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(helpNodeLocation);
        return jScrollPane;
    }

    private Component buildLocalNode() {
	helpLocalNode.setContentType("text/html");
        helpLocalNode.setEditable(false);
        try {
	    File file = new File("/net/t1/derbel/toto.html");
	    helpLocalNode.setPage(file.toURL());
	} catch (IOException e) {
	    //e.printStackTrace();
            helpLocalNode.setText("Help not available");
        }
        JScrollPane jScrollPane = new JScrollPane(helpLocalNode);
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
        setSize(600, 400);
	super.show();
    }
}

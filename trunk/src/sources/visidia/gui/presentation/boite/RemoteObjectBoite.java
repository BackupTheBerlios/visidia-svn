package visidia.gui.presentation.boite;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Hashtable;
import javax.swing.text.*;
import javax.swing.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.donnees.*;
import visidia.gui.metier.*;
import visidia.network.*;

/**
 * Cette classe cree une boite pour specifier l'emplacement des serveurs 
 * relatifs à chaque noeud du graphe.   
 */
public class RemoteObjectBoite implements ActionListener {
    
    protected JDialog dialog;
    protected JScrollPane scroller;
    protected JButton buttonOk;
    protected JPanel labelPanel, buttonPane, mainPane;
    protected JLabel label1, label2, label3;

    public RemoteObjectBoite(FenetreDeSimulationDist parent, String titre, Hashtable table) {
	this.dialog = new JDialog(parent, titre);
	mainPane = new JPanel();
	mainPane.setLayout(new BorderLayout());
	mainPane.setPreferredSize(new Dimension(400,150));
	ajouterBoutons(table);
    }


    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	dialog.pack();
	dialog.show();
	dialog.setLocationRelativeTo(parent);
    }
    
    /**
     * Ajoute les boutons en bas de la boite.           
     */
    public void ajouterBoutons(Hashtable nodeServerStub) {
	
	Enumeration theServers = nodeServerStub.keys();
	int size = nodeServerStub.size();
	
	buttonPane = new JPanel(new FlowLayout());
	labelPanel = new JPanel(new GridLayout(size+1,0));
	
	buttonOk = new JButton("Ok");
	buttonOk.addActionListener(this);
	
	label1 = new JLabel("Host");
	label2 = new JLabel("Server Name");
	label3 = new JLabel("List Of Nodes");
	
	labelPanel.add(label1);
	labelPanel.add(label2);
	labelPanel.add(label3);
	
	try {	
	    while(theServers.hasMoreElements()){
		Vector server = (Vector)theServers.nextElement();
		JLabel serverName = new JLabel((String)server.elementAt(0));
		labelPanel.add(serverName);
		NodeServer nodeServer = (NodeServer)nodeServerStub.get(server);
		Vector nodeByHost = nodeServer.getNodes();
		String nameOfServerUrl = (String)nodeByHost.remove(0);
		JLabel serverUrl = new JLabel(nameOfServerUrl);
		labelPanel.add(serverUrl);
		JComboBox jcb = new JComboBox(nodeByHost);
		labelPanel.add(jcb);
	    }
	} catch (Exception e) {
	}
    

	buttonPane.add(buttonOk);
	
	scroller = new JScrollPane(labelPanel);
	scroller.setOpaque(true);
	
	mainPane.add(scroller,BorderLayout.CENTER);
	mainPane.add(buttonPane,BorderLayout.SOUTH);
	
	dialog.getContentPane().add(mainPane,BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    dialog.setVisible(false);
	    dialog.dispose();
	}
    }
    
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return dialog;
    }
}

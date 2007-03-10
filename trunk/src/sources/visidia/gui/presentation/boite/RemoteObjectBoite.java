package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import visidia.network.NodeServer;

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
	this.mainPane = new JPanel();
	this.mainPane.setLayout(new BorderLayout());
	this.mainPane.setPreferredSize(new Dimension(400,150));
	this.ajouterBoutons(table);
    }


    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	this.dialog.pack();
	this.dialog.setVisible(true);
	this.dialog.setLocationRelativeTo(parent);
    }
    
    /**
     * Ajoute les boutons en bas de la boite.           
     */
    public void ajouterBoutons(Hashtable nodeServerStub) {
	
	Enumeration theServers = nodeServerStub.keys();
	int size = nodeServerStub.size();
	
	this.buttonPane = new JPanel(new FlowLayout());
	this.labelPanel = new JPanel(new GridLayout(size+1,0));
	
	this.buttonOk = new JButton("Ok");
	this.buttonOk.addActionListener(this);
	
	this.label1 = new JLabel("Host");
	this.label2 = new JLabel("Server Name");
	this.label3 = new JLabel("List Of Nodes");
	
	this.labelPanel.add(this.label1);
	this.labelPanel.add(this.label2);
	this.labelPanel.add(this.label3);
	
	try {	
	    while(theServers.hasMoreElements()){
		Vector server = (Vector)theServers.nextElement();
		JLabel serverName = new JLabel((String)server.elementAt(0));
		this.labelPanel.add(serverName);
		NodeServer nodeServer = (NodeServer)nodeServerStub.get(server);
		Vector nodeByHost = nodeServer.getNodes();
		String nameOfServerUrl = (String)nodeByHost.remove(0);
		JLabel serverUrl = new JLabel(nameOfServerUrl);
		this.labelPanel.add(serverUrl);
		JComboBox jcb = new JComboBox(nodeByHost);
		this.labelPanel.add(jcb);
	    }
	} catch (Exception e) {
	}
    

	this.buttonPane.add(this.buttonOk);
	
	this.scroller = new JScrollPane(this.labelPanel);
	this.scroller.setOpaque(true);
	
	this.mainPane.add(this.scroller,BorderLayout.CENTER);
	this.mainPane.add(this.buttonPane,BorderLayout.SOUTH);
	
	this.dialog.getContentPane().add(this.mainPane,BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == this.buttonOk) {
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
    }
    
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return this.dialog;
    }
}

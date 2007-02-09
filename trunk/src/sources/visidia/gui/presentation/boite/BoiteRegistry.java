package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

/**
 * Cette classe cree une boite pour spécifier l'emplacement des serveurs 
 * relatifs à chaque noeud du graphe.   
 */
public class BoiteRegistry implements ActionListener {
    
    protected FenetreDeSimulationDist parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;

    protected JPanel registryPanel, buttonPane;
    protected JLabel reg;
    protected JTextField registryPort;
    
    //protected HashTable table;
    
    
    //Constructeurs
    public BoiteRegistry(FenetreDeSimulationDist parent, String titre) {
	
	this.dialog = new JDialog(parent, titre);
	this.parent = parent;
	this.dialog.getContentPane().setLayout(new GridLayout(2,0));
	this.ajouterBoutons(); 
	
    }
    
    //Methodes
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	this.dialog.pack();
	this.dialog.show();
	this.dialog.setLocationRelativeTo(parent);
    }
    
    
    /**
     * Ajoute les boutons en bas de la boite. Suivant "est_editable", 
     * certains d'entre eux seront actifs ou non.                     
     */
    public void ajouterBoutons() {
	
	this.buttonPane = new JPanel(new FlowLayout());
	this.registryPanel = new JPanel(new GridLayout(1,2));
	
	//ajout des bouttons ok et cancel
	this.buttonOk = new JButton("Ok");
	this.buttonOk.addActionListener(this);
	
	this.buttonCancel = new JButton("Cancel");
	this.buttonCancel.addActionListener(this);
	

	//ajout des champs de saisie de l'emplacement du serveur 
	//pour chaque noued du graphe
	this.reg = new JLabel("Registry port : ");
	this.registryPort = new JTextField("");

	this.registryPanel.add(this.reg);
	this.registryPanel.add(this.registryPort);

	
	this.buttonPane.add(this.buttonOk);
	this.buttonPane.add(this.buttonCancel);    

	this.dialog.getContentPane().add(this.registryPanel, BorderLayout.CENTER);
	this.dialog.getContentPane().add(this.buttonPane, BorderLayout.SOUTH);
    }
    

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == this.buttonOk) {
	    String portNumber = this.registryPort.getText();
	    if (!(portNumber.equals("") | (portNumber == null)))
		this.parent.setRegistryPort(portNumber);
	  
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
	
	if(e.getSource() == this.buttonCancel) {
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
    }

    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return this.dialog;
    }
}

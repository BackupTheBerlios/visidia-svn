package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import visidia.gui.presentation.userInterfaceSimulation.*;

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
	dialog.getContentPane().setLayout(new GridLayout(2,0));
	ajouterBoutons(); 
	
    }
    
    //Methodes
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	dialog.pack();
	dialog.show();
	dialog.setLocationRelativeTo(parent);
    }
    
    
    /**
     * Ajoute les boutons en bas de la boite. Suivant "est_editable", 
     * certains d'entre eux seront actifs ou non.                     
     */
    public void ajouterBoutons() {
	
	buttonPane = new JPanel(new FlowLayout());
	registryPanel = new JPanel(new GridLayout(1,2));
	
	//ajout des bouttons ok et cancel
	buttonOk = new JButton("Ok");
	buttonOk.addActionListener(this);
	
	buttonCancel = new JButton("Cancel");
	buttonCancel.addActionListener(this);
	

	//ajout des champs de saisie de l'emplacement du serveur 
	//pour chaque noued du graphe
	reg = new JLabel("Registry port : ");
	registryPort = new JTextField("");

	registryPanel.add(reg);
	registryPanel.add(registryPort);

	
	buttonPane.add(buttonOk);
	buttonPane.add(buttonCancel);    

	dialog.getContentPane().add(registryPanel, BorderLayout.CENTER);
	dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    String portNumber = registryPort.getText();
	    if (!(portNumber.equals("") | portNumber == null))
		parent.setRegistryPort(portNumber);
	  
	    dialog.setVisible(false);
	    dialog.dispose();
	}
	
	if(e.getSource() == buttonCancel) {
	    dialog.setVisible(false);
	    dialog.dispose();
	}
    }

    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return dialog;
    }
}

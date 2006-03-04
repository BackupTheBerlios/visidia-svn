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

/**
 * Cette classe cree une boite pour specifier l'emplacement des serveurs 
 * relatifs a chaque noeud du graphe.   
 */
public class BoiteExperimentComplet implements ActionListener {
    
    protected FenetreDeSimulationDist parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;

    protected JPanel panel, buttonPane;
    protected JLabel jl;
    protected JTextField sizeField;
    
    //protected HashTable table;
    
    
    //Constructeurs
    public BoiteExperimentComplet(FenetreDeSimulationDist parent, String titre) {
	
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
	panel = new JPanel(new GridLayout(1,2));
	
	//ajout des bouttons ok et cancel
	buttonOk = new JButton("Ok");
	buttonOk.addActionListener(this);
	
	buttonCancel = new JButton("Cancel");
	buttonCancel.addActionListener(this);
	

	//ajout des champs de saisie de l'emplacement du serveur 
	//pour chaque noued du graphe
	jl = new JLabel("graph size : ");
	sizeField = new JTextField("");

	panel.add(jl);
	panel.add(sizeField);

	
	buttonPane.add(buttonOk);
	buttonPane.add(buttonCancel);    

	dialog.getContentPane().add(panel, BorderLayout.CENTER);
	dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    String size = sizeField.getText();
	    parent.setExperimentSize(new Integer(size));
	
	    System.out.println(size);
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

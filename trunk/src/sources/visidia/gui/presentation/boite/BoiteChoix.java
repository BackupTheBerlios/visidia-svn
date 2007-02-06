package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.gui.donnees.*;



/**
 * Cette classe cree une boite utilisée pour afficher et modifier    
 * la table des correspondances etat-couleur                         
 */
public class BoiteChoix implements ActionListener {
    
    /** La fenêtre parent : la boite sera centrée sur cette fenêetre.*/
    protected Editeur parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
     /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;
    /** la table des couples couleur-etat */
    protected Hashtable dico;
    protected ReglePanel plancheDeCouleurs;
    protected Color couleur ;
    
    
    //Constructeurs
    public BoiteChoix(Editeur parent, String titre) {
	
	this.dialog = new JDialog(parent, titre);
	this.parent = parent;
      	this.dico = TableCouleurs.getTableCouleurs() ;
	this.plancheDeCouleurs = new ReglePanel( this.dico , this);
	
	this.dialog.getContentPane().setLayout(new BorderLayout());
	this.dialog.getContentPane().add(this.plancheDeCouleurs, BorderLayout.NORTH);
	
	this.ajouterBoutons(); 
	
    }
    //Methodes
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	this.dialog.pack();
	this.dialog.show();
	this.dialog.setLocationRelativeTo(parent);
    }
    
    /** Ajoute un bouton nomme "label" au panel "pane" */
    public JButton addButton(JPanel pane, String label) {
	JPanel tmp = new JPanel(new FlowLayout());
	JButton button = new JButton(label);
	tmp.add(button);
	button.setSize(button.getMinimumSize());
	pane.add(tmp);
	pane.add(Box.createRigidArea(new Dimension(0, 5)));
	return button;
    }
    
    
    /**
     * Ajoute les boutons en bas de la boite. Suivant "est_editable", 
     * certains d'entre eux seront actifs ou non.                     
     */
    public void ajouterBoutons() {
    JPanel buttonPane = new JPanel(new FlowLayout());
    
    this.buttonOk = new JButton("Ok");
    this.buttonOk.addActionListener(this);
    
    this.buttonCancel = new JButton("Cancel");
    this.buttonCancel.addActionListener(this);

    buttonPane.add(this.buttonOk);
    buttonPane.add(this.buttonCancel);    
    this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
  }
  

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == this.buttonOk) {
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












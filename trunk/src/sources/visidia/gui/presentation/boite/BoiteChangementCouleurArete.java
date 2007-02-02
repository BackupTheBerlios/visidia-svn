package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.event.*;
import java.util.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
//import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.gui.donnees.conteneurs.*;

/*
 * Cette classe crée une boite utilisée pour modifier l'état    
 * d'un ou de pusieurs arêtes selectionnées sur la fenêtre de    
 * simulation.                                                  
 */
public class BoiteChangementCouleurArete implements ActionListener{
    
    // instance Variables
    /** The parent window : the box will be centered on this window */
    protected FenetreDeSimulation parent;
    /** The JDialog where all will be painted */
    protected JDialog dialog;
    /** The Ok button */
    protected JButton buttonOk;
    /** The Cancel button */
    protected JButton buttonCancel;
    /** The apply button */
    protected JButton buttonApply;
    
    protected Ensemble selectionAretes;
    /** Ce booleen vaut VRAI si une des caracteristiques de l'ObjetVisu
	a été modifiée par l'utilisateur, et FAUX sinon.*/
    protected boolean modif;
    /** Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur
	du trait de la forme dessine */
    protected JPanel etatPanel;
    /* Une BoiteCouleur qui permet de modifier la couleur */
    protected BoiteCouleur couleur;
    protected FormeDessin forme;
    
    //Constructeurs
    /**
     * Crée une nouvelle boite pour afficher les caracteristiques de
     * "un_objet".  Ces caracteristiques seront modifiables.
     */
    public BoiteChangementCouleurArete(FenetreDeSimulation parent, Ensemble uneSelection) {
	this(parent, uneSelection , "Edges properties");
    }
    
    /**
     * Crée une nouvelle boite appelée "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public BoiteChangementCouleurArete(FenetreDeSimulation parent,
				       Ensemble uneSelection,
				       String titre) {
       
	this.dialog = new JDialog(parent, titre);
	this.parent = parent;
	this.modif = false;
	
	this.selectionAretes = uneSelection;
	
	etatPanel = new JPanel();
	
	Enumeration e = selectionAretes.elements();
	forme = (FormeDessin)e.nextElement();
	couleur = new BoiteCouleur(this,
				   "Line Color (R,G,B): ",
				   forme.couleurTrait().getRed(),
				   forme.couleurTrait().getGreen(),
				   forme.couleurTrait().getBlue(),
				   true);
	etatPanel.add(couleur.panel());
	
	dialog.getContentPane().setLayout(new BorderLayout());
	dialog.getContentPane().add(etatPanel, BorderLayout.NORTH);
	ajouterBoutons();
    }

    //Methodes  

    /** Affiche la boite et la centré par rapport à "parent".*/
    public void show(Frame parent) {
	dialog.pack();
	dialog.show();
	dialog.setLocationRelativeTo(parent);
    }
    
    /** Ajoute les boutons en bas de la boite.*/
    public void ajouterBoutons() {
	JPanel buttonPane = new JPanel(new FlowLayout());
	
	buttonOk = new JButton("Ok");
	buttonOk.addActionListener(this);
    
	buttonCancel = new JButton("Cancel");
	buttonCancel.addActionListener(this);
	
	buttonApply = new JButton("Apply");
	buttonApply.addActionListener(this);
	
	buttonPane.add(buttonOk);
	buttonPane.add(buttonCancel);    
	buttonPane.add(buttonApply);
	buttonApply.setEnabled(modif);
	dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
  

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    try{
		buttonOk();
		dialog.setVisible(false);
		dialog.dispose();
	    } catch(NumberFormatException exception){
		StringTokenizer st = new StringTokenizer(exception.getMessage(), "\n");
		int nb_lignes = st.countTokens();
		String message = new String();
		for(int i=0;i<nb_lignes;i++)
		    message = message + "\n" + st.nextToken();
		JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	}
	if(e.getSource() == buttonApply) {
	    try{
		buttonOk();
		parent.repaint();
		modif = false;
		buttonApply.setEnabled(false);
	    } catch(NumberFormatException exception){
		StringTokenizer st = new StringTokenizer(exception.getMessage(), "\n");
		int nb_lignes = st.countTokens();
		String message = new String();
		for(int i=0;i<nb_lignes;i++)
		    message = message + "\n" + st.nextToken();
		JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
	    }
	}
	if(e.getSource() == buttonCancel) {
	    dialog.setVisible(false);
	    dialog.dispose();
	}
    }
    
    /**
     * Retourne VRAI si une des caracteristiques de l'ObjetVisu a été modifiée,
     * et FAUX sinon.                                                          
     */ 
    public void elementModified() {
	modif = true;
	buttonApply.setEnabled(modif);
    }
    
    /** Cette méthode est appelée si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	Enumeration e = selectionAretes.elements();
	Color trait;
	try{
	    trait = new Color(couleur.R,couleur.G,couleur.B);
	} catch (NumberFormatException exception){
	    throw new NumberFormatException("Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
	}
	while(e.hasMoreElements()){
	    ((FormeDessin)e.nextElement()).changerCouleurTrait(trait);
	}
	parent.simulationPanel().repaint();
    }
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return dialog;
    }
}















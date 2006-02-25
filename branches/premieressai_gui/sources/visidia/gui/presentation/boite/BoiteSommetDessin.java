package visidia.gui.presentation.boite;

import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.Editeur;
import visidia.gui.presentation.factory.*; // pour avoir les fabriques
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*; // a enlever ?
import java.util.*;

/** Cette class "raffine" sa super classe en lui ajoutant les champs permettant 
 *  d'afficher et de modifier les caracteristiques specifiques au sommets
 **/ 

public class BoiteSommetDessin extends BoiteFormeDessin{

    // Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur du fond de l'objet. 
  protected LigneChoixCouleur couleur_fond;
    // Le champ de saisie de l'abscisse du sommet.
  protected JTextField centreX;
    // Le champ de saisie de l'ordonnee du sommet.
  protected JTextField centreY;
    // Le champ de saisie de l'abscisse du sommet.
  protected int initialXvalue;
  // Le champ de saisie de l'ordonnee du sommet.
  protected int initialYvalue;  

						  
  /** Cree une nouvelle boite pour afficher les caracteristiques de "sommet". Ces caracteristiques seront modifiables.*/
  public BoiteSommetDessin(JFrame parent, SommetDessin sommet) {
    this(parent, sommet, "Object properties", true);
  }


  /** Cree une nouvelle boite appelee "titre" pour afficher les 
    caracteristiques de "sommet". Suivant la valeur de "est_editable", 
  les caracteristiques de l'ObjetVisu seront modifiables.*/
  public BoiteSommetDessin(JFrame parent, SommetDessin sommet,
                            String titre, boolean est_editable) {
    super(parent, sommet, titre, est_editable);

    initialXvalue = sommet.centreX();
    initialYvalue = sommet.centreY();
    
    couleur_fond = new LigneChoixCouleur(this,
					 "Fill color (R,G,B) : ",
					 sommet.couleurFond().getRed(),
					 sommet.couleurFond().getGreen(),
					 sommet.couleurFond().getBlue(), 
					 est_editable);
    caracteristicsPane.add(couleur_fond.panel());

    centreX = ligne_editable(caracteristicsPane, 
			     "X :",
			     Integer.toString(initialXvalue),
			     est_editable);
    centreX.addActionListener(this);

    centreY = ligne_editable(caracteristicsPane, 
			     "Y :",
			     Integer.toString(initialYvalue),
			     est_editable);
    centreY.addActionListener(this);
  }
  
  public void buttonOk() {
    Color fond;
    int x, y;
    
    try {
	fond = new Color(couleur_fond.getRed(), couleur_fond.getGreen(), couleur_fond.getBlue());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
    }
    super.buttonOk();
    ((SommetDessin)forme).changerCouleurFond(fond);
    
    try {
	x = Integer.parseInt(centreX.getText());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for X:\nAn integer is expected.");
    }
    try {
	y = Integer.parseInt(centreY.getText());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for Y:\nAn integer is expected.");
    }
    super.buttonOk();
    ((SommetDessin)forme).placer(x, y);
  }

    /** Cette methode permet d'activer ou de desactiver le bouton de selection 
    de la couleur de fond du SommetDessin, en fonction de la valeur du booleen 
    passe en argument..*/
    public void couleurFondSetEditable(boolean t) {
	couleur_fond.setEditable(t);
    }
    
    public void centreXSetEditable(boolean t) {
	centreX.setEditable(t);
    }
    
    public void centreYSetEditable(boolean t) {
	centreY.setEditable(t);
    }
    
    public void actionPerformed(ActionEvent evt) {
	if (evt.getSource() == centreX) {
	    int x;
	    try {
		x = Integer.parseInt(centreX.getText());
	    } catch(NumberFormatException exception) {
		JOptionPane.showMessageDialog(dialog,
					      "Bad argument type for X:\n"
					      + "An integer is expected.",
					      "Error",
					      JOptionPane.ERROR_MESSAGE);
		centreX.setText(Integer.toString(initialXvalue));
	    }
	    elementModified();
	}
	if (evt.getSource() == centreY) {
	    int y;
	    try {
		y = Integer.parseInt(centreY.getText());
	    } catch(NumberFormatException exception) {
	  JOptionPane.showMessageDialog(dialog,
					"Bad argument type for Y:\n"
					+ "An integer is expected.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
	  centreY.setText(Integer.toString(initialYvalue));
      }
	    elementModified();
	}
	super.actionPerformed(evt);
  }
    
}



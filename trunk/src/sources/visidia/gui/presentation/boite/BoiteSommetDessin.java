package visidia.gui.presentation.boite;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import visidia.gui.presentation.SommetDessin;

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

    this.initialXvalue = sommet.centreX();
    this.initialYvalue = sommet.centreY();
    
    this.couleur_fond = new LigneChoixCouleur(this,
					 "Fill color (R,G,B) : ",
					 sommet.couleurFond().getRed(),
					 sommet.couleurFond().getGreen(),
					 sommet.couleurFond().getBlue(), 
					 est_editable);
    this.caracteristicsPane.add(this.couleur_fond.panel());

    this.centreX = this.ligne_editable(this.caracteristicsPane, 
			     "X :",
			     Integer.toString(this.initialXvalue),
			     est_editable);
    this.centreX.addActionListener(this);

    this.centreY = this.ligne_editable(this.caracteristicsPane, 
			     "Y :",
			     Integer.toString(this.initialYvalue),
			     est_editable);
    this.centreY.addActionListener(this);
  }
  
  public void buttonOk() {
    Color fond;
    int x, y;
    
    try {
	fond = new Color(this.couleur_fond.getRed(), this.couleur_fond.getGreen(), this.couleur_fond.getBlue());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
    }
    super.buttonOk();
    ((SommetDessin)this.forme).changerCouleurFond(fond);
    
    try {
	x = Integer.parseInt(this.centreX.getText());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for X:\nAn integer is expected.");
    }
    try {
	y = Integer.parseInt(this.centreY.getText());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for Y:\nAn integer is expected.");
    }
    super.buttonOk();
    ((SommetDessin)this.forme).placer(x, y);
  }

    /** Cette methode permet d'activer ou de desactiver le bouton de selection 
    de la couleur de fond du SommetDessin, en fonction de la valeur du booleen 
    passe en argument..*/
    public void couleurFondSetEditable(boolean t) {
	this.couleur_fond.setEditable(t);
    }
    
    public void centreXSetEditable(boolean t) {
	this.centreX.setEditable(t);
    }
    
    public void centreYSetEditable(boolean t) {
	this.centreY.setEditable(t);
    }
    
    public void actionPerformed(ActionEvent evt) {
	if (evt.getSource() == this.centreX) {
	    //int x;
	    try {
		/*x =*/ Integer.parseInt(this.centreX.getText());
	    } catch(NumberFormatException exception) {
		JOptionPane.showMessageDialog(this.dialog,
					      "Bad argument type for X:\n"
					      + "An integer is expected.",
					      "Error",
					      JOptionPane.ERROR_MESSAGE);
		this.centreX.setText(Integer.toString(this.initialXvalue));
	    }
	    this.elementModified();
	}
	if (evt.getSource() == this.centreY) {
	    //int y;
	    try {
		/*y =*/ Integer.parseInt(this.centreY.getText());
	    } catch(NumberFormatException exception) {
	  JOptionPane.showMessageDialog(this.dialog,
					"Bad argument type for Y:\n"
					+ "An integer is expected.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
	  this.centreY.setText(Integer.toString(this.initialYvalue));
      }
	    this.elementModified();
	}
	super.actionPerformed(evt);
  }
    
}



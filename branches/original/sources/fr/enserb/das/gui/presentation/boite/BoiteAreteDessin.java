package fr.enserb.das.gui.presentation.boite;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.Editeur;

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs permettant  
 * d'afficher et de modifier les caract�ristiques sp�cifiques d'une ar�te :         */
public class BoiteAreteDessin extends BoiteFormeDessin implements ActionListener {
  

    // Variable d'instance
    /** Le champ de saisie de l'abscisse de l'origine de la fl�che.*/
    protected JTextField origineX;
    /** Le champ de saisie de l'ordonn�e de l'origine de la fl�che.*/
    protected JTextField destinationX;
    /** Le champ de saisie de l'abscisse de la destination de la fl�che.*/
    protected JTextField origineY;
    /** Le champ de saisie de l'ordonn�e de la destination de la fl�che.*/
    protected JTextField destinationY;
    /** La valeur de l'abscisse de l'origine de la fl�che.*/
    protected int initialStartX;
    /** La valeur de l'ordonn�e de l'origine de la fl�che.*/
    protected int initialStartY;  
    /** La valeur de l'abscisse de la destination de la fl�che.*/
    protected int initialEndX;
    /** La valeur de l'ordonn�e de la destination de la fl�che.*/
    protected int initialEndY;
    
       
    //Construsteurs
    /** Cr�e une nouvelle boite pour afficher les caract�ristiques de "une_arete"*/
    public BoiteAreteDessin(JFrame parent, AreteDessin une_arete) {
	this(parent, une_arete, "Edge properties", true);
    }
    
    
    /** Cr�e une nouvelle boite, centr�e sur "parent" pour afficher les caract�ristiques de "une_arete".
     * La boite sera appel�e "titre"
     * Suivant la valeur de "est_editable", les caract�ristiques de l'ar�te sont modifiables.
     **/
    public BoiteAreteDessin(JFrame parent, AreteDessin une_arete, String titre,
		      boolean est_editable) {
	super(parent, une_arete, titre, est_editable);
	SommetDessin s = ((AreteDessin)forme).getArete().origine().getSommetDessin();
	initialStartX = s.centreX();
	initialStartY = s.centreY();
	s = ((AreteDessin)forme).getArete().destination().getSommetDessin();
	initialEndX =  s.centreX();
	initialEndY =  s.centreY();
	
	origineX = ligne_editable(caracteristicsPane,
				  "Origin X :",
				  Integer.toString(initialStartX),
				  est_editable);
	origineX.addActionListener(this);

	origineY = ligne_editable(caracteristicsPane, 
				  "Origin Y :",
				  Integer.toString(initialStartY),
				  est_editable);
	origineY.addActionListener(this);
	
	destinationX = ligne_editable(caracteristicsPane,
				      "Destination X :",
				      Integer.toString(initialEndX),
				      est_editable);
	destinationX.addActionListener(this);
	
	destinationY = ligne_editable(caracteristicsPane,
				      "Destination Y :",
				      Integer.toString(initialEndY),
				      est_editable);
	destinationY.addActionListener(this);
    }
    
    
    //Methodes
     
    /** Cette m�thode est appel�e quand l'utilisateur actionne un des boutons de la boite.*/
    public void actionPerformed(ActionEvent e) {
	int x1, y1, x2, y2;
	if (e.getSource() == origineX) {
	  try {
	      x1 = Integer.parseInt(origineX.getText());
	  } catch(NumberFormatException exception) {
	      JOptionPane.showMessageDialog(dialog,
					    "Bad argument type for starting point X:\n"
					    + "An integer is expected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
	      origineX.setText(Integer.toString(initialStartX));
	  }
	  elementModified();
      }
	if (e.getSource() == origineY) {
	    try {
	      y1 = Integer.parseInt(origineY.getText());
	    } catch(NumberFormatException exception) {
		JOptionPane.showMessageDialog(dialog,
					      "Bad argument type for starting point Y:\n"
					    + "An integer is expected.",
					      "Error",
					      JOptionPane.ERROR_MESSAGE);
	      origineY.setText(Integer.toString(initialStartY));
	    }
	    elementModified();
      }
	if (e.getSource() == destinationX) {
	    try {
		x2 = Integer.parseInt(destinationX.getText());
	  } catch(NumberFormatException exception) {	
	      JOptionPane.showMessageDialog(dialog,
					    "Bad argument type for ending point X:\n"
					    + "An integer is expected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
	      destinationX.setText(Integer.toString(initialEndX));
	  }
		elementModified();
	}
	if (e.getSource() == destinationY) {
	  try {
	      y2 = Integer.parseInt(destinationY.getText());
	  } catch(NumberFormatException exception) {
	      JOptionPane.showMessageDialog(dialog,
					    "Bad argument type for ending point Y:\n"
					    + "An integer is expected.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
	      destinationY.setText(Integer.toString(initialEndY));
	  }
	  elementModified();
	}
      super.actionPerformed(e);
    }

   /** Cette m�thode est appel�e quand l'utilisateur appuie sur le bouton Ok */ 
    public void buttonOk() {
	int x1, y1, x2, y2;
	try {
	    x1 = Integer.parseInt(origineX.getText());
	} catch(NumberFormatException exception) {
	    throw new NumberFormatException("Bad argument type for starting point X:\nAn integer is expected.");
	}
	try {
	    y1 = Integer.parseInt(origineY.getText());
	} catch(NumberFormatException exception) {
	    throw new NumberFormatException("Bad argument type for end point Y:\nAn integer is expected.");
	}
	try {
	    x2 = Integer.parseInt(destinationX.getText());
	} catch(NumberFormatException exception) {
	    throw new NumberFormatException("Bad argument type for starting point X:\nAn integer is expected.");
	}
	try {
	    y2 = Integer.parseInt(destinationY.getText());
	} catch(NumberFormatException exception) {
	    throw new NumberFormatException("Bad argument type for end point Y:\nAn integer is expected.");
	    }
	
	super.buttonOk();

      ((AreteDessin)forme).getArete().origine().getSommetDessin().placer(x1,y1);
      ((AreteDessin)forme).getArete().destination().getSommetDessin().placer(x2,y2);

    }

    public void origineXSetEditable(boolean t) {
	origineX.setEditable(t);
    }
    
    public void origineYSetEditable(boolean t) {
	origineY.setEditable(t);
    }

    public void destinationXSetEditable(boolean t) {
	destinationX.setEditable(t);
    }
    
    public void destinationYSetEditable(boolean t) {
	destinationY.setEditable(t);
    }


    

}












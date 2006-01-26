package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.enserb.das.gui.presentation.*;

/** Cette classe crée un panel qui contient un bouton et un label. Le label affiche 
la valeur d'une couleur (format R,G,B), et le bouton permet d'ouvrir une boite de choix 
de couleur. Ce panel sera affiché à l'intérieur d'une fenêtre, sous la forme d'une ligne.
L'existence de cette classe permet de simplifier le choix d'une nouvelle couleur.**/
public class LigneChoixCouleur implements ActionListener {
  
  /** Le panel dans lequel on affichera les éléments.*/
  protected JPanel pane;
  /** Le label.*/
  protected JLabel jlabel;
  /** Le bouton.*/
  protected JButton button;
  /** Le texte affiché par le label.*/
  protected String label;
  
  /** La composante rouge de la couleur.*/ 
  protected int R;
  /** La composante vertee de la couleur.*/ 
  protected int G;
  /** La composante bleue de la couleur.*/ 
  protected int B;
  
  /** La BoiteFormeDessin parente (on en a besoin pour appeller une méthode si la couleur choisie est modifiée).*/
  protected BoiteFormeDessin parent;
  
  /** Crée une nouvelle ligne.
    * @param parent la BoiteFormeDessin parente.
    * @param label le texte affiché dans le label.
    * @param R la composante rouge de la couleur d'origine.
    * @param G la composante verte de la couleur d'origine.
    * @param B la composante bleue de la couleur d'origine.
    * @param editable si ce booléen vaut VRAI, le bouton sera actif, sinon, il sera inactif.
    **/
  LigneChoixCouleur(BoiteFormeDessin parent,String label, int R, int G, int B, boolean editable) {
    pane = new JPanel(new GridLayout(1, 2));

    this.parent = parent;
    
    this.R = R;
    this.G = G;
    this.B = B;
    this.label = label;
    
    String initialValue = new String(Integer.toString(R) 
				     + ", "+ Integer.toString(G) 
				     + ", "+ Integer.toString(B)); 
    
    String tmp = new String(label + initialValue);
    while (tmp.length() < 38) {
      tmp = tmp + " ";
    }
    jlabel = new JLabel(tmp);
    
    pane.add(jlabel);
    button = new JButton("Change color");
    button.addActionListener(this);
    button.setEnabled(editable);
    pane.add(button);
  }

  /** Retourne R*/
  public int getRed() {
    return this.R;
  }
  /** Retourne G*/
  public int getGreen() {
    return this.G;
  }
  /** Retourne B*/
  public int getBlue() {
    return this.B;
  }
  
  /** Active ou désactive le bouton suivant la valeur du booléen "editable" passé en argument.*/
  public void setEditable(boolean editable) {
    button.setEnabled(editable);
  }
  
  /** Méthode appelée quand l'utilisateur appuie sur le bouton.*/
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button) {
      Color choosedColor = JColorChooser.showDialog(parent.dialog(), 
						    "Choose color", 
						    new Color(R, G, B));
      if (choosedColor != null) {
	this.R = choosedColor.getRed();
	this.G = choosedColor.getGreen();
	this.B = choosedColor.getBlue();
	parent.elementModified();
	
      }
      String tmp = new String(label
			      + Integer.toString(getRed()) 
			      + ", " + Integer.toString(getGreen()) 
			      + ", " + Integer.toString(getBlue()));
      while (tmp.length() < 38) {
	tmp = tmp + " ";
      }
      jlabel.setText(tmp);
    }
  }

  /** Retourne le JPanel.*/
  public JPanel panel() {
    return pane;
  }
  
}

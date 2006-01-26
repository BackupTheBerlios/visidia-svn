package fr.enserb.das.gui.presentation.boite;

import javax.swing.*;
import java.awt.*;

/** Cette classe affiche une boite de saisie d'une valeur quelconque. 
* Les méthodes de cette classe doivent être redéfinies en fonction de la saisie attendue.
* Tant que la méthode <b>saisieCorrecte()</b> renvoie FAUX, la boite est réaffichée, et une nouvelle saisie doit être effectuée.
**/
public class BoiteSaisie {
  
  /** La fenêtre parente sur laquelle sera centrée la boite.*/
  protected JFrame parent;
  /** La chaîne de caractère saisie par l'utilisateur.*/
  protected String typedText;
  /** Un booléen qui vaut VRAI si une valeur a été saisie, et FAUX sinon.*/
  protected boolean saisie = false;

  /** Crée une nouvelle boite de saisie.
    * @param parent la fenêtre parente.
    * @param titre le titre de la boite.
    * @param message une chaîne de caractères affichée au dessus du champ de saisie.
    * @param chooseFrom un tableau de valeur possibles pour la saisie. Si ce tableau vaut NULL, la boite contiendra un champ de saisie. Sinon, elle contiendra un liste de choix.
    * @param valeurInit la valeur initiale de la saisie.**/
  public BoiteSaisie(JFrame parent, String titre, String message, Object[] chooseFrom, String valeurInit) {
    this.parent = parent;
    typedText = valeurInit;
    
    while (!saisie) {
      Object typedObject = JOptionPane.showInputDialog(parent, 
						       message, 
						       titre, 
						       JOptionPane.QUESTION_MESSAGE,
						       null, 
						       chooseFrom,
						       valeurInit);
      if (typedObject != null) {
	typedText = (String)typedObject;
	if (saisieCorrecte()) {
	  saisie = true;
	  boutonOkAppuye();
	} else {
	  saisieInvalide();
	}
      } else {
	saisie = true;
      }
    }
  }

  /** Retourne la valeur saisie sous forme de chaîne de caractères.*/
  protected String valeurDeRetour() {
    return typedText;
  }
  
  /** Cette méthode est appelée si la saisie est incorrecte. */
  protected void saisieInvalide() {
  }
  
  /** Cette méthode renvoie VRAI si la saisie est correcte, et FAUX sinon.*/
  protected boolean saisieCorrecte() {
    return true;
  }
  
  /** Cette méthode est appelée quand l'utilisateur appuie sur le bouton "ok", et que la valeur saisie est correcte.*/
  protected void boutonOkAppuye() {
  }

}





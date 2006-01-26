package fr.enserb.das.gui.presentation.boite;

import javax.swing.*;
import java.awt.*;

/** Cette classe affiche une boite de saisie d'une valeur quelconque. 
* Les m�thodes de cette classe doivent �tre red�finies en fonction de la saisie attendue.
* Tant que la m�thode <b>saisieCorrecte()</b> renvoie FAUX, la boite est r�affich�e, et une nouvelle saisie doit �tre effectu�e.
**/
public class BoiteSaisie {
  
  /** La fen�tre parente sur laquelle sera centr�e la boite.*/
  protected JFrame parent;
  /** La cha�ne de caract�re saisie par l'utilisateur.*/
  protected String typedText;
  /** Un bool�en qui vaut VRAI si une valeur a �t� saisie, et FAUX sinon.*/
  protected boolean saisie = false;

  /** Cr�e une nouvelle boite de saisie.
    * @param parent la fen�tre parente.
    * @param titre le titre de la boite.
    * @param message une cha�ne de caract�res affich�e au dessus du champ de saisie.
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

  /** Retourne la valeur saisie sous forme de cha�ne de caract�res.*/
  protected String valeurDeRetour() {
    return typedText;
  }
  
  /** Cette m�thode est appel�e si la saisie est incorrecte. */
  protected void saisieInvalide() {
  }
  
  /** Cette m�thode renvoie VRAI si la saisie est correcte, et FAUX sinon.*/
  protected boolean saisieCorrecte() {
    return true;
  }
  
  /** Cette m�thode est appel�e quand l'utilisateur appuie sur le bouton "ok", et que la valeur saisie est correcte.*/
  protected void boutonOkAppuye() {
  }

}





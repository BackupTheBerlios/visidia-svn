package visidia.gui.presentation.boite;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Cette classe affiche une boite de saisie d'une valeur quelconque. Les
 * methodes de cette classe doivent etre redefinies en fonction de la saisie
 * attendue. Tant que la methode <b>saisieCorrecte()</b> renvoie FAUX, la boite
 * est reaffichee, et une nouvelle saisie doit etre effectuee.
 */
public class BoiteSaisie {

	/** La fenetre parente sur laquelle sera centree la boite. */
	protected JFrame parent;

	/** La chaine de caractere saisie par l'utilisateur. */
	protected String typedText;

	/** Un booleen qui vaut VRAI si une valeur a ete saisie, et FAUX sinon. */
	protected boolean saisie = false;

	/***************************************************************************
	 * Cree une nouvelle boite de saisie.
	 * 
	 * @param parent
	 *            la fenetre parente.
	 * @param titre
	 *            le titre de la boite.
	 * @param message
	 *            une chaine de caracteres affichee au dessus du champ de
	 *            saisie.
	 * @param chooseFrom
	 *            un tableau de valeur possibles pour la saisie. Si ce tableau
	 *            vaut NULL, la boite contiendra un champ de saisie. Sinon, elle
	 *            contiendra un liste de choix.
	 * @param valeurInit
	 *            la valeur initiale de la saisie.
	 **************************************************************************/
	public BoiteSaisie(JFrame parent, String titre, String message,
			Object[] chooseFrom, String valeurInit) {
		this.parent = parent;
		this.typedText = valeurInit;

		while (!this.saisie) {
			Object typedObject = JOptionPane.showInputDialog(parent, message,
					titre, JOptionPane.QUESTION_MESSAGE, null, chooseFrom,
					valeurInit);
			if (typedObject != null) {
				this.typedText = (String) typedObject;
				if (this.saisieCorrecte()) {
					this.saisie = true;
					this.boutonOkAppuye();
				} else {
					this.saisieInvalide();
				}
			} else {
				this.saisie = true;
			}
		}
	}

	/** Retourne la valeur saisie sous forme de chaine de caracteres. */
	protected String valeurDeRetour() {
		return this.typedText;
	}

	/** Cette methode est appelee si la saisie est incorrecte. */
	protected void saisieInvalide() {
	}

	/** Cette methode renvoie VRAI si la saisie est correcte, et FAUX sinon. */
	protected boolean saisieCorrecte() {
		return true;
	}

	/**
	 * Cette methode est appelee quand l'utilisateur appuie sur le bouton "ok",
	 * et que la valeur saisie est correcte.
	 */
	protected void boutonOkAppuye() {
	}

}

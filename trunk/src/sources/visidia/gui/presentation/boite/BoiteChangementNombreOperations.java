package visidia.gui.presentation.boite;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import visidia.gui.presentation.userInterfaceEdition.Editeur;

/*******************************************************************************
 * Cette classe permet de changer le nombre d'opérations a annuler * ou a
 * restaurer grace aux commandes de undo/redo par groupes *
 ******************************************************************************/

public class BoiteChangementNombreOperations extends BoiteSaisie {
	// Constructeur
	/***************************************************************************
	 * Crée une nouvelle boite, dans laquelle la valeur par défaut est "nb_op".
	 **************************************************************************/
	public BoiteChangementNombreOperations(JFrame le_parent, int nb_op) {
		super(le_parent, "Number of operations to undo/redo",
				"Choose the number of operations to undo/redo \n"
						+ "by using the undo by set/redo by set commands.",
				null, Integer.toString(nb_op));
	}

	protected void saisieInvalide() {
		JOptionPane.showMessageDialog(this.parent,
				"Bad type of argument (an integer is waited).", "Error",
				JOptionPane.ERROR_MESSAGE);
		return;
	}

	protected void boutonOkAppuye() {
		Integer i = new Integer(this.valeurDeRetour());
		((Editeur) this.parent).setNbOp(i.intValue());
	}

	/** La saisie est correcte si c'est un entier. */
	protected boolean saisieCorrecte() {
		// Integer i;
		try {
			// i =
			new Integer(this.valeurDeRetour());
		} catch (NumberFormatException exception) {
			return false;
		}
		return true;
	}

}

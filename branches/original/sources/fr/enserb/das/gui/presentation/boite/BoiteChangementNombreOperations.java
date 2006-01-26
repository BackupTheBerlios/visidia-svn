package fr.enserb.das.gui.presentation.boite;

import javax.swing.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.*;

/*******************************************************************
 * Cette classe permet de changer le nombre d'opérations à annuler *
 * ou à restaurer grâce aux commandes de undo/redo par groupes     *
 ******************************************************************/
public class BoiteChangementNombreOperations extends BoiteSaisie {
    //Constructeur 
    /** Crée une nouvelle boite, dans laquelle la valeur par défau est "nb_op".**/
    public BoiteChangementNombreOperations(JFrame le_parent, int nb_op) {
	super(le_parent, "Number of operations to undo/redo", 
	      "Choose the number of operations to undo/redo \n" + 
	      "by using the undo by set/redo by set commands.",
	      null, 
	      Integer.toString(nb_op));
    }
    
    protected void saisieInvalide() {
	JOptionPane.showMessageDialog(parent,"Bad type of argument (an integer is waited).",
				      "Error", 
				      JOptionPane.ERROR_MESSAGE); 
	return;
    }
    
    protected void boutonOkAppuye() {
	Integer i = new Integer(valeurDeRetour());
	((Editeur)parent).setNbOp(i.intValue());
    }
    
    /** La saisie est correcte si c'est un entier.*/
    protected boolean saisieCorrecte() {
	Integer i;
	try {
	    i = new Integer(valeurDeRetour());
	} catch(NumberFormatException exception) {
	    return false;
	}
	return true;
    }
    
}










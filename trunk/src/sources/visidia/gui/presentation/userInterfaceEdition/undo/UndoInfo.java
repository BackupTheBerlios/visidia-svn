package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;
import java.util.*;
//import java.io.*;

/** Cette classe implémente la structure de données utilisée pour
 * gérer les opérations de undo/redo. **/

public class UndoInfo extends Vector {
  
    /** L'index de l'operation courante. */
    protected int curseur;
  
    /** Instancie un nouvel objet UndoInfo vide.*/
    public UndoInfo() {
	super();
	curseur = -1;
    }
  
    /* ************************************************************* */
    /* Méthodes privées "accessoires", pour faciliter l'implémentation
       des autres méthodes.*/
    /* ************************************************************* */



    /** Accesseur au curseur. */
    private int curseur() {
	return curseur;
    }

    /** Retourne le groupe d'opérations courant.*/
    private UndoInfoElement currentGroup() {
	try { 
	    return ((UndoInfoElement)elementAt(curseur));
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }


    /** Retourne le groupe d'opérations à l'index "i" */
    private UndoInfoElement groupAt(int i) {
	try{
	    return ((UndoInfoElement)elementAt(i));
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }
    /* ************************************************************* */
    /* Méthodes 'fondamentales' : le strict minimum.                 */
    /* ************************************************************* */
  
    /** Crée un nouveau groupe d'opérations
     * @param undo la description de l'annulation de l'opération
     * @param redo la description de la restauration l'opération
     */
    public void newGroup(String undo, String redo) {
	if (redoMore()) {
	    trimGroups();
	}
	addElement(new UndoInfoElement(undo, redo));
	curseur++;
    }

    /** Ajoute une opération dans la liste, et incrémente le nombre
     * d'opérations simples dans l'opération complexe courante. */
    public void addInfo(UndoObject objet) {
	if(currentGroup() != null)
	    currentGroup().add(objet);
    }

    /** Detruit les informations concernant les groupes crées
     * posterieurement au groupe courant. */  
    private void trimGroups() {
	removeRange(curseur() + 1, size());
    }    
  
    /** Cette méthode permet d'annuler l'opération complexe courante,
     * en annulant chacune des opérations simples qui la composent. */
    public void undo() {
	if(currentGroup() != null) {
	    currentGroup().undo();
	    curseur--;
	}
    }
  
    /** Cette méthode permet de restaurer l'opération complexe
     * courante, en restaurant chacune des opérations simples qui la
     * composent. */
    public void redo() {
	//if(currentGroup() != null) {
	try{
	    curseur++;
	    currentGroup().redo();
	    //}
	} catch (Exception e) {
	}
    }
    
    /** Retourne VRAI si il reste au moins une opération complexe
     * susceptible d'être annulée. */
    public boolean undoMore() {
	return (curseur >= 0);
    }

    /** Retourne VRAI si il reste au moins une opération complexe
     * susceptible d'être restaurée. */
    public boolean redoMore() {
	return (curseur < (size() - 1));
    }

    /* ************************************************************* */
    /* méthodes rendues nécessaires par certaines fonctionnalités    */
    /* d'autographe (ex : glissement d'un sommet...)                 */
    /* ************************************************************* */

    /** Supprime le groupe d'opérations courant. */
    public void removeEmptyGroup() {
	if(currentGroup() != null) {
	    if (currentGroup().isEmpty()) {
		remove(curseur);
		curseur = curseur - 1;
	    }
	}
    }

    /** Annule la dernière opération et supprime le groupe correspondant. */
    public void undoAndRemove() {
	undo();
	remove(curseur + 1);
    }

    /**  Retire l'UndoObject qui contient la FormeDessin passée en
     *  argument, dans le groupe courant */
    public void removeObject(FormeDessin objet) {
	int i=0;
	try {
	    while (!objet.equals(currentGroup().getInfo(i).content())) {
		i++;
	    }
	    currentGroup().remove(i);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return;
	}
    }
  

    /* ************************************************************* */
    /* méthodes 'd'agrément' : elles raffinent le système           */
    /* (undo/redo par lots, affichage des descriptions...)           */
    /* ************************************************************* */

    /** Cette méthode permet d'annuler les "i" dernières operations
     * (ou toutes les opérations si il y en a moins de "i") */
    public void undo(int i) {
	int compteur = 0;
	try{
	    while (undoMore() && compteur < i) {
		currentGroup().undo();
		curseur--;
		compteur++;
	    }
	} catch (Exception e) {
	    return;
	}
    }
  
    /** Cette méthode permet de restaurer les "i" dernières opérations
     * (ou toutes les opérations si il y en a moins de "i")*/
    public void redo(int i) {
	int compteur = 0;
	try{
	    while (redoMore() && compteur < i) {
		curseur++;
		currentGroup().redo();
		compteur++;
	    }
	} catch(Exception e) {
	    return;
	}
    }

    /** Retourne la description de ce qui sera fait lors du prochain undo*/
    public String undoDescription() {
	if (undoMore()) {
	    return (currentGroup().undoDescription());
	} else { 
	    return "Undo";
	}
    }
  
    /** Retourne la description de ce qui sera fait lors du prochain redo*/
    public String redoDescription() {
	if (redoMore()) {
	    return (groupAt(curseur() + 1).redoDescription());
	} else { 
	    return "Redo";
	}
    }
  
  
}




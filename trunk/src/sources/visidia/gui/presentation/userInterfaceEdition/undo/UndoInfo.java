package visidia.gui.presentation.userInterfaceEdition.undo;

import java.util.Vector;

import visidia.gui.presentation.FormeDessin;

/** Cette classe implémente la structure de données utilisée pour
 * gérer les opérations de undo/redo. **/

public class UndoInfo extends Vector {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -2426695505759586262L;
	/** L'index de l'operation courante. */
    protected int curseur;
  
    /** Instancie un nouvel objet UndoInfo vide.*/
    public UndoInfo() {
	super();
	this.curseur = -1;
    }
  
    /* ************************************************************* */
    /* Méthodes privées "accessoires", pour faciliter l'implémentation
       des autres méthodes.*/
    /* ************************************************************* */



    /** Accesseur au curseur. */
    private int curseur() {
	return this.curseur;
    }

    /** Retourne le groupe d'opérations courant.*/
    private UndoInfoElement currentGroup() {
	try { 
	    return ((UndoInfoElement)this.elementAt(this.curseur));
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }


    /** Retourne le groupe d'opérations à l'index "i" */
    private UndoInfoElement groupAt(int i) {
	try{
	    return ((UndoInfoElement)this.elementAt(i));
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
	if (this.redoMore()) {
	    this.trimGroups();
	}
	this.addElement(new UndoInfoElement(undo, redo));
	this.curseur++;
    }

    /** Ajoute une opération dans la liste, et incrémente le nombre
     * d'opérations simples dans l'opération complexe courante. */
    public void addInfo(UndoObject objet) {
	if(this.currentGroup() != null)
	    this.currentGroup().add(objet);
    }

    /** Detruit les informations concernant les groupes crées
     * posterieurement au groupe courant. */  
    private void trimGroups() {
	this.removeRange(this.curseur() + 1, this.size());
    }    
  
    /** Cette méthode permet d'annuler l'opération complexe courante,
     * en annulant chacune des opérations simples qui la composent. */
    public void undo() {
	if(this.currentGroup() != null) {
	    this.currentGroup().undo();
	    this.curseur--;
	}
    }
  
    /** Cette méthode permet de restaurer l'opération complexe
     * courante, en restaurant chacune des opérations simples qui la
     * composent. */
    public void redo() {
	//if(currentGroup() != null) {
	try{
	    this.curseur++;
	    this.currentGroup().redo();
	    //}
	} catch (Exception e) {
	}
    }
    
    /** Retourne VRAI si il reste au moins une opération complexe
     * susceptible d'être annulée. */
    public boolean undoMore() {
	return (this.curseur >= 0);
    }

    /** Retourne VRAI si il reste au moins une opération complexe
     * susceptible d'être restaurée. */
    public boolean redoMore() {
	return (this.curseur < (this.size() - 1));
    }

    /* ************************************************************* */
    /* méthodes rendues nécessaires par certaines fonctionnalités    */
    /* d'autographe (ex : glissement d'un sommet...)                 */
    /* ************************************************************* */

    /** Supprime le groupe d'opérations courant. */
    public void removeEmptyGroup() {
	if(this.currentGroup() != null) {
	    if (this.currentGroup().isEmpty()) {
		this.remove(this.curseur);
		this.curseur = this.curseur - 1;
	    }
	}
    }

    /** Annule la dernière opération et supprime le groupe correspondant. */
    public void undoAndRemove() {
	this.undo();
	this.remove(this.curseur + 1);
    }

    /**  Retire l'UndoObject qui contient la FormeDessin passée en
     *  argument, dans le groupe courant */
    public void removeObject(FormeDessin objet) {
	int i=0;
	try {
	    while (!objet.equals(this.currentGroup().getInfo(i).content())) {
		i++;
	    }
	    this.currentGroup().remove(i);
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
	    while (this.undoMore() && (compteur < i)) {
		this.currentGroup().undo();
		this.curseur--;
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
	    while (this.redoMore() && (compteur < i)) {
		this.curseur++;
		this.currentGroup().redo();
		compteur++;
	    }
	} catch(Exception e) {
	    return;
	}
    }

    /** Retourne la description de ce qui sera fait lors du prochain undo*/
    public String undoDescription() {
	if (this.undoMore()) {
	    return (this.currentGroup().undoDescription());
	} else { 
	    return "Undo";
	}
    }
  
    /** Retourne la description de ce qui sera fait lors du prochain redo*/
    public String redoDescription() {
	if (this.redoMore()) {
	    return (this.groupAt(this.curseur() + 1).redoDescription());
	} else { 
	    return "Redo";
	}
    }
  
  
}




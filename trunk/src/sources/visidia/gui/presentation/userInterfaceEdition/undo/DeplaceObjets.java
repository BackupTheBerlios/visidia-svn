package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;
import visidia.gui.donnees.conteneurs.*;
import java.util.*;

/** Cette classe contient les informations pour annuler les
 * déplacements d'objets */

public class DeplaceObjets implements UndoObject {
  
    protected Ensemble objets;
    protected int dx;
    protected int dy;
    
    /** Construit un nouvel objet, correspondant au déplacement (dx,
     * dy), de l'ensemble d'objets objets.*/
    public DeplaceObjets(Ensemble objets,
			 int dx,
			 int dy) {
	this.objets = objets;
	this.dx = dx;
	this.dy = dy;
    }
    
    /** Construit un nouvel objet, correspondant au déplacement (dx,
     * dy), du sommet sommet.*/
    public DeplaceObjets(SommetDessin sommet, int dx, int dy) {
	this.objets = new Ensemble();
	objets.inserer(sommet);
	this.dx = dx;
	this.dy = dy;
    }
    
    public void undo() {
	try {
	    VueGraphe.deplacerFormeDessin(objets.elements(),
					  -dx,
					  -dy);
	} catch (Exception e) {
	    return;
	}
    }
    
    public void redo() {
	try{
	    VueGraphe.deplacerFormeDessin(objets.elements(),
					  dx,
					  dy);
	} catch (Exception e) {
	    return;
	}
    }	
    
    public FormeDessin content() {
	return null;
    }
}










package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.donnees.conteneurs.*;
import java.util.*;

/** Cette classe contient les informations pour annuler les déplacements d'objets */

public class DeplaceObjets implements UndoObject {
  
  protected Ensemble objets;
  protected int dx;
  protected int dy;
  
    /** Construit un nouvel objet, correspondant au déplacement (dx, dy), de l'ensemble d'objets objets.*/
  public DeplaceObjets(Ensemble objets,
		       int dx,
		       int dy) {
    this.objets = objets;
    this.dx = dx;
    this.dy = dy;
  }

/** Construit un nouvel objet, correspondant au déplacement (dx, dy), du sommet sommet.*/
  public DeplaceObjets(SommetDessin sommet, int dx, int dy) {
    this.objets = new Ensemble();
    objets.inserer(sommet);
    this.dx = dx;
    this.dy = dy;
  }

  public void undo() {
    VueGraphe.deplacerFormeDessin(objets.elements(),
				  -dx,
				  -dy);
  }
  
  public void redo() {
    VueGraphe.deplacerFormeDessin(objets.elements(),
				  dx,
				  dy);
  }
  
    public FormeDessin content() {
	return null;
    }
}










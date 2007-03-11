package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;

/**
 * Cette classe contient les informations pour annuler les déplacements d'objets
 */

public class DeplaceObjets implements UndoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9007707846920142128L;

	protected Ensemble objets;

	protected int dx;

	protected int dy;

	/**
	 * Construit un nouvel objet, correspondant au déplacement (dx, dy), de
	 * l'ensemble d'objets objets.
	 */
	public DeplaceObjets(Ensemble objets, int dx, int dy) {
		this.objets = objets;
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Construit un nouvel objet, correspondant au déplacement (dx, dy), du
	 * sommet sommet.
	 */
	public DeplaceObjets(SommetDessin sommet, int dx, int dy) {
		this.objets = new Ensemble();
		this.objets.inserer(sommet);
		this.dx = dx;
		this.dy = dy;
	}

	public void undo() {
		try {
			VueGraphe.deplacerFormeDessin(this.objets.elements(), -this.dx,
					-this.dy);
		} catch (Exception e) {
			return;
		}
	}

	public void redo() {
		try {
			VueGraphe.deplacerFormeDessin(this.objets.elements(), this.dx,
					this.dy);
		} catch (Exception e) {
			return;
		}
	}

	public FormeDessin content() {
		return null;
	}
}

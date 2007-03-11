package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.FormeDessin;

/**
 * Cette classe permet d'annuler et de restaurer les creations d'objets dans un
 * graphe.
 */

public class AjouteObjet implements UndoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7822557371002569458L;

	protected FormeDessin maForme;

	/** L'forme cree dans le graphe est passe en argument a ce constructeur. */
	public AjouteObjet(FormeDessin forme) {
		this.maForme = forme;
	}

	public void undo() {
		this.maForme.getVueGraphe().delObject(this.maForme);
	}

	public void redo() {
		this.maForme.getVueGraphe().putObject(this.maForme);
	}

	public FormeDessin content() {
		return this.maForme;
	}
}

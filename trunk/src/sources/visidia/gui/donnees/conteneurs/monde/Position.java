package visidia.gui.donnees.conteneurs.monde;

import java.io.Serializable;

/**
 * Un objet de classe Position est en quelque sorte une référence pointant sur
 * un élément d'un monde. C'est cette classe qui encapsule l'objet situé à la
 * "position" courante dans le monde.
 */
public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3456322232125828473L;

	// Variables d'instance.
	/** Le monde auquel appartient cette position */
	protected Monde un_monde;

	/** L'objet situe dans le monde a cette position. */
	protected Object element = null;

	/** La position precedente dans le monde. */
	protected Position precedent;

	/** La position suivante dans le monde. */
	protected Position suivant;

	// Constructeurs.

	protected Position(Monde un_monde, Object element, Position precedent,
			Position suivant) {
		this.un_monde = un_monde;
		this.element = element;
		this.precedent = precedent;
		this.suivant = suivant;
	}

	protected Position(Monde un_monde) {
		this.un_monde = un_monde;
		this.precedent = this.suivant = this;
	}

	// Methodes.
	/**
	 * Ajoute une position avant l'objet "element" dans le monde.
	 */
	protected Position AjouterAvant(Object element) {
		Position nouveau = new Position(this.un_monde, element, this.precedent,
				this);
		this.precedent.suivant = nouveau;
		this.precedent = nouveau;
		return nouveau;
	}

	/**
	 * Retourne l'élément du monde correspondant à cette position.
	 */
	public Object lireElement() {
		return this.element;
	}

	/**
	 * Supprime du monde l'élément correspondant à cette position
	 */
	public void supprimerElement() {
		if (this.un_monde != null) {
			this.precedent.suivant = this.suivant;
			this.suivant.precedent = this.precedent;
			this.precedent = this.suivant = null;
			this.element = null;
			this.un_monde.taille--;
			this.un_monde = null;
		}
	}
}

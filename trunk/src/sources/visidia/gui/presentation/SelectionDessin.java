package visidia.gui.presentation;

import java.util.Enumeration;

import visidia.gui.donnees.conteneurs.Ensemble;

/**
 * Implements a selection of elements of a graph without any supposition about
 * the relations between these elements.
 */

public class SelectionDessin {
	// Variables d'instance

	/** Set of elected elements */
	protected Ensemble formes;

	// Constructor
	// Instanciation of a selection

	public SelectionDessin() {
		this.formes = new Ensemble();
	}

	// Methods

	// desenluminate the elements of the selection
	public void desenluminer() {
		FormeDessin elt;
		Enumeration e = this.formes.elements();

		while (e.hasMoreElements()) {
			elt = (FormeDessin) e.nextElement();
			elt.enluminer(false);
		}

	}

	// Deselect the elements of the selection
	public void deSelect() {

		FormeDessin elt;
		Enumeration e = this.formes.elements();

		while (e.hasMoreElements()) {
			elt = (FormeDessin) e.nextElement();
			elt.enluminer(false);
		}
		this.formes.vider();
	}

	// Select the elements of the selection
	public void select() {
		FormeDessin elt;
		Enumeration e = this.formes.elements();
		while (e.hasMoreElements()) {
			elt = (FormeDessin) e.nextElement();
			elt.enluminer(true);
		}
	}

	// Tester wether an element is in the selection
	public boolean contient(FormeDessin forme) {
		return this.formes.contient(forme);
	}

	// Modificators

	// Insert an element to the selection
	// (if this element is already in the selection, nothing happens)
	public void insererElement(FormeDessin forme) {
		this.formes.inserer(forme);
		forme.enluminer(true);
	}

	// delete an element from the selection
	public void supprimerElement(FormeDessin forme) {
		this.formes.supprimer(forme);
		forme.enluminer(false);
	}

	// Accessors
	public boolean estVide() {
		return (this.formes.estVide());
	}

	public Enumeration elements() {
		return this.formes.elements();
	}

	public int nbElements() {
		return this.formes.taille();
	}

}

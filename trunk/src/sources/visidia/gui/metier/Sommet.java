package visidia.gui.metier;

import java.util.Enumeration;

import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.donnees.conteneurs.MultiEnsemble;
import visidia.gui.donnees.conteneurs.monde.Position;
import visidia.gui.presentation.SommetDessin;

/**
 * This class implements the vertex of a graph
 */
public class Sommet extends ObjetGraphe implements Cloneable {

	/* instance variables */

	/**
	 * 
	 */
	private static final long serialVersionUID = 4199213725347995303L;

	/**
	 * Set of vertices which are predecessors of this vertex They are vertices
	 * at the other extremity of incoming edges
	 */
	protected MultiEnsemble predecesseurs;

	/**
	 * Set of vertices which are successors of this vertex They are vertices at
	 * the other extremity of outcoming edges
	 */
	protected MultiEnsemble successeurs;

	/** Set of outcoming edges */
	protected Ensemble aretes_sortantes;

	/** Set of incoming edges */
	protected Ensemble aretes_entrantes;

	// protected variables
	Position position;

	// Constructor.

	/**
	 * construct a new vertex in un_graphe
	 */
	public Sommet(Graphe un_graphe) {
		this.graph = un_graphe;

		this.position = this.graph.vertex.ajouterElement(this);
		this.predecesseurs = new MultiEnsemble();
		this.successeurs = new MultiEnsemble();
		this.aretes_sortantes = new Ensemble();
		this.aretes_entrantes = new Ensemble();
	}

	// Cloneable methods.

	/**
	 * duplicate the current vertex THe new vertex hasn't got any incident edge
	 */
	public Object cloner() {
		return this.cloner(this.graph);
	}

	public Object cloner(Graphe graphe) {
		try {
			Sommet le_clone = (Sommet) super.clone();
			le_clone.position = graphe.vertex.ajouterElement(le_clone);
			le_clone.graph = graphe;
			le_clone.predecesseurs = new MultiEnsemble();
			le_clone.successeurs = new MultiEnsemble();
			le_clone.aretes_sortantes = new Ensemble();
			le_clone.aretes_entrantes = new Ensemble();
			return le_clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	protected Object clone() {
		return this.cloner();
	}

	// GraphObject method

	/**
	 * delete the vertex and its incidents edges
	 */
	public void supprimer() {
		this.position.supprimerElement();
	}

	/**
	 * Add the vertex in its graph
	 * 
	 */
	public void ajouter() {
		this.position = this.graph.vertex.ajouterElement(this);
	}

	/**
	 * return the outcoming degree
	 */
	public int degreSortant() {
		return this.aretes_sortantes.taille();
	}

	/**
	 * return the incoming degree
	 */
	public int degreEntrant() {
		return this.aretes_entrantes.taille();
	}

	/**
	 * return the total degree
	 */
	public int degre() {
		return (this.aretes_entrantes.taille() + this.aretes_sortantes.taille());
	}

	/**
	 * determines the number of time un_sommet is successor of the current
	 * vertex
	 */
	public int aPourSuccesseur(Sommet un_sommet) {
		return this.successeurs.cardinalite(un_sommet);
	}

	/**
	 * determines the number of time un_sommet is predecessor of the current
	 * vertex
	 */
	public int aPourPredecesseur(Sommet un_sommet) {
		return this.predecesseurs.cardinalite(un_sommet);
	}

	/**
	 * determines the number of time un_sommet is neighbor of the current vertex
	 */
	public int aPourVoisin(Sommet un_sommet) {
		return (this.successeurs.cardinalite(un_sommet) + this.predecesseurs
				.cardinalite(un_sommet));
	}

	/**
	 * returns an enumeration of the successors
	 */
	public Enumeration successeurs() {
		return this.successeurs.elements();
	}

	/**
	 * returns an enumeration of the predecessors
	 */
	public Enumeration predecesseurs() {
		return this.predecesseurs.elements();
	}

	/**
	 * returns an enumeration of the neighbors.
	 */
	public Enumeration sommetsVoisins() {
		// notes : we work here with Ensemble
		Ensemble e = this.predecesseurs.ensemble();

		// The union permit to delete duplicates
		e.union(this.successeurs);
		return e.elements();
	}

	/**
	 * returns an enumeration of outcoming edges
	 */
	public Enumeration aretesSortantes() {
		return this.aretes_sortantes.elements();
	}

	/**
	 * returns an enumeration of incoming edges
	 */
	public Enumeration aretesEntrantes() {
		return this.aretes_entrantes.elements();
	}

	/**
	 * returns an enumeration of incident edges
	 */
	public Enumeration aretesIncidentes() {
		Ensemble e = (Ensemble) this.aretes_entrantes.clone();

		// The union permit to delete duplicates
		e.union(this.aretes_sortantes);
		return e.elements();
	}

	/**
	 * Make the fusion of the vertex 'un_sommet' with the current vertex The
	 * vertex un_sommet is destroyed, but the current vertex gets its edges
	 */
	public void fusionner(Sommet un_sommet) {

		Enumeration e = un_sommet.aretesSortantes();
		while (e.hasMoreElements())
			((Arete) e.nextElement()).changerOrigine(this);

		e = un_sommet.aretesEntrantes();
		while (e.hasMoreElements())
			((Arete) e.nextElement()).changerDestination(this);
		un_sommet.supprimer();

	}

	// recalculate position of all incident edges

	public void repositionnerAretes() {
		Enumeration e = this.aretesIncidentes();
		while (e.hasMoreElements()) {
			Arete a_reposition = (Arete) e.nextElement();
			a_reposition.getAreteDessin().repositionner(
					a_reposition.origine().getSommetDessin(),
					a_reposition.destination().getSommetDessin());
		}
	}

	// accessor and modificator to the associated sommetDessin
	public void setSommetDessin(SommetDessin a) {
		this.formeDessin = a;
	}

	public SommetDessin getSommetDessin() {
		return ((SommetDessin) this.formeDessin);
	}
}

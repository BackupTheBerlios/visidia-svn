package visidia.gui.donnees.conteneurs;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Cette classe implémente un ensemble d'objets, pouvant être présents plusieurs
 * fois dans l'ensemble.. Un compteur mémorise le nombre de fois qu'un même
 * élément a été inséré. Les méthodes "equals" et "hashCode" des objets sont
 * utilisées.
 */
public class MultiEnsemble extends Ensemble {

	// Constructeurs.

	/**
	 * 
	 */
	private static final long serialVersionUID = 6429889527647953311L;

	/**
	 * Crée un MultiEnsemble vide.
	 */
	public MultiEnsemble() {
		super();
	}

	/**
	 * Crée un MultiEnsemble avec une table de hachage donnée. Utilisé par
	 * clone.
	 */
	protected MultiEnsemble(Hashtable table) {
		super(table);
	}

	// Méthodes.

	/**
	 * Insère un objet dans le MultiEnsemble. Si l'objet est déjà présent, son
	 * compteur est incrémenté. "un_objet" ne doit pas être "null".
	 */
	public void inserer(Object un_objet) {
		Object c = this.table.get(un_objet);
		if (c == null) {
			this.table.put(un_objet, new MECompteur());
		} else {
			((MECompteur) c).i++;
		}
	}

	/**
	 * Insère un objet dans le MultiEnsemble avec une cardinalité donnée. Si
	 * l'objet est déjà présent, son compteur est incrémenté de la cardinalité.
	 * "un_objet" ne doit pas être "null".
	 */
	public void inserer(Object un_objet, int cardinalite) {
		// assert(cardinalite > 0)
		Object c = this.table.get(un_objet);
		if (c == null) {
			this.table.put(un_objet, new MECompteur(cardinalite));
		} else {
			((MECompteur) c).i += cardinalite;
		}
	}

	/**
	 * Supprime un objet de l'ensemble. Si l'objet n'est pas présent, rien ne se
	 * passe. Si l'objet a été insére plusieurs fois, son compteur est
	 * décrémenté.
	 */
	public void supprimer(Object un_objet) {
		MECompteur c = (MECompteur) this.table.get(un_objet);
		if ((c != null) && (c.i != 1)) {
			c.i--;
		} else {
			this.table.remove(un_objet);
		}
	}

	/**
	 * Retourne la cardinalité d'un élément de l'ensemble, ie le nombre de fois
	 * qu'il est présent dans l'ensemble.
	 */
	public int cardinalite(Object un_objet) {
		Object c = this.table.get(un_objet);
		if (c == null) {
			return 0;
		} else {
			return ((MECompteur) c).i;
		}
	}

	/**
	 * Fait une copie indépendante. Ne clone pas les éléments. C'est un
	 * traitement assez couteux.
	 */
	public Object clone() {
		MultiEnsemble nouveau = new MultiEnsemble((Hashtable) this.table
				.clone());
		for (Enumeration e = this.table.keys(); e.hasMoreElements();) {
			Object cle = e.nextElement();
			this.table.put(cle, ((MECompteur) this.table.get(cle)).clone());
		}
		return nouveau;
	}

	/**
	 * Cree un ensemble contenant les éléments du MultiEnsemble courant.
	 */
	public Ensemble ensemble() {
		return new Ensemble((Hashtable) this.table.clone());
	}

	/**
	 * Ajoute tous les éléments de un_multiensemble au MultiEnsemble courant.
	 */
	public void union(MultiEnsemble un_multiensemble) {
		Enumeration e = un_multiensemble.table.keys();

		while (e.hasMoreElements()) {
			Object objet_courant = e.nextElement();
			this.inserer(objet_courant, ((MECompteur) un_multiensemble.table
					.get(objet_courant)).i);
		}
	}
}

// Classe privee

class MECompteur implements Cloneable, Serializable {

	// Variable d'instance.

	/**
	 * 
	 */
	private static final long serialVersionUID = -685397766371649858L;

	int i;

	// Constructeurs.

	protected MECompteur() {
		this.i = 1;
	}

	protected MECompteur(int i) {
		this.i = i;
	}

	// Méthodes.

	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return new MECompteur(this.i);
		}
	}
}

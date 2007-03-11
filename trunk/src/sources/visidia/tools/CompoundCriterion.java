package visidia.tools;

/**
 * cette classe définit un critère qui est la conjonctions d'une liste de critères.
 * Lorsque la liste  est vide la méthode <code>isMatchedBy()</code> retourne <code>false</code>.
 */
import java.util.Iterator;
import java.util.LinkedList;

public class CompoundCriterion implements Criterion {
	private LinkedList<Criterion> criterionList = null;

	/**
	 * Contruit une nouvelle classe de composition de critères. A la creation
	 * elle ne contient aucun critère.
	 */
	public CompoundCriterion() {
		this.criterionList = new LinkedList<Criterion>();
	}

	/**
	 * ajoute un critère a la liste de critères.
	 */
	public void add(Criterion c) {
		this.criterionList.add(c);
	}

	/**
	 * supprime un critère de la liste des critères.
	 */
	public boolean remove(Criterion c) {
		return this.criterionList.remove(c);
	}

	/**
	 * supprime tous les critères.
	 */
	public void removeAllCriterion() {
		this.criterionList = new LinkedList<Criterion>();
	}

	public boolean isMatchedBy(Object o) {
		if (this.criterionList.isEmpty()) {
			return false;
		}

		Iterator iterator = this.criterionList.iterator();
		while (iterator.hasNext()) {
			Criterion c = (Criterion) iterator.next();
			if (!c.isMatchedBy(o)) {
				return false;
			}
		}

		return true;
	}

}

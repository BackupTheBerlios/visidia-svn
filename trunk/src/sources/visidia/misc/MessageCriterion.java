package visidia.misc;

import visidia.tools.Criterion;

/**
 * classe représentant un critère de sélection portant sur un objet de type
 * <code>Message</code>.
 */
public class MessageCriterion implements Criterion {

	/**
	 * méthode de test de l'objet.
	 */
	public boolean isMatchedBy(Object o) {
		return o instanceof Message;
	}
}

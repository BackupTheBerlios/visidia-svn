package visidia.misc;

/**
 * permet de choisir un objet selon sa classe.
 */
public class IntegerMessageCriterion extends MessageCriterion {
	public boolean isMatchedBy(Object o) {
		return o instanceof IntegerMessage;
	}
}

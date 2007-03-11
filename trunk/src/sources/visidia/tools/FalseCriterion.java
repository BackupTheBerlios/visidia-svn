package visidia.tools;

/**
 * A criterion that does not match any object.
 */
public class FalseCriterion implements Criterion {
	public boolean isMatchedBy(Object o) {
		return false;
	}
}

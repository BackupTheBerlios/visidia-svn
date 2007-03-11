package visidia.tools;

/**
 * A criterion that match all objects.
 */
public class TrueCriterion implements Criterion {
	public boolean isMatchedBy(Object o) {
		return true;
	}
}

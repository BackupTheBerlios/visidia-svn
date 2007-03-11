package visidia.visidiassert;

public class VisidiaAssertion {
	public static void verify(boolean condition, String assertion,
			Object uneInstance) {
		if (debogage) {
			if (condition)
				return;
			else
				throw new AssertionException("Assertion -> " + assertion
						+ " in Class " + uneInstance.getClass().getName());
		} else
			return;
	}

	private static final boolean debogage = true;
}

class AssertionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4526896685514647211L;

	AssertionException() {
		super("Assertion Failed");
	}

	AssertionException(String message) {
		super(message);
	}
}

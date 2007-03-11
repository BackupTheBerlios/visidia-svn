/**
 * Use  me when  your class  implements a  method that  should  not be
 * called in every cases.
 */
package visidia.misc;

public class ForbiddenCallException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3245989347901387131L;

	public ForbiddenCallException() {
		super();
	}

	public ForbiddenCallException(String message) {
		super(message);
	}

	public ForbiddenCallException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForbiddenCallException(Throwable cause) {
		super(cause);
	}
}

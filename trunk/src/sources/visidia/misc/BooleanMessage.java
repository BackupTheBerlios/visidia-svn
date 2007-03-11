package visidia.misc;

/**
 * Message contenant un boolean.
 */
public class BooleanMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493274078199394719L;

	boolean data;

	public BooleanMessage(boolean value) {
		this.data = value;
	}

	public BooleanMessage(boolean data, MessageType type) {
		this.data = data;
		this.setType(type);
	}

	public boolean value() {
		return this.data;
	}

	public boolean data() {
		return this.data;
	}

	/**
	 * the reurned object is a new Boolean initialized with the value of data
	 * 
	 */

	public Object getData() {
		return new Boolean(this.data);
	}

	public Object clone() {
		return new BooleanMessage(this.data, this.getType());
	}

	public String toString() {
		return (new Boolean(this.data)).toString();
	}

}

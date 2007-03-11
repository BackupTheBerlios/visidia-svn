package visidia.misc;

/**
 * Message contenant une chaine de caractere.
 */
public class ArrowMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7063097464935592651L;

	Arrow data;

	public ArrowMessage(Arrow data) {
		this.data = data;
	}

	public ArrowMessage(Arrow data, MessageType type) {
		this.setType(type);
		this.data = data;
	}

	public Arrow data() {
		return this.data;
	}

	/**
	 * the returned message is a new Arrow initialized with the data value
	 * 
	 */
	public Object getData() {
		return this.data;
	}

	public Object clone() {
		return new ArrowMessage(this.data, this.getType());
	}

	public String toString() {
		if (this.data.isMarked) {
			return this.data.left + " X " + this.data.right;
		} else {
			return this.data.left + " - " + this.data.right;
		}
	}

}

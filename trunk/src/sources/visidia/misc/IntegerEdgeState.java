package visidia.misc;

/**
 * 
 */
public class IntegerEdgeState extends EdgeState {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2403841400262546203L;

	private int val;

	public IntegerEdgeState(int value) {
		this.val = value;
	}

	public int value() {
		return this.val;
	}

	/**
	 * crée une copy de cet objet.
	 */
	public Object clone() {
		return new IntegerEdgeState(this.val);
	}
}

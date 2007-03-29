package visidia.simulation.agents;

public class MoveException extends Exception {

	static final long serialVersionUID = 456446;
	
	public static final int NoDoorFound = 1;
	public static final int SwitchedOffVertex = 2;
	
	private int type = 0;
	
	public MoveException(int t) {
		this.type = t;
	}
	
	public int getMouvementTypeException() {
		return this.type;
	}
}

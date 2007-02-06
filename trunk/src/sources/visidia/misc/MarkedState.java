package visidia.misc;



public class MarkedState extends EdgeState {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5161335517354611050L;
	boolean isMarked;

    public MarkedState(boolean b){
	this.isMarked = b;
    }

    public boolean isMarked(){
	return this.isMarked;
    }
    public Object clone(){
	return new MarkedState(this.isMarked);
    }

}

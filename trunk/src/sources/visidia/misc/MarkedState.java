package visidia.misc;



public class MarkedState extends EdgeState {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5161335517354611050L;
	boolean isMarked;

    public MarkedState(boolean b){
	isMarked = b;
    }

    public boolean isMarked(){
	return isMarked;
    }
    public Object clone(){
	return new MarkedState(isMarked);
    }

}

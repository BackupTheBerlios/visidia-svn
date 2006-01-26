package fr.enserb.das.misc;



public class MarkedState extends EdgeState {

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

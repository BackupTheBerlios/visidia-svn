package visidia.misc;



public class SyncState extends EdgeState {

    boolean isSynchro;

    public SyncState(boolean b){
	isSynchro = b;
    }

    public boolean isSynchronized(){
	return isSynchro;
    }

    public Object clone(){
	return new SyncState(isSynchro);
    }
}

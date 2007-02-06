package visidia.misc;



public class SyncState extends EdgeState {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4314755802953155996L;
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

package visidia.simulation.synchro.synAlgos;

import visidia.simulation.synchro.synObj.*;

import visidia.rule.*;

import visidia.misc.*;
import visidia.simulation.*;
import java.util.LinkedList;
import java.util.Collection;
/** all synchronisation algorithms should extend this class
    *
    */

public abstract class AbSynAlgo extends Algorithm implements IntSynchronization
{
  
    protected int answer[];
        
    public AbSynAlgo(){
	super();
    }
  

    public Collection getListTypes(){
	Collection typesList = new LinkedList();
        typesList.add(MSG_TYPES.SYNC);
	typesList.add(MSG_TYPES.TERM);
	return typesList;
    }
	    
    public void setNeighbourhood(Star neighbourhood)
    {
	((SynObjectRules)this.synob).setNeighbourhood(neighbourhood);
    }
    abstract  public Object clone();
    
    
    public String toString(){
	return "Abstract Synchro";
    }
    public void init(){
    }
/**
 *  breaks synchronisation.
 * in fact it informs the gui.
 */
    public void breakSynchro() {
	for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new SyncState(false),door);
        }
    }
    
}    







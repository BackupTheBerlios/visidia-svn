package visidia.simulation.synchro.synAlgosDist;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synObj.*;
import visidia.simulation.synchro.synAlgosDist.*;

import visidia.simulation.rulesDist.*;//
import visidia.network.NodeTry;
import visidia.rule.*;

import visidia.misc.*;
import visidia.simulation.*;
import java.util.Random;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Collection;


public abstract class AbSynAlgoDist extends AlgorithmDist implements IntSynchronization
{
    protected int answer[];
       
    public AbSynAlgoDist(){
	super();
	addMessageType(MSG_TYPES.SYNC);
	addMessageType(MSG_TYPES.TERM);
	
    }
            
    public void setNeighbourhood(Star neighbourhood)     {
	((SynObjectRules) synob).setNeighbourhood(neighbourhood);
    }

    abstract public Object clone();
      
    public void init() {
    }
    public void breakSynchro() {
	for( int door = 0; door < getArity(); door++){
            setDoorState(new SyncState(false),door);
        }
    }

    public abstract void trySynchronize();
}
	
    	
    
    






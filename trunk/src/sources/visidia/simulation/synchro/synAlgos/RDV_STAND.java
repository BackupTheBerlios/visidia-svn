package visidia.simulation.synchro.synAlgos;

import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synObj.*;
import visidia.simulation.synchro.synAlgos.*;


import visidia.simulation.rules.*;
import visidia.rule.*;


import visidia.misc.*;
import visidia.simulation.*;
import java.util.Random;
import java.util.*;

/** RDV standar: duel is not done ins trySynchronize 
 */
public class RDV_STAND extends AbSynAlgo implements IntSynchronization {
    public RDV_STAND() {
	super();
    }

    public Object clone() {
	return new RDV_STAND();
    }
    public String toString(){
	return "RDV";
    }
    
    /* a duel , useful for RDV family algorithms */
    public boolean duelWith(int neighbour){
	int my = 0;
	int his = 0;
	while (synob.isConnected(neighbour) && my == his) {
	    my = Math.abs(SynchronizedRandom.nextInt());
	    boolean b = sendTo(neighbour, new IntegerMessage(my, MSG_TYPES.DUEL));
	    synob.setConnected(neighbour, b);
	    if (synob.isConnected(neighbour)) {
		IntegerMessage msg = (IntegerMessage) receiveFrom(neighbour);
		if (msg != null) {
		    his = msg.value();
		    if (my != his) {
			return my > his;
		    }
		} else {
		    synob.setConnected(neighbour, false);
		    return false;
		}
	    }
	}
        return false;
    }
    	
    public void trySynchronize() {
	/* Synchronisation Algorithme */
	int arity = getArity();
        answer = new int[arity];
	
	waitWhileDisconnected();
	
	synob.reset();//setNeighbourhood(s);//reset
	
	/*choice of the neighbour*/
	int door = getRandomConnectedDoor();
	
	boolean b = sendTo(door, new IntegerMessage(1, MSG_TYPES.SYNC));
	synob.setConnected(door, b);
        for (int i = 0; i < arity; i++){
            if (i != door) {
                b = sendTo(i, new IntegerMessage(0,MSG_TYPES.SYNC));
		synob.setConnected(i, b);
	    }
	}
        
        for (int i = 0; i < arity; i++) {
	    if (synob.isConnected(i)) {
		IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		if (msg != null) {
		    answer[i]= msg.value();
		    if(msg.getType().equals(MSG_TYPES.TERM)){
			if (answer[i]== SynCT.LOCAL_END)
			    synob.setFinished(i,true);
			if(answer[i] == SynCT.GLOBAL_END){
			    synob.setGlobEnd(true);
			    synob.setFinished(i,true);
			}
		    }
		} else {
		    synob.setConnected(i, false);
		}
	    }
	}
	
	if (synob.isConnected(door) && answer[door] == 1){
	    setDoorState(new SyncState(true),door);
	    synob.addSynchronizedDoor(door);
	    return;
	}
    }
    
    public void reconnectionEvent(int door) {
	Message m;
	while ((m = receiveFrom(door)) != null) {
	}
    }
} 
      

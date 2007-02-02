package visidia.simulation.synchro.synAlgos;

import visidia.simulation.synchro.SynCT;
//import visidia.simulation.synchro.synObj.*;
//import visidia.simulation.synchro.synAlgos.*;


//import visidia.simulation.rules.*;
//import visidia.rule.*;


import visidia.misc.*;
//import visidia.simulation.*;
//import java.util.Random;
//import java.util.Vector;

public class LC2 extends AbSynAlgo implements IntSynchronization
{
    public LC2(){
	super();
    }

    public Object clone() {
	return new LC2();
    }
    public String toString(){
	return "LC2";
    }
    /**
     * the algorithm of synchronizations. modifications are ported in synob.
     * warning: the synob of the algorithm (using this one) is also modified.
     * in fact it is the same!
     */
    public void trySynchronize() {
	/* Synchronisation Algorithme */
	
	//waitWhileDisconnected();
	int arity = getArity();
        answer = new int[arity];
	synob.reset();
	
	/*random */
	int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	/*Send to all neighbours */
	for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i)) {
		boolean b = 
		    sendTo(i, new IntegerMessage (choosenNumber, MSG_TYPES.SYNC));
		synob.setConnected(i, b);
	    }
	}
	
	/*receive all numbers from neighbours */
	for (int i = 0; i < arity; i++){
	    if (! synob.hasFinished(i) && synob.isConnected(i)){
		IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		if (msg != null) {
		    answer[i]= msg.value();
		    if(msg.getType().equals(MSG_TYPES.TERM)){
			if (answer[i]== SynCT.LOCAL_END)
			    synob.setFinished(i,true);
			if (answer[i] == SynCT.GLOBAL_END){
			    synob.setGlobEnd(true);
			    synob.setFinished(i,true);
			}
		    }
		} else { // The message is null
		    synob.setConnected(i, false);
		}
	    }
	}
	
	// Derniere chance de recuperer la synchro	
	for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && ! synob.isConnected(i)) {
		boolean b = 
		    sendTo(i, new IntegerMessage (choosenNumber, MSG_TYPES.SYNC));
		synob.setConnected(i, b);
		if (b) {
		    IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		    if (msg != null) {
			answer[i]= msg.value();
			if(msg.getType().equals(MSG_TYPES.TERM)){
			    if (answer[i]== SynCT.LOCAL_END)
				synob.setFinished(i,true);
			    if (answer[i] == SynCT.GLOBAL_END){
				synob.setGlobEnd(true);
				synob.setFinished(i,true);
			    }
			}
		    } else {	// The message is null
			synob.setConnected(i, false);
		    }
		    
		}
	    }
	}
	
	/*get the max */
        int max = choosenNumber;
        for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
		if (answer[i] >= max)
		    max = answer[i];
	    }
	}

	for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
		boolean b = sendTo(i, new IntegerMessage(max, MSG_TYPES.SYNC));
		synob.setConnected(i, b);
	    }
	}
	/*get alla answers from neighbours */
        for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
		IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		if (msg != null) {
		    answer[i] = msg.value();
		} else {
		    synob.setConnected(i, false);
		}
	    }
	}
	
	/*get the max */
        max = choosenNumber;
        for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
		if (answer[i] >= max)
		    max = answer[i];
	    }
	}

	/* if elected */
        if (choosenNumber >= max) {
            for (int door = 0; door < arity; door++) {
		if (! synob.hasFinished(door) && synob.isConnected(door)) {
		    setDoorState(new SyncState(true), door);
		    synob.addSynchronizedDoor(door);
		}
	    }
	    for (int i = 0; i < arity; i++) {
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    boolean b = sendTo(i, new IntegerMessage(1, MSG_TYPES.SYNC));
		    synob.setConnected(i, b);
		}
	    }

	    for (int i = 0; i < arity; i++) {
		if (! synob.hasFinished(i) && synob.isConnected(i)){
		    IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		    if (msg == null)
			synob.setConnected(i, false);
		}
	    }
	    
	    synob.setState(SynCT.IAM_THE_CENTER);
	    return;
        }
	/* not elected */
        else {
	    synob.setState(SynCT.NOT_IN_THE_STAR);
	    for (int i = 0; i < arity; i++){
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    boolean b = sendTo(i, new IntegerMessage(0, MSG_TYPES.SYNC));
		    synob.setConnected(i, b);
		}
	    }

            for (int i = 0; i < arity; i++) {
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    IntegerMessage msg= (IntegerMessage) receiveFrom(i);
		    if (msg != null) {
			int value = msg.value();
			if (value == 1) {
			    synob.center = i;
			    synob.setState(SynCT.IN_THE_STAR);
			}
		    } else {
			synob.setConnected(i, false);
		    }
		}
	    }
	}
    }	

    public void reconnectionEvent(int door) {
	Message m;
	while ((m = receiveFrom(door)) != null) {
	}
    }
}

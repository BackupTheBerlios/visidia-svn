package visidia.simulation.synchro.synAlgos;

import visidia.misc.IntegerMessage;
import visidia.misc.MSG_TYPES;
import visidia.misc.Message;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.synchro.SynCT;

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
	int arity = this.getArity();
        this.answer = new int[arity];
	this.synob.reset();
	
	/*random */
	int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	/*Send to all neighbours */
	for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i)) {
		boolean b = 
		    this.sendTo(i, new IntegerMessage (choosenNumber, MSG_TYPES.SYNC));
		this.synob.setConnected(i, b);
	    }
	}
	
	/*receive all numbers from neighbours */
	for (int i = 0; i < arity; i++){
	    if (! this.synob.hasFinished(i) && this.synob.isConnected(i)){
		IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		if (msg != null) {
		    this.answer[i]= msg.value();
		    if(msg.getType().equals(MSG_TYPES.TERM)){
			if (this.answer[i]== SynCT.LOCAL_END)
			    this.synob.setFinished(i,true);
			if (this.answer[i] == SynCT.GLOBAL_END){
			    this.synob.setGlobEnd(true);
			    this.synob.setFinished(i,true);
			}
		    }
		} else { // The message is null
		    this.synob.setConnected(i, false);
		}
	    }
	}
	
	// Derniere chance de recuperer la synchro	
	for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i) && ! this.synob.isConnected(i)) {
		boolean b = 
		    this.sendTo(i, new IntegerMessage (choosenNumber, MSG_TYPES.SYNC));
		this.synob.setConnected(i, b);
		if (b) {
		    IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		    if (msg != null) {
			this.answer[i]= msg.value();
			if(msg.getType().equals(MSG_TYPES.TERM)){
			    if (this.answer[i]== SynCT.LOCAL_END)
				this.synob.setFinished(i,true);
			    if (this.answer[i] == SynCT.GLOBAL_END){
				this.synob.setGlobEnd(true);
				this.synob.setFinished(i,true);
			    }
			}
		    } else {	// The message is null
			this.synob.setConnected(i, false);
		    }
		    
		}
	    }
	}
	
	/*get the max */
        int max = choosenNumber;
        for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		if (this.answer[i] >= max)
		    max = this.answer[i];
	    }
	}

	for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		boolean b = this.sendTo(i, new IntegerMessage(max, MSG_TYPES.SYNC));
		this.synob.setConnected(i, b);
	    }
	}
	/*get alla answers from neighbours */
        for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		if (msg != null) {
		    this.answer[i] = msg.value();
		} else {
		    this.synob.setConnected(i, false);
		}
	    }
	}
	
	/*get the max */
        max = choosenNumber;
        for (int i = 0; i < arity; i++) {
	    if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		if (this.answer[i] >= max)
		    max = this.answer[i];
	    }
	}

	/* if elected */
        if (choosenNumber >= max) {
            for (int door = 0; door < arity; door++) {
		if (! this.synob.hasFinished(door) && this.synob.isConnected(door)) {
		    this.setDoorState(new SyncState(true), door);
		    this.synob.addSynchronizedDoor(door);
		}
	    }
	    for (int i = 0; i < arity; i++) {
		if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		    boolean b = this.sendTo(i, new IntegerMessage(1, MSG_TYPES.SYNC));
		    this.synob.setConnected(i, b);
		}
	    }

	    for (int i = 0; i < arity; i++) {
		if (! this.synob.hasFinished(i) && this.synob.isConnected(i)){
		    IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		    if (msg == null)
			this.synob.setConnected(i, false);
		}
	    }
	    
	    this.synob.setState(SynCT.IAM_THE_CENTER);
	    return;
        }
	/* not elected */
        else {
	    this.synob.setState(SynCT.NOT_IN_THE_STAR);
	    for (int i = 0; i < arity; i++){
		if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		    boolean b = this.sendTo(i, new IntegerMessage(0, MSG_TYPES.SYNC));
		    this.synob.setConnected(i, b);
		}
	    }

            for (int i = 0; i < arity; i++) {
		if (! this.synob.hasFinished(i) && this.synob.isConnected(i)) {
		    IntegerMessage msg= (IntegerMessage) this.receiveFrom(i);
		    if (msg != null) {
			int value = msg.value();
			if (value == 1) {
			    this.synob.center = i;
			    this.synob.setState(SynCT.IN_THE_STAR);
			}
		    } else {
			this.synob.setConnected(i, false);
		    }
		}
	    }
	}
    }	

    public void reconnectionEvent(int door) {
	Message m;
	while ((m = this.receiveFrom(door)) != null) {
	}
    }
}

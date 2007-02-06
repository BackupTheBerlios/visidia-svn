package visidia.simulation.synchro.synAlgos;

import visidia.simulation.synchro.SynCT;
import visidia.misc.*;


public class RDV extends AbSynAlgo implements IntSynchronization {
    public RDV() {
	super();
    }

    public Object clone() {
	return new RDV();
    }
    public String toString(){
	return "RDV";
    }
    
    /* a duel , useful for RDV family algorithms */
    public boolean duelWith(int neighbour){
	int my = 0;
	int his = 0;
	while (this.synob.isConnected(neighbour) && (my == his)) {
	    my = Math.abs(SynchronizedRandom.nextInt());
	    boolean b = this.sendTo(neighbour, new IntegerMessage(my, MSG_TYPES.DUEL));
	    this.synob.setConnected(neighbour, b);
	    if (this.synob.isConnected(neighbour)) {
		IntegerMessage msg = (IntegerMessage) this.receiveFrom(neighbour);
		if (msg != null) {
		    his = msg.value();
		    if (my != his) {
			return my > his;
		    }
		} else {
		    this.synob.setConnected(neighbour, false);
		    return false;
		}
	    }
	}
        return false;
    }
    	
    public void trySynchronize() {
	/* Synchronisation Algorithme */
	int arity = this.getArity();
        this.answer = new int[arity];
	
	//waitWhileDisconnected();
	
	this.synob.reset();//setNeighbourhood(s);//reset
	
	/*choice of the neighbour*/
	int door = this.getRandomConnectedDoor();
	
	boolean b = this.sendTo(door, new IntegerMessage(1, MSG_TYPES.SYNC));
	this.synob.setConnected(door, b);
        for (int i = 0; i < arity; i++){
            if (i != door) {
                b = this.sendTo(i, new IntegerMessage(0,MSG_TYPES.SYNC));
		this.synob.setConnected(i, b);
	    }
	}
        
        for (int i = 0; i < arity; i++) {
	    if (this.synob.isConnected(i)) {
		IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		if (msg != null) {
		    this.answer[i]= msg.value();
		    if(msg.getType().equals(MSG_TYPES.TERM)){
			if (this.answer[i]== SynCT.LOCAL_END)
			    this.synob.setFinished(i,true);
			if(this.answer[i] == SynCT.GLOBAL_END){
			    this.synob.setGlobEnd(true);
			    this.synob.setFinished(i,true);
			}
		    }
		} else {
		    this.synob.setConnected(i, false);
		}
	    }
	}
	
	//PFA2003 derniere chance
	for (int i = 0; i < arity; i++) {
	    if (! this.synob.isConnected(i)) {
		b = this.sendTo(i, new IntegerMessage(0,MSG_TYPES.SYNC));
		this.synob.setConnected(i, b);
		if (b) {
		    IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
		    if (msg != null) {
			this.answer[i]= msg.value();
		    } else {
			this.synob.setConnected(i, false);
		    }
		}
	    }
	}

	if (this.synob.isConnected(door) && (this.answer[door] == 1)){
	    if(this.duelWith(door)) {
		this.setDoorState(new SyncState(true),door);
		this.synob.addSynchronizedDoor(door);
		this.synob.setState(SynCT.IAM_THE_CENTER);    
		return;
	    } else {
		this.synob.center = door;
		this.synob.setState(SynCT.IN_THE_STAR);
		return;
	    }
	} else
	    this.synob.setState(SynCT.NOT_IN_THE_STAR);
	//System.out.println ("----" + getId() + " 6");
	return;
    }
    
    public void reconnectionEvent(int door) {
	Message m;
	while ((m = this.receiveFrom(door)) != null) {
	}
    }
} 
      

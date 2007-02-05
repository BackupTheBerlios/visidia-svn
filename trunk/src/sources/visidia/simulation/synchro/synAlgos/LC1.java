package visidia.simulation.synchro.synAlgos;

import visidia.simulation.synchro.SynCT;

import visidia.misc.*;

public class LC1 extends  AbSynAlgo implements IntSynchronization
{
   
    public LC1(){
	super();
    }
  
    public Object clone()
    {
	return new LC1();
    }
    public String toString(){
	return "LC1";
    }
   
    public void trySynchronize(){
	int arity = getArity() ;
        int[] answer = new int[arity] ;

	//waitWhileDisconnected();
        synob.reset();
	/*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	
	/*Send to all neighbours */
	for (int i=0;i<arity;i++) {
	    if (! synob.hasFinished(i)) {
		boolean b = sendTo(i, new IntegerMessage(choosenNumber, MSG_TYPES.SYNC));
		synob.setConnected(i, b);
	    }
	}
       	/*receive all numbers from neighbours */
	for( int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
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

	//derniere chance
	for (int i = 0; i < arity; i++) {
	    if (! synob.hasFinished(i) && ! synob.isConnected(i)) {
		boolean b = sendTo(i, new IntegerMessage(choosenNumber, MSG_TYPES.SYNC));
		synob.setConnected(i, b);
		if (b) {
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
	}

	/*get the max */
        int max = choosenNumber;
        for( int i=0;i < arity ; i++){
	    if (! synob.hasFinished(i) && synob.isConnected(i)) {
		if( answer[i] >= max )
		    max = answer[i];
	    }
	}

	/* if elected */
        if (choosenNumber >= max) {
            for( int door = 0; door < arity; door++){
		if (! synob.hasFinished(door)){
		    setDoorState(new SyncState(true),door);
		    synob.addSynchronizedDoor(door);
		}
	    }
	    for( int i = 0; i < arity; i++){
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    boolean b = sendTo(i, new IntegerMessage(1, MSG_TYPES.SYNC));
		    synob.setConnected(i, b);
		}
	    }
	    
	    for (int i = 0; i < arity; i++) {
		if (! synob.hasFinished(i)){
		    Message msg = receiveFrom(i);
		}
	    }
	    synob.setState(SynCT.IAM_THE_CENTER);
	    return;
        }
	// not elected
        else {
	    synob.setState(SynCT.NOT_IN_THE_STAR);
	    for( int i = 0; i < arity; i++){
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    boolean b = sendTo(i, new IntegerMessage(0, MSG_TYPES.SYNC));
		    synob.setConnected(i, b);
		}
	    }
            for (int i = 0; i < arity; i++) {
		if (! synob.hasFinished(i) && synob.isConnected(i)) {
		    IntegerMessage msg = (IntegerMessage) receiveFrom(i);
		    if (msg == null) {
			synob.setConnected(i, false);
		    } else {
			int value = msg.value();
			if (value == 1) {
			    synob.addCenter(i);
			    synob.setState(SynCT.IN_THE_STAR);
			}
		    }
		}
	    }
	    return;
	}
    }

    public void reconnectionEvent(int door) {
	Message m;
	while ((m = receiveFrom(door)) != null) {
	}
    }
} 

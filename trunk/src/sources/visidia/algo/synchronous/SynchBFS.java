package visidia.algo.synchronous;


import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;


public class SynchBFS extends SyncAlgorithm {
    
    static MessageType wave = new MessageType("wave", true, new java.awt.Color(50,100,150));
    

    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(wave);
	
        return typesList;
    }
    
    public void init() {

	boolean run = true;

	int degres = getArity();
	Integer id = getId();
	
	nextPulse();
	
	while(run) {
	    if(id == 1) {
		putProperty("label", new String("R"));
		sendAll(new StringMessage("WAVE",wave));
		run = false;
		nextPulse();
	    } else {
		if(anyMsg()) {
		    Door door = new Door();
		    StringMessage msg = (StringMessage)receive(door);
		    // je marque mon père
		    setDoorState(new MarkedState(true),door.getNum());
		    putProperty("label", new String("L"));
		    // j'envoi à tout le monde
		    sendAll(new StringMessage("WAVE",wave));
		    run = false;
		}
		nextPulse();
	    }
	}
    }

    public Object clone() {
	return new SynchBFS();
    }

}

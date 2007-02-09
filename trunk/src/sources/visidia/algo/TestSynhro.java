package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.MessageType;
import visidia.simulation.SyncAlgorithm;


public class TestSynhro extends SyncAlgorithm {
    
    static MessageType round1 = new MessageType("round-1", true, new java.awt.Color(50,100,150));
    static MessageType round2Mis = new MessageType("round-2-MIS", true, new java.awt.Color(10,56,62));
    static MessageType round2NotMis = new MessageType("round-2-NOT-MIS", true, new java.awt.Color(43,156,21));
    
    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(round1);
        typesList.add(round2Mis);
        typesList.add(round2NotMis);
        return typesList;
    }
    
    public void init() {

	//int degres = getArity();
	
	Integer id = this.getId();
	//boolean win = true;
	
	this.nextPulse();
	
	boolean firstTime = true;
	while(this.getPulse() != 20) {
	    if(id == 1) {
		if(this.getPulse()==2)
		    this.sendAll(new IntegerMessage(this.getPulse(),round1));
		this.nextPulse();
	    } else {
		if(this.anyMsg() && firstTime) {
		    this.sendAll(new IntegerMessage(this.getPulse(),round1));
		    firstTime = false;
		}
		this.nextPulse();
	    }
	}
    }

    public Object clone() {
	return new TestSynhro();
    }

}

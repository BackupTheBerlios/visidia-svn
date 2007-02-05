package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;


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
	
	Integer id = getId();
	//boolean win = true;
	
	nextPulse();
	
	boolean firstTime = true;
	while(getPulse() != 20) {
	    if(id == 1) {
		if(getPulse()==2)
		    sendAll(new IntegerMessage(getPulse(),round1));
		nextPulse();
	    } else {
		if(anyMsg() && firstTime) {
		    sendAll(new IntegerMessage(getPulse(),round1));
		    firstTime = false;
		}
		nextPulse();
	    }
	}
    }

    public Object clone() {
	return new TestSynhro();
    }

}

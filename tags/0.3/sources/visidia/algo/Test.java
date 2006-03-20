package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;


public class Test extends Algorithm {
    
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

	int degres = getArity();
	
	Integer id = getId();
	boolean win = true;
	
	putProperty("toto",new Toto());
	
	for (int i =0; i< degres ; i++) {
	    sendTo(i, new IntegerMessage(id,round1));
	}
	
	for (int i =0; i< degres ; i++) {
	    IntegerMessage msg = (IntegerMessage)receiveFrom(i);
	    Integer data = (Integer)msg.data();
	    if (data.intValue() > id.intValue()) {
		win  = false;
	    }
	}
	
	if(win) {
	    putProperty("label","I");
	    for (int i =0; i< degres ; i++) {
		sendTo(i, new StringMessage("MIS",round2Mis));
	    }
	    Vector vect = new Vector();
	    vect.add("toto");
	    vect.add("titi");
	    sendAll(new VectorMessage(vect));
	} else {
	    sendAll(new StringMessage("NOT MIS",round2NotMis));
	    boolean bool = true;
	    while(bool) {
		Door door = new Door();
		StringMessage msg = (StringMessage)receive(door);
		if(((String)msg.data()).compareTo("MIS")==0) {
		    setDoorState(new MarkedState(true),door.getNum());
		    bool=false;
		}
	    }
	    putProperty("label","F");
	    
	}
    }

    public class Toto {
	int i = 1;
	int j = 2;
	public Toto() {
	    
	}
	
	public String toString() {
	    return i+";"+j;
	}
    }
    

    public Object clone() {
	return new Test();
    }

}

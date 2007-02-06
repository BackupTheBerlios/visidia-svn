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

	int degres = this.getArity();
	
	Integer id = this.getId();
	boolean win = true;
	
	this.putProperty("toto",new Toto());
	
	for (int i =0; i< degres ; i++) {
	    this.sendTo(i, new IntegerMessage(id,round1));
	}
	
	for (int i =0; i< degres ; i++) {
	    IntegerMessage msg = (IntegerMessage)this.receiveFrom(i);
	    Integer data = msg.data();
	    if (data.intValue() > id.intValue()) {
		win  = false;
	    }
	}
	
	if(win) {
	    this.putProperty("label","I");
	    for (int i =0; i< degres ; i++) {
		this.sendTo(i, new StringMessage("MIS",round2Mis));
	    }
	    Vector vect = new Vector();
	    vect.add("toto");
	    vect.add("titi");
	    this.sendAll(new VectorMessage(vect));
	} else {
	    this.sendAll(new StringMessage("NOT MIS",round2NotMis));
	    boolean bool = true;
	    while(bool) {
		Door door = new Door();
		StringMessage msg = (StringMessage)this.receive(door);
		if((msg.data()).compareTo("MIS")==0) {
		    this.setDoorState(new MarkedState(true),door.getNum());
		    bool=false;
		}
	    }
	    this.putProperty("label","F");
	    
	}
    }

    public class Toto {
	int i = 1;
	int j = 2;
	public Toto() {
	    
	}
	
	public String toString() {
	    return this.i+";"+this.j;
	}
    }
    

    public Object clone() {
	return new Test();
    }

}

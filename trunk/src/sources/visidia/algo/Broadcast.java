package visidia.algo;

import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Broadcast extends Algorithm {
    
    
    static MessageType wave = new MessageType("Wave", true);
    static MessageType ack = new MessageType("Acknowledgment", true, java.awt.Color.blue);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(ack);
        typesList.add(wave);
        return typesList;
    }
    
    public void init(){
	int degres = getArity() ;
	int fatherDoor;
	//int[] childrenDoors = new int[degres];
	
	String label = (String) getProperty("label");

	if(label.compareTo("A") == 0) {
	    for(int i=0; i < degres; i++){
		sendTo(i, new StringMessage("Wave",wave));
	    }
	} else {
	    Door door = new Door();
	    Message msg = receive(door);

	    fatherDoor = door.getNum();

	    sendTo(fatherDoor,new StringMessage("Ack",ack));

	    putProperty("label",new String("A"));
	    setDoorState(new MarkedState(true),fatherDoor);

	    for(int i=0; i < degres; i++){
		if(i != fatherDoor) 
		    sendTo(i, new StringMessage("Wave",wave));
	    }
	}
    }

    public Object clone(){
        return new Broadcast();
    }

}

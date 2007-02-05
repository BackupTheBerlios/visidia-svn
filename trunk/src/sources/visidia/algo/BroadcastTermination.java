package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class BroadcastTermination extends Algorithm {
    
    
    static MessageType termination = new MessageType("termination", true, java.awt.Color.green);
    static MessageType wave = new MessageType("Wave", true);
    static MessageType ack = new MessageType("Acknowledgment", true, java.awt.Color.blue);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(ack);
        typesList.add(wave);
        typesList.add(termination);
        return typesList;
    }
    
    public void init(){
	int degres = getArity() ;
	int fatherDoor;
	int[] childrenStates = new int[degres];
	//Random generator = new Random();
	boolean terminated = false;

	String label = (String) getProperty("label");
	if(label.compareTo("A") == 0) {
	    for(int i=0; i < degres; i++){
		sendTo(i, new StringMessage("Wave",wave));
	    }
	    
	    while(!terminated){
		Door door = new Door();
		StringMessage msg = (StringMessage)receive(door);
		int doorNum= door.getNum();
		String data = msg.data();
		if(data.compareTo("Ack_Yes")==0) {
		    childrenStates[doorNum]=-1;
		} else if(data.compareTo("Ack_No")==0) {
		    childrenStates[doorNum]=1;
		} else if(data.compareTo("Wave")==0) {
		    sendTo(doorNum, new StringMessage("Ack_No",ack));
		} else if(data.compareTo("END")==0) {
		    childrenStates[doorNum]=1;
		}
		
		terminated = true;
		for( int i = 0; i < degres; i++ ) {
		    if(childrenStates[i]!=1)
			terminated = false;
		}
	    }
	    putProperty("label",new String("L"));
	
	} else {

	    Door doorB = new Door();
	    Message msgB = receive(doorB);
	    
	    fatherDoor = doorB.getNum();

	    sendTo(fatherDoor,new StringMessage("Ack_Yes",ack));

	    putProperty("label",new String("I"));
	    setDoorState(new MarkedState(true),fatherDoor);

	    for(int i=0; i < degres; i++){
		if(i != fatherDoor) {
		    sendTo(i, new StringMessage("Wave",wave));
		}
	    }
	    
	    
	    childrenStates[fatherDoor]=1;
	    if (degres != 1) {
		while(!terminated){
		    Door door = new Door();
		    StringMessage msg = (StringMessage)receive(door);
		    int doorNum= door.getNum();
		    String data = msg.data();
		    if(data.compareTo("Ack_Yes")==0) {
			childrenStates[doorNum]=-1;
		    } else if(data.compareTo("Ack_No")==0) {
			childrenStates[doorNum]=1;
		    } else if(data.compareTo("Wave")==0) {
			sendTo(doorNum, new StringMessage("Ack_No",ack));
			
		    } else if(data.compareTo("END")==0) {
			childrenStates[doorNum]=1;
		    }
		    
		    terminated = true;
		    for( int i = 0; i < degres; i++ ) {
			if(childrenStates[i]!=1)
			    terminated = false;
		    }
		}
	    }
	    sendTo(fatherDoor, new StringMessage("END",termination));
	    putProperty("label",new String("F"));
	}
    }

    public Object clone(){
        return new BroadcastTermination();
    }

}


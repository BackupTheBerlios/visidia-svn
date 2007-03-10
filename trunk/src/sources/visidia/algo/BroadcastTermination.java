package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.MarkedState;
//import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.simulation.Algorithm;
import visidia.simulation.Door;

public class BroadcastTermination extends Algorithm {
    
    
    static MessageType termination = new MessageType("termination", true, java.awt.Color.green);
    static MessageType wave = new MessageType("Wave", true);
    static MessageType ack = new MessageType("Acknowledgment", true, java.awt.Color.blue);
    
    public Collection getListTypes(){
        Collection<MessageType> typesList = new LinkedList<MessageType>();
        typesList.add(ack);
        typesList.add(wave);
        typesList.add(termination);
        return typesList;
    }
    
    public void init(){
	int degres = this.getArity() ;
	int fatherDoor;
	int[] childrenStates = new int[degres];
	//Random generator = new Random();
	boolean terminated = false;

	String label = (String) this.getProperty("label");
	if(label.compareTo("A") == 0) {
	    for(int i=0; i < degres; i++){
		this.sendTo(i, new StringMessage("Wave",wave));
	    }
	    
	    while(!terminated){
		Door door = new Door();
		StringMessage msg = (StringMessage)this.receive(door);
		int doorNum= door.getNum();
		String data = msg.data();
		if(data.compareTo("Ack_Yes")==0) {
		    childrenStates[doorNum]=-1;
		} else if(data.compareTo("Ack_No")==0) {
		    childrenStates[doorNum]=1;
		} else if(data.compareTo("Wave")==0) {
		    this.sendTo(doorNum, new StringMessage("Ack_No",ack));
		} else if(data.compareTo("END")==0) {
		    childrenStates[doorNum]=1;
		}
		
		terminated = true;
		for( int i = 0; i < degres; i++ ) {
		    if(childrenStates[i]!=1)
			terminated = false;
		}
	    }
	    this.putProperty("label",new String("L"));
	
	} else {

	    Door doorB = new Door();
	    this.receive(doorB);
	    
	    fatherDoor = doorB.getNum();

	    this.sendTo(fatherDoor,new StringMessage("Ack_Yes",ack));

	    this.putProperty("label",new String("I"));
	    this.setDoorState(new MarkedState(true),fatherDoor);

	    for(int i=0; i < degres; i++){
		if(i != fatherDoor) {
		    this.sendTo(i, new StringMessage("Wave",wave));
		}
	    }
	    
	    
	    childrenStates[fatherDoor]=1;
	    if (degres != 1) {
		while(!terminated){
		    Door door = new Door();
		    StringMessage msg = (StringMessage)this.receive(door);
		    int doorNum= door.getNum();
		    String data = msg.data();
		    if(data.compareTo("Ack_Yes")==0) {
			childrenStates[doorNum]=-1;
		    } else if(data.compareTo("Ack_No")==0) {
			childrenStates[doorNum]=1;
		    } else if(data.compareTo("Wave")==0) {
			this.sendTo(doorNum, new StringMessage("Ack_No",ack));
			
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
	    this.sendTo(fatherDoor, new StringMessage("END",termination));
	    this.putProperty("label",new String("F"));
	}
    }

    public Object clone(){
        return new BroadcastTermination();
    }

}


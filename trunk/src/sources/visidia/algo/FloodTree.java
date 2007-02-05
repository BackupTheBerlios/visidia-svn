package visidia.algo;

import visidia.simulation.*;
import visidia.misc.*;

public class FloodTree extends Algorithm {
    
    /* the code to be exectued by each node */
    public void init(){    
	/* the degree of the node */
	int nodeDegree = getArity() ;
	
	/* the door leading to the father in the tree */
	int parentDoor;
	
	/* the node label */
	String label = (String) getProperty("label");
	
	/* the current node is the source */
	if(label.equals("R")) {
	    /* the node brodcasts a message to neighbors */
	    sendAll(new StringMessage("WAVE"));
	    
	} else {
	    /* the node waits untill a message arrives. The message is
	     * returned in the msg variable. The incoming door is
	     * returned in the door variable */
	    Door door = new Door();
	    Message msg = receive(door);
	    parentDoor = door.getNum();
	    
	    /* the node becomes in the tree */
	    putProperty("label",new String("T"));

	    /* the edge is marked in bold in the GUI */
	    setDoorState(new MarkedState(true),parentDoor);
	    
	    /* the node broadcast a message to its neighbors except
	     * his parent in the tree */
	    for(int i=0; i < nodeDegree; i++){ 
		if(i != parentDoor)
		    sendTo(i, new StringMessage("WAVE")); 
	    }
	    /* other message are ignored and the node locally
	     * terminates: no more action to do */
	} 
    }
    
    public Object clone(){
        return new FloodTree();
    }    
}

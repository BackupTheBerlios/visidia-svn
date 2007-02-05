package visidia.algo;

import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class LinialMis extends Algorithm {
    
    /* the code to be exectued by each node */
    public void init(){
	
	/* the degree of the node */
	int nodeDegree = getArity();
	
	/* vector of door leading to active neighbors */
	Vector<Integer> finishedNeighbors = new Vector<Integer>();

	/* initrally, the node is not in the MIS and it is not a
	 * neighbor of a node in the MIS */
	boolean covered = false;
	
	/* Java random generator */
	Random random = new Random();
	
	/* main loop */
	while(!covered) {
	    /* pick at random an integer */
	    Integer x = new Integer(random.nextInt());

	    
	    /* if there exist active neighbors */ 
	    if(finishedNeighbors.size() != nodeDegree) {
		
		/* send x to all active neighbors */
		for(Integer i = 0; i < nodeDegree; i++) {
		    if(! finishedNeighbors.contains(i))
			/* send x to the neighbor connected on door i */
			sendTo(i,new IntegerMessage(x));
		}
		
		/* receive the integers sent by active neighbors */
		boolean biggest = true;
		for(Integer i =0; i < nodeDegree; i++) {
		    if(! finishedNeighbors.contains(i)){
			/* receive a message from the neighbor connected on door i */
			IntegerMessage msg = (IntegerMessage)receiveFrom(i);
			if (msg.getData() >=  x)
			    biggest = false;
		    }
		}
		
		if (biggest){
		    
		    /* the node is in the MIS */
		    covered = true;
		    for(Integer i =0; i < nodeDegree; i++) {
			if(! finishedNeighbors.contains(i))
			    sendTo(i,new StringMessage("MIS"));
		    }
		    
		    /* set the label of the node to M in the local
		     * data structure of the node: the node is labeled
		     * M in the GUI */
		    putProperty("label","M");

		} else {
		    
		    /* the node is not in the MIS */
		    for(Integer i =0; i < nodeDegree; i++) {
			if(! finishedNeighbors.contains(i))
			    sendTo(i,new StringMessage("NOT MIS"));
		    }

		    /* receive messages from neighbors */
		    for(Integer i =0; i < nodeDegree; i++) {
			if(! finishedNeighbors.contains(i)) {
			    StringMessage msg = (StringMessage)receiveFrom(i);
			    if (msg.getData().equals("MIS")) {
				/* a neighbor is in the MIS */
				covered = true;
			    }
			}
		    }
		    
		    if (covered) {
			
			/* inform active neighbors that the node is covered by the MIS */
			for(Integer i =0; i < nodeDegree; i++) {
			    if(! finishedNeighbors.contains(i))
				sendTo(i,new StringMessage("COVERED"));
			}
			
			/* set the label of the node to C in the local data
			 * structure of the node: the node is labeled C in the
			 * GUI */
			putProperty("label","C");
			
		    } else {
			
			/* inform active neighbors that the node is not covered by the MIS */
			for(Integer i =0; i < nodeDegree; i++) {
			    if(! finishedNeighbors.contains(i))
				sendTo(i,new StringMessage("NOT COVERED"));
			}
			/* check if any neighor was covered */
			for(Integer i =0; i < nodeDegree; i++) {
			    if(! finishedNeighbors.contains(i)) {
				StringMessage msg = (StringMessage)receiveFrom(i);
				if (msg.getData().equals("COVERED")) {
				    finishedNeighbors.add(i);
				}
			    }
			}
		    }
		}
	    } else {
		/* the node is isolated and joins the MIS */
		covered = true;
		putProperty("label","M"); 
	    } 
	} 
    }
    
    public Object clone(){
        return new LinialMis();
    }    
}

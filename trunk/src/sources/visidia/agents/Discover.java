package visidia.agents;

import visidia.simulation.agents.Agent;

import java.util.NoSuchElementException;


/**
 * Agent creates to test the whiteBoard Lock
 * Mark all Vertex by it name
 */
public class Discover extends Agent {

    

    protected void init() {

	int markedVertex = 0;

        setAgentMover("LinearAgentMover");

	sleep(10000);
	
	do {

	    try {
		getVertexProperty(this.toString());
	    } catch (NoSuchElementException e) {
		setVertexProperty(this.toString(),"PASSE");
		setProperty("marked",++markedVertex);
	    }

	    if(markedVertex == getNetSize())
		break;

	    move();
	    
	} while(true); 
	
    }

}

package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;


/**
 * Agent creates to test the whiteBoard Lock
 * Mark all Vertex by it name
 */
public class Discover extends Agent {

    

    protected void init() {

	int markedVertex = 0;

        this.setAgentMover("LinearAgentMover");

	this.sleep(10000);
	
	do {

	    try {
		this.getVertexProperty(this.toString());
	    } catch (NoSuchElementException e) {
		this.setVertexProperty(this.toString(),"PASSE");
		this.setProperty("marked",++markedVertex);
	    }

	    if(markedVertex == this.getNetSize())
		break;

	    this.move();
	    
	} while(true); 
	
    }

}

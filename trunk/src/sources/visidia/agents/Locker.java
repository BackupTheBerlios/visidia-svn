package visidia.agents;

import visidia.simulation.agents.Agent;

import java.lang.IllegalStateException;

/**
 * Agent creates to test the whiteBoard Lock
 * It blocks Vertex, move, and unlocks Vertex it has locked
 */
public class Locker extends Agent {

    

    protected void init() {

        this.setAgentMover("RandomAgentMover");
	
	do {
	    
	    try {
		this.lockVertexProperties();
	    } catch (IllegalStateException e) {
		this.unlockVertexProperties();
	    }
	    this.move();
	    
	} while(true); 
	
    }

}

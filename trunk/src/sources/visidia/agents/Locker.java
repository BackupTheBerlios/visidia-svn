package visidia.agents;

import visidia.simulation.agents.Agent;

import java.lang.IllegalStateException;

/**
 * Agent creates to test the whiteBoard Lock
 * It blocks Vertex, move, and unlocks Vertex it has locked
 */
public class Locker extends Agent {

    

    protected void init() {

        setAgentMover("RandomAgentMover");
	
	do {
	    
	    try {
		lockVertexProperties();
	    } catch (IllegalStateException e) {
		unlockVertexProperties();
	    }
	    move();
	    
	} while(true); 
	
    }

}

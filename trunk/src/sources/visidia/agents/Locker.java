package visidia.agents;

import visidia.simulation.agents.Agent;

import java.lang.IllegalStateException;

/**
 * Agent created to test the whiteBoard Lock.
 * It blocks Vertex, moves, and unlocks Vertex it has locked
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

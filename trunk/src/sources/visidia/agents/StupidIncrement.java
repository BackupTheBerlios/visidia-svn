package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Agent created to test the whiteBoard Lock.
 * It just blocks the Vertex WhiteBoard, increments
 * a variable "test", and ends
 */
public class StupidIncrement extends Agent {

    protected void init() {

	lockVertexProperties();
	try {
	    Integer i = (Integer)getVertexProperty("test");
	    i = new Integer(i.intValue() + 1);
	    setVertexProperty("test",i);
	} catch (NoSuchElementException e) {
	    setVertexProperty("test",new Integer(1));
	}
	unlockVertexProperties();
	
    }

}

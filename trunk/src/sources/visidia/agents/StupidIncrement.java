package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Agent creates to test the whiteBoard Lock
 * It just blocks the Vertex WhiteBoard, increments
 * a variable "test", and ends
 */
public class StupidIncrement extends Agent {

    protected void init() {

	this.lockVertexProperties();
	try {
	    Integer i = (Integer)this.getVertexProperty("test");
	    i = new Integer(i.intValue() + 1);
	    this.setVertexProperty("test",i);
	} catch (NoSuchElementException e) {
	    this.setVertexProperty("test",new Integer(1));
	}
	this.unlockVertexProperties();
	
    }

}

package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Agent  created to  test the  whiteBoard Lock.   It just  blocks the
 * Vertex WhiteBoard, increments a  variable "test", and ends. You may
 * want to use this agent with the ThoudandOnOne chooser: this chooser
 * place 1000 StupidIncrement agents on one vertex.
 *
 * @see visidia.agents.agentchooser.ThousandOnOne
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

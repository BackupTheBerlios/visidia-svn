package visidia.agents;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;

public class MovingBack extends Agent {

    public MovingBack(AgentSimulator sim, Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {

	for(int i = 0; i < getArity(); ++i) {
	    moveToDoor(i);
	    moveBack();
	}
    }
}

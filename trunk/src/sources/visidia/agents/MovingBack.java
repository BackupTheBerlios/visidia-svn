package visidia.agents;

import visidia.simulation.agents.Agent;

public class MovingBack extends Agent {

    protected void init() {

	for(int i = 0; i < getArity(); ++i) {
	    moveToDoor(i);
	    moveBack();
	}
    }
}

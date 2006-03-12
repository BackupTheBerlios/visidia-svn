package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent4 extends SynchronizedAgent {

    protected void init() {
        
        setAgentMover("RandomAgentMover");

        for (int i=0; i < 4; ++i) {
	    sleep(1880);
	    nextPulse();
            move();
	}
    }
}

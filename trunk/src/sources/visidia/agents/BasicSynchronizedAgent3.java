package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent3 extends SynchronizedAgent {

    protected void init() {
        
        setAgentMover("RandomAgentMover");

        for (int i=0; i < 4; ++i) {
	    sleep(300);
	    nextPulse();
            move();
	}
    }
}

package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent2 extends SynchronizedAgent {

    protected void init() {

        setAgentMover("RandomAgentMover");

	for(int i=0; i<7;++i) {
	    sleep(2500);
	    nextPulse();
            move();
	}
    }
}

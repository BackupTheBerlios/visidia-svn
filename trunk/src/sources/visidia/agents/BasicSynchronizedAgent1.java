package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent1 extends SynchronizedAgent {

    protected void init() {

        setAgentMover("RandomAgentMover");

        for(int i=0; i<10; ++i) {
	    sleep(1000);
	    nextPulse();
            move();
	}

    }

}

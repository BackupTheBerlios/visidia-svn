package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;


/**
 * I wait  2.5 seconds  between each  move and I  move 7  times before
 * dying.
 *
 * @see BasicSynchronizedAgent1
 */
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

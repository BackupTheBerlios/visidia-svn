package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent2 extends SynchronizedAgent {

    protected void init() {

        Random rnd = new Random();

	for(int i=0; i<7;++i) {
	    sleep(2500);
	    nextPulse();
            moveToDoor(rnd.nextInt(getArity()));
	}

    }

}

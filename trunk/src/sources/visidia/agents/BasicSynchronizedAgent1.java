package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent1 extends SynchronizedAgent {

    protected void init() {

        Random rnd = new Random();

        for(int i=0; i<10; ++i) {
	    sleep(1000);
	    nextPulse();
            moveToDoor(rnd.nextInt(getArity()));
	}

    }

}

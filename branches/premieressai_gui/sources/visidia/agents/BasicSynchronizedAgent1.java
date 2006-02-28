package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.SynchronizedAgent;

import java.util.Hashtable;

public class BasicSynchronizedAgent1 extends SynchronizedAgent {

    public BasicSynchronizedAgent1(AgentSimulator sim, Hashtable hash) {
        super(sim, hash);
    }

    protected void init() {

        Random rnd = new Random();

        for(int i=0; i<3; ++i) {
	    sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
	    nextPulse();
	}

    }

}

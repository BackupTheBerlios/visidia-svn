package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.SynchronizedAgent;

import java.util.Hashtable;

public class BasicSynchronizedAgent2 extends SynchronizedAgent {

    public BasicSynchronizedAgent2(AgentSimulator sim, Hashtable hash) {
        super(sim, hash);
    }

    protected void init() {

        Random rnd = new Random();

	for(int i=0; i<5;++i) {
	    sleep(2500);
            moveToDoor(rnd.nextInt(getArity()));
	    nextPulse();
	}

    }

}

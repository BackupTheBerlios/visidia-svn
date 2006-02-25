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

        do {
	    sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}
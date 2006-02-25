package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.SynchronizedAgent;

import java.util.Hashtable;

public class BasicSynchronizedAgent4 extends SynchronizedAgent {

    public BasicSynchronizedAgent4(AgentSimulator sim, Hashtable hash) {
        super(sim, hash);
    }

    protected void init() {

        Random rnd = new Random();

        do {
	    sleep(1880);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}

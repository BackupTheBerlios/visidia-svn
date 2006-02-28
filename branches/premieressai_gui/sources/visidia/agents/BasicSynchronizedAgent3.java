package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.SynchronizedAgent;

import java.util.Hashtable;

public class BasicSynchronizedAgent3 extends SynchronizedAgent {

    public BasicSynchronizedAgent3(AgentSimulator sim, Hashtable hash) {
        super(sim, hash);
    }

    protected void init() {

        Random rnd = new Random();
	int test = 0;

        do {
// 	    if(test++ == 2)
// 		killMe();
	    sleep(300);
            moveToDoor(rnd.nextInt(getArity()));
	    nextPulse();
        } while (test++ < 2);

    }

}

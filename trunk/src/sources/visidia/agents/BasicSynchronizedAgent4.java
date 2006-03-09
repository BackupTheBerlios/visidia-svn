package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.SynchronizedAgent;

import java.util.Hashtable;

public class BasicSynchronizedAgent4 extends SynchronizedAgent {

    protected void init() {

        Random rnd = new Random();
	int test = 0;

        do {
	    sleep(1880);
	    nextPulse();
            moveToDoor(rnd.nextInt(getArity()));


 	    if(test++ == 4)
		return;
	    
	} while (1 == 1);

    }

}

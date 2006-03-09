package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.SynchronizedAgent;

public class BasicSynchronizedAgent3 extends SynchronizedAgent {

    protected void init() {

        Random rnd = new Random();
	int test = 0;

        do {
	    
 	    if(test++ == 6)
		return;
	    sleep(300);
	    nextPulse();
            moveToDoor(rnd.nextInt(getArity()));

        } while (1 == 1);

    }

}

package visidia.agents;

import java.util.Random;

import visidia.simulation.Simulator;
import visidia.simulation.*;

import java.util.Hashtable;

public class BasicSynchronizedAgent1 extends SynchronizedAgent {

    public BasicSynchronizedAgent1(Simulator sim, Hashtable hash) {
        super(sim);
    }

    protected void init() {

        Random rnd = new Random();

        do {
	    sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}

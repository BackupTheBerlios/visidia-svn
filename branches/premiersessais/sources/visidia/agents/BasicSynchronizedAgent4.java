package visidia.agents;

import java.util.Random;

import visidia.simulation.Simulator;
import visidia.simulation.*;

import java.util.Hashtable;

public class BasicSynchronizedAgent4 extends SynchronizedAgent {

    public BasicSynchronizedAgent4(Simulator sim, Hashtable hash) {
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

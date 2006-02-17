package visidia.agents;

import java.util.Random;

import visidia.simulation.Simulator;
import visidia.simulation.*;

import java.util.Hashtable;

public class BasicSynchronizedAgent2 extends SynchronizedAgent {

    public BasicSynchronizedAgent2(Simulator sim, Hashtable hash) {
        super(sim);
    }

    protected void init() {

        Random rnd = new Random();

        do {
	    sleep(2500);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}

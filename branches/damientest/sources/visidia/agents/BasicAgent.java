package visidia.agents;

import java.util.Random;

import visidia.simulation.Simulator;
import visidia.simulation.Agent;


public class BasicAgent extends Agent {

    public BasicAgent(Simulator sim) {
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

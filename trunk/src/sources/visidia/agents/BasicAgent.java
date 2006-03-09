package visidia.agents;

import java.util.Random;

import visidia.simulation.agents.Agent;


public class BasicAgent extends Agent {

    protected void init() {

        Random rnd = new Random();

        do {
            sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}

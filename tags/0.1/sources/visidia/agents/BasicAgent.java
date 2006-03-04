package visidia.agents;

import java.util.Random;
import java.util.Hashtable;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;


public class BasicAgent extends Agent {

    public BasicAgent(AgentSimulator sim, Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {

        Random rnd = new Random();

        do {
            sleep(1000);
            moveToDoor(rnd.nextInt(getArity()));
        } while (1 == 1);

    }

}

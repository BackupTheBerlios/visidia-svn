package visidia.agentsmover;

import java.util.Random;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

/**
 * Provide a random move for an Agent. On a vertex, the agent go to
 * a random door.
 */
public class RandomAgentMover extends AgentMover {
    
    public RandomAgentMover(Agent ag, AgentSimulator sim) {
        super(ag, sim);
    }

    protected int findNextDoor() {
        Random rand = new Random();

        return rand.nextInt(agent().getArity());
    }
}

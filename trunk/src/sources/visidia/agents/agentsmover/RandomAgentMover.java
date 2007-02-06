package visidia.agents.agentsmover;

import java.util.Random;

import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

/**
 * Provides a random move for an Agent. On a vertex, the agent goes to
 * a random door.
 */
public class RandomAgentMover extends AgentMover {

    private Random rand = new Random();
    
    public RandomAgentMover(Agent ag) {
        super(ag);
    }

    protected int findNextDoor() {
        return this.rand.nextInt(this.agent().getArity());
    }
}

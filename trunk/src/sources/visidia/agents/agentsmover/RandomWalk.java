package visidia.agents.agentsmover;

import java.util.Random;

import visidia.simulation.agents.Agent;

/**
 * Provides a random move for an Agent. On a vertex, the agent goes to a random
 * door.
 */
public class RandomWalk extends RandomAgentMover {

	public RandomWalk(Agent ag) {
		super(ag);
	}

	public final void move() throws InterruptedException {
		Random rand = new Random();
		if (0 == rand.nextInt(2)) {
			super.move();
		}
	}
}

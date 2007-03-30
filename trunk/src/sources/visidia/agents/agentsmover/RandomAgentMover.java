package visidia.agents.agentsmover;

import java.util.Random;

import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

import visidia.simulation.agents.MoveException;

/**
 * Provides a random move for an Agent. On a vertex, the agent goes to a random
 * door.
 */
public class RandomAgentMover extends AgentMover {

	private Random rand = new Random();

	public RandomAgentMover(Agent ag) {
		super(ag);
	}

	public int findNextDoor() throws MoveException {
		int arity;
		arity = this.agent().getArity();
	
		if(arity == 0)
			throw new MoveException(MoveException.NoDoorFound);
		else if(!(this.agent().getSimulator().getVertexArrival(this.agent()).getVisualization())) {
			throw new MoveException(MoveException.SwitchedOffVertex);
		}
		else
			return this.rand.nextInt(arity);
	}
}

package visidia.agents.agentsmover;

import java.util.Arrays;

import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;
import visidia.simulation.agents.MoveException;

/**
 * Provides a linear move for an Agent. On a vertex, the agent go to the first
 * never-visited door.
 * 
 * /!\ Warning, this implementation implies that each vertex has an unique
 * identifier !!!
 */
public class LinearAgentMover extends AgentMover {

	// Remembers the door on which the agent will go next time
	int[] nextDoorToGo;

	/**
	 * Constructor. Allows to create a new AgentMover for a given Agent.
	 */
	public LinearAgentMover(Agent ag) {
		super(ag);
		this.nextDoorToGo = new int[ag.getNetSize()];

		/* Starts on the first door */
		Arrays.fill(this.nextDoorToGo, 0);
	}

	public int findNextDoor() throws MoveException {
		int vertex = this.agent().getVertexIdentity();
		int doorToGo = this.nextDoorToGo[vertex];
		int arity = this.agent().getArity();

		/* The following door is the current one plus 1 */
		while(arity == 0) {
			//throw new MoveException(MoveException.NoDoorFound);
		    try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
		}
	    if(!(this.agent().getSimulator().getVertexArrival(this.agent()).getVisualization())) {
			throw new MoveException(MoveException.SwitchedOffVertex);
		}
		else {
			this.nextDoorToGo[vertex] = (this.nextDoorToGo[vertex] + 1) % arity;
		}

		return doorToGo;
	}
}

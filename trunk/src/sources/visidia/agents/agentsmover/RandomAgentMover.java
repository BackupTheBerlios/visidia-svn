package visidia.agents.agentsmover;

import java.util.Enumeration;
import java.util.Random;

import visidia.graph.Vertex;
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
		int door;
		while(arity == 0){
			try {
				arity = this.agent().getArity();
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
		}
		
		Vertex vertex =  this.agent().getSimulator().getVertexArrival(this.agent());
		
		if(!(vertex.getVisualization())) {
			throw new MoveException(MoveException.SwitchedOffVertex);
		}
		else{
			door = this.rand.nextInt(arity);
			while((!isOpenDoor(door, vertex))&& vertex.getVisualization() ){
				arity = this.agent().getArity();
				door = this.rand.nextInt(arity);
			}
		}
		//return this.rand.nextInt(arity);
		return door;
	}
	
	public Boolean isOpenDoor(int door, Vertex vertex){
		
		Enumeration e = vertex.neighbours();
		int i = 0;
		while (e.hasMoreElements()) {
			Vertex v = (Vertex) e.nextElement();
			if ((i==door) && (v.getVisualization())) {
				return true;
			}
			i++;
		}
		return false;
	}
}

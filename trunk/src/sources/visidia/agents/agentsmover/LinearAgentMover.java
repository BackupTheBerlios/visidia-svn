package visidia.agents.agentsmover;

import java.util.Enumeration;
import java.util.Hashtable;

import visidia.graph.Vertex;
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
	private Hashtable<Integer, Integer> nextDoorToGo = new Hashtable<Integer, Integer>();
	/**
	 * Constructor. Allows to create a new AgentMover for a given Agent.
	 */	
	
	public LinearAgentMover(Agent ag) {		
		super(ag);
		int k = 0;	
		while(k < ag.getNetSize()){
			this.nextDoorToGo.put(k, new Integer(0));
			k++;
		}
	}

	
	public int findNextDoor() throws MoveException {
		
		int vertex = this.agent().getVertexIdentity();
		this.updateNextDoorToGo();
		int doorToGo = (Integer)this.nextDoorToGo.get(vertex);
		int arity = this.agent().getArity();
		Vertex sgv = this.agent().getSimulator().getVertexArrival(this.agent());
		while(arity == 0) {
			try {
		    	arity = this.agent().getArity();
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
		}
		
	    if(!(sgv.getVisualization())) {
			throw new MoveException(MoveException.SwitchedOffVertex);
		}
		else {
			this.nextDoorToGo.put(vertex, (((Integer)this.nextDoorToGo.get(vertex) + 1) % arity));
			while(!this.isOpenDoor(doorToGo, sgv)){				
				arity = this.agent().getArity();
				doorToGo = (Integer)this.nextDoorToGo.get(vertex);
				this.nextDoorToGo.put(vertex, (((Integer)this.nextDoorToGo.get(vertex) + 1) % arity));
			}
			
		}

		return doorToGo;
	}
	
/*
 * update the nextDoorToGo to add informations about added vertex
 */	
	protected void updateNextDoorToGo(){
		int id;
		Enumeration vertices;
		vertices = this.agent().getSimulator().getGraph().vertices();
		while(vertices.hasMoreElements()){
			id = ((Vertex)vertices.nextElement()).identity();
			if(!this.nextDoorToGo.containsKey(id)){
				this.nextDoorToGo.put(id, 0);
			}
		}
		
	}
}
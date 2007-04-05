package visidia.agents;

import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;
import visidia.simulation.agents.stats.FailedMoveStat;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collection;
import visidia.simulation.agents.MoveException;
import java.lang.Class;
import visidia.agents.Spanning_Tree_Agent_SelfStabilized;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Depth_Traversal_Agent extends Spanning_Tree_Agent {

	private volatile Integer idTree = -1;
	
	public void init() {
		System.out.println("Depth_Traversal_Agent: je suis créé ");
		this.setAgentMover("RandomAgentMover");
		AgentMover am = this.getAgentMover();

		this.setProperty("NumberVertexVisited", new Integer(1));
		
		// Waiting for the call of the method setIdTreeToVisit
		while(this.idTree == -1) this.sleep(100L);
		
		while (true) {

			/*
			 * arrival
			 */
			
			Integer treeId = this.getVertexIdTree();

			// The vertex is not belonging to any tree
			if(treeId == null) {
				return;
			}
			else {
				// The vertex belong to the tree
								
				Collection<Integer> unvisitedChildsC = this.getVertexChildsUnvisited();
				Iterator<Integer> unvisitedChildsI = unvisitedChildsC.iterator();

				if(unvisitedChildsI.hasNext()) {
					// It remains unvisited children
					Integer nextDoor = unvisitedChildsI.next();
					this.setVertexPortToChildAsVisited(nextDoor);
					this.setProperty("NumberVertexVisited", (Integer)this.getProperty("NumberVertexVisited") +1);

					move(nextDoor);
				}
				else {
					// All children of the vertex have been visited
					
					Integer nextDoor = this.getVertexParent(this.idTree);

					if(nextDoor == null) {
						// The current vertex is the root

						this.sayTerminaisonResultToSpanningTreeAgent();
						return;
					}
					else {
						// The current vertex is NOT the root
						move(nextDoor);
					}
				}
			}
			
		}

	}
	
	/**
	 * When the agent is created by a Spanning_Tree_Agent_SelfStabilized agent, this method 
	 * is used to indicate to him how many vertices have been found
	 *
	 */
	private void sayTerminaisonResultToSpanningTreeAgent() {
		
		Collection<Agent> agentsC = this.agentsOnVertex();
		Iterator<Agent> agentsI = agentsC.iterator();
		
		while(agentsI.hasNext()) {
			Agent a = agentsI.next();

			if(a.className().equals("Spanning_Tree_Agent_SelfStabilized")) {
				if(((Spanning_Tree_Agent_SelfStabilized)a).isWaitingForTerminaisonTest()) {
					((Spanning_Tree_Agent_SelfStabilized)a).setResultOfTestTerminaison(((Integer)this.getProperty("NumberVertexVisited")).intValue());
					break;
				}
			}
		}
	}
	
	

	/**
	 * Return a collection of vertices that have not yet been visited by the current agent
	 * @return
	 */
	protected Collection<Integer> getVertexChildsUnvisited() {
		
		Collection<Integer> childs = this.getVertexChilds(this.idTree);

		Collection<Integer> visitedChildC = this.getVertexChildsVisited();
		Iterator<Integer> visitedChildI = visitedChildC.iterator();
		
		while(visitedChildI.hasNext()) {
			Integer visitedChild = visitedChildI.next();
			if(childs.contains(visitedChild)) childs.remove(visitedChild);
		}

		return childs;
	}
	
	/**
	 * Return a collection of vertices that have alreay been visited by the current agent
	 * @return
	 */
	protected Collection<Integer> getVertexChildsVisited() {
		
		LinkedList<Integer> parent = new LinkedList<Integer>();

		for (Integer i = 0; i < this.getArity(); i++) {

			try {
				if (((String) this.getProperty("Vertex" + this.getVertexIdentity()
						+ "Port" + i.toString())).equals("Visited")) {
					parent.add(new Integer(i));
				}
			} catch (NoSuchElementException e) {
			}
		}

		return parent;
	}
	
	/**
	 * Set the specified port of the current Vertex as visited
	 * @param p Port to modify
	 */
	protected void setVertexPortToChildAsVisited(Integer p) {
		this.setProperty("Vertex" + this.getVertexIdentity() + "Port"
				+ p.toString(), "Visited");
	}

	/**
	 * Specify the idTree that the agent will travers
	 * Only the first call have effect
	 * @param idTree
	 */
	public void setIdTreeToVisit(Integer idTree) {
		if(this.idTree == -1) this.idTree = idTree;
	}
}

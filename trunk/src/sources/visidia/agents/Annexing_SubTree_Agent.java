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
public class Annexing_SubTree_Agent extends Depth_Traversal_Agent {

	private volatile Integer weaker = -1;
	private Integer stronger;
	
	public void init() {

		this.setAgentMover("RandomAgentMover");
		AgentMover am = this.getAgentMover();
	

		// Waiting for the call of the method setIdTreeToVisit
		while(this.weaker == -1) this.sleep(100L);
				
		this.stronger = this.getVertexIdTree();
		
		System.out.println("Annexing_SubTree_Agent2: id reçu " + this.weaker + "-" + this.stronger);

		boolean isMarkingEdge = false;
		boolean firstVertex = true;
		
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
				// The vertex belong to a tree
				
				
				
				// Finishing to mark the edge
				
				if(!firstVertex) {
					
					if(this.isPortIsChildInTheTree(this.weaker,this.entryDoor())) {
						this.setVertexPortToChildAsVisited(this.entryDoor());
					}
					
					if(isMarkingEdge && this.getVertexIdTree().equals(this.stronger)) {
						// Trying to add an edge between two marked vertices
						System.out.println("Annexing_SubTree_Agent2: arrete ajoutee par erreur -> move back");
						
						this.moveBack();
						this.delVertexPort(this.stronger, this.entryDoor());
						System.out.println("Annexing_SubTree_Agent2: arrete ajoutee par erreur -> suppression arrête ajoutée par erreur, retour au sommet où l'erreur a été trouvée");
						this.move(this.entryDoor());
						System.out.println("Annexing_SubTree_Agent2: arrete ajoutee par erreur -> arrivée sur le sommet où l'erreur a été trouvée");
					}
					else if(isMarkingEdge && treeId < this.stronger){
						// Adding edge between a vertice in the tre and one other no in the tree (normal)
						System.out.println("Annexing_SubTree_Agent2: adding new edge");
						this.setVertexPortToParent(this.stronger, this.entryDoor());
						this.setVertexIdTree(this.stronger);
					}
					else if(isMarkingEdge && treeId > this.stronger) {
						this.setVertexPortToParent(this.stronger, this.entryDoor());
					}
					else if(this.getVertexIdTree().equals(this.weaker) && !this.isPortIsParentInTheTree(this.weaker,this.entryDoor())) {
						// Arriving in a vertex from a tempory edge that we have considered belonging to the weaker tree wheras it not
						// belonging to
						System.out.println("Annexing_SubTree_Agent2: incorrect door taken, moveback");
						this.moveBack();
						if(isMarkingEdge) this.delVertexPort(this.stronger, this.entryDoor());
						System.out.println("Annexing_SubTree_Agent2: incorrect door taken, remove wrong door");
					}					
					
					isMarkingEdge = false;				
					
					
				}
				
				// Killing Agent if necessary
				if(treeId > this.stronger) {
					// Vertex beloging to a bigger agent founded
					System.out.println("Annexing_SubTree_Agent2: je me tue j'ai trouvé plus fort que moi");
					return;
				}
				
				
				// Choosing next destination
				Collection<Integer> unvisitedChildsC = this.getVertexChildsUnvisited();
				Iterator<Integer> unvisitedChildsI = unvisitedChildsC.iterator();

				if(unvisitedChildsI.hasNext()) {
					// It remains unvisited children
					Integer nextDoor = unvisitedChildsI.next();
					
					System.out.println("Annexing_SubTree_Agent2: j'ai trouvé un fils" + nextDoor);
					
					this.setVertexPortToChildAsVisited(nextDoor);
					
					if(!this.isPortMarkedInTheTree(this.stronger, nextDoor)) {
						// The vertex is no marked by an other agent so we can add it
						this.setVertexPortToChild(this.stronger, nextDoor);
						isMarkingEdge = true;
					}

					move(nextDoor);
				}
				else {
					// All children of the vertex have been visited

					Integer nextDoor = this.getVertexParent(this.weaker);
					
					if(nextDoor == null) {
						// The current vertex is the root
						// Work is finish
						System.out.println("Annexing_SubTree_Agent2: ok j'ai fini");

						return;
					}
					else {
						// The current vertex is NOT the root
						System.out.println("Annexing_SubTree_Agent2: parent trouvé porte " + nextDoor);
						
						isMarkingEdge = !this.isPortMarkedInTheTree(this.stronger, nextDoor);
						
						if(isMarkingEdge) {
							System.out.println("Annexing_SubTree_Agent2: je remontre à mon père en faisant une arrête");
							this.setVertexPortToChild(this.stronger, nextDoor);
						}
						else {
							System.out.println("Annexing_SubTree_Agent2: je remontre à mon père sans faire d'arrête");
							//this.setVertexPortToParent(this.stronger, nextDoor);
						}
						
						move(nextDoor);
					}
				}
				
				firstVertex = false;
			}
			
		}

	}
	
	

	/**
	 * Return a collection of vertices that have not yet been visited by the current agent
	 * @return
	 */
	protected Collection<Integer> getVertexChildsUnvisited() {
		
		Collection<Integer> childs = this.getVertexChilds(this.weaker);

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
		
		Collection<Integer> parent = new LinkedList<Integer>();

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
	public void setIdTreeToAnnexe(Integer idTree) {
		if(this.weaker == -1) this.weaker = idTree;
	}
}

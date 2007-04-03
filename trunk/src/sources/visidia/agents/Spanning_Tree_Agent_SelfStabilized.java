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

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_SelfStabilized extends Spanning_Tree_Agent {

	public void init() {

		this.setAgentMover("RandomAgentMover");
		AgentMover am = this.getAgentMover();

		this.setProperty("EntryPort", new Integer(-1));
		
		boolean goBackToRemoveChild = false;
		boolean entryDoorBelongingToTheTree = true;
				
		while (true) {

			/*
			 * arrival
			 */
			
			Integer treeId = this.getVertexIdTree();

			if(goBackToRemoveChild) {
				// Entering in a vertex after a go back from an alreay marked vertex
				this.delVertexPort(this.getIdentity(), (Integer) this.entryDoor());
				goBackToRemoveChild = false;
			}
			
			// Testing the presence of other agent on the vertex
			Collection<Agent> c = (Collection<Agent>)this.agentsOnVertex();
			Iterator<Agent> i = c.iterator();
			if(i.hasNext()) {
				System.out.println("Agent" + Integer.valueOf(this.getIdentity()).toString() + " une personne");

				Agent a = i.next();
				if(this.getIdentity() > a.getIdentity()) {
					a.death();
					System.out.println("Agent" + Integer.valueOf(this.getIdentity()).toString() + " kill Agent" + Integer.valueOf(a.getIdentity()).toString());
				}
				else if(this.getIdentity() < a.getIdentity()) {
					System.out.println("Agent" + Integer.valueOf(this.getIdentity()).toString() + " se sucide suite à une mauvaise rencontre.");
					this.createEatingClone();
					return;
				}
			}
			
			
			if(treeId == null || treeId < this.getIdentity()) {	// First visit of an agent on the vertex

				this.setVertexIdTree(this.getIdentity());
				
				if((Integer)this.getProperty("EntryPort") != -1) {
					this.setVertexPortToParent(this.getIdentity(), (Integer) this.getProperty("EntryPort"));

					// Coloration graphique
					this.markDoor(this.entryDoor());
				}
				
				if(treeId != null && treeId < this.getIdentity()) {
					
					// Creating agent for eating the subtree
					this.createEatingClone();	
				}
			}
			else if(treeId == this.getIdentity()) {	// Vertex visited by me
				if(!entryDoorBelongingToTheTree) { // Go to the precent vertex to remove the edge from its childs
					goBackToRemoveChild = true;		
				}
				
			}
			else if(treeId > this.getIdentity()) {	// Vertex visited by a bigger agent

				// We must indicate the parent of the Vertex in the Tree of the agent, even if he will die
				// in order to enable the clone to find and eat the old Tree
				if((Integer)this.getProperty("EntryPort") != -1) {
					this.setVertexPortToParent(this.getIdentity(), (Integer) this.getProperty("EntryPort"));

					// Coloration graphique
					this.markDoor(this.entryDoor());
					
					this.createEatingClone();

				}
				
				System.out.println("Agent" + Integer.valueOf(this.getIdentity()).toString() + " se sucide.");			
				return;
			}
			

			this.sleep(3000L);

			
			/*
			 * Depart
			 */

			if(goBackToRemoveChild) {

				entryDoorBelongingToTheTree = true;
				this.setProperty("ExitPort", new Integer(this.entryDoor()));
				this.moveBack();
			}
			else {
				
				// Choose the next door
				try {
					this.setProperty("ExitPort", new Integer(am.findNextDoor()));
				} catch (MoveException e) {
					this.processingAgentWhenSwitchingOff();
				}

				entryDoorBelongingToTheTree = this.getVertexPort(this.getIdentity(),
						(Integer) this.getProperty("ExitPort")) != null;

				// Set the door as a Child
				if (!entryDoorBelongingToTheTree)
					this.setVertexPortToChild(this.getIdentity(), (Integer) this
							.getProperty("ExitPort"));

				// Move to the choosen door$
				System.out.println("move: " + this.getProperty("ExitPort") + "/" + (this.getArity() -1));
				this.move((Integer) this.getProperty("ExitPort"));	
			}
			
			this.setProperty("EntryPort", new Integer(this.entryDoor()));
			
			System.out.println("---------------");

		}

	}
	
	private void createEatingClone() {
		
		System.out.println("createEatingClone");
		try {
			this.createAgent(Class.forName("visidia.agents.BasicAgent"));					
		}
		catch(java.lang.ClassNotFoundException e) {}
	}
	

}

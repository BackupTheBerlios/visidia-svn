package visidia.agents;

import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;
import visidia.simulation.agents.stats.FailedMoveStat;
import visidia.agents.Depth_Traversal_Agent;
import visidia.agents.Annexing_SubTree_Agent;
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

	private volatile boolean waitingForTerminaisonTest = false;
	private volatile boolean isTerminated = false;
	private volatile boolean killByAnOtherAgent = false;

	public void init() {

		this.setAgentMover("RandomAgentMover");
		AgentMover am = this.getAgentMover();

		this.setProperty("EntryPort", new Integer(-1));
		this.setProperty("RootVertex", new Integer(this.getVertexIdentity()));
		
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
			Collection<Spanning_Tree_Agent_SelfStabilized> c = this.agentsSTASOnVertex();
			Iterator<Spanning_Tree_Agent_SelfStabilized> i = c.iterator();
			if(i.hasNext()) {
				// Other agent found
				Spanning_Tree_Agent_SelfStabilized a = i.next();
				if(this.getIdentity() > a.getIdentity()) {
					// Meeting with a smaller agent => killing him
					a.killAgentAfterHisWork();
				}
				else if(this.getIdentity() < a.getIdentity()) {
					// Meeting with a bigger agent => sucide

					this.setVertexPortToParent(this.getIdentity(), (Integer) this.getProperty("EntryPort"));
					
					this.createEatingClone(this.getIdentity());
					
					return;
				}
			}
			
			// An other agent kill me
			if(this.killByAnOtherAgent) return;
			
			if(treeId == null || treeId < this.getIdentity()) {	// First visit of an agent on the vertex || vertex visited by a smaller agent

				// We adding the vertex to the tree only if the entry edge have been added or if this is the first vertex visited
				if(!entryDoorBelongingToTheTree || (Integer)this.getProperty("EntryPort") == -1) this.setVertexIdTree(this.getIdentity());
				
				if((Integer)this.getProperty("EntryPort") != -1) {
					this.setVertexPortToParent(this.getIdentity(), (Integer) this.getProperty("EntryPort"));

					// Coloration graphique
					this.markDoor(this.entryDoor());
				}
				
				if(treeId != null && treeId < this.getIdentity()) {
					// Vertex visited by a smaller agent
					System.out.println("Spanning_Tree_Agent_SelfStabilized: createEatingClone " + treeId);
					this.createEatingClone(treeId);	
				}
			}
			else if(treeId == this.getIdentity()) {	// Vertex visited by me
				
				if(!entryDoorBelongingToTheTree) { // Go to the precedent vertex to remove the edge from its childs
					goBackToRemoveChild = true;		
				}
				else {
					
					if((Integer)this.getProperty("EntryPort") != -1) {
						// Testing the terminaison if we are at the root and there is not the first vertex visited
						if(((Integer)this.getProperty("RootVertex")).equals(this.getVertexIdentity())) {
							if(this.isTreeTerminated()) {
								return;
							}
						}
					}
				}
				
			}
			else if(treeId > this.getIdentity()) {	// Vertex visited by a bigger agent

				// We must indicate the parent of the Vertex in the Tree of the agent, even if he will die
				// in order to enable the clone to find and eat the old Tree
				if((Integer)this.getProperty("EntryPort") != -1) {
					this.setVertexPortToParent(this.getIdentity(), (Integer) this.getProperty("EntryPort"));

					// Coloration graphique
					this.markDoor(this.entryDoor());
					
					this.createEatingClone(this.getIdentity());

				}
				
				return;
			}

			
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

				// Move to the choosen door
				this.move((Integer) this.getProperty("ExitPort"));	
			}
			
			this.setProperty("EntryPort", new Integer(this.entryDoor()));

		}

	}
	
	private void createEatingClone(Integer idTreeToAnnexe) {
		
		boolean isEatingCloneCreated;
		
		try {
			isEatingCloneCreated = ((Boolean)this.getProperty("EatingTree" + idTreeToAnnexe.toString())).booleanValue();
		} catch (NoSuchElementException e) {
			isEatingCloneCreated = false;
		}
		
		if(!isEatingCloneCreated) {
		
			try {
				this.createAgent(Class.forName("visidia.agents.Annexing_SubTree_Agent"));					
			}
			catch(java.lang.ClassNotFoundException e) {}
			
			boolean agentFound = false;
			
			while(!agentFound) {
				Collection<Agent> agentsC = this.agentsOnVertex();
				Iterator<Agent> agentsI = agentsC.iterator();
				
				while(agentsI.hasNext()) {
					Agent a = agentsI.next();
					
					if(a.className().equals("Annexing_SubTree_Agent")) {
						System.out.println("Spanning_Tree_Agent_SelfStabilized: voici ton identité");
						((Annexing_SubTree_Agent)a).setIdTreeToAnnexe(idTreeToAnnexe);
						
						agentFound = true;
						break;
					}
				}
				
				if(!agentFound) this.sleep(100L);
			}
			
			this.setProperty("EatingTree" + idTreeToAnnexe.toString(),new Boolean(true));
		}
	}
	
	private boolean isTreeTerminated() {
		
		try {
			this.createAgent(Class.forName("visidia.agents.Depth_Traversal_Agent"));					
		}
		catch(java.lang.ClassNotFoundException e) {}
		
		this.waitingForTerminaisonTest = true;
		
		boolean agentFound = false;
		
		while(!agentFound) {
			Collection<Agent> agentsC = this.agentsOnVertex();
			Iterator<Agent> agentsI = agentsC.iterator();
			
			while(agentsI.hasNext()) {
				Agent a = agentsI.next();
				
				if(a.className().equals("Depth_Traversal_Agent")) {
					((Depth_Traversal_Agent)a).setIdTreeToVisit(this.getIdentity());
					
					agentFound = true;
					break;
				}
			}
			
			if(!agentFound) this.sleep(100L);
		}
		
		// Waiting for answer of the clone
		while(this.waitingForTerminaisonTest == true) this.sleep(500L);

		return this.isTerminated;
		
	}
	
	public void setResultOfTestTerminaison(int nbrVerticesFound) {
		this.isTerminated = this.getNetSize() == nbrVerticesFound;
		this.waitingForTerminaisonTest = false;
	}
	
	public boolean isWaitingForTerminaisonTest() {
		return this.waitingForTerminaisonTest;
	}
	
	public void killAgentAfterHisWork() {
		this.killByAnOtherAgent = true;
	}
	
    public Collection<Spanning_Tree_Agent_SelfStabilized> agentsSTASOnVertex(){
    	// We must remove from this list the agents who don't are Sappning_Tree_Agent_SelfStabilized
    	Collection<Agent> c = this.agentsOnVertex();
    	Iterator<Agent> i = c.iterator();
    	
    	Collection<Spanning_Tree_Agent_SelfStabilized> cSTAS = new LinkedList<Spanning_Tree_Agent_SelfStabilized>();
 
		while(i.hasNext()) {
			Agent a = i.next();
			
			if(a.className().equals("Spanning_Tree_Agent_SelfStabilized")) {
				cSTAS.add((Spanning_Tree_Agent_SelfStabilized)a);
			}
		}

    	return cSTAS;
    }

}

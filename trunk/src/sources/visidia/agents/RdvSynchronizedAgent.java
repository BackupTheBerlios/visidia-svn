package visidia.agents;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.SynchronizedAgent;

/**
 * Implements the rendez-vous algorithm 
 */
public class RdvSynchronizedAgent extends SynchronizedAgent {


	protected void init() {
		/* Memory of the agent that will be fusionned with the other agents */
		this.setProperty("myName", this.toString());

		/* Agent move method */
		this.setAgentMover("RandomAgentMover");

		/* Random Class to know whether to move the agent or not
		 * (if not, the agent remains on its vertex) 
		 * The algorithm makes the hypothesis that an agent has the
		 * choice to remain on a vertex */
		Random rnd = new Random();

		int agentId = this.getIdentity();



		/* The algorithm ends when the total number of 'RdvSynchronizedAgent' on the graph is equal to 1 */
		while (! oneAgentRdvRemaining()) {

			/* See all agents on the current vertex */
			Collection agentsOnVertex = this.getSimulator().getAgentsVertexCollection(this.getVertexIdentity());

			/* Make the union of the agents' memories if current agent has a better ID */
			if (agentsOnVertex.size() > 1){
				Iterator it = agentsOnVertex.iterator();

				/* Find the better ID : if the agent has a lower ID, it saves its memory to the
				 * whiteboard of the vertex on which the agent is, and then it kills itself */ 
				while (it.hasNext()){
					Agent currentAgent = (Agent) it.next();	
					int currentId = currentAgent.getIdentity();

					if (agentId < currentId){
						fusionAgentToVertex(this);
						return;   /* Kills itself */
					}
				}
				this.nextPulse();

				/* If here, the agent 'this' has the better ID on the vertex : it adds to its memory
				 * the whiteboard of the vertex on which it is */
				this.setProperty("myName", this.getProperty("myName") + " kills : (" + ((String) this.getVertexProperty("fusion")) + " )");
				this.setVertexProperty("fusion", "");

			}

			/* Move the agent to a next vertex with a chance of 1 out of 3 */
			int choice = rnd.nextInt() % 2;

			if (choice == 0){
				this.move();
			}
			this.nextPulse();

		}
	}




	/**
	 * This function returns true if it remains exactly one agent of type RdvSynchronizedAgent on the graph, false otherwise
	 */
	private Boolean oneAgentRdvRemaining(){
		Collection allAgents = this.getSimulator().getAllAgents();

		if (! allAgents.isEmpty()){
			Iterator it = allAgents.iterator();
			while(it.hasNext()){
				Agent currentAgent = (Agent) it.next();
				/* Try to find an agent of the same class with a different ID */
				if((currentAgent.getClass() == this.getClass()) && (currentAgent.getIdentity() != this.getIdentity())){
					return false;
				}
			}
			/* The agent didn't find any other agent of its class  : it is the only one remaining on the graph */
			return true;
		} else {
			return false;
		}
	}


	/**
	 * This function adds the agent memory to the whiteboard of the vertex on which the agent is 
	 */
	private void fusionAgentToVertex(Agent agent){
		String agentName = (String) this.getProperty("myName");
		this.lockVertexProperties();
		String currentFusion = "";
		try {
			currentFusion = (String) this.getVertexProperty("fusion");
		} catch (NoSuchElementException e){
			this.setVertexProperty("fusion", currentFusion);
		}
		this.setVertexProperty("fusion", currentFusion + " " + agentName);
		this.unlockVertexProperties();
	}


}

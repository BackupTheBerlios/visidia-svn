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
public class RdvSynchronizedAgentBIS extends SynchronizedAgent {


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
		System.out.println(this.getProperty("myName") + " a pour id " + agentId);
		Integer test = new Integer(agentId);
		System.out.println(this.getProperty("myName") + " a pour Integer.id " + test);
		this.setVertexProperty("maxId", new Integer(agentId));
		System.out.println(this.getProperty("myName") + " a mis dans le sommet " + this.getVertexProperty("maxId"));

		/* The algorithm ends when the total number of 'RdvSynchronizedAgent' on the graph is equal to 1 */
		while (! oneAgentRdvRemaining()) {


			/* Puts its ID on the vertex if it is better */
			this.lockVertexProperties();
			try {
				Integer currentBestId = (Integer) this.getVertexProperty("maxId");
				if (agentId > currentBestId.intValue()){
					this.setVertexProperty("maxId", new Integer(agentId));
				}
			} catch (NoSuchElementException e){
				this.setVertexProperty("maxId", new Integer(agentId));
			}
			this.unlockVertexProperties();

			this.nextPulse();


			
			/* If 'maxId' on the vertex is better than its ID,
			 * it puts its memory on the vertex and then kills itself */
			if (agentId < ((Integer) this.getVertexProperty("maxId")).intValue()){
				fusionAgentToVertex(this);
				return;  /* Kills itself */
			}

			this.nextPulse();


			/* If here, the agent 'this' has the better ID on the vertex : it adds to its memory
			 * the whiteboard of the vertex on which it is */
			String currentFusion = "";
			try {
				currentFusion = (String) this.getVertexProperty("fusion");
				if (! currentFusion.isEmpty()){
					this.setProperty("myName", this.getProperty("myName") + " kills : (" + ((String) this.getVertexProperty("fusion")) + " )");
				}
			} catch (NoSuchElementException e){
				this.setVertexProperty("fusion", "");
			}


			/* Erase the memory of the vertex (memory of the agent in 'fusion' and 'maxId') */
			this.lockVertexProperties();
			this.setVertexProperty("fusion", "");
			this.setVertexProperty("maxId", 0);
			this.unlockVertexProperties();


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

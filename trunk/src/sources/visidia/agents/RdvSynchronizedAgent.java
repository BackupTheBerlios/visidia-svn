package visidia.agents;

import java.util.Collection;
import java.util.Iterator;
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


		/* The algorithm ends when the total number of agents on the graph is equal to 1 */
		while (this.getSimulator().getAllAgents().size() != 1) {
			/* See all agents on the arrival's vertex */
			int vertexId = this.getSimulator().getVertexIdentity(this);
			//System.out.println(this.getProperty("myName")+" est sur le sommet : "+vertexId);
			Collection agentsOnVertex = this.getSimulator().getAgentsVertexCollection(vertexId);


			/* Make the union of the agents' memories if current agent has a better ID */
			if (! agentsOnVertex.isEmpty() ){
				int agentId = this.getIdentity();
				String agentName = (String) this.getProperty("myName");
				Iterator it = agentsOnVertex.iterator();


				while (it.hasNext()){
					Agent currentAgent = (Agent) it.next();	
					int currentId = currentAgent.getIdentity();
					String currentAgentName = (String) currentAgent.getProperty("myName");


					/* Save the agents'name and kill the agent if better ID */
					if (agentId > currentId){
						this.setProperty("myName", agentName + " kills ( " + currentAgentName + " )");
						this.getSimulator().realyKillAgent(currentAgent);
						//System.out.println("L'agent "+agentName+" a tué l'agent "+currentAgentName);
					}
				}
			}

			/* Move the agent to a next vertex with a chance of 1 out of 3 */
			int choice = rnd.nextInt() % 2;
			//System.out.println(this.getProperty("myName")+" a tiré : "+choice);

			if (choice == 0){
				this.move();
			}
			this.nextPulse();

		}
	}
}

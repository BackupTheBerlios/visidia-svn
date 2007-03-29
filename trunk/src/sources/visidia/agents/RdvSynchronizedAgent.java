package visidia.agents;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.SynchronizedAgent;

/**
 * Implements the rendez-vous algorithm 
 */
public class RdvSynchronizedAgent extends SynchronizedAgent {



	protected void init() {
		/* Memory of the agent that will be fusionned with the other agents */
		this.setProperty("myName", this.toString());

		/* Total number of agents on the graph */
		int nbAllAgent = this.getSimulator().getAllAgents().size();

		/* Agent move method */
		this.setAgentMover("RandomAgentMover");

		/* Random Class to know whether to move the agent or not
		 * (if not, the agent remains on its vertex) 
		 * The algorithm makes the hypothesis that an agent has the
		 * choice to remain on a vertex */
		Random rnd = new Random();



		while (nbAllAgent != 1) {

			/* Move the agent to a next vertex with a chance of 1 out of 3 */
			int choice = rnd.nextInt() % 2;
			System.out.println(this.getProperty("myName")+" a tiré : "+choice);

			this.nextPulse();
			if (choice == 0){
				this.move();
				this.nextPulse();

				/* See all agents on the arrival's vertex */
				Vertex vertexArrival = this.getSimulator().getVertexArrival(this);
				System.out.println(vertexArrival+" : "+this.getSimulator().getVertexIdentity(this)+ this.getProperty("myName"));
				Collection agentsOnVertex = vertexArrival.getAgentsNames();


				/* Make the union of the agents' memories if current agent has a better ID */
				if (! agentsOnVertex.isEmpty() ){
					System.out.println("Il y a plusieurs agents sur un sommet");
					int id = this.getIdentity();
					String myName = (String) this.getProperty("myName");
					Iterator it = agentsOnVertex.iterator();


					while (it.hasNext()){
						Agent currentAgent = (Agent) it.next();	
						int currentId = currentAgent.getIdentity();
						String currentName = (String) currentAgent.getProperty("myName");


						/* Save the agents'name and kill the agent */
						if (id > currentId){
							this.setProperty("myName", myName + currentName);
							this.getSimulator().killAgent(currentAgent);
						}
					}
				}
				this.nextPulse();
			}
			else{
				this.nextPulse();
				this.nextPulse();
			}
		}
	}


}


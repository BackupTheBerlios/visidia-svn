package visidia.agents;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;

public class Collecting_Information_Agent extends Agent {

 
 
	protected void init() {
		// TODO Auto-generated method stub
		
		this.setAgentMover("RandomAgentMover");
		
		while (this.getSimulator().getAllAgents().size()!=1) {
			
			this.move();
			
			Vertex vertex_D = this.getSimulator().getVertexDeparture(this);
			Vertex vertex_A = this.getSimulator().getVertexArrival(this);
			//System.out.println("je suis "+this+ "vertex_D :"+vertex_D);
			//System.out.println("je suis "+this+ "vertex_A :"+vertex_A);
			Collection <Agent> agentsCollection=vertex_D.getAgentsNames();
			agentsCollection =this.getSimulator().getAllAgents();
			if (agentsCollection!=null){
			    Iterator it=agentsCollection.iterator();
			    Agent agentTempo= (Agent) it.next();
		      //  System.out.println(vertex_A+" -->"+agentTempo.getSimulator().getVertexDeparture(this) );
		        //System.out.println(vertex_D+" -->"+agentTempo.getSimulator().getVertexArrival(this) );
			    if (vertex_D==agentTempo.getSimulator().getVertexDeparture(this) && 
			    		vertex_A==agentTempo.getSimulator().getVertexArrival(this)){
			    	if(agentTempo.canBeManipulated) this.eatingMemory(agentTempo);
			    }
			    //System.out.println("j'ai détecté un agent");}
			    while (it.hasNext()){
				   agentTempo=(Agent) it.next();
				   if (agentTempo!=this &&
						   vertex_D==agentTempo.getSimulator().getVertexDeparture(this) && 
						   vertex_A==agentTempo.getSimulator().getVertexArrival(this)) {
					//   System.out.println("j'ai détecté un agent");
					   this.eatingMemory(agentTempo);
				   }
			    }
			 
			}
			
			
			
		} 	
		
	}

	
	
	
	
	
	
	
	private void eatingMemory(Agent agentTempo) {
		System.out.println("je suis eating memory");
		agentTempo.canBeManipulated = false;
		if (this.canBeManipulated){
		Set keysSet = agentTempo.getWhiteBoard().keys();
		//comment faire si on a la meme ckef dans le deux hashtables???
		if (!keysSet.isEmpty()){
		Iterator it = keysSet.iterator();
		String key = (String)it.next();
			while (it.hasNext()){
				this.setProperty(key, agentTempo.getProperty(key));
				key=(String)it.next();
			}
			agentTempo.assasinateAgent();
		}
		}
	}
}

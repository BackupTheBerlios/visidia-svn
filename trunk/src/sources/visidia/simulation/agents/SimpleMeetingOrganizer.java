package visidia.simulation.agents;

import visidia.agents.*;

import java.util.Hashtable;
import java.util.Iterator;

import java.util.Collection;


import java.util.Vector;

public class SimpleMeetingOrganizer implements MeetingOrganizer {
  
 /**
      * agentsForVertexId is implemented to organize meeting of agents.
      * @return A list of on where two or more agents are meeting
      * @deprecated ...
      **/

    // This methode implemet the way used to meet others, in a first time
    // It doesn't change the agents list.
    public SimpleMeetingOrganizer(){
	System.out.println("Hello, this is the organizer start...");
    }


    public void howToMeetTogether(Collection agentsOnVertex){
	Iterator it = agentsOnVertex.iterator();
	while(it.hasNext()) {
	    Agent agent = (SynchronizedAgent)it.next();
	    
	    if( agent.meet == true ){
		System.out.println("Debut du planing de l'agent "+agent.toString());
		if(agentsOnVertex.size() > 1){
		    whatToDoIfMeeted(agentsOnVertex, agent);
		}
	    }
	}
	// 	System.out.println("Start of meeting methods ");
	// 	if(agentsOnVertex.size() > 1){
	// 	    whatToDoIfMeeted(agentsOnVertex);
	// 	}
    }
    
    
    public void whatToDoIfMeeted(Collection meetedAgents, Agent agentManager){
	//	if(meetedAgents.size())
	Iterator it = meetedAgents.iterator();
	
	while(it.hasNext()) {
	    Agent agent = (SynchronizedAgent)it.next();
	    
	    //	    System.out.println("DEBUG : Debut de whatToDo...");
	    if( agent.meet == true ){
		if( agent != agentManager){
		System.out.println("Debut du planing de l'agent "+agent.toString());
		agentManager.planning(agent);
		}
	    }
	}
	
    }
}

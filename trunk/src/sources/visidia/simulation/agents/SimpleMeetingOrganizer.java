package visidia.simulation.agents;

import visidia.agents.*;

import java.util.Hashtable;
import java.util.Iterator;

import java.util.Collection;


import java.util.Vector;

public class SimpleMeetingOrganizer implements MeetingOrganizer {
  
    /* With this variable we can enable or not the meeting 
     */
    private  boolean enable = false;
    
    public SimpleMeetingOrganizer(){
	enable = true;
    }


    /* Implement the strategie of meeting between network agents
     * @param netAgents : collection of network agents (or the set 
     * of agents conserned by the meeting)
     * @see whatToDoIfMeeted
     */
    public void howToMeetTogether(Collection netAgents){
	if(enable == false) return;
	Iterator it = netAgents.iterator();
	
	while(it.hasNext()) {
            Agent agent = (Agent) it.next();
            if (agent instanceof SynchronizedAgent && ((SynchronizedAgent)agent).meet == true
                && netAgents.size() > 1)
                whatToDoIfMeeted(netAgents, (SynchronizedAgent)agent);
	}
    }
    
    /* Describe the work done during the meeting, execute agents planning on the network.
     * @param meetedAgents : collection of synchronizedAgents who participate to the meeting.
     * @param agentManager : The agent who manage the meeting
     * @see visidia.simulation.agents.SynchronizedAgent.planningn
     */
    public void whatToDoIfMeeted(Collection meetedAgents, SynchronizedAgent agentManager){
	if( enable == false ) return;
	Iterator it = meetedAgents.iterator();
	
	while(it.hasNext()) {
            Agent agent = (Agent)it.next();
            if (agent instanceof SynchronizedAgent && ((SynchronizedAgent)agent).meet == true
                && agent != agentManager)
		agentManager.planning((SynchronizedAgent)agent);
	}
    }
}

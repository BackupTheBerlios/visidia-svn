package visidia.simulation.agents;

import visidia.agents.*;

import java.util.Hashtable;
import java.util.Iterator;

import java.util.Collection;


import java.util.Vector;

/**
 * Example of meeting organizer. Allows a simple communication between
 * agents when they meet.  Each  agent atempts to execute its planning
 * with all the agents present on the vertex.
 **/
public class SimpleMeetingOrganizer implements MeetingOrganizer {
  
    /* With this variable we can enable or not the meeting 
     */
    private  boolean enable = false;
    
    public SimpleMeetingOrganizer(){
	enable = true;
    }


    /** Implements the strategy of meeting between network agents.
     *
     * @param netAgents : collection of network agents (or the set 
     * of agents conserned by the meeting)
     * @see #whatToDoIfMeeted(Collection, SynchronizedAgent)
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
    
    /** Describes the  work done  during the meeting,  executes agents
     * planning on the network.
     *
     * @param  meetedAgents :  collection of  synchronizedAgents which
     * take part in the meeting.
     * @param agentManager : The agent which manages the meeting
     * @see visidia.simulation.agents.SynchronizedAgent.#planning(SynchronizedAgent)
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

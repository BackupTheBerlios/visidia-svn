package visidia.simulation.agents;

import java.util.Collection;

/**
 * Class  in  charge  of  the  organization of  meeting.   Allows  the
 * communication between  agents when they meet  each other. Describes
 * how to organize agents meeting.
 **/
public interface MeetingOrganizer {

    /** Implements the strategy of meeting between network agents
     *
     * @param netAgents : collection of network agents (or the set 
     * of agents conserned by the meeting)
     * @see #whatToDoIfMeeted(Collection, SynchronizedAgent)
     */
    public void howToMeetTogether(Collection netAgents);


    /* Describes  the work  done during  the meeting,  executes agents
     * planning on the network.
     *
     * @param  meetedAgents :  collection of  synchronizedAgents which
     * take part in the meeting.
     * @param agentManager : The agent which manages the meeting
     * @see visidia.simulation.agents.SynchronizedAgent#planning(SynchronizedAgent)
     */
    public void whatToDoIfMeeted(Collection meetedAgents, SynchronizedAgent agentManager);

}

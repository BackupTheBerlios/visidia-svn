package visidia.simulation.agents;

import java.util.Collection;


public interface MeetingOrganizer {

  /* Implement the strategie of meeting between network agents
     * @param netAgents : collection of network agents (or the set 
     * of agents conserned by the meeting)
     * @see whatToDoIfMeeted
     */
    public void howToMeetTogether(Collection netAgents);


    /* Describe the work done during the meeting, execute agents planning on the network.
     * @param meetedAgents : collection of synchronizedAgents who participate to the meeting.
     * @param agentManager : The agent who manage the meeting
     * @see visidia.simulation.agents.SynchronizedAgent.planning
     */
    public void whatToDoIfMeeted(Collection meetedAgents, SynchronizedAgent agentManager);

}

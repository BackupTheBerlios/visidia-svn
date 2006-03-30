package visidia.simulation.agents;

import java.util.Collection;


public interface MeetingOrganizer {
    /** 
     ** @descr 
     ** @see whatToDoIfMeeted()
     **/
    public void howToMeetTogether(Collection agentsTable);

    public void whatToDoIfMeeted(Collection agentsTable, Agent agentManager);

}

package visidia.agents;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.SynchronizedAgent;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.SimpleMeetingOrganizer;
/**
 * I'm one of the synchronized agents.  I was developed to show how to
 * write agents that are waiting each other.  <p>
 *
 * I wait 1 second between each move and I move 10 times before dying.
 */
public class BasicSynchronizedAgentMeet extends SynchronizedAgent {
    static int j = 0;
    int i = 0;
    //    SimpleMeetingOrganizer meetOrg = new SimpleMeetingOrganizer(); 
    static {
	j++;
    }
    
    public void planning(Agent agent){
	System.out.println(this.toString()+" : Vous etes le monsieur "+(i++)+" que je rencontre today."+ agent.toString());
    }

    protected void init() {
	
        setAgentMover("RandomAgentMover");
	
        for(int i=0; i<10; ++i) {
	    sleep(1000);
	    
            /**
             * nextPulse() is  the method to use when  you finish your
             * work. It waits for  the other synchronized agents to do
             * the  same nextPulse(). When  all are  done, nextPulse()
             * returns and the next action is executed.
             */
	    
            nextPulse();
	    //	    meetOrg.howToMeetTogether(this.agentsOnVertex());
            move();
	}

    }

}

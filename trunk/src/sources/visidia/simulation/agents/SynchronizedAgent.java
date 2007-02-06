package visidia.simulation.agents;

import visidia.simulation.SimulationAbortError;
import java.util.Hashtable;

/**
 *  Extend this class to implement Synchronized Agents
 *
 * @see Agent 
 */

public abstract class SynchronizedAgent extends Agent {           

    /**
     * 
     */

    // J'aime vraiment pas ces static dans cette classe, mais bon ca
    // fonctionne quand même
    
    private static int nbAgents = 0;
    private static int count = 0;
    private static int pulseNumber = 0;
    protected SimpleMeetingOrganizer meetOrg = new SimpleMeetingOrganizer(); 
    protected boolean meet = false;
    protected Hashtable<Integer, String> meetedAgentsnames;
    protected static Integer meetingnum = 0;

    /**
     * used for synchronizing the agents
     **/    
    private static Object synchronisation = new Object();
    
    /**
     * Creates  a new  synchronized  agent. The  variable nbAgents  is
     * incremented  so  that  the  synchronisation is  handled.  Every
     * living synchronized agent is counted. 
     */
    public SynchronizedAgent() {
        super();
	this.meet = true;
	this.meetedAgentsnames = new Hashtable<Integer,String>();
        ++nbAgents;
    }

    /**
     * Clears nbAgents and count  to restart from the begining. Called
     * when the simultion is finished or aborted.
     */
    public static void clear() {
        nbAgents = 0;
        count = 0;
	pulseNumber = 0;
    }
    /**
     * Call  this   method  when  you   want  synchronisation  between
     * agents. Every  synchronized agent will wait until  the last has
     * finished.
     * The meeting is organized if the agent accept this and the number
     * of agents is at less 2.
     */
    public void nextPulse() {
	
	synchronized( synchronisation ) {
	    
	    ++count;

	    // la fonction howToMeetTogether est buggué. à modifier
	    // acun risque de plantage si la fonction planning n'est
	    // pas utlisé par l'utilisateur final
	    if( (this.meet == true) && (this.agentsOnVertex().size() > 1) )
		this.meetOrg.howToMeetTogether(this.agentsOnVertex());
		
	    if( count < nbAgents ) {
		try {
		    synchronisation.wait();
		} catch(InterruptedException e) {
                    throw new SimulationAbortError(e);
		}
		
		return;
	    }

	
	    /* Reached by the last thread calling nextPulse */
	    this.unblockAgents();
	    
     	}
    }

    protected void planning(SynchronizedAgent agent){
	if( this.meet == true )
	    this.meetedAgentsnames.put(new Integer(meetingnum+1),agent.toString());
    }


    protected void unblockAgents() {
	super.newPulse(++pulseNumber);
	count = 0;
	synchronisation.notifyAll();
    }

    /**
     * Handles the death of the synchronized agents.
     */
    protected void death() {
	
	super.death();
	
	synchronized( synchronisation ) {
	    
	    --nbAgents;
	    
	    /* I have to check if the other agents 
	       are not waiting for me */
	    if( count == nbAgents )
		this.unblockAgents();

	}

    }

}

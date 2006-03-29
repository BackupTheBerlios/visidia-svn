package visidia.simulation.agents;

import visidia.simulation.SimulationAbortError;

/**
 *  Extend this class to implement Synchronized Agents
 *
 * @see Agent 
 */

public abstract class SynchronizedAgent extends Agent {           

    /**
     * 
     */
    private static int nbAgents = 0;
    private static int count = 0;
    private static Boolean synchronisation = new Boolean(true);

    /**
     * Creates  a new  synchronized  agent. The  variable nbAgents  is
     * incremented  so  that  the  synchronisation is  handled.  Every
     * living synchronized agent is counted. 
     */
    public SynchronizedAgent() {
        super();
        ++nbAgents;
    }

    /**
     * Clears nbAgents and count  to restart from the begining. Called
     * when the simultion is finished or aborted.
     */
    public static void clear() {
        nbAgents = 0;
        count = 0;
    }
    /**
     * Call  this   method  when  you   want  synchronisation  between
     * agents. Every  synchronized agent will wait until  the last has
     * finished.
     */
    public void nextPulse() {
     
	synchronized( synchronisation ) {
	    ++count;

	    if( count < nbAgents ) {
		try {
		    synchronisation.wait();
		} catch(InterruptedException e) {
                    throw new SimulationAbortError(e);
		}
		
		return;
	    }
	    
	    /* Reached by the last thread calling nextPulse */
	    unblockAgents();
     	}
    }

    public void unblockAgents() {
	    incrementStat("Pulse");
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
	    if( count == nbAgents ) {
		count = 0;
		synchronisation.notifyAll();
	    }

	}

    }

}

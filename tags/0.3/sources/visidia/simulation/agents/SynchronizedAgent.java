/**
 *  Extend this class to implement Synchronized Agent
 * 
 */

package visidia.simulation.agents;

import visidia.simulation.SimulationAbortError;

public abstract class SynchronizedAgent extends Agent {                     

    private static int nbAgents = 0;
    private static int count = 0;
    private static Boolean synchronisation = new Boolean(true);

    public SynchronizedAgent() {
        super();
        ++nbAgents;
    }

    public static void clear() {
        nbAgents = 0;
        count = 0;
    }

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
	    count = 0;
	    synchronisation.notifyAll();

     	}

    }

    protected void death() {

	super.death();

	synchronized( synchronisation ) {

	    --nbAgents;

	    /* I have to check if the others agents 
	       are not waiting for me */
	    if( count == nbAgents ) {
		count = 0;
		synchronisation.notifyAll();
	    }

	}

    }

}

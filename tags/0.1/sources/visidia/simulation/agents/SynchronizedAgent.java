/**
 *  Extend this class to implement Synchronized Agent
 * 
 */

package visidia.simulation.agents;

import java.util.Hashtable;

public abstract class SynchronizedAgent extends Agent {                     

    private static int nbAgents = 0;
    private static int count = 0;
    private static Boolean synchronisation = new Boolean(true);

    public SynchronizedAgent(AgentSimulator sim) {
        this(sim, new Hashtable());
    }

    public SynchronizedAgent(AgentSimulator sim, Hashtable hash) {
        super(sim, hash);
        ++nbAgents;
    }


    public void nextPulse() {
     
	synchronized( synchronisation ) {
	    ++count;

	    if( count < nbAgents ) {
		try {
		    synchronisation.wait();
		} catch(InterruptedException e) {
		    System.out.println("Synchronisation problem : " + e);
		    System.exit(1);
		}
		
		return;
	    }

	
	    /* Reached by the last thread calling nextPulse */
	    System.out.println(" ------------- TOP ----------------- ");
	    count = 0;
	    synchronisation.notifyAll();

     	}

    }


    public void death() {

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

	System.out.println("Mort de l'agent synchronise " + getIdentity());

    }

}



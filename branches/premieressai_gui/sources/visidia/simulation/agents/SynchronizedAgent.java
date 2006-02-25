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

    public void moveToDoor(int door) {
     
	synchronized( synchronisation ) {
	    ++count;

	    if( count < nbAgents ) {
		try {
		    // le wait libère la zone sensible
		    synchronisation.wait();
		} catch(InterruptedException e) {
		    System.out.println("Synchronisation problem : " + e);
		    System.exit(1);
		}
		
		super.moveToDoor(door);
		return;
	    }

            count = 0;
            synchronisation.notifyAll();	

            /* now we can all move */
            super.moveToDoor(door);
	}
    }
}



/**
 *  Extend this class to implement Synchronized Agent
 * 
 */

package visidia.simulation;

import java.util.Hashtable;

public abstract class SynchronizedAgent extends Agent {                     

    private static int nbAgents = 0;
    private static int count = 0;
    private static Boolean nextTop = new Boolean(false);

    public SynchronizedAgent(Simulator sim) {
        this(sim, new Hashtable());
    }

    public SynchronizedAgent(Simulator sim, Hashtable hash) {
        super(sim, hash);
        ++nbAgents;
    }

    public void moveToDoor(int door) {
     
	synchronized( nextTop ) {
	    ++count;

	    if( count < nbAgents ) {
		try {
		    // le wait libère la zone sensible
		    nextTop.wait();
		} catch(InterruptedException e) {
		    System.out.println("Synchronisation problem : " + e);
		    System.exit(1);
		}
		
		super.moveToDoor(door);
		return;
	    }

            count = 0;
            nextTop.notifyAll();	

            /* now we can all move */
            super.moveToDoor(door);
	}
    }
}



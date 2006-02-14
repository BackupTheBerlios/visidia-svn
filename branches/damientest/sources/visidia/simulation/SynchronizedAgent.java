/**
 *  Extend this class to implement Synchronized Agent
 * 
 */

package visidia.simulation;

import java.util.Hashtable;

public abstract class SynchronizedAgent extends Agent {                     

    private static int nbAgents = 0;
    private static Integer count = new Integer(0);

    public SynchronizedAgent(Simulator sim) {
	super(sim);
	++nbAgents;
    }

    public SynchronizedAgent(Simulator sim, Hashtable hash) {
	this(sim);
    }


    public void moveToDoor(int door) {
     
	synchronized( count ) {
	    count = new Integer(count.intValue() + 1);

	    if( count.intValue() < nbAgents ) {
		try {
		    // le wait libère la zone sensible
		    count.wait();
		} catch(InterruptedException e) {
		    System.out.println("Synchronisation problem : " + e);
		    System.exit(1);
		}
		
		super.moveToDoor(door);
		return;
	    }
	}

	notifyAll();	

	/* now we can all move */
	super.moveToDoor(door);
	
	count = new Integer(0);
    }

    

}



/**
 *  Extend this class to implement Synchronized Agent
 * 
 */

package visidia.simulation.agents;

public abstract class SynchronizedAgent extends Agent {                     

    private static int nbAgents = 0;
    private static int count = 0;
    private static Boolean synchronisation = new Boolean(true);

    public SynchronizedAgent() {
        super();
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



package visidia.simulation.agents;

import visidia.simulation.SimulAck;
import visidia.tools.VQueue;

/**
 * Class  used  to monitor  moving  of agents.   To  be  used with  an
 * acknowledgment queue.  Send waitForAnswer()  when you want  to wait
 * for the arrival of your message in the GUI.
 */

public class MovingMonitor implements Runnable {

    /**
     * This queue contains the GUI acknowledgments.
     */
    private VQueue ackQ;

    /**
     * Object catched in the queue.
     */
    private SimulAck fromQueue;


    private boolean aborted = false;
    /**
     * Used to synchronise threads on the queue.
     */
    private Object synchronisation = new Object();


    /**
     * Creates a  new MovingMonitor with  the simulator acknowledgment
     * queue.
     */
    public MovingMonitor(VQueue ackQ) {
        this.ackQ = ackQ;
    }

    /**
     * Used by the Runnable interface to start the thread.
     */
    public final void run() {

        fromQueue = null;

        while (!aborted) {
            synchronized ( synchronisation ) {

                try {
                    // Wait  until  somebody   get  the  element  just
                    // grabbed from the queue.
                    while (fromQueue != null)
                        synchronisation.wait();

                    // Someone took the object, grab another one.
                    fromQueue = (SimulAck) ackQ.get();
                    synchronisation.notifyAll();

                } catch (InterruptedException e) {
		    // e.printStackTrace();
                    // No pb here, simulator has stoped me.
                    return;
                }
            }
        }
    }

    public void abortAck() {
	aborted = true;
	try {
	    ackQ.notifyAllGet();
	} catch (Exception e) {
	}
	
    }
    
    /**
     * Waits for your message to arrive to the GUI.
     * 
     * @param key The number of the message in the queue.
     */
    // public SimulAck waitForAnswer(Long key) throws InterruptedException {
    public void waitForAnswer(Long key) throws InterruptedException {
        synchronized( synchronisation ) {

            // Wait until the answer is for me.
            while((fromQueue == null) || ! fromQueue.number().equals(key))
                synchronisation.wait();
            
            // The   object   grab    from   the   queue   is   my
            // acknowledgment. I need to return now.
            // SimulAck forMe = fromQueue;
            
            fromQueue = null;
            synchronisation.notifyAll();

            // return forMe;
	    
        }

    }

}

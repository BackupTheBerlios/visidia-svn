package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;

/**
 * I'm one of the synchronized agents.  I was developed to show how to
 * write agents that are waiting each other.  <p>
 *
 * I'm waiting 1  second between each move and I  move 10 times before
 * dying.
 */
public class BasicSynchronizedAgent1 extends SynchronizedAgent {

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
            move();
	}

    }

}

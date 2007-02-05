package visidia.agents;

import visidia.simulation.agents.SynchronizedAgent;


import java.util.Random;

/**
 *
 */
public class Akka extends SynchronizedAgent {

    protected void init() {
	Random rand = new Random();
	while(true) {
	    int i = rand.nextInt(getArity()+1);
	    if( i < getArity())
		moveToDoor(i);
	    
	    nextPulse();
	
	    lockVertexProperties();
	    Integer tmp = (Integer) getVertexProperty("nb");
	    tmp ++;
	    
	    setVertexProperty("nb",tmp);
	    unlockVertexProperties();
	    
	    nextPulse();
	    
	}
    }
}


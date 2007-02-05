package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Arrays;
import java.util.Random;


public class Cop extends Agent {

    static boolean thiefCaptured = false;
    
    protected void init() {
	Random randMove = new Random();
	
	while ( ! thiefCaptured ) {
	    int degree = getArity();
	    int randomDirection = Math.abs(randMove.nextInt(degree));
		
	    moveToDoor(randomDirection);
	    
	    lockVertexProperties();
	    if(((String)getVertexProperty("label")).equals("S")) {
		thiefCaptured = true;
		setVertexProperty("label", new String("C"));
	    }
	    unlockVertexProperties();
	}
    }
}

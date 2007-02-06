package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Random;


public class Cop extends Agent {

    static boolean thiefCaptured = false;
    
    protected void init() {
	Random randMove = new Random();
	
	while ( ! thiefCaptured ) {
	    int degree = this.getArity();
	    int randomDirection = Math.abs(randMove.nextInt(degree));
		
	    this.moveToDoor(randomDirection);
	    
	    this.lockVertexProperties();
	    if(((String)this.getVertexProperty("label")).equals("S")) {
		thiefCaptured = true;
		this.setVertexProperty("label", new String("C"));
	    }
	    this.unlockVertexProperties();
	}
    }
}

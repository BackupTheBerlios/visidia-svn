package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Random;


public class Thief extends Agent {

    protected void init() {
	Random randHide = new Random();
	Random randMove = new Random();
	
	boolean captured = false;
	
	while ( ! captured ) {
	    int degree = this.getArity();
	    int randomDirection = Math.abs(randMove.nextInt(degree));
	    this.moveToDoor(randomDirection);
	    
	    String oldLabel = (String)this.getVertexProperty("label");
	    
	    this.setVertexProperty("label", new String("H"));
	    try{
		Thread.sleep((Math.abs(randHide.nextInt(5))+1)*500);
	    } catch (Exception e) {
	    }
	    
	    if(oldLabel.equals("N") ) {
		this.setVertexProperty("label", new String("S"));
		try{
		    Thread.sleep(3000);
		} catch (Exception e) {
		}
		
		this.lockVertexProperties();
		if(((String)this.getVertexProperty("label")).equals("C"))
		    captured = true;
		else 
		    this.setVertexProperty("label", new String("D"));
		this.unlockVertexProperties();
	    } else {
		this.setVertexProperty("label", new String("D"));
	    }
	}
    }
}

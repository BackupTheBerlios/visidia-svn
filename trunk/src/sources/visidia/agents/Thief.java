package visidia.agents;

import visidia.simulation.agents.Agent;
import java.util.Random;


public class Thief extends Agent {

    protected void init() {
	Random randHide = new Random();
	Random randMove = new Random();
	
	boolean captured = false;
	
	while ( ! captured ) {
	    int degree = getArity();
	    int randomDirection = Math.abs(randMove.nextInt(degree));
	    moveToDoor(randomDirection);
	    
	    String oldLabel = (String)getVertexProperty("label");
	    
	    setVertexProperty("label", new String("H"));
	    try{
		Thread.sleep((Math.abs(randHide.nextInt(5))+1)*500);
	    } catch (Exception e) {
	    }
	    
	    if(oldLabel.equals("N") ) {
		setVertexProperty("label", new String("S"));
		try{
		    Thread.sleep(3000);
		} catch (Exception e) {
		}
		
		lockVertexProperties();
		if(((String)getVertexProperty("label")).equals("C"))
		    captured = true;
		else 
		    setVertexProperty("label", new String("D"));
		unlockVertexProperties();
	    } else {
		setVertexProperty("label", new String("D"));
	    }
	}
    }
}

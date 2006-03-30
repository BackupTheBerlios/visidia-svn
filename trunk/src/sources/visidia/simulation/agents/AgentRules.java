package visidia.simulation.agents;

public class AgentRules extends AbstractAgentsRules {

    public void init(){

	setAgentMover("RandomAgentMover");
	
	while (true) {
	    int v = getVertexIdentity();
	    if (write(v)) {
		setVertexProperty("wb",new Boolean(true));
		move();
		v = getVertexIdentity();
		if(write(v)) {
		    //il faut d'apres xav attrapper l'exception
		    if(!(Boolean)getVertexProperty("wb")) {
			System.out.println("Handshake succes");
		    }
		}
		moveBack();
		setVertexProperty("wb",new Boolean(false));
	    }
	    else {
	      
	    }
	    
	}
    }
   
}


package visidia.agents.agentchooser;

import visidia.simulation.agents.AgentChooser;

public class ThousandOnOne extends AgentChooser {

    protected String agentName() {
	return "StupidIncrement";
    }

    protected void chooseForVertex(Integer vertexIdentity) {
	
	if(vertexIdentity.intValue() == 0) {
	    for(int i=0; i < 1000; ++i)
		addAgent(vertexIdentity,agentName());
	}
	
    }

}

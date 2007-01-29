package visidia.agents.agentchooser;

import visidia.simulation.agents.AgentChooser;

/**
 * Place 1000 agents  on the first vertex of the  graph. They will all
 * try  to increment  a field  in the  vertex whiteboard.   If locking
 * system works properly, you will  find 1000 on the whiteboard 'test'
 * field at the end.
 *
 * @see visidia.agents.StupidIncrement
 */
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

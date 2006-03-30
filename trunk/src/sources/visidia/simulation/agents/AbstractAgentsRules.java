package visidia.simulation.agents; 

import java.util.*;

import visidia.graph.Vertex;
import visidia.rule.RelabelingSystem;
    
public abstract class AbstractAgentsRules extends SynchronizedAgent {
    
    private RelabelingSystem rSys = null;
    private static Hashtable<Integer, Agent> writeRights;

    public void setRule(RelabelingSystem rSys) {
	this.rSys = rSys;
    }
    
    protected boolean write(int vertex) {
        if (writeRights == null)
            updateWriteRights();

        Agent authorizedAgent = (Agent) writeRights.get(new Integer(vertex));

        if (authorizedAgent == null) {
            writeRights.put(new Integer(vertex), this);
            authorizedAgent = this;
        }

        return authorizedAgent == this;
    }

    private void updateWriteRights() {
	Hashtable<Vertex, Collection> positions;
	positions = getAgentPositions();

	Set<Vertex> vertices = positions.keySet();

	writeRights = new Hashtable(positions.size());

	for(Vertex vertex : vertices) {
	    Collection agents = positions.get(vertex);
	    Vector agentsVector = new Vector(agents);
	    Random rand = new Random();
            int size = agents.size();
            Integer vertexId = vertex.identity();
            int randomPosition = rand.nextInt(size);
            Agent randomAgent = (Agent)agentsVector.get(randomPosition);

	    writeRights.put(vertexId, randomAgent);
	}
    }

    protected void unblockAgents() {
        updateWriteRights();
        super.unblockAgents();
    }

    protected RelabelingSystem getRelabelling() {
        return rSys;
    }
}

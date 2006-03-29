package visidia.simulation.agents; 

import java.util.*;

import visidia.graph.Vertex;
import visidia.rule.RelabelingSystem;
    
public abstract class AbstractAgentsRules extends SynchronizedAgent {
    
    RelabelingSystem rSys = null;
    static Hashtable<Integer, Agent> writeRights;

    public void setRule(RelabelingSystem rSys) {
	this.rSys = rSys;
    }
    
    protected boolean write(int vertex) {
	return writeRights.get(new Integer(vertex)) == this;
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

	    writeRights.put(vertex.identity(), 
			    (Agent)(agentsVector.get(rand.nextInt(agents.size()))));
	}
    }

    protected boolean wb() {
	
    }
}

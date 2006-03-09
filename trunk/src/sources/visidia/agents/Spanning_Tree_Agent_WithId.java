package visidia.agents;

import java.util.Hashtable;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;


/**
 * Implements a spanning tree algorithm with an agent. This agent uses
 * the unique identification for each vertex.
 */
public class Spanning_Tree_Agent_WithId extends Agent {

    boolean[] vertexMarks;

    public Spanning_Tree_Agent_WithId (AgentSimulator sim, 
                                       Hashtable defaultValues) {
        super(sim, defaultValues);
    }

    protected void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setAgentMover("RandomAgentMover");
        vertexMarks = new boolean [nbVertices];

        while ( nbSelectedEdges < (nbVertices - 1) )
            {
                if ( ! isMarked(getVertexIdentity()) ) {
                    //setDoorState(new MarkedState(true), entryDoor());
                    mark(getVertexIdentity());
                    nbVertices ++;
                }
                move();
            }
    }

    private void mark (int vertex) {
        vertexMarks[vertex] = true;
    }

    private boolean isMarked(int vertex) {
        return vertexMarks[vertex];
    }

}

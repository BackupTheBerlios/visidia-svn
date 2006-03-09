package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * Implements  a spanning  tree  algorith with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setAgentMover("LinearAgentMover");

        while ( nbSelectedEdges < nbVertices - 1 )
            {
                if ( ! vertexIsMarked() ) {
                    markVertex();
                    nbSelectedEdges ++;
                }
                
                move();
            }
    }

    private void markVertex () {
        setVertexProperty("marked", new Boolean(true));
    }

    private boolean vertexIsMarked() {
        return ((Boolean)getVertexProperty("marked")).booleanValue();
    }
}

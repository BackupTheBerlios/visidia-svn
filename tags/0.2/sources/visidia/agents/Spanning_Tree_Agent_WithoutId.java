package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Implements  a spanning  tree  algorith with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setAgentMover("RandomAgentMover");

        markVertex();

        while ( nbSelectedEdges < nbVertices - 1 ) {
            move();

            if ( ! vertexIsMarked() ) {
                markEntryDoor();
                markVertex();
                nbSelectedEdges ++;
            }
        }
    }

    private void markVertex () {
        setVertexProperty("marked", new Boolean(true));
    }

    private boolean vertexIsMarked() {
        boolean mark;

        // If the vertex is not already marked, an exception is thrown
        // by the WhiteBoard.
        try {
            mark = ((Boolean)getVertexProperty("marked")).booleanValue();
        } catch (NoSuchElementException e) {
            mark = false;
        }

        return mark;
    }
}

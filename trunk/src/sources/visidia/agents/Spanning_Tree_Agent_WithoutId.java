package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {


    /**
     * Have a look at Spanning_Tree_Agent_WithId#init() for comments.
     */ 
    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setAgentMover("RandomAgentMover");

        mark();

        while ( nbSelectedEdges < nbVertices - 1 ) {

            move();

            if ( ! isMarked() ) {
                markDoor(entryDoor());
                mark();
                nbSelectedEdges ++;
            }
            else {
                incrementStat("Failed moves");
            }
        }
    }

    private void mark () {
        setVertexProperty("marked", new Boolean(true));
    }

    private boolean isMarked() {
        boolean mark;

        /**
         * If the vertex is not already marked, an exception is thrown
         * by the WhiteBoard.
         */
        try {
            mark = ((Boolean)getVertexProperty("marked")).booleanValue();
        } catch (NoSuchElementException e) {
            mark = false;
        }

        return mark;
    }
}

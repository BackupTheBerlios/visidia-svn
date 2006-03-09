package visidia.agents;

import visidia.simulation.agents.Agent;

/**
 * Implemente  un algorithme  d'arbre recouvrant  a l'aide  d'un agent
 * mobile.  L'algorithme  fonctionne sur  des  sommets  qui n'ont  pas
 * d'identifiants.
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setAgentMover("LinearAgentMover");

        while ( nbSelectedEdges < nbVertices - 1 )
            {
                if ( ! vertexIsMarked() ) {
                    //setDoorState(new MarkedState(true), entryDoor());
                    markVertex();
                    nbVertices ++;
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

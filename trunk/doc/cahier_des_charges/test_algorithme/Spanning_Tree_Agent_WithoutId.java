/**
 * Implemente  un algorithme  d'arbre recouvrant  a l'aide  d'un agent
 * mobile.  L'algorithme  fonctionne sur  des  sommets  qui n'ont  pas
 * d'identifiants.
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();
        AgentMover mover = new LinearAgentMover(this);

        vertexMarks = new boolean [nbVertices];

        while ( nbSelectedEdges < nbVertices - 1 )
            {
                if ( ! isMarked(curVertex()) ) {
                    setDoorState(new MarkedState(true), entryDoor());
                    mark(curVertex());
                    nbVertices ++;
                }
                
                mover.step();
            }
    }

    private void mark (int vertex) {
        putVertexProperty("marked", new Boolean(true));
    }

    private boolean isMarked(int vertex) {
        return getVertexProperty("marked").booleanValue();
    }

}

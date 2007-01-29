/**
 * Implemente  un algorithme  d'arbre recouvrant  a l'aide  d'un agent
 * mobile.  L'algorithme  fonctionne sur  des  sommets  qui n'ont  pas
 * d'identifiants.
 */
public class Spanning_Tree_Agent_WithoutId extends Agent {

    public Spanning_Tree_Agent_WithoutId (Simulator sim) {
        super(sim);
    }

    public void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setMover("LinearAgentMover");

        while ( nbSelectedEdges < nbVertices - 1 )
            {
                if ( ! vertexIsMarked() ) {
                    setDoorState(new MarkedState(true), entryDoor());
                    markVertex();
                    nbVertices ++;
                }
                
                move();
            }
    }

    private void markVertex () {
        putVertexProperty("marked", new Boolean(true));
    }

    private boolean vertexIsMarked() {
        return ((Boolean)getVertexProperty("marked")).booleanValue();
    }
}

/**
 * Implemente  un algorithme  d'arbre recouvrant  a l'aide  d'un agent
 * mobile. L'algorithme tient  compte d'une identification unique pour
 * chaque sommet.
 */
public class Spanning_Tree_Agent_WithId extends Agent {

    boolean[] vertexMarks;

    public Spanning_Tree_Agent_WithId (Simulator sim) {
        super(sim);
    }

    protected void init() {
        
        int nbSelectedEdges = 0;
        int nbVertices = getNetSize();

        setMover("LinearAgentMover");
        vertexMarks = new boolean [nbVertices];

        while ( nbSelectedEdges < nbVertices - 1 )
            {
                if ( ! isMarked(getVertexIdentity()) ) {
                    setDoorState(new MarkedState(true), entryDoor());
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

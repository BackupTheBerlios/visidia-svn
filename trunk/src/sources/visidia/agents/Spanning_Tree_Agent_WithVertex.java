package visidia.agents;


import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.FailedMoveStat;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent_WithVertex extends Agent {
	int counter = 0, visited = 1;
    int nbSelectedEdges;
    int nbVertices;
  
    /**
     * Have a look at Spanning_Tree_Agent_WithId#init() for comments.
     */ 
    private void initWBVars() {
    	
        //Initialisation des variables qu'on veut afficher dans le white board.
        this.setProperty("nbVertices",Integer.valueOf(this.getNetSize()));
        this.setProperty("nbSelectedEdges", new Integer(0));
        this.setProperty("portFrom", new Integer(-1));
        
    }
    
    public void init() {
    	
    	this.initWBVars();
        
        this.setAgentMover("RandomAgentMover");
        
        this.setAsRoot(this.getIdentity());
        
        while ( (Integer)this.getProperty("nbSelectedEdges") < (Integer)this.getProperty("nbVertices") - 1 ) {

        	
            /*System.out.println("Test1 : ");
            if((Integer)this.getProperty("nbSelectedEdges") > 0) System.out.println(this.entryDoor());
            else System.out.println("-");*/
            
            // Movement
            this.move();

            System.out.println("Entri dor : ");
            System.out.println(this.entryDoor());

            System.out.println("Id : ");
            System.out.println(this.getVertexIdentity());

            this.setProperty("portFrom", this.entryDoor());
    
 
            //Vertex vertex_A = this.getSimulator().getVertexArrival(this);


			if (!this.isMemberOfTheTree(this.getIdentity())) {
			
				this.setParent((Integer)this.getProperty("portFrom"),this.getIdentity());
				
				// Coloration graphique
				this.markDoor(this.entryDoor());
			
				this.setProperty("nbSelectedEdges", (Integer)this.getProperty("nbSelectedEdges") +1);
			
			}

		}

	}
            
       
    

    private void setParent(Integer p, Integer idTree) {
		this.setVertexProperty("IdOfTheTree",idTree);
		this.setVertexProperty("ParentPort",new Integer(p));
    }
    
    private void setAsRoot(Integer idTree) {
		this.setVertexProperty("IdOfTheTree",idTree);
        this.setVertexProperty("ParentPort",new Integer(-1));
       
    }
    
 
    private int getParent() {
        int parent;

        try {
        	parent = ((Integer)this.getVertexProperty("ParentPort")).intValue();
        } catch (NoSuchElementException e) {
        	parent = -2;
        }

        return parent;
    }
    
    
    private boolean isMemberOfTheTree(Integer idTree) {
    	//return idTree.equals(this.getIdOfTheTree()) && this.getParent() != -2;
    	return idTree.equals(this.getIdOfTheTree());
    }
    
    private boolean isRootOfTheTree(Integer idTree) {
    	return idTree.equals(this.getIdOfTheTree()) && this.getParent() == -1;
    }
   
    private int getIdOfTheTree() {
        int idOfTheTree;

        try {
        	idOfTheTree = ((Integer)this.getVertexProperty("IdOfTheTree")).intValue();
        } catch (NoSuchElementException e) {
        	idOfTheTree = -2;
        }

        return idOfTheTree;
    }
    
}

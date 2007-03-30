package visidia.agents;


import java.util.NoSuchElementException;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;
import visidia.simulation.agents.stats.FailedMoveStat;
import java.util.LinkedList;
import java.util.Iterator;
import visidia.simulation.agents.MoveException;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public class Spanning_Tree_Agent extends Agent {
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
        
    }
    
    public void init() {
    	
    	this.initWBVars();   
        this.setAgentMover("RandomAgentMover");
        
        AgentMover am = this.getAgentMover();
        
        // Initialisation du sommet de départ
        this.setVertexIdTree(this.getIdentity());
        
        while ( (Integer)this.getProperty("nbSelectedEdges") < (Integer)this.getProperty("nbVertices") - 1 ) {
        	
        	System.out.println("nbSelectedEdges" + ((Integer)this.getProperty("nbSelectedEdges")).toString());
        	System.out.println("nbVertices" + ((Integer)this.getProperty("nbVertices")).toString());
        	/*
        	 * DEPART VERTEX
        	 */
        	
            // Choose the next door
        	try {
        		this.setProperty("ExitPort", new Integer(am.findNextDoor()));
        	}
        	catch(MoveException e) {
        		this.processingAgentWhenSwitchingOff();
        	}
        	
        	boolean newDoor = this.getVertexPort(this.getIdentity(), (Integer)this.getProperty("ExitPort")) == null;
        	
        	// Set the door as a Child
        	if(newDoor)
        		this.setVertexPortToChild(this.getIdentity(), (Integer)this.getProperty("ExitPort"));
        	
        	// Move to the choosen door
            this.move((Integer)this.getProperty("ExitPort"));

            
        	/*
        	 * ARRIVAL VERTEX
        	 */
            
    		this.setProperty("EntryPort", new Integer(this.entryDoor()));
    
    		
            //Vertex vertex_A = this.getSimulator().getVertexArrival(this);

    		
			if (!this.isVertexBelongToTheTree(this.getIdentity())) {
			
				System.out.println("add");
				this.setVertexIdTree(this.getIdentity());
				this.setVertexPortToParent(this.getIdentity(), (Integer)this.getProperty("EntryPort"));
				
				// Coloration graphique
				this.markDoor(this.entryDoor());
			
				this.setProperty("nbSelectedEdges", (Integer)this.getProperty("nbSelectedEdges") +1);
			
			}
			else if(!newDoor) {
			}
			else {
				// The vertex is already member of the tree
				System.out.println("notaddmoveback");
				
				// Move back
				//this.move((Integer)this.getProperty("EntryPort"));
				this.moveBack();
				
				this.delVertexPort(this.getIdentity(), (Integer)this.getProperty("EntryPort"));

				System.out.println("notaddmoveback3");
}
			
			System.out.println("fininit()");

		}

	}
            
       
    
    protected void setVertexIdTree(Integer idTree) {
		this.setVertexProperty("IdTree",idTree);
    }
 
    private Integer getVertexIdTree() {
        Integer idOfTheTree;

        try {
        	idOfTheTree = (Integer)this.getVertexProperty("IdTree");
        } catch (NoSuchElementException e) {
        	idOfTheTree = null;
        }

        return idOfTheTree;
    }
    
    private boolean isVertexBelongToTheTree(Integer idTree) {
    	return idTree.equals(this.getVertexIdTree());
    }
    
    private boolean isRootOfTheTree(Integer idTree) {
    	return idTree.equals(this.getVertexIdTree()) && this.getVertexParent(this.getVertexIdTree()) == null;
    }
    
    protected void setVertexPortToParent(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port" + p.toString(),"Parent");
    }

    protected void setVertexPortToChild(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port" + p.toString(),"Child");
    }

    protected void delVertexPort(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port" + p.toString(),"");
    }
 
    /**
     * Return an Iterator of the childs of the tree specified
     * @param idTree Identification of the tree
     * @return vertex childs
     */
    protected Iterator<Integer> getVertexChilds(Integer idTree) {
  
    	LinkedList<Integer> parent = new LinkedList<Integer>();
    	
    	for(Integer i=0; i < this.getArity(); i++) {
    		
            try {
            	if(((String)this.getVertexProperty("Tree" + idTree.toString() + "Port" + i.toString())).equals("Child")) {
            		parent.add(new Integer(i));
            	}
            } catch (NoSuchElementException e) {
            }
    	}
    	
    	return parent.iterator();
    	
    }
    
    
    /**
     * Return the parent vertex or null if the vertex haven't a parent (he's root)
     * @param idTree Identification of the tree
     * @return vertex parent
     */
    protected Integer getVertexParent(Integer idTree) {
    	      	
    	for(Integer i=0; i < this.getArity(); i++) {
    		
            try {
            	if(((String)this.getVertexProperty("Tree" + idTree.toString() + "Port" + i.toString())).equals("Parent")) {
            		return new Integer(i);
            	}
            } catch (NoSuchElementException e) {
            }
    	}
    	
    	return null;
    	
    }
    

    protected String getVertexPort(Integer idTree, Integer port) {
    	try {
    		return (String)this.getVertexProperty("Tree" + idTree.toString() + "Port" + port.toString());   	    		
    	}
    	catch(NoSuchElementException e) {
    		return null;
    	}
    }

    
}

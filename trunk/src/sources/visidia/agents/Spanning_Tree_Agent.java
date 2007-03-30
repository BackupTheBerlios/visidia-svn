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
public abstract class Spanning_Tree_Agent extends Agent {
    
    abstract public void init();
  
    
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

package visidia.agents;

import java.util.Arrays;

import visidia.graph.Vertex;
import visidia.simulation.agents.stats.FailedMoveStat;
import java.util.Iterator;
import java.util.Set;

import visidia.graph.Vertex;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.FailedMoveStat;

public class Spanning_Tree_Agent_Akka extends Agent{


	
	 
	 protected void init() {
		 
		    //int nbSelectedEdges  = 0;
	        int nbVertices = this.getNetSize();
	        
	        
	        //Initialisation des variables qu'on veut afficher dans
	        //la mémoire de l'agent.
	        this.setProperty("nbSelectedEdges",0);
	        this.setProperty("nbVertices", nbVertices);
	        
	        
	        this.setAgentMover("RandomAgentMover");
	        //this.vertexMarks = new boolean [nbVertices];
	       
	        /**
	         * Puts false on all cells of vertexMarks.
	         */
	      
	        //Arrays.fill(this.getSimulator().vertexMarks, false); 
            //Arrays.fill(this.getSimulator().markedBy, 300);
	        /**
	         * Marks the first vertex as already been seen.
	         */
	        this.mark(this.getVertexIdentity());
	    

	        /**
	         * A tree has nbVertices - 1 edges.
	         */
	       
	        while (this.nbSelectedEdges < (nbVertices - 1) ) {
	        	System.out.println(this.getIdentity()+" nbSelectedEdges= "+this.nbSelectedEdges);
	        	
	            this.move();
	            
	            
	            
	            Vertex vertex_A = this.getSimulator().getVertexArrival(this);
	            
	            if(this.getSimulator().vertexMarks[vertex_A.identity()] && 
	            		this.getSimulator().markedBy[vertex_A.identity()]!=this.getIdentity()){
	            	this.eatingTree(this.getSimulator().markedBy[vertex_A.identity()]);
	            
	            	this.incrementStat(new FailedMoveStat(this.getClass()));
	            	}
	            
	            
	            
	            
	            else{
		            if ( ! this.isMarked(this.getVertexIdentity()) ) {
		                /**
		                 * The current vertex has not been seen already.
		                 */
		
		                /**
		                 * Put the last  edge in bold. It will  be part of the
		                 * tree.
		                 */
		                this.markDoor(this.entryDoor());
		
		                this.mark(this.getVertexIdentity());
		                this.setProperty("Entry Door for vertex_"+this.getVertexIdentity(),this.entryDoor());
		                this.setProperty("Vertex"+this.getVertexIdentity(),"marked by me");
		            
		                this.nbSelectedEdges = (Integer)this.getWhiteBoard().getValue("nbSelectedEdges");
		                this.nbSelectedEdges ++;
		                this.setProperty("nbSelectedEdges",this.nbSelectedEdges );
		                
		                
		                
		               
		                
		            }
		            else {
		                this.incrementStat(new FailedMoveStat(this.getClass()));
		            }
	            }
	            nbVertices = (Integer) this.getWhiteBoard().getValue("nbVertices");
                this.setProperty("nbVertices", nbVertices);
	            
	        }
	    }

	 
	 
	 
	 
	 
	    private void mark (int vertex) {
	    	this.getSimulator().vertexMarks[vertex] = true;
	        this.getSimulator().markedBy[vertex]=this.getIdentity();
	        this.setVertexProperty("marked","by "+this );
	    }

	    private boolean isMarked(int vertex) {
	        return (this.getSimulator().vertexMarks[vertex]);
	    }

	    
	    
	    
	    
	    
	    
	 protected void eatingTree(Integer id){
		 
		if (id < this.getIdentity()){			 
		Set agentsSet =this.getSimulator().getAllAgents();
		System.out.println("grand "+agentsSet);
 	    Iterator <Agent> it = agentsSet.iterator();
 	    Agent agentTempo=null;
 	       while (it.hasNext()){
 	    	  agentTempo=it.next(); 
 	        	if (agentTempo.getIdentity()==id){
 	    		      
 	        		int i=0,j=0;
 	    		       while (i<this.getNetSize()){
 	    		         if (this.getSimulator().markedBy[i]==id){
 	    		        	 this.getSimulator().markedBy[i]=this.getIdentity();
 	    		        	 j++;
 	    		         }
 	    		        i++;
 	    		       } 
 	    		      
 	    		    	 this.markDoor(this.entryDoor());
 	    		    	 agentTempo.assasinateAgent();
 	    		    	 this.nbSelectedEdges+=j;
 	    		    	 this.setProperty("nbSelectedEdges",this.nbSelectedEdges );
 	    		     
 	    	    }	 
 	       }
 	       
		
	    }
		
		if (id> this.getIdentity()){
			
			int i=0,j=0;
		       while (i<this.getNetSize()){
		         if (this.getSimulator().markedBy[i]==this.getIdentity()){
		        	 this.getSimulator().markedBy[i]=id;
		        	 j++;
		         }
		        i++;
		       }
		   
			
			Set agentsSet =this.getSimulator().getAllAgents();
			System.out.println("petit "+agentsSet);
     	    Iterator <Agent> it = agentsSet.iterator();
     	    Agent agentTempo=null, agentToBeAlive = null;
     	       while (it.hasNext()){
     	    	   agentTempo=it.next();
     	        	if (agentTempo.getIdentity()==id){
     	        		System.out.println("petit id= "+id);
     	        		System.out.println("petit "+agentTempo.getIdentity());
        	            agentToBeAlive= agentTempo;
        	            agentToBeAlive.nbSelectedEdges+=j;
        	            agentToBeAlive.setProperty("nbSelectedEdges",agentToBeAlive.nbSelectedEdges );
     	        	}
     	       }
     	       System.out.println("je suis "+agentToBeAlive+" j'augmente de "+j);
        	
        	 this.markDoor(this.entryDoor());   
 			this.setDeath();
 			this.death();	
			

	 }

	 }
	 
	 
	 
}

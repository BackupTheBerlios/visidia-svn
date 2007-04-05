package visidia.agents;

import java.util.Iterator;
import java.util.Collection;

public class Annexing_SubTree_Agent extends Spanning_Tree_Agent{

	
	private Integer stronger;
	private volatile Integer weaker = -1;
	
	public  void init() {
		
	this.setAgentMover("RandomAgentMover");
	Integer destination= new Integer (-1);
	Integer idTreeToAnnex = new Integer(-1);
	boolean markingEdge= false;

	this.stronger = this.getVertexIdTree();
	
	// Wainting the information from my creator
    while(this.weaker == -1) this.sleep(100L);
		
	while (true){
		
		     //seeking for a child to visit.
		
			if((destination=this.getVertexChildNotYetVisited(this.weaker))!=-1){
				//destination is a child not yet visited.
				this.setProperty("Vertex"+this.getVertexIdentity()+"Port"+destination, "alreadyVisited");
				
				if(!this.isPortMarkedInTheTree(this.stronger, destination)) {
					// verify that someone doesn't have alreay maked the edge that we are taking
					this.setVertexPortToChild(this.stronger, destination);
					markingEdge=true;					
				}
			}
			
			else {
				//In case there no child to visit, the agent visits the father
				
				if ((destination=this.isThereOtherFatherNotYetVisited(this.weaker))!=-1) {
				//destination is the father to visit
				this.setProperty("Vertex"+this.getVertexIdentity()+"Port"+destination, "alreadyVisited");
				this.setVertexPortToChild(this.stronger, destination);
				
				}
				else {
				
					if (this.isRootOfTheTree(this.weaker)){

						System.out.println("Annexing: work finished");
						return;
					}
					else {
						
						destination=(Integer)this.getProperty("MyEntryDoor"+this.getVertexIdentity());
					
					}
				}
				
				
		    }
			
		
			this.move(destination);
			
			//visiting a vertex already visited by a stronger agent, I suicide.
			if (this.getVertexIdTree() > this.stronger) {
				System.out.println("Annexing: stronger vertex found" + this.getVertexIdentity() + this.stronger);
				return;
			}
			
			if (markingEdge && this.getVertexIdentity() == this.stronger.intValue()) {
				// We are marking an edge which already belong to the tree
				this.moveBack();
				this.delVertexPort(this.stronger, this.entryDoor());
				this.move(this.entryDoor());
				markingEdge=false;
			}
			else if (markingEdge){
				// We are marking an edge which doesn't belong to the tree
				this.setVertexPortToParent(this.stronger, this.entryDoor() );
				this.setVertexIdTree(this.stronger);
				markingEdge=false;
			}
			
			this.setProperty("MyEntryDoor"+this.getVertexIdentity(), this.entryDoor());

			
			
			
	}
		
		
		
	}
	
	/**
	 * 
	 * @return -1 if there are no more father to visit, otherwise il returns 
	 * a door "father" to move to.
	 */
	protected Integer isThereOtherFatherNotYetVisited(Integer idTreeToAnnex){
		Integer destination= new Integer (-1);
		if ( (destination=this.getVertexParent(idTreeToAnnex))!=null &&
		!this.getWhiteBoard().containsElement("Vertex"+this.getVertexIdentity()+"Port"+destination))return destination;
		  //destination!=(Integer)this.getProperty("MyEntryDoor"+this.getVertexIdentity())) return destination; 
	
	  return -1;
	}
	
	
	/**
	 * 
	 * @return -1 if there no more child to visit, otherwise it returns 
	 * the door to move to.
	 */
	protected Integer getVertexChildNotYetVisited(Integer idYoungTree){
		Collection co = this.getVertexChilds(idYoungTree);
		Iterator it= co.iterator();
		Integer childToVisit= new Integer (-1);
		
		while (it.hasNext()){
			
			childToVisit=(Integer)it.next();
	
			 if (!this.getWhiteBoard().containsElement("Vertex"+this.getVertexIdentity()+"Port"+childToVisit)) {
				System.out.println("Annexing: childToVisit " + childToVisit + " " + idYoungTree);
				return childToVisit;

			 }
		}
		return -1;
	}
   
	
	public void setIdTreeToAnnexe(Integer idTree) {
		this.weaker = idTree;
	}
}

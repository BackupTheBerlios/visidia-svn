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

	this.stronger = this.getVertexIdTree();
	
	// Wainting the information from my creator
    while(this.weaker == -1) this.sleep(100L);
		
	while (true){
		    System.out.println("child: "+this.getVertexChildNotYetVisited(this.weaker));
			if((destination=this.getVertexChildNotYetVisited(this.weaker))!=-1){				
				//l'agent mémorise qu'il a visité ce fils (=destination)
				this.setProperty("Vertex"+this.getVertexIdentity()+"Port"+destination, "alreadyVisited");;
			}
			
			else {
				System.out.println("parent: "+this.getVertexParent(this.getVertexIdTree()));
				if ((destination=this.isThereOtherFatherNotYetVisited(this.weaker))!=-1) {
				//l'agent m�morise le p�re qu'il visite	
				this.setProperty("Vertex"+this.getVertexIdentity()+"Port"+destination, "alreadyVisited");
				
				
				}
				else {
					System.out.println(this.isRootOfTheTree(this.weaker));
					if (this.isRootOfTheTree(this.weaker)){
						System.out.println("j'ai fini");
						return;
					}
					else {
						
						destination=(Integer)this.getProperty("MyEntryDoor"+this.getVertexIdentity());
						System.out.println("retour: "+destination);
					}
				}
				
				
		    }
			
			System.out.println("destination "+destination);
			this.move(destination);
			this.setVertexIdTree(this.stronger);
            //le clone m�morise le port d'où il est entr�.
			this.setProperty("MyEntryDoor"+this.getVertexIdentity(), this.entryDoor());
			if (this.getVertexIdTree()>this.getIdentity())  this.death();
			
			
			
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
		if (it==null) return childToVisit;
		while (it.hasNext()){
		childToVisit=(Integer)it.next();
		System.out.println("test de fils: "+this.getWhiteBoard().containsElement("Vertex"+this.getVertexIdentity()+"Port"+childToVisit));
		 if (!this.getWhiteBoard().containsElement("Vertex"+this.getVertexIdentity()+"Port"+childToVisit)) return childToVisit;
				//childToVisit!=(Integer)this.getProperty("MyEntryDoor"+this.getVertexIdentity())) return childToVisit; 
		}
		return -1;
	}
   
	
	public void setIdTreeToAnnexe(Integer idTree) {
		this.weaker = idTree;
	}
}

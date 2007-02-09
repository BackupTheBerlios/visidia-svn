package visidia.graph;

import java.io.Serializable;

class SimpleGraphEdge implements Edge, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1657052512457268915L;
	SimpleGraphVertex vtx1 = null;
    SimpleGraphVertex vtx2 = null;
    SimpleGraph graph = null; // référence sur la structure mère.

    Object data;

    SimpleGraphEdge(SimpleGraph graph, SimpleGraphVertex v1, SimpleGraphVertex v2){
	this.vtx1 = v1;
	this.vtx2 = v2;
	this.graph = graph;
    }

    public Vertex vertex1(){
	return this.vtx1;
    }
    
    public Vertex vertex2(){
	return this.vtx2;
    }
    
    
    /**
     *
     */
    public Object getData(){
	return this.data;
    }
    
    protected void updateData(Object dt){
	this.data = dt;
    }


    /**
     *
     */
    public void setData(Object dt){
	SimpleGraphEdge edg = (SimpleGraphEdge) this.graph.edge(this.vtx2.identity(), this.vtx1.identity());
	
      	if( edg == this ){
	  edg = (SimpleGraphEdge) this.graph.edge(this.vtx2.identity(), this.vtx1.identity());
	}

	edg.updateData(dt);
	this.data = dt;
    }
    
}


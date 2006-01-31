package fr.enserb.das.graph;

import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import fr.enserb.das.assert.Assertion;



class SimpleGraphEdge implements Edge, Serializable{
    SimpleGraphVertex vtx1 = null;
    SimpleGraphVertex vtx2 = null;
    SimpleGraph graph = null; // reference sur la structure mere.

    Object data;

    SimpleGraphEdge(SimpleGraph graph, SimpleGraphVertex v1, SimpleGraphVertex v2){
	vtx1 = v1;
	vtx2 = v2;
	this.graph = graph;
    }

    public Vertex vertex1(){
	return vtx1;
    }
    
    public Vertex vertex2(){
	return vtx2;
    }
    
    
    /**
     *
     */
    public Object getData(){
	return data;
    }
    
    protected void updateData(Object dt){
	data = dt;
    }


    /**
     *
     */
    public void setData(Object dt){
	SimpleGraphEdge edg = (SimpleGraphEdge) graph.edge(vtx2.identity(), vtx1.identity());
	
      	if( edg == this ){
	  edg = (SimpleGraphEdge) graph.edge(vtx2.identity(), vtx1.identity());
	}

	edg.updateData(dt);
	data = dt;
    }
    
}

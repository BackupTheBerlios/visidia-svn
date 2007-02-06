package visidia.gui.metier.simulation;

import java.util.Hashtable;
import visidia.simulation.*;

public class AlgoChoice implements AlgoChoiceInterface{

    private    Hashtable algoTable;
    private int numberOfVertices;

    public AlgoChoice(int nbVertices){
	this.algoTable = new Hashtable();
	this.numberOfVertices = nbVertices;
    }

    // returns the algorithm of the vertex identified by this id
    // returns null if no algorithm was associated
    public Algorithm getAlgorithm(int id){
	try{
	    return (Algorithm)((Algorithm)this.algoTable.get(String.valueOf(id))).clone();
	}catch(NullPointerException e){return null;}
    }

    // put an algorithm to the vertex with 'id' identifier
    public void putAlgorithm(int id, Algorithm algo){
	this.algoTable.put(String.valueOf(id),algo);
    }
    public void putAlgorithm(String id, Algorithm algo){
	this.algoTable.put(id,algo);
    }

    // put this algorithm for all the vertices of the graph
    public void putAlgorithmToAllVertices(Algorithm algo){
	try{
	    for (int id=0;id<this.numberOfVertices;id++){
		this.algoTable.put(String.valueOf(id),algo);
	    }
	}
	catch(Exception e){System.out.println("ERROR IN ALGORITHM ATTRIBUTION ! : "+e);}
    }

    // determines wether all the vertices have an algorithm
    public boolean verticesHaveAlgorithm(){
	for (int id=0;id<this.numberOfVertices;id++){
	    if (!this.algoTable.containsKey(String.valueOf(id))) return false;
	}
	return true;
    }
    public Hashtable getTableAlgo(){
	return this.algoTable;
    }
}



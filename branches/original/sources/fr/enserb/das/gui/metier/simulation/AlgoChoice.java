package fr.enserb.das.gui.metier.simulation;

import java.util.Hashtable;
import fr.enserb.das.simulation.*;

public class AlgoChoice implements AlgoChoiceInterface{

    private    Hashtable algoTable;
    private int numberOfVertices;

    public AlgoChoice(int nbVertices){
	algoTable = new Hashtable();
	numberOfVertices = nbVertices;
    }

    // returns the algorithm of the vertex identified by this id
    // returns null if no algorithm was associated
    public Algorithm getAlgorithm(int id){
	try{
	    return (Algorithm)((Algorithm)algoTable.get(String.valueOf(id))).clone();
	}catch(NullPointerException e){return null;}
    }

    // put an algorithm to the vertex with 'id' identifier
    public void putAlgorithm(int id, Algorithm algo){
	algoTable.put(String.valueOf(id),algo);
    }
    public void putAlgorithm(String id, Algorithm algo){
	algoTable.put(id,algo);
    }

    // put this algorithm for all the vertices of the graph
    public void putAlgorithmToAllVertices(Algorithm algo){
	try{
	    for (int id=0;id<numberOfVertices;id++){
		algoTable.put(String.valueOf(id),algo);
	    }
	}
	catch(Exception e){System.out.println("ERROR IN ALGORITHM ATTRIBUTION ! : "+e);}
    }

    // determines wether all the vertices have an algorithm
    public boolean verticesHaveAlgorithm(){
	for (int id=0;id<numberOfVertices;id++){
	    if (!algoTable.containsKey(String.valueOf(id))) return false;
	}
	return true;
    }
    public Hashtable getTableAlgo(){
	return algoTable;
    }
}



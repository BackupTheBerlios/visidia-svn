package visidia.gui.donnees;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JApplet;

import visidia.algo.Mazurkiewicz_Election;
import visidia.simulation.Algorithm;

public class TableAlgo{

    protected static Hashtable<String, Algorithm> tableauAlgo;

    public static void setTableAlgo(JApplet uneApplet){
	tableauAlgo = new Hashtable<String, Algorithm> ();

	//String jarAdress = new String("jar:"+uneApplet.getCodeBase()+"DistributedAlgoSimulator.jar!/");
	
	try{
	    //tableauAlgo.put("Election", new Election());
	    // tableauAlgo.put("algorule", new AlgoRule());
	    //tableauAlgo.put("Election With Net Size", new ElectionWithNetSize());
	    tableauAlgo.put("Mazurkiewicz", new Mazurkiewicz_Election());
	    //tableauAlgo.put("Router", new Router());
	    //tableauAlgo.put("Sequence Test", new SequenceTest());
	    //  tableauAlgo.put("simpleRule", new SimpleRule());
	    //tableauAlgo.put("Spanning Algo", new SpanningTreeAlgo());
	    // tableauAlgo.put("spanningMessage", new SpanningTreeMessage());
	    //tableauAlgo.put("Synchro", new Synchro());
	    //tableauAlgo.put("Star Synchro", new SynchroEtoile());
	    //tableauAlgo.put("DijkstraVan...", new ColorationDijkstraFeijenVanGasteren());
	    //tableauAlgo.put("Cycle", new Cycle());
	    //tableauAlgo.put("Spanning1ID_without_synchro_visualization", new Spanning1ID_without_synchro_visualization());
	}catch(Exception e){}
    }
    
   
    // returns the image by the key parameter

    public static Algorithm getAlgo(String key){
	if (tableauAlgo.containsKey(key))
	    return (Algorithm)(tableauAlgo.get(key));
	else 
	    return null;
    }

    public static Vector getKeys(){
	Enumeration e = tableauAlgo.keys();
	Vector result = new Vector();

	while (e.hasMoreElements())
	    result.add(e.nextElement());
	return result;
    }
    
}

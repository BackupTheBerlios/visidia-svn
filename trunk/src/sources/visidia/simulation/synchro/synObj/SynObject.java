package visidia.simulation.synchro.synObj;

import  visidia.simulation.synchro.SynCT;
//import visidia.rule.Star;
//import visidia.rule.Neighbour;

import java.util.Vector;
import java.io.Serializable;

//import  visidia.simulation.synchro.synObj.*;
//import  visidia.simulation.synchro.synAlgos.*;
//import  visidia.simulation.synchro.SynCT;


/*The class mother of Synchronisation objects */
/**
 * The class mother of Synchronisation objects
 */
public class SynObject implements Serializable {
    
    public Vector synDoors = new Vector();
    protected boolean edgMark[];
    protected boolean connected[];
    protected Vector synchroCenters = new Vector();//for LC1
    public int center;
    public int synState;
    public int arity =0;
    public boolean run=true;
    
    /*  Basic */
  /** this methode is used to oinitialize the structures.
    *
    * @param ar the arity.
    */  
    public void init(int ar){
	arity = ar;
	connected = new boolean[arity];
	edgMark = new boolean[arity];
	for(int i=0;i<arity;i++) {
	    edgMark[i] = false;
	    connected[i] = true;
	}
    }
/**
    * clears the structures.
    */
    public void reset(){
	synDoors.clear();
	synchroCenters.clear();
    }
    
    public Object clone(){
	return new SynObject();
    }
    public String toString(){
	return ("<state="+synState+"SynDoors"+synDoors+">"); 
    }
    /* Synchro State Accessors */
/** 
    * Sets the synchronisation state  of the node. 
    * @param synstate possible values are defined in class SynCT
    */
    public void setState(int synstate){
	synState = synstate;
    }
/**
    * Returns true if the node is in elected state, returns false otherwise.
    */
    public boolean isElected(){
	return (synState == SynCT.IAM_THE_CENTER);
    }
/**
    * Returns true if the node is in the star, returns false otherwise.
    */
    public boolean isInStar(){
	return (synState == SynCT.IN_THE_STAR);
    }
/**
    * Returns true if the node is in not the star, returns false otherwise.
    */
    public boolean isNotInStar(){
	return (synState == SynCT.NOT_IN_THE_STAR);
    }
/**
    * Adds a new synchronized neighbour to synDoors (in synob).
    */
    public void addSynchronizedDoor(int i){
	synDoors.add(new Integer(i));
    }
/**
    * Sets the mark of the neighbour to "mark" 
    * @param neighbour the neighbour door.
    * @param mark the new mark state.
    */
    public void setMark(int neighbour, boolean mark){
	edgMark[neighbour] = mark;
    }
    public boolean getMark(int neighbour){
	return edgMark[neighbour];
    }
    /** 
    * Returns the number of the door of synchronized neighbour in position i.
    * @param i position in synDoors.
    */
    public int getDoor(int i){
	return ((Integer) synDoors.get(i)).intValue();
    }
    public boolean isConnected(int i) {
	return connected[i];
    }
    public boolean setConnected(int i, boolean b) {
	return connected[i] = b;
    }
       
    /* par defaut on ne gere pas la Terminaison, Alors pour ne pas
     refaire des codes ou on ne gere pas la Term*/
    public boolean hasFinished(int neighbour){
	//System.out.println("ATTENTION");
	return false;
    }
    public boolean allFinished(){
	//System.out.println("ATTENTION");
	return  false;
    }
    public void setGlobEnd(boolean b){
	//System.out.println("ATTENTION");
    }
    public void setFinished(int neighbour, boolean b){
	//System.out.println("ATTENTION");
    }
    /* LC1 Methodes */
    /**
     * vide l'ensemble des centres des etoites de synchronisation.
     */
     public void resetCenters(){
	synchroCenters.clear();
    }
    /**
     * Add a new center of synchronization. 
     */
    public void addCenter(int i){
	synchroCenters.add(new Integer(i));
    }
 /**
     *  returns the centers of stars.
     */
    public Vector getCenters(){
	return synchroCenters;
    }
    
}
    
    

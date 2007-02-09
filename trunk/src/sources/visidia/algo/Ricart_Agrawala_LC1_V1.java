package visidia.algo;
import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import visidia.misc.ColorState;
import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

/**
 * The algorithm of Ricart and Agrawala solves the problem
 * of conflicts between ressources.
 * The conflict between a node and its neihbours happens when
 * a node changes its state to Hungry. The node which have the state 
 * Hungry may ask its neighbours the ressource. Then, if its neighbour
 * answer by an affirmitive, it changes its state into Eating.
 * In this version, we init the graph with the state Thinking. Randomly, some states
 * change their label to Hungry node and try to access the ressource.
 * If the ressource is available (all its neighbours send him the autorization) then 
 * it changes its states to Eating else it keeps its states to Hungry and increment his state.
 * This version increment the node state when it cannot access its critical section during the synchronization.
 */

public class Ricart_Agrawala_LC1_V1 extends Algorithm {

    final int starCenter=-1;
    final int notInTheStar=-2;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.green);
    static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);

    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(synchronization);
	typesList.add(labels);
	typesList.add(booleen);
	return typesList;
    }
   
    public void init(){
	final int arity=this.getArity();
	
	Vector synchro;
	boolean run=true;
	int count = 0;
	int eatingcount=0;
	String myThinkingNode = new String("T,0");
	String myEatingNode = new String("E,0");
	int myNumber = Math.abs(SynchronizedRandom.nextInt());
	this.putProperty("label",myThinkingNode);

	while(run){
	    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	    String theHungryNode = new String("H,"+count);
	    String theThinkingNode = new String("T,"+count);
	    String theEatingNode = new String("E,"+count);
	    if (choosenNumber>=myNumber) {
		if(this.getProperty("label").toString().substring(0,1).compareTo("H")==0){
		    this.putProperty("label",theHungryNode);
		}
		if(this.getProperty("label").toString().substring(0,1).compareTo("T")==0){
		    count=0;
		    this.putProperty("label",theHungryNode);
		}
		if(this.getProperty("label").toString().substring(0,1).compareTo("E")==0){
		    eatingcount++;
		    this.putProperty("label",theEatingNode);
		}
	    }
	    if(choosenNumber<=myNumber) {
		if(eatingcount >= 2){
		    if(this.getProperty("label").toString().substring(0,1).compareTo("E")==0){
			count = 0;
			eatingcount=0;
			for(int i=0; i<arity;i++)
			    this.setEdgeColor(i, new ColorState(Color.black));
			this.putProperty("label",theThinkingNode);  
		    }
		}
		if(this.getProperty("label").toString().substring(0,1).compareTo("T")==0){
		    this.putProperty("label",theThinkingNode);
		}
		if(this.getProperty("label").toString().substring(0,1).compareTo("H")==0){
		    this.putProperty("label",theHungryNode);
		}
	    }
	    
	    synchro=this.starSynchro();
	    if(synchro!=null){
		if (((Integer) synchro.elementAt(0)).intValue()==-1) {
		    StringMessage nodeLabel = new StringMessage(new String(this.getProperty("label").toString()));
		    String myLabelString = nodeLabel.data().substring(0,1);
		    int myLabelInt = (new Integer(nodeLabel.data().substring(2))).intValue();
		    
		    boolean hungryResult=false;
		    boolean hungryResult1=false;
		    
		    for(int i=0;i<arity;i++){
			String msg = ((StringMessage)this.receiveFrom(i)).data();
			String neighLabelString = msg.substring(0,1);
			int neighLabelInt = (new Integer(msg.substring(2))).intValue();
			if(myLabelString.compareTo("H")==0){
			    if(neighLabelString.compareTo("H")==0){
				if(neighLabelInt <= myLabelInt){
				    hungryResult = true;
				}
				else{
				    hungryResult1 = true;				
				}
			    }
			    if(neighLabelString.compareTo("T")==0){
				hungryResult = true;
			    }
			    if(neighLabelString.compareTo("E")==0){
				hungryResult1 = true;
			    }
			}
		    }
		    if((hungryResult == true) && (hungryResult1==false)){
			for(int i=0;i<arity;i++)
			    this.setEdgeColor(i, new ColorState(Color.red));
			this.putProperty("label",myEatingNode);
		    }
		    if(hungryResult1 == true){
			count++;
			this.putProperty("label",theHungryNode);
		    }
		    this.breakSynchro();
		}
		else {
		    StringMessage nodeLabel = new StringMessage(new String(this.getProperty("label").toString()),labels);
		    for (int i=0;i<synchro.size();i++)
                        this.sendTo(((Integer) synchro.elementAt(i)).intValue(),nodeLabel);
		}
	    }
	}
    }

    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    public Vector starSynchro(){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        Vector neighbourCenter;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        this.sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
        
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = this.receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < this.getArity(); door++){
                this.setDoorState(new SyncState(true),door);
            }
            
            this.sendAll(new IntegerMessage(new Integer(1),synchronization));
            
            for (int i=0;i<arite;i++) {
                Message msg=this.receiveFrom(i);
            }
            neighbourCenter=new Vector();
            neighbourCenter.add(new Integer(-1));
            
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            this.sendAll(new IntegerMessage(new Integer(0),synchronization));
            
            for (int i=0; i<arite;i++) {
                Message msg=this.receiveFrom(i);
                if  (((IntegerMessage)msg).value() == 1) {
                    neighbourCenter.add(new Integer(i));
                }
            }
            if (neighbourCenter.size()==0)
                neighbourCenter=null;
            
            return neighbourCenter;
            
        }
    }

    public void breakSynchro() {
	
	for( int door = 0; door < this.getArity(); door++){
	    this.setDoorState(new SyncState(false),door);
	}
    }
    
    public Object clone(){
	return new Ricart_Agrawala_LC1_V1();
    }
}





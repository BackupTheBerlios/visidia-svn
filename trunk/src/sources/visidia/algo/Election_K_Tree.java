
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Election_K_Tree extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    final int K=3;

    static int synchroNumber=0;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection<MessageType> typesList = new LinkedList<MessageType>();
        typesList.add(synchronization);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    public void init(){
        
        final String nodeN=new String("N");
        final String nodeF=new String("F");
        final String nodeE=new String("E");
        
        final int neighbour=this.getArity();
        
        String neighbourState[];
        Vector synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[];
        
        this.putProperty("label",new String(nodeN));
        
        neighbourState=new String[neighbour];
        
        finishedNode=new boolean[this.getArity()];
        for (int i=0;i<this.getArity();i++) {
            finishedNode[i]=false;
        }
        
        while(run){
            synchro=this.starSynchro(finishedNode);
	    if (synchro != null ){
		if (((Integer) synchro.elementAt(0)).intValue()==-1) {
		    int n_Count=0;
		    int f_Count=0;
		    
		    for (int door=0;door<neighbour;door++)
			if (!finishedNode[door]) {
			    neighbourState[door]=((StringMessage) this.receiveFrom(door)).data();
			    if (neighbourState[door].compareTo(nodeN) == 0)
				n_Count++;
			    else
				f_Count++;
			}
			else
			    f_Count++;
		    
		    if ((((String) this.getProperty("label")).compareTo(nodeN)==0) &&
			(n_Count<=this.K) && (n_Count>0)) {
			this.putProperty("label",new String(nodeF));
			run=false;
		    }
		    else
			if ((((String) this.getProperty("label")).compareTo(nodeN)==0) &&
			    (n_Count==0)) {
			    this.putProperty("label",new String(nodeE));
			    run=false;
			}
		    
		    
		    
		    this.breakSynchro();
		    
		}
		else {
		    for (int i=0;i<synchro.size();i++)
			this.sendTo(((Integer) synchro.elementAt(i)).intValue(),new StringMessage((String) this.getProperty("label"),labels));
                }
                
            }
            
        }
        
        this.sendAll(new IntegerMessage(new Integer(-1),synchronization));
        
    }
    
    public Vector starSynchro(boolean finishedNode[]){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        Vector<Integer> neighbourCenter;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        //sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
         for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                this.sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }

        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++)
	    if (! finishedNode[i]) {
		Message msg = this.receiveFrom(i);
		answer[i]= ((IntegerMessage)msg).value();
		if (answer[i]==-1)
                    finishedNode[i]=true;
	    }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++)
	    if (! finishedNode[i]) {
		if( answer[i] >= max )
		    max = answer[i];
	    }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < this.getArity(); door++)
		if (! finishedNode[door]) {
		    this.setDoorState(new SyncState(true),door);
		}
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    this.sendTo(i,new IntegerMessage(new Integer(1),synchronization));
		}
	    }
            
            for (int i=0;i<arite;i++)
		if (! finishedNode[i]) {
		    /*Message msg=*/this.receiveFrom(i);
		}
	    
            neighbourCenter=new Vector<Integer>();
            neighbourCenter.add(new Integer(-1));
	    
            synchroNumber++;
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    this.sendTo(i,new IntegerMessage(new Integer(0),synchronization));
		}
	    }
	    
            for (int i=0; i<arite;i++) 
		if (! finishedNode[i]) {
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
            this.setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Election_K_Tree();
    }
}


package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Election_K_Tree extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    final int K=3;

    static int synchroNumber=0;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    public void init(){
        
        final String nodeN=new String("N");
        final String nodeF=new String("F");
        final String nodeE=new String("E");
        
        final int neighbour=getArity();
        
        String neighbourState[];
        Vector synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[];
        
        putProperty("label",new String(nodeN));
        
        neighbourState=new String[neighbour];
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        while(run){
            synchro=starSynchro(finishedNode);
	    if (synchro != null ){
		if (((Integer) synchro.elementAt(0)).intValue()==-1) {
		    int n_Count=0;
		    int f_Count=0;
		    
		    for (int door=0;door<neighbour;door++)
			if (!finishedNode[door]) {
			    neighbourState[door]=((StringMessage) receiveFrom(door)).data();
			    if (neighbourState[door].compareTo(nodeN) == 0)
				n_Count++;
			    else
				f_Count++;
			}
			else
			    f_Count++;
		    
		    if ((((String) getProperty("label")).compareTo(nodeN)==0) &&
			(n_Count<=K) && (n_Count>0)) {
			putProperty("label",new String(nodeF));
			run=false;
		    }
		    else
			if ((((String) getProperty("label")).compareTo(nodeN)==0) &&
			    (n_Count==0)) {
			    putProperty("label",new String(nodeE));
			    run=false;
			}
		    
		    
		    
		    breakSynchro();
		    
		}
		else {
		    for (int i=0;i<synchro.size();i++)
			sendTo(((Integer) synchro.elementAt(i)).intValue(),new StringMessage((String) getProperty("label"),labels));
                }
                
            }
            
        }
        
        sendAll(new IntegerMessage(new Integer(-1),synchronization));
        
    }
    
    public Vector starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        Vector neighbourCenter;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        //sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
         for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }

        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++)
	    if (! finishedNode[i]) {
		Message msg = receiveFrom(i);
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
            for( int door = 0; door < getArity(); door++)
		if (! finishedNode[door]) {
		    setDoorState(new SyncState(true),door);
		}
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    sendTo(i,new IntegerMessage(new Integer(1),synchronization));
		}
	    }
            
            for (int i=0;i<arite;i++)
		if (! finishedNode[i]) {
		    Message msg=receiveFrom(i);
		}
	    
            neighbourCenter=new Vector();
            neighbourCenter.add(new Integer(-1));
	    
            synchroNumber++;
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    sendTo(i,new IntegerMessage(new Integer(0),synchronization));
		}
	    }
	    
            for (int i=0; i<arite;i++) 
		if (! finishedNode[i]) {
		    Message msg=receiveFrom(i);
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
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Election_K_Tree();
    }
}

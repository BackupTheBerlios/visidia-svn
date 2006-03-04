
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Election_Tree extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
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
        int synchro;
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
            if( synchro==starCenter ){
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
                (n_Count==1)) {
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
                if ( synchro != notInTheStar) {
                    
                    sendTo(synchro,new StringMessage((String) getProperty("label"),labels));
                }
                
            }
            
        }
        
        sendAll(new IntegerMessage(new Integer(-1),synchronization));
        
    }
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
                if (answer[i]==-1)
                    finishedNode[i]=true;
            }
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if (! finishedNode[i]) {
                if( answer[i] >= max )
                    max = answer[i];
            }
        }
        
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
            }
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                if (! finishedNode[door])
                    setDoorState(new SyncState(true),door);
            }
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                }
            }
            
            for (int i=0;i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                }
            }
            
            return starCenter;
        }
        else {
            int inTheStar=notInTheStar;
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(0),synchronization));
                }
            }
            
            for (int i=0; i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                    if  (((IntegerMessage)msg).value() == 1) {
                        inTheStar=i;
                    }
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Election_Tree();
    }
}

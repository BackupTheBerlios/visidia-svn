
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class DetectStable extends Algorithm {
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
        
        final int arity=getArity();
        
        int graphSize=getNetSize();
        int neighboursA[];
        int myA=-1;
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean myP=false;
        int myNumber = Math.abs(SynchronizedRandom.nextInt());
        String myState=new String("F: "+myA);
        boolean finishedNode[];
        
        putProperty("label",new String(myState));
        
        neighboursA=new int[arity];
        
        finishedNode=new boolean[arity];
        for (int i=0;i<arity;i++) {
            finishedNode[i]=false;
        }
        
        while(run){
            
            int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
            if ((choosenNumber>=myNumber) && (!myP)) {
                myP=true;
                myState="T: "+myA;
                putProperty("label",new String(myState));
            }
            
            synchro=starSynchro(finishedNode);
            if( synchro==starCenter ){
                int minA=2*graphSize;
                
                for (int door=0;door<arity;door++)
                    if (!finishedNode[door]) {
                        neighboursA[door]=((IntegerMessage) receiveFrom(door)).value();
                        if (neighboursA[door]<minA)
                            minA=neighboursA[door];
                    }
                if (myP) {
                    myA=1+minA;
                    myState="T: "+myA;
                }
                
                putProperty("label",new String(myState));
                
                if (myA>graphSize)
                    run=false;
                
                breakSynchro();
                
            }
            else {
                if ( synchro != notInTheStar) {
                    
                    sendTo(synchro,new IntegerMessage(new Integer(myA),labels));
                }
            }
        }
        
        for( int i = 0; i < getArity(); i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(-1),synchronization));
            }
        }
        
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
        return new DetectStable();
    }
}












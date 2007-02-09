
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

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
        
        final int neighbour=this.getArity();
        
        String neighbourState[];
        int synchro;
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
            if( synchro==this.starCenter ){
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
                (n_Count==1)) {
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
                if ( synchro != this.notInTheStar) {
                    
                    this.sendTo(synchro,new StringMessage((String) this.getProperty("label"),labels));
                }
                
            }
            
        }
        
        this.sendAll(new IntegerMessage(new Integer(-1),synchronization));
        
    }
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                this.sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = this.receiveFrom(i);
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
                this.sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = this.receiveFrom(i);
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
                    this.setDoorState(new SyncState(true),door);
            }
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    this.sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                }
            }
            
            for (int i=0;i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=this.receiveFrom(i);
                }
            }
            
            return this.starCenter;
        }
        else {
            int inTheStar=this.notInTheStar;
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    this.sendTo(i,new IntegerMessage(new Integer(0),synchronization));
                }
            }
            
            for (int i=0; i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=this.receiveFrom(i);
                    if  (((IntegerMessage)msg).value() == 1) {
                        inTheStar=i;
                    }
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Election_Tree();
    }
}

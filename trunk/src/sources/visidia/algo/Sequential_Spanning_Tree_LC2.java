
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Sequential_Spanning_Tree_LC2 extends Algorithm {
    
    /* R1: A-0-N  ---> M-1-A
       R2: M-1-A  ---> A-1-F
       R1 > R2 */
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    final String aNode=new String("A");
    final String nNode=new String("N");
    final String mNode=new String("M");
    final String fNode=new String("F");
    
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    static MessageType termination = new MessageType("termination", false, java.awt.Color.green);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        typesList.add(termination);
        return typesList;
    }
    
    public void init(){
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[];
        String neighboursLabel[];
        //String lastName;
        int pere=-1;
        
        
        finishedNode= new boolean[this.getArity()];
        for (int i=0;i<this.getArity();i++)
            finishedNode[i]=false;
        
        neighboursLabel=new String[this.getArity()];
        
        while(run){
            
            synchro=this.starSynchro(finishedNode);
            if (synchro==-3)
                run=false;
            else
                if (synchro==this.starCenter){
                    int neighbourA=-1;
                    int neighbourM=-1;
                    int neighbourN=-1;
                    int nbreN=0;
                    int nbreF=0;
                    
                    for (int door=0;door<this.getArity();door++)
                        if (!finishedNode[door]) {
                            neighboursLabel[door]=((StringMessage) this.receiveFrom(door)).data();
                            
                            if (neighboursLabel[door].compareTo(this.aNode)==0)
                                neighbourA=door;
                            
                            if (neighboursLabel[door].compareTo(this.nNode)==0) {
                                nbreN++;
                                neighbourN=door;
                            }
                            
                            if (neighboursLabel[door].compareTo(this.fNode)==0)
                                nbreF++;
                            
                            if ((neighboursLabel[door].compareTo(this.mNode)==0) && (door==pere))
                                neighbourM=door;
                            
                        }
                    if ((((String) this.getProperty("label")).compareTo(this.aNode)==0) &&
                    (nbreF==this.getArity()))
                        run=false;
                    
                    if ((((String) this.getProperty("label")).compareTo(this.nNode)==0) &&
                    (neighbourA !=-1)) {
                        this.putProperty("label",new String(this.aNode));
                        this.setDoorState(new MarkedState(true),neighbourA);
                        pere=neighbourA;
                        for (int door=0;door<this.getArity();door++)
                            if (!finishedNode[door]) {
                                if (door!=neighbourA)
                                    this.sendTo(door,new StringMessage(neighboursLabel[door],labels));
                                else
                                    this.sendTo(door,new StringMessage(new String("M"),labels));
                            }
                    }
                    else {
                        if ((((String) this.getProperty("label")).compareTo(this.aNode)==0) &&
                        (neighbourM!=-1) && (nbreN==0)) {
                            this.putProperty("label",new String(this.fNode));
                            for (int door=0;door<this.getArity();door++)
                                if (!finishedNode[door]) {
                                    if (door!=neighbourM)
                                        this.sendTo(door,new StringMessage(neighboursLabel[door],labels));
                                    else
                                        this.sendTo(door,new StringMessage(new String("A"),labels));
                                }
                        }
                        else
                            if ((((String) this.getProperty("label")).compareTo(this.aNode)==0) &&
                            (neighbourN !=-1)) {
                                this.putProperty("label",new String(this.mNode));
                                this.setDoorState(new MarkedState(true),neighbourN);
                                for (int door=0;door<this.getArity();door++)
                                    if (!finishedNode[door]) {
                                        if (door!=neighbourN)
                                            this.sendTo(door,new StringMessage(neighboursLabel[door],labels));
                                        else
                                            this.sendTo(door,new StringMessage(new String("A"),labels));
                                    }
                            }
                            else
                                for (int door=0;door<this.getArity();door++)
                                    if (!finishedNode[door])
                                        this.sendTo(door,new StringMessage(neighboursLabel[door],labels));
                    }
                    this.breakSynchro();
                }
                else
                    if (synchro!=this.notInTheStar) {
                        String newState;
                        this.sendTo(synchro,new StringMessage((String) this.getProperty("label"),labels));
                        newState=((StringMessage) this.receiveFrom(synchro)).data();
                        if ((newState.compareTo(this.aNode)==0) &&
                        (((String) this.getProperty("label")).compareTo(this.nNode)==0))
                            pere=synchro;
                        this.putProperty("label",new String(newState));
                    }
        }
        this.sendAll(new IntegerMessage(new Integer(-3),termination));
    }
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        boolean theEnd=false;
        
        
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
                if (answer[i]==-3) {
                    finishedNode[i]=true;
                    theEnd=true;
                }
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
        if (theEnd)
            max=-3;
        
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
                if (answer[i]==-3) {
                    theEnd=true;
                }
            }
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if (! finishedNode[i])
                if( answer[i] >= max )
                    max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                if (! finishedNode[door])
                    this.setDoorState(new SyncState(true),door);
            }
            
            
            if (! theEnd) {
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        this.sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                    }
                }
                
                for (int i=0;i<arite;i++) {
                    if (! finishedNode[i]) {
                        this.receiveFrom(i);
                    }
                }
                
                return this.starCenter;
            }
            else {
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        this.sendTo(i,new IntegerMessage(new Integer(-3),termination));
                    }
                }
                
                for (int i=0;i<arite;i++) {
                    if (! finishedNode[i]) {
                        this.receiveFrom(i);
                    }
                }
                
                return -3;
            }
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
                    else
                        if (((IntegerMessage)msg).value() == -3) {
                            finishedNode[i]=true;
                            theEnd=true;
                        }
                }
            }
            if (inTheStar!=this.notInTheStar)
                return inTheStar;
            else
                if (theEnd)
                    return -3;
                else
                    return this.notInTheStar;
            
        }
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Sequential_Spanning_Tree_LC2();
    }
}












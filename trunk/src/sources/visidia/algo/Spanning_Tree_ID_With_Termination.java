
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_ID_With_Termination extends Algorithm {
    
    /* R1: (X,i)-0-(Y,j)  ---> (X,i)-i-(M,i) ; j<i ; X=N,M & Y=N,M,F
       R2:   (M,i)    --->   (F,i)  , (M,i)-0-(Y,j) [j!=i];(X,i)-1-(M,i)-1-(m,i)
     */
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    final int nNode=0;//new String("N");
    final int mNode=1;//new String("M");
    final int fNode=2;//new String("F");
    
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
        Vector neighboursLabel[];
        boolean finishedNode[];
        int neighboursLink[]=new int[this.getArity()];
        Vector name=new Vector(2);
        
        for (int i=0; i<this.getArity();i++)
            neighboursLink[i]=0;
        
        neighboursLabel=new Vector[this.getArity()];
        finishedNode=new boolean[this.getArity()];
        
        for (int i=0;i<this.getArity();i++)
            finishedNode[i]=false;
        
        
        name.add(this.getId());
        name.add(new Integer(this.nNode));
        
        String disp=new String("(A , "+name.elementAt(0)+")");
        this.putProperty("label",new String(disp));
        
        while(run){
            
            synchro=this.starSynchro(finishedNode);
            if (synchro==-3) {
                run=false;
		// breakSynchro();
	    }
            else
                if (synchro==this.starCenter){
                    int neighbourX=-1;
                    //int neighbourM=-1;
                    int nbreNM=0;
                    int nbreF=0;
                    boolean existLowerOrHigher=false;
                    
                    for (int door=0;door<this.getArity();door++){
                        if (!finishedNode[door]) {
                            neighboursLabel[door]=new Vector(((VectorMessage) this.receiveFrom(door)).data());
                            
                            if ((((Integer)neighboursLabel[door].elementAt(1)).intValue()!=this.fNode) &&
                            (((Integer)neighboursLabel[door].elementAt(0)).intValue()> ((Integer)name.elementAt(0)).intValue()))
                                neighbourX=door;
                            else
                                if (((Integer)neighboursLabel[door].elementAt(0)).intValue()!=((Integer)name.elementAt(0)).intValue())
                                    existLowerOrHigher=true;
                            
                            if (((Integer)neighboursLabel[door].elementAt(1)).intValue()==this.fNode)
                                nbreF++;
                            
                            if ((((Integer)neighboursLabel[door].elementAt(1)).intValue()!=this.fNode) &&
                            (neighboursLink[door]==((Integer)name.elementAt(0)).intValue()))
                                nbreNM++;
                        }
                    }
                    
                    if ((((Integer)name.elementAt(1)).intValue()==this.nNode) &&
                    (nbreF==this.getArity()))
                        run=false;
                    
                    if (neighbourX !=-1) {
                        name.setElementAt(new Integer(((Integer)neighboursLabel[neighbourX].elementAt(0)).intValue()),0);
                        
                        for (int door=0;door<this.getArity();door++)
                            if (neighboursLink[door]<((Integer)name.elementAt(0)).intValue())
                                this.setDoorState(new MarkedState(false),door);
                        
                        String display;
                        display=new String("(A' , "+name.elementAt(0)+")");
                        this.putProperty("label",new String(display));
                        this.setDoorState(new MarkedState(true),neighbourX);
                        name.setElementAt(new Integer(this.mNode),1);
                        
                        neighboursLink[neighbourX]=((Integer)name.elementAt(0)).intValue();
                        
                        for (int door=0;door<this.getArity();door++)
                            if (door!=neighbourX)
                                this.sendTo(door,new IntegerMessage(new Integer(0),labels));
                            else {
                                this.sendTo(door,new IntegerMessage(new Integer(1),labels));
                            }
                        
                    }
                    else {
                        if ((((Integer)name.elementAt(1)).intValue()==this.mNode) &&
                        (nbreNM <=1) && (! existLowerOrHigher)) {
                            
                            String display;
                            display=new String("(F , "+name.elementAt(0)+")");
                            this.putProperty("label",new String(display));
                            name.setElementAt(new Integer(this.fNode),1);
                            
                            for (int door=0;door<this.getArity();door++)
                                this.sendTo(door,new IntegerMessage(new Integer(0),labels));
                            
                        }
                        else
                            for (int door=0;door<this.getArity();door++)
                                this.sendTo(door,new IntegerMessage(new Integer(0),labels));
                    }
                    
                    this.breakSynchro();
                }
                else
                    if (synchro!=this.notInTheStar) {
                        Integer change;
                        
                        this.sendTo(synchro,new VectorMessage((Vector)name.clone(),labels));
                        change=((IntegerMessage) this.receiveFrom(synchro)).data();
                        if (change.intValue()==1)
                            neighboursLink[synchro]=((Integer)name.elementAt(0)).intValue();
                    }
        }
        //printStatistics();
        this.sendAll(new IntegerMessage(new Integer(-3),termination));
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    
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
                        Message msg=this.receiveFrom(i);
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
                        Message msg=this.receiveFrom(i);
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
        return new Spanning_Tree_ID_With_Termination();
    }
}

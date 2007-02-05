
package visidia.algoRMI;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_ID_With_Termination extends AlgorithmDist {
    
    /* R1: (X,i)-0-(Y,j)  ---> (X,i)-i-(M,i) ; j<i ; X=N,M & Y=N,M,F
       R2:   (M,i)    --->   (F,i)  , (M,i)-0-(Y,j) [j!=i];(X,i)-1-(M,i)-1-(m,i)
     */
    
    final private int starCenter=-1;
    final private int notInTheStar=-2;
    final private int nNode=0;//new String("N");
    final private int mNode=1;//new String("M");
    final private int fNode=2;//new String("F");
    
    private boolean run=true; /* booleen de fin  de l'algorithme */

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    static MessageType termination = new MessageType("termination", false, java.awt.Color.green);
    static MessageType labels = new MessageType("labels", true);
    
    public Spanning_Tree_ID_With_Termination () {
	addMessageType(synchronization);
	addMessageType(termination);
	addMessageType(labels);
    }
    
    public void init(){
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        //boolean run=true; /* booleen de fin  de l'algorithme */
        Vector neighboursLabel[];
        boolean finishedNode[];
        int neighboursLink[]=new int[getArity()];
        Vector name=new Vector(2);
        
        for (int i=0; i<getArity();i++)
            neighboursLink[i]=0;
        
        neighboursLabel=new Vector[getArity()];
        finishedNode=new boolean[getArity()];
        
        for (int i=0;i<getArity();i++)
            finishedNode[i]=false;
        
        
        name.add(getId());
        name.add(new Integer(nNode));
        
        String disp=new String("(A , "+(Integer)name.elementAt(0)+")");
        putProperty("label",new String(disp));
        
        while(run){
            
            synchro=starSynchro(finishedNode);
            if (synchro==-3)
                run=false;
            else
                if (synchro==starCenter){
                    int neighbourX=-1;
                    //int neighbourM=-1;
                    int nbreNM=0;
                    int nbreF=0;
                    boolean existLowerOrHigher=false;
                    
                    for (int door=0;door<getArity();door++){
                        if (!finishedNode[door]) {
                            neighboursLabel[door]=new Vector(((VectorMessage) receiveFrom(door)).data());
                            
                            if ((((Integer)neighboursLabel[door].elementAt(1)).intValue()!=fNode) &&
                            (((Integer)neighboursLabel[door].elementAt(0)).intValue()> ((Integer)name.elementAt(0)).intValue()))
                                neighbourX=door;
                            else
                                if (((Integer)neighboursLabel[door].elementAt(0)).intValue()!=((Integer)name.elementAt(0)).intValue())
                                    existLowerOrHigher=true;
                            
                            if (((Integer)neighboursLabel[door].elementAt(1)).intValue()==fNode)
                                nbreF++;
                            
                            if ((((Integer)neighboursLabel[door].elementAt(1)).intValue()!=fNode) &&
                            (neighboursLink[door]==((Integer)name.elementAt(0)).intValue()))
                                nbreNM++;
                        }
                    }
                    
                    if ((((Integer)name.elementAt(1)).intValue()==nNode) &&
                    (nbreF==getArity()))
                        run=false;
                    
                    if (neighbourX !=-1) {
                        name.setElementAt(new Integer(((Integer)neighboursLabel[neighbourX].elementAt(0)).intValue()),0);
                        
                        for (int door=0;door<getArity();door++)
                            if (neighboursLink[door]<((Integer)name.elementAt(0)).intValue())
                                setDoorState(new MarkedState(false),door);
                        
                        String display;
                        display=new String("(A' , "+(Integer)name.elementAt(0)+")");
                        putProperty("label",new String(display));
                        setDoorState(new MarkedState(true),neighbourX);
                        name.setElementAt(new Integer(mNode),1);
                        
                        neighboursLink[neighbourX]=((Integer)name.elementAt(0)).intValue();
                        
                        for (int door=0;door<getArity();door++)
                            if (door!=neighbourX)
                                sendTo(door,new IntegerMessage(new Integer(0),labels));
                            else {
                                sendTo(door,new IntegerMessage(new Integer(1),labels));
                            }
                        
                    }
                    else {
                        if ((((Integer)name.elementAt(1)).intValue()==mNode) &&
                        (nbreNM <=1) && (! existLowerOrHigher)) {
                            
                            String display;
                            display=new String("(F , "+(Integer)name.elementAt(0)+")");
                            putProperty("label",new String(display));
                            name.setElementAt(new Integer(fNode),1);
                            
                            for (int door=0;door<getArity();door++)
                                sendTo(door,new IntegerMessage(new Integer(0),labels));
                            
                        }
                        else
                            for (int door=0;door<getArity();door++)
                                sendTo(door,new IntegerMessage(new Integer(0),labels));
                    }
                    
                    breakSynchro();
                }
                else
                    if (synchro!=notInTheStar) {
                        Integer change;
                        
                        sendTo(synchro,new VectorMessage((Vector)name.clone(),labels));
                        change=((IntegerMessage) receiveFrom(synchro)).data();
                        if (change.intValue()==1)
                            neighboursLink[synchro]=((Integer)name.elementAt(0)).intValue();
                    }
        }
        //printStatistics();
        sendAll(new IntegerMessage(new Integer(-3),termination));
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        boolean theEnd=false;
        
        
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
                sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
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
                    setDoorState(new SyncState(true),door);
            }
            
            
            if (! theEnd) {
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
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        sendTo(i,new IntegerMessage(new Integer(-3),termination));
                    }
                }
                
                for (int i=0;i<arite;i++) {
                    if (! finishedNode[i]) {
                        Message msg=receiveFrom(i);
                    }
                }
                
                return -3;
            }
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
                    else
                        if (((IntegerMessage)msg).value() == -3) {
                            finishedNode[i]=true;
                            theEnd=true;
                        }
                }
            }
            if (inTheStar!=notInTheStar)
                return inTheStar;
            else
                if (theEnd)
                    return -3;
                else
                    return notInTheStar;
            
        }
    }
    
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Spanning_Tree_ID_With_Termination();
    }
}

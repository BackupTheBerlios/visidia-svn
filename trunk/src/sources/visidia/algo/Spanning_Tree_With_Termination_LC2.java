
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_With_Termination_LC2 extends Algorithm {
    
    /* R1: A-0-N  ---> A-1-M
       R2: M-0-N  ---> M-1-M
       R3:   M    --->   F  , M-0-N;m-1-M-1-m;A-1-M-1-m
     */
    
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
        
        int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        String neighboursLabel[];
        boolean neighboursLink[];
        boolean finishedNode[];
        String lastName;
        int pere=-1;
        
        neighboursLink=new boolean[getArity()];
        for (int i=0; i<getArity();i++)
            neighboursLink[i]=false;
        
        finishedNode= new boolean[getArity()];
        for (int i=0;i<getArity();i++)
            finishedNode[i]=false;
        
        neighboursLabel=new String[getArity()];
        
        while(run){
            synchro=starSynchro(finishedNode);
            if (synchro==-3) {
                run=false;
		//breakSynchro();
	    }
            else
                if (synchro==starCenter){
                    int neighbourAM=-1;
                    int nbreN=0;
                    int nbreF=0;
                    int nbreLinkAM=0;
                    
                    for (int door=0;door<getArity();door++)
                        if (!finishedNode[door]) {
                            neighboursLabel[door]=((StringMessage) receiveFrom(door)).data();
                            
                            if ((neighboursLabel[door].compareTo(aNode)==0) ||
                            (neighboursLabel[door].compareTo(mNode)==0)) {
                                neighbourAM=door;
                                if (neighboursLink[door])
                                    nbreLinkAM++;
                            }
                            
                            if (neighboursLabel[door].compareTo(nNode)==0)
                                nbreN++;
                            
                            if (neighboursLabel[door].compareTo(fNode)==0)
                                nbreF++;
                            
                        }
                    if ((((String) getProperty("label")).compareTo(aNode)==0) &&
                    (nbreF==getArity()))
                        run=false;
                    
                    if ((((String) getProperty("label")).compareTo(nNode)==0) &&
                    (neighbourAM !=-1)) {
                        putProperty("label",new String(mNode));
                        setDoorState(new MarkedState(true),neighbourAM);
                        neighboursLink[neighbourAM]=true;
                        for (int door=0;door<getArity();door++)
                            if (!finishedNode[door]) {
                                if (door!=neighbourAM) {
                                    sendTo(door,new IntegerMessage(new Integer(0),labels));
                                }
                                else {
                                    sendTo(door,new IntegerMessage(new Integer(1),labels));
                                }
                            }
                    }
                    else {
                        if ((((String) getProperty("label")).compareTo(mNode)==0) &&
                        (nbreN==0) && (nbreLinkAM<=1)) {
                            putProperty("label",new String(fNode));
                            for (int door=0;door<getArity();door++)
                                if (!finishedNode[door]) {
                                    sendTo(door,new IntegerMessage(new Integer(0),labels));
                                }
                        }
                        else
                            for (int door=0;door<getArity();door++)
                                if (!finishedNode[door]) {
                                    sendTo(door,new IntegerMessage(new Integer(0),labels));
                                }
                    }
                    breakSynchro();
                }
                else
                    if (synchro!=notInTheStar) {
                        String newState;
                        int linkOn;
                        sendTo(synchro,new StringMessage((String) getProperty("label"),labels));
                        linkOn=((IntegerMessage) receiveFrom(synchro)).value();
                        if (linkOn==1)
                            neighboursLink[synchro]=true;
                    }
        }
        sendAll(new IntegerMessage(new Integer(-3),termination));
    }
    
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
        return new Spanning_Tree_With_Termination_LC2();
    }
}












package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_Dijkstra_Feijen_VanGastren extends Algorithm {
    
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    static MessageType termination = new MessageType("termination", false, java.awt.Color.green);
    static MessageType labels = new MessageType("labels", true);
    
    final StringMessage end=new StringMessage(new String("End"),labels);
    final StringMessage activate=new StringMessage(new String("Ac"),labels);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        typesList.add(termination);
        return typesList;
    }
    
    public void init(){
        
        final String active=new String("Ac");
        final String passive=new String("Pa");
        final String nodeA=new String("A");
        final String nodeN=new String("N");
        
        String myStates=nodeN;
        String state=active;
        boolean initial=false;
        int synchro;
        boolean run=true;
        String neighbours[];
        boolean finishedNode[];
        String myState=new String("N,M,Ac,b,NT");
        boolean myToken=false;
        String myLabelD=new String("M"), myColorD=new String("b");
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        neighbours=new String[getArity()];
        
        if (((String) getProperty("label")).compareTo("A")==0) {
            state=active;
            myLabelD=new String("A");
            myStates=nodeA;
            myState=myStates+","+myLabelD+","+state+","+myColorD+",";
            myState=myState+"HT";
            myToken=true;
        }
        putProperty("label",new String(myState));
        
        while(run){
            
            synchro=starSynchro(finishedNode);
            if (synchro==-3)
                run=false;
            else
                if (synchro == starCenter){
                    int neighbourN=-1,neighbourA=-1;
                    String label[],labelD[],aP[], cD[], tD[];
                    
                    label=new String[getArity()];
                    labelD=new String[getArity()];
                    aP=new String[getArity()];
                    cD=new String[getArity()];
                    tD=new String[getArity()];
                    
                    for (int door=0;door<getArity();door++)
                        if (!finishedNode[door]) {
                            neighbours[door]=((StringMessage) receiveFrom(door)).data();
                            
                            label[door]=new String(neighbours[door].substring(0,1));
                            labelD[door]=new String(neighbours[door].substring(2,3));
                            aP[door]=new String(neighbours[door].substring(4,6));
                            cD[door]=new String(neighbours[door].substring(7,8));
                            tD[door]=new String(neighbours[door].substring(9,11));
                            
                            if (label[door].compareTo(nodeN)==0)
                                neighbourN=door;
                            
                            if (label[door].compareTo(nodeA)==0)
                                neighbourA=door;
                            
                        }
                    
                    //System.out.println(getId()+" : "+neighbourN);
                    
                    if ((myStates.compareTo(nodeA)==0) && (neighbourN !=-1)) {
                        setDoorState(new MarkedState(true),neighbourN);
                        sendTo(neighbourN,activate);
                    }
                    else
                        if ((myStates.compareTo(nodeA)==0) && (neighbourN==-1) && (state.compareTo(active)==0)) {
                            state=passive;
                        }
                        else
                            if ((myStates.compareTo(nodeN)==0) && (neighbourA !=-1)) {
                                myStates=nodeA;
                                setDoorState(new MarkedState(true),neighbourA);
                            }
                    
                    if (myToken) {
                        if (aP[nextDoor()].compareTo("Pa")==0) {
                            sendTo(nextDoor(),new StringMessage(new String(myColorD),labels));
                            myColorD=new String("w");
                            myToken=false;
                        }
                    }
                    
                    for( int i = 0; i < getArity(); i++){
                        if (! finishedNode[i]) {
                            sendTo(i,end);
                        }
                    }
                    
                    myState=myStates+","+myLabelD+","+state+","+myColorD+",";
                    
                    if (myToken)
                        myState=myState+"HT";
                    else
                        myState=myState+"NT";
                    
                    putProperty("label",myState);
                    
                    
                    breakSynchro();
                }
            
                else
                    if (synchro!=notInTheStar) {
                        String son;
                        
                        sendTo(synchro,new StringMessage(myState,labels));
                        
                        son=((StringMessage)receiveFrom(synchro)).data();
                        
                        while (son.compareTo(end.data())!=0) {
                            if (son.compareTo(activate.data())!=0) {
                                if (myLabelD.compareTo("A")!=0)
                                    if (son.compareTo("b")==0)
                                        myColorD=new String("b");
                                if (myLabelD.compareTo("A")==0)
                                    if (son.compareTo("b")==0)
                                        myColorD=new String("w");
                                    else
                                        if (myColorD.compareTo("w")==0)
                                            run =false;
                                myToken=true;
                            }
                            else {
                                myStates=nodeA;
                            }
                            son=((StringMessage)receiveFrom(synchro)).data();
                        }
                        
                        myState=myStates+","+myLabelD+","+state+","+myColorD+",";
                        
                        if (myToken)
                            myState=myState+"HT";
                        else
                            myState=myState+"NT";
                        
                        putProperty("label",myState);
                        
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
        return new Spanning_Tree_Dijkstra_Feijen_VanGastren();
    }
}

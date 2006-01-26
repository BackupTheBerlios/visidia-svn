package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;

public class Spanning_Tree__Dijkstra_Scholten_LC2 extends Algorithm {
    
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    
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
        
        final String active=new String("Ac");
        final String passive=new String("Pa");
        final Integer p0=new Integer(-1);
        final StringMessage mes=new StringMessage(new String("mes"),labels);
        final StringMessage noMes=new StringMessage(new String("No Mes"),labels);
        final StringMessage sig=new StringMessage(new String("sig"),labels);
        final String nodeA=new String("A");
        final String nodeN=new String("N");
        
        String myState=nodeN;
        String state=passive;
        int sc=0;
        Integer father=null;
        boolean initial=false;
        int synchro;
        boolean run=true;
        String neighboursLabel;
        boolean finishedNode[];
        
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        // Si le neoud est p0 etiqueté avec un noeud distingué A
        if (((String) getProperty("label")).compareTo("A")==0) {
            state=active;
            father=p0;
            myState=nodeA;
            putProperty("label",new String("A : " + active+" : sc="+sc));
        }
        else
            putProperty("label",new String("N : " + passive+" : sc="+sc));
        
        while(run){
            
            synchro=starSynchro(finishedNode);
            if (synchro==-3)
                run=false;
            else
                if (synchro == starCenter){
                    int neighbourA=-1;
                    boolean neighbourN=false;
                    
                    for (int door=0;door<getArity();door++)
                        if (!finishedNode[door]) {
                            neighboursLabel=((StringMessage) receiveFrom(door)).data();
                            
                            if (neighboursLabel.compareTo(nodeA)==0)
                                neighbourA=door;
                            
                            if (neighboursLabel.compareTo(nodeN)==0)
                                neighbourN=true;
                        }
                    
                    //System.out.println(getId()+" : "+neighbourN);
                    
                    if ((myState.compareTo(nodeN)==0) && (neighbourA !=-1)) {
                        myState=nodeA;
                        state=active;
                        father=new Integer(neighbourA);
                        putProperty("label",new String("A : " + state+" : sc="+sc));
                        setDoorState(new MarkedState(true),neighbourA);
                        
                        for (int door=0;door<getArity();door++)
                            if (!finishedNode[door]) {
                                if (door==father.intValue())
                                    sendTo(door,mes);
                                else
                                    sendTo(door,noMes);
                            }
                    }
                    else {
                        if ((myState.compareTo(nodeA)==0) && (!neighbourN) && (state.compareTo(active)==0)) {
                            state=passive;
                            putProperty("label",new String("A : " + state+" : sc="+sc));
                            
                            if (sc==0)
                                if (father==p0){
                                    sendAll(noMes);
                                    run=false;
                                }
                                else {
                                    if (father != null)
                                        for (int i=0;i<getArity();i++){
                                            if (!finishedNode[i])
                                                if (i==father.intValue()) {
                                                    sendTo(i,sig);
                                                    //                           setDoorState(new MarkedState(false),i);
                                                }
                                                else
                                                    sendTo(i,noMes);
                                        }
                                    else
                                        sendAll(noMes);
                                    
                                    father=null;
                                }
                            else
                                sendAll(noMes);
                        }
                        else
                            if ((myState.compareTo(nodeA)==0) && (state.compareTo(passive)==0) && (sc==0) && (father!=null)) {
                                if (father==p0){
                                    sendAll(noMes);
                                    run=false;
                                }
                                else {
                                    for (int i=0;i<getArity();i++){
                                        if (!finishedNode[i])
                                            if (i==father.intValue()){
                                                sendTo(i,sig);
                                                //                         setDoorState(new MarkedState(false),i);
                                            }
                                            else
                                                sendTo(i,noMes);
                                    }
                                    
                                    father=null;
                                }
                            }
                            else
                                sendAll(noMes);
                    }
                    
                    breakSynchro();
                }
            
                else
                    if (synchro!=notInTheStar) {
                        String son;
                        
                        sendTo(synchro,new StringMessage(myState,labels));
                        
                        son=((StringMessage)receiveFrom(synchro)).data();
                        
                        if (son.compareTo(mes.data())==0) {
                            sc++;
                            putProperty("label",new String("A : " + state+" : sc="+sc));
                        }
                        else
                            if (son.compareTo(sig.data())==0) {
                                sc--;
                                putProperty("label",new String("A : " + state+" : sc="+sc));
                            }
                        
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
        return new Spanning_Tree__Dijkstra_Scholten_LC2();
    }
}

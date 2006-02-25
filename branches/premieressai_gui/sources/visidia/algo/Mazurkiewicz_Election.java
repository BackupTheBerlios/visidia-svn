
package visidia.algo;

import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;
import visidia.algo2.Know;

public class Mazurkiewicz_Election extends Algorithm {
    
    static int synchroNumber=0;
    static int messagesNumber=0;
    
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
        
        int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[];
        int arity=getArity();
        
        Know node=new Know();
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        putProperty("label",new String("0"));
        node.Initial(graphS);
        
        while(run){
            
            synchro=starSynchro(finishedNode);
            
            if( synchro==starCenter ){
                for (int door=0;door<arity;door++) {
                    if (!finishedNode[door])
                        receiveKnowledge(node,door);
                }
                
                if ((node.MyName() == 0) || ( node.Neighbour() < node.NeighbourNode(node.MyName()))){
                    node.ChangeName(node.Max()+1);
                }
                
                sendAll(new IntegerMessage(node.MyName(),labels));
                putProperty("label",new String((new Integer(node.MyName())).toString()));
                
                node.ChangeKnowledge(node.MyName(),node.Neighbour());
                
                messagesNumber=messagesNumber+arity;
                
                sendToAllKnowledge(node);
                
                if ( node.Max() == graphS ) {
                    run=false;
                }
                changeTable(node);
                breakSynchro();
                
            }
            else {
                if ( synchro != notInTheStar) {
                    sendKnowledge(node,synchro);
                    
                    Message newName = receiveFrom(synchro);
                    node.ChangeKnowledge(0,((IntegerMessage)newName).value());
                    putProperty("My Neighbour", new String((new Integer(node.Neighbour())).toString()));
                    receiveKnowledge(node,synchro);
                    changeTable(node);
                }
                
            }
            
            
        }
        sendAll(new IntegerMessage(new Integer(-1),synchronization));
        
        System.out.println("Nombre de starSynchro = "+synchroNumber+"   Nombre de messages = "+messagesNumber);
        
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
            synchroNumber++;
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
    
    private void receiveKnowledge(Know node,int door) {
        
        VectorMessage vm=(VectorMessage) receiveFrom(door);
        Vector data =vm.data();
        /*System.out.println("1-centre recoit de "+door+":" + ((Integer)data.elementAt(0)).intValue());*/
        while (((Integer)data.elementAt(0)).intValue() !=-1) {
            node.ChangeKnowledge(((Integer)data.elementAt(0)).intValue(), ((Integer)data.elementAt(1)).intValue());
            vm=(VectorMessage) receiveFrom(door);
            data =vm.data();
            /*	    System.out.println("1-centre recoit de "+door+":" + ((Integer)data.elementAt(0)).intValue());*/
            
        }
    }
    
    private void sendToAllKnowledge(Know node) {
        
        int arity=getArity();
        Vector vec = new Vector(2);
        for (int i=1;i<=node.Max();i++) {
            if ((node.NeighbourNode(i))!=-1) {
                vec.add(new Integer(i));
                vec.add(new Integer(node.NeighbourNode(i)));
                sendAll(new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber=messagesNumber+arity;
                /*	System.out.println("3-centre envois :"+ i +"=" + node.NeighbourNode(i));*/
                
            }
        }
        
        
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        sendAll(new VectorMessage((Vector)vec.clone(),labels));
        /* System.out.println( "4-centre envoit  -1"); */
    }
    
    private void sendKnowledge(Know node,int synchro) {
        Vector vec = new Vector(2);
        
        for (int i=1;i<=node.Max();i++) {
            if ((node.NeighbourNode(i))!=-1) {
                vec.add(new Integer(i));
                vec.add(new Integer(node.NeighbourNode(i)));
                sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber++;
            }
        }
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels));
    }
    
    private void changeTable (Know node) {
	for (int i=1;i<=node.Max();i++) {
	    if ((node.NeighbourNode(i))!=-1) {
		Integer nodeI=new Integer(i);
		putProperty(nodeI.toString(), new String((new Integer(node.NeighbourNode(i))).toString()));
	    } 
	}
    }
    public Object clone(){
        return new Mazurkiewicz_Election();
    }
}





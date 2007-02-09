
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import visidia.algo2.Knowledge;
import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.misc.VectorMessage;
import visidia.simulation.Algorithm;

public class Mazurkiewicz_Reconstruction extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    
    static int synchroNumber=0;
    static int messagesNumber=0;
    
    
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
        
        int graphS=this.getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[];
        int lastName;
        Knowledge node=new Knowledge();
        
        this.putProperty("label",new String("0"));
        node.initial(graphS);
        
        finishedNode=new boolean[this.getArity()];
        for (int i=0;i<this.getArity();i++) {
            finishedNode[i]=false;
        }
        
        while(run){
            
            synchro=this.starSynchro(finishedNode);
            if( synchro==this.starCenter ){
                
                for (int door=0;door<this.getArity();door++) {
                    if (!finishedNode[door])
                        this.receiveKnowledge(node,door);
                }
                lastName=node.myName();
                
                if ((node.myName() == 0) || (node.maxSet(node.neighbourNode(node.myName()),node.neighbour()))){
                    node.changeName(node.max()+1);
                }
                
                Vector nameVector=new Vector(2);
                nameVector.add(new Integer(node.myName()));
                nameVector.add(new Integer(lastName));
                
                
                this.sendAll(new VectorMessage(nameVector,labels));
                messagesNumber+=this.getArity();
                this.putProperty("label",new String((new Integer(node.myName())).toString()));
                
                Vector vec=this.addVector(node.myName(),node.neighbour());
                
                node.changeKnowledge(vec);
                
                
                this.broadcastKnowledge(node);
                
                this.changeTable(node);
                if ( node.endKnowledge(graphS) ) {
                    run=false;
                }
                this.breakSynchro();
                
            }
            else {
                if ( synchro != this.notInTheStar) {
                    
                    this.sendKnowledge(node,synchro);
                    
                    Message newName = this.receiveFrom(synchro);
                    node.changeNeighbours(((VectorMessage)newName).data());
                    this.putProperty("My Neighbours", node.neighbour());
                    this.receiveKnowledge(node,synchro);
                    this.changeTable(node);
                }
                
            }
            
            
        }
        
        this.sendAll(new IntegerMessage(new Integer(-1),synchronization));
        System.out.println("Nombre de starSynchro = "+synchroNumber+"   Nombre de messages = "+messagesNumber);
        
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
            synchroNumber++;
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
    
    private void receiveKnowledge(Knowledge node,int door) {
        int prop;
        VectorMessage vm=(VectorMessage) this.receiveFrom(door);
        Vector data =vm.data();
        while (((Integer)data.elementAt(0)).intValue() !=-1) {
            node.changeKnowledge(data);
            prop=((Integer)data.elementAt(0)).intValue();
            vm=(VectorMessage) this.receiveFrom(door);
            data =vm.data();
        }
    }
    
    private void broadcastKnowledge(Knowledge node) {
        
        Vector vec = new Vector();
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                vec=this.addVector(i,node.neighbourNode(i));
                this.sendAll(new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber+=this.getArity();
            }
        }
        
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        this.sendAll(new VectorMessage((Vector)vec.clone(),labels));
    }
    
    private void sendKnowledge(Knowledge node,int synchro) {
        Vector vec = new Vector();
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                vec=this.addVector(i,node.neighbourNode(i));
                this.sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber++;
            }
        }
        
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        this.sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels));
        
    }
    
    private void changeTable(Knowledge node) {
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                Integer nodeI=new Integer(i);
                this.putProperty(nodeI.toString(), node.neighbourNode(i));
            }
        }
    }
    
    private Vector addVector(int num, Vector vec){
        Vector intVec=new Vector();
        
        intVec.add(new Integer(num));
        if (vec!=null) {
            for (int i=0;i<vec.size();i++)
                intVec.add(vec.elementAt(i));
            
        }
        return intVec;
        
    }
    
    
    
    public Object clone(){
        return new Mazurkiewicz_Reconstruction();
    }
}

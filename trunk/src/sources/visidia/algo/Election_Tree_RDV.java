
package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import visidia.misc.IntegerMessage;
import visidia.misc.IntegerMessageCriterion;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Election_Tree_RDV extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection<MessageType> typesList = new LinkedList<MessageType>();
        typesList.add(synchronization);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    public void init(){
        
        final String nodeN=new String("N");
        final String nodeF=new String("F");
        final String nodeE=new String("E");
        
        //final int neighbour=getArity();
        
        String neighbourState;
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[]=new boolean[this.getArity()];
        int nbr_arity=this.getArity();
        
        this.putProperty("label",new String(nodeN));
        
        for (int i=0;i<this.getArity();i++)
            finishedNode[i]=false;
        
        
        while(run){
            synchro=this.synchronization(finishedNode);
            
            if (nbr_arity==1) {
                this.putProperty("label",new String(nodeF));
                run=false;
            }
            
            this.sendTo(synchro,new StringMessage((String) this.getProperty("label"),labels)) ;
            neighbourState=((StringMessage) this.receiveFrom(synchro)).data();
            
            if (neighbourState.compareTo(nodeF)==0) {
                if (((String) this.getProperty("label")).compareTo(nodeF)!=0) {
                    nbr_arity--;
                    finishedNode[synchro]=true;
                }
                else {
                    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
                    this.sendTo(synchro,new IntegerMessage(new Integer(choosenNumber)));
                    Message msg = this.receiveFrom(synchro);
                    int answer= ((IntegerMessage)msg).value();
                    
                    if (choosenNumber>answer) {
                        this.putProperty("label",new String(nodeE));
                        run=false;
                    }
                    else
                        if (choosenNumber<answer)
                            run=false;
                        else
                            this.putProperty("label",new String(nodeN));
                }
                
            }
        }
    }
    
    
    public int synchronization(boolean finishedNode[]){
        int i = -1;
        int a =this.getArity();
        
        //interface graphique:je ne suis plus synchro
        for(int door=0;door < a;door++)
            this.setDoorState(new SyncState(false),door);
        
        while(i <0){
            i = this.trySynchronize(finishedNode);
        }
        //interface graphique: je suis synchro sur la porte i
        this.setDoorState(new SyncState(true),i);
        return i;
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(boolean finishedNode[]){
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        
        /*choice of the neighbour*/
        
        Random generator = new Random();
        int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        
        while (finishedNode[choosenNeighbour]) {
            generator = new Random();
            choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        }
        
        this.sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),synchronization));
        for(int i=0; i < arite; i++){
            if (i != choosenNeighbour)
                if (!finishedNode[i])
                    this.sendTo(i, new IntegerMessage(new Integer(0),synchronization));
            
        }
        
        for( int i = 0; i < arite; i++){
            if (!finishedNode[i]) {
                Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
                IntegerMessage smsg = (IntegerMessage) msg;
                
                answer[i]= smsg.value();
            }
        }
        
        if (answer[choosenNeighbour] == 1){
            return choosenNeighbour;
        }
        else {
            return -1 ;
        }
    }
    
    
    public Object clone(){
        return new Election_Tree_RDV();
    }
}













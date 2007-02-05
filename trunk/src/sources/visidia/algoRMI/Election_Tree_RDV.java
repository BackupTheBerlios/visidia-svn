
package visidia.algoRMI;

import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Election_Tree_RDV extends AlgorithmDist {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Election_Tree_RDV() {
	addMessageType(synchronization);
	addMessageType(labels);
    }
    
    public void init(){
        
        final String nodeN=new String("N");
        final String nodeF=new String("F");
        final String nodeE=new String("E");
        
        //final int neighbour=getArity();
        
        String neighbourState;
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        boolean finishedNode[]=new boolean[getArity()];
        int nbr_arity=getArity();
        
        putProperty("label",new String(nodeN));
        
        for (int i=0;i<getArity();i++)
            finishedNode[i]=false;
        
        
        while(run){
            synchro=synchronization(finishedNode);
            
            if (nbr_arity==1) {
                putProperty("label",new String(nodeF));
                run=false;
            }
            
            sendTo(synchro,new StringMessage((String) getProperty("label"),labels)) ;
            neighbourState=((StringMessage) receiveFrom(synchro)).data();
            
            if (neighbourState.compareTo(nodeF)==0) {
                if (((String) getProperty("label")).compareTo(nodeF)!=0) {
                    nbr_arity--;
                    finishedNode[synchro]=true;
                }
                else {
                    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
                    sendTo(synchro,new IntegerMessage(new Integer(choosenNumber)));
                    Message msg = receiveFrom(synchro);
                    int answer= ((IntegerMessage)msg).value();
                    
                    if (choosenNumber>answer) {
                        putProperty("label",new String(nodeE));
                        run=false;
                    }
                    else
                        if (choosenNumber<answer)
                            run=false;
                        else
                            putProperty("label",new String(nodeN));
                }
                
            }
        }
    }
    
    
    public int synchronization(boolean finishedNode[]){
        int i = -1;
        int a =getArity();
        
        //interface graphique:je ne suis plus synchro
        for(int door=0;door < a;door++)
            setDoorState(new SyncState(false),door);
        
        while(i <0){
            i = trySynchronize(finishedNode);
        }
        //interface graphique: je suis synchro sur la porte i
        setDoorState(new SyncState(true),i);
        incrementSynch();
	return i;
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(boolean finishedNode[]){
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*choice of the neighbour*/
        
        Random generator = new Random();
        int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        
        while (finishedNode[choosenNeighbour]) {
            generator = new Random();
            choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        }
        
        sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),synchronization));
        for(int i=0; i < arite; i++){
            if (i != choosenNeighbour)
                if (!finishedNode[i])
                    sendTo(i, new IntegerMessage(new Integer(0),synchronization));
            
        }
        
        for( int i = 0; i < arite; i++){
            if (!finishedNode[i]) {
                Message msg = receiveFrom(i,new IntegerMessageCriterion());
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













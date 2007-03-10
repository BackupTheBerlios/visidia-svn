
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import visidia.misc.IntegerMessage;
import visidia.misc.IntegerMessageCriterion;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.simulation.Algorithm;

public class Spanning_Tree_RDV extends Algorithm {
    
    /* R1: A-0-N  ---> A-1-A
     */
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
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        String neighbourValue;
        
        while(run){
            
            synchro=this.synchronization();
            
            this.sendTo(synchro,new StringMessage((String) this.getProperty("label"),labels));
            neighbourValue=((StringMessage) this.receiveFrom(synchro)).data();
            
            if ((neighbourValue.compareTo("A")==0) &&
            (((String) this.getProperty("label")).compareTo("N")==0)) {
                this.putProperty("label",new String("A"));
                this.setDoorState(new MarkedState(true),synchro);
            }
            
        }
        //printStatistics();
    }
    
    
    public int synchronization(){
        int i = -1;
        int a =this.getArity();
        
        //interface graphique:je ne suis plus synchro
        for(int door=0;door < a;door++)
            this.setDoorState(new SyncState(false),door);
        
        while(i <0){
            i = this.trySynchronize();
        }
        //interface graphique: je suis synchro sur la porte i
        this.setDoorState(new SyncState(true),i);
        return i;
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(){
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        
        /*choice of the neighbour*/
        Random generator = new Random();
        int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        
        this.sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),synchronization));
        for(int i=0; i < arite; i++){
            if( i != choosenNeighbour)
                this.sendTo(i, new IntegerMessage(new Integer(0),synchronization));
            
        }
        
        for( int i = 0; i < arite; i++){
            Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
            IntegerMessage smsg = (IntegerMessage) msg;
            
            answer[i]= smsg.value();
        }
        
        if (answer[choosenNeighbour] == 1){
            return choosenNeighbour;
        }
        else {
            return -1 ;
        }
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Spanning_Tree_RDV();
    }
}

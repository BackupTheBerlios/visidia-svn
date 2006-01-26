
package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;

public class Spanning_Tree_RDV extends Algorithm {
    
    /* R1: A-0-N  ---> A-1-A
     */
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
        String neighbourValue;
        
        while(run){
            
            synchro=synchronization();
            
            sendTo(synchro,new StringMessage((String) getProperty("label"),labels));
            neighbourValue=((StringMessage) receiveFrom(synchro)).data();
            
            if ((neighbourValue.compareTo("A")==0) &&
            (((String) getProperty("label")).compareTo("N")==0)) {
                putProperty("label",new String("A"));
                setDoorState(new MarkedState(true),synchro);
            }
            
        }
        //printStatistics();
    }
    
    
    public int synchronization(){
        int i = -1;
        int a =getArity();
        
        //interface graphique:je ne suis plus synchro
        for(int door=0;door < a;door++)
            setDoorState(new SyncState(false),door);
        
        while(i <0){
            i = trySynchronize();
        }
        //interface graphique: je suis synchro sur la porte i
        setDoorState(new SyncState(true),i);
        return i;
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(){
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*choice of the neighbour*/
        Random generator = new Random();
        int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        
        sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),synchronization));
        for(int i=0; i < arite; i++){
            if( i != choosenNeighbour)
                sendTo(i, new IntegerMessage(new Integer(0),synchronization));
            
        }
        
        for( int i = 0; i < arite; i++){
            Message msg = receiveFrom(i,new IntegerMessageCriterion());
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
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Spanning_Tree_RDV();
    }
}

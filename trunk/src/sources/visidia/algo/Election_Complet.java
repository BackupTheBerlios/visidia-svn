
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;


public class Election_Complet extends Algorithm {
    
    /* R1: N-0-N  ---> N-1-F
       R2:   N    --->   E
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
    
    final String fNode=new String("F");
    final String nNode=new String("N");
    final String eNode=new String("E");
    
    public void init(){
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        String neighbourLabel;
        boolean finishedNode[]=new boolean[getArity()];
        //String lastName;
        int nb;
        //Vector name;
        
        for (int i=0; i<getArity();i++)
            finishedNode[i]=false;
        
        
        while(run){
            
            synchro=synchronization(finishedNode);
            
            nb=getArity();
            for (int i=0;i<getArity();i++)
                if (finishedNode[i])
                    nb--;
            if (nb==0) {
                putProperty("label",new String(eNode));
                break;
            }
            
            sendTo(synchro,new StringMessage((String) getProperty("label"),labels));
            neighbourLabel=((StringMessage) receiveFrom(synchro)).data();
            
            if ((((String) getProperty("label")).compareTo(nNode)==0) &&
            (neighbourLabel.compareTo(nNode)==0)) {
                
                int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
                sendTo(synchro,new IntegerMessage(new Integer(choosenNumber)));
                Message msg = receiveFrom(synchro);
                int answer= ((IntegerMessage)msg).value();
                
                if (choosenNumber<answer) {
                    putProperty("label",new String(fNode));
                    run=false;
                }
                /*else
                    if (choosenNumber>answer) {
                        finishedNode[synchro]=true;
                        nbr_arity--;
                        }*/
            }
            
            //System.out.println(nbr_arity);
            
            
            
        }
        
        for (int i=0;i<getArity();i++)
            if (!finishedNode[i])
                sendTo(i,new IntegerMessage(new Integer(-1),synchronization));
    }
    
    /**
     * essaie de synchroniser le noeud.retourne uniquement
     * si la synchronisation est reussie.
     */
    public int synchronization(boolean finishedNode[]){
        int i = -2;
        int a =getArity();
        
        //interface graphique:je ne suis plus synchro
        for(int door=0;door < a;door++)
            setDoorState(new SyncState(false),door);
        
        while (i <-1) {
            i = trySynchronize(finishedNode);
        }
        //interface graphique: je suis synchro sur la porte i
        if (i>-1)
            setDoorState(new SyncState(true),i);
        return i;
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(boolean finishedNode[]){
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        int nb;
        
        /*choice of the neighbour*/
        Random generator = new Random();
        int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        
        nb=arite;
        for (int i=0;i<arite;i++)
            if (finishedNode[i])
                nb--;
        
        if (nb==0)
            return -1;
        
        while (finishedNode[choosenNeighbour]) {
            generator = new Random();
            choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
        }
        
        sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),synchronization));
        for(int i=0; i < arite; i++){
            if( i != choosenNeighbour)
                if (!finishedNode[i])
                    sendTo(i, new IntegerMessage(new Integer(0),synchronization));
            
        }
        
        for( int i = 0; i < arite; i++){
            if (!finishedNode[i]) {
                Message msg = receiveFrom(i,new IntegerMessageCriterion());
                IntegerMessage smsg = (IntegerMessage) msg;
                
                answer[i]= smsg.value();
                if (answer[i]==-1)
                    finishedNode[i]=true;
                
            }
        }
        if (answer[choosenNeighbour] == 1){
            return choosenNeighbour;
        }
        else
            return -2 ;
        
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new MarkedState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Election_Complet();
    }
}












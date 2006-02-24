
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_LC2_V2 extends Algorithm {
    
    /* R1: N-0-A-0-N  ---> A-1-A-1-A
       R2: A-0-N-0-N  ---> A-1-A-1-A
     */
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    final String aNode=new String("A");
    final String nNode=new String("N");
    
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
        String neighboursLabel[];
        //boolean neihboursLink[];
        String lastName;
        int pere=-1;
        
        neighboursLabel=new String[getArity()];
        
        while(run){
            
            synchro=starSynchro();
            if (synchro==starCenter){
                
                int nbreN=0;
                int doorA=-1;
                
                for (int door=0;door<getArity();door++){
                    neighboursLabel[door]=((StringMessage) receiveFrom(door)).data();
                    
                    if (neighboursLabel[door].compareTo(nNode)==0)
                        nbreN++;
                    
                    if (neighboursLabel[door].compareTo(aNode)==0)
                        doorA=door;
                }
                
                if ((((String) getProperty("label")).compareTo(nNode)==0) && (doorA!=-1)) {
                    setDoorState(new MarkedState(true),doorA);
                    putProperty("label",new String(aNode));
                }
                if ((((String) getProperty("label")).compareTo(aNode)==0) &&
                (nbreN!=0)) {
                    for (int door=0;door<getArity();door++) {
                        if (neighboursLabel[door].compareTo(nNode)==0) {
                            setDoorState(new MarkedState(true),door);
                        }
                        sendTo(door,new StringMessage(aNode,labels));
                    }
                }
                else
                    for (int door=0;door<getArity();door++) {
                        sendTo(door,new StringMessage(neighboursLabel[door],labels));
                    }
                breakSynchro();
            }
            else
                if (synchro!=notInTheStar) {
                    String newState;
                    sendTo(synchro,new StringMessage(((String) getProperty("label")),labels));
                    newState=((StringMessage) receiveFrom(synchro)).data();
                    putProperty("label",new String(newState));
                }
        }
        //printStatistics();
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    public int starSynchro(){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
        
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        sendAll(new IntegerMessage(new Integer(max),synchronization));
        
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                setDoorState(new SyncState(true),door);
            }
            
            
            sendAll(new IntegerMessage(1,synchronization));
            
            for (int i=0;i<arite;i++) {
                Message msg=receiveFrom(i);
            }
            
            return starCenter;
        }
        else {
            int inTheStar=notInTheStar;
            
            sendAll(new IntegerMessage(0,synchronization));
            
            for (int i=0; i<arite;i++) {
                Message msg=receiveFrom(i);
                if  (((IntegerMessage)msg).value() == 1) {
                    inTheStar=i;
                }
            }
            return inTheStar;
            
        }
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Spanning_Tree_LC2_V2();
    }
}












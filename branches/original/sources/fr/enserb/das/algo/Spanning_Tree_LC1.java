
package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;

public class Spanning_Tree_LC1 extends Algorithm {
    
    /* R1: N-0-A-0-N  ---> A-1-A-1-A
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
        Vector synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        String neighboursLabel[];
        //boolean neihboursLink[];
        String lastName;
        int pere=-1;
        
        neighboursLabel=new String[getArity()];
        
        while(run){
            
            synchro=starSynchro();
            if (synchro != null ){
                if (((Integer) synchro.elementAt(0)).intValue()==-1) {
                    int doorA=-1;
                    
                    for (int door=0;door<getArity();door++){
                        neighboursLabel[door]=((StringMessage) receiveFrom(door)).data();
                        
                        if (neighboursLabel[door].compareTo(aNode)==0)
                            doorA=door;
                        
                    }
                    
                    if ((((String) getProperty("label")).compareTo(nNode)==0) &&
                    (doorA!=-1)) {
                        setDoorState(new MarkedState(true),doorA);
                        putProperty("label",new String(aNode));
                    }
                    breakSynchro();
                }
                else {
                    for (int i=0;i<synchro.size();i++)
                        sendTo(((Integer) synchro.elementAt(i)).intValue(),new StringMessage(((String) getProperty("label")),labels));
                }
            }
        }
        //printStatistics();
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une étoile
     */
    public Vector starSynchro(){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        Vector neighbourCenter;
        
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
        
        if (choosenNumber >= max) {
            for( int door = 0; door < getArity(); door++){
                setDoorState(new SyncState(true),door);
            }
            
            sendAll(new IntegerMessage(new Integer(1),synchronization));
            
            for (int i=0;i<arite;i++) {
                Message msg=receiveFrom(i);
            }
            neighbourCenter=new Vector();
            neighbourCenter.add(new Integer(-1));
            
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            sendAll(new IntegerMessage(new Integer(0),synchronization));
            
            for (int i=0; i<arite;i++) {
                Message msg=receiveFrom(i);
                if  (((IntegerMessage)msg).value() == 1) {
                    neighbourCenter.add(new Integer(i));
                }
            }
            if (neighbourCenter.size()==0)
                neighbourCenter=null;
            
            return neighbourCenter;
            
        }
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Spanning_Tree_LC1();
    }
}












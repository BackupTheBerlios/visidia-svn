
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_ID_LC2_V2 extends Algorithm {
    
    /* R1: k-0-i-0-j  ---> max-1-max-1-max  // j<i
     */
    
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
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        int neighboursLink[]=new int[getArity()];
        int name;
        
        for (int i=0; i<getArity();i++)
            neighboursLink[i]=0;
        
        name=getId().intValue();
        
        String disp=new String(new Integer(name).toString());
        putProperty("label",new String(disp));
        
        while(run){
            
            synchro=starSynchro();
            if (synchro==starCenter){
                int neighbourValue[]=new int[getArity()];
                int neighbourDoor=-1;
                int max=name;
                boolean existHigher=false;
                
                for (int door=0;door<getArity();door++){
                    neighbourValue[door]=(((IntegerMessage) receiveFrom(door)).data()).intValue();
                    
                    if (neighbourValue[door] > max) {
                        neighbourDoor=door;
                        max=neighbourValue[door];
                    }
                }
                
                if (neighbourDoor !=-1) {
                    name=neighbourValue[neighbourDoor];
                    String display=new String(new Integer(name).toString());
                    putProperty("label",new String(display));
                    setDoorState(new MarkedState(true),neighbourDoor);
                }
                
                for (int i=0;i<getArity();i++)
                    if (neighbourValue[i]< name)
                        setDoorState(new MarkedState(true),i);
                
                sendAll(new IntegerMessage(new Integer(name),labels));
                
                breakSynchro();
            }
            else
                if (synchro!=notInTheStar) {
                    Integer newName;
                    
                    sendTo(synchro,new IntegerMessage(new Integer(name),labels));
                    newName=((IntegerMessage) receiveFrom(synchro)).data();
                    if (newName.intValue()!=name) {
                        //neighboursLink[synchro]=newName.intValue();
                        for (int i=0;i<getArity();i++)
                            if (i!=synchro)
                                setDoorState(new MarkedState(false),i);
                        String display=new String(newName.toString());
                        putProperty("label",new String(display));
                        name=newName.intValue();
                    }
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
        return new Spanning_Tree_ID_LC2_V2();
    }
}

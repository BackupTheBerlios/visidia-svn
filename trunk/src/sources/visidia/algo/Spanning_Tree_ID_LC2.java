
package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Spanning_Tree_ID_LC2 extends Algorithm {
    
    /* R1: (N,i)-0-(N,j)  ---> (N,i)-1-(N,i)  // j<i
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
        int neighboursLink[]=new int[this.getArity()];
        int name;
        
        for (int i=0; i<this.getArity();i++)
            neighboursLink[i]=0;
        
        name=this.getId().intValue();
        
        Integer disp=new Integer(name);
        this.putProperty("label",disp.toString());
        
        while(run){
            
            synchro=this.starSynchro();
            if (synchro==this.starCenter){
                int neighbourValue[]=new int[this.getArity()];
                int neighbourDoor=-1;
                //boolean existHigher=false;
                
                for (int door=0;door<this.getArity();door++){
                    neighbourValue[door]=(((IntegerMessage) this.receiveFrom(door)).data()).intValue();
                    
                    if (neighbourValue[door] > name)
                        neighbourDoor=door;
                }
                
                if (neighbourDoor !=-1) {
                    name=neighbourValue[neighbourDoor];
                    disp=new Integer(name);
                    this.putProperty("label",disp.toString());
                    for (int i=0;i<this.getArity();i++)
                        if (neighboursLink[i]<name)
                            this.setDoorState(new MarkedState(false),i);
                    
                    this.setDoorState(new MarkedState(true),neighbourDoor);
                    
                    neighboursLink[neighbourDoor]=name;
                }
                
                this.sendAll(new IntegerMessage(new Integer(name),labels));
                
                this.breakSynchro();
            }
            else
                if (synchro!=this.notInTheStar) {
                    Integer newName;
                    
                    this.sendTo(synchro,new IntegerMessage(new Integer(name),labels));
                    newName=((IntegerMessage) this.receiveFrom(synchro)).data();
                    neighboursLink[synchro]=newName.intValue();
                }
        }
        //printStatistics();
    }
    
    /**
     * renvoie <code>true</code> si le noeud est centre d'une etoile
     */
    public int starSynchro(){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        this.sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
        
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = this.receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        this.sendAll(new IntegerMessage(new Integer(max),synchronization));
        
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = this.receiveFrom(i);
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
                this.setDoorState(new SyncState(true),door);
            }
            
            this.sendAll(new IntegerMessage(1,synchronization));
            
            for (int i=0;i<arite;i++) {
                this.receiveFrom(i);
            }
            
            return this.starCenter;
        }
        else {
            int inTheStar=this.notInTheStar;
            
            this.sendAll(new IntegerMessage(0,synchronization));
            
            for (int i=0; i<arite;i++) {
                Message msg=this.receiveFrom(i);
                if  (((IntegerMessage)msg).value() == 1) {
                    inTheStar=i;
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Spanning_Tree_ID_LC2();
    }
}


package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Spanning_Tree_ID_RDV extends Algorithm {
    
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
        int neighbourValue;
        
        for (int i=0; i<this.getArity();i++)
            neighboursLink[i]=0;
        
        name=this.getId().intValue();
        
        Integer disp=new Integer(name);
        this.putProperty("label",disp.toString());
        
        while(run){
            
            synchro=this.synchronization();
            
            this.sendTo(synchro,new IntegerMessage(new Integer(name),labels));
            neighbourValue=(((IntegerMessage) this.receiveFrom(synchro)).data()).intValue();
            
            if (neighbourValue > name) {
                name=neighbourValue;
                neighboursLink[synchro]=name;
                disp=new Integer(name);
                this.putProperty("label",disp.toString());
                for (int i=0;i<this.getArity();i++)
                    if (neighboursLink[i]<name)
                        this.setDoorState(new MarkedState(false),i);
                
                this.setDoorState(new MarkedState(true),synchro);
            }
            else
                if (neighbourValue<name)
                    neighboursLink[synchro]=name;
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
        return new Spanning_Tree_ID_RDV();
    }
}

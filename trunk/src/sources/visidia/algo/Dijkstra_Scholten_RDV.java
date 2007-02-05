
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Dijkstra_Scholten_RDV extends Algorithm {
    
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
        final String active=new String("Ac");
        final String passive=new String("Pa");
        final Integer p0=new Integer(-1);
        
        //final StringMessage mes=new StringMessage(new String("mes"),labels);
        //final StringMessage sig=new StringMessage(new String("sig"),labels);
        
        String state=passive;
        String my_State=new String("N");
        int sc=0;
        Integer father=null;
        boolean son[];
        //boolean initial=false;
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        String neighbourValue;
        int myNumber = Math.abs(SynchronizedRandom.nextInt());
        
        if (((String) getProperty("label")).compareTo("A")==0) {
            state=active;
            father=p0;
            myNumber=1999999999;
            my_State=new String("A");
            String affiche=new String("A,Ac,0");
            putProperty("label",affiche);
        }
        else {
            String affiche=new String("N,Pa,0");
            putProperty("label",affiche);
        }
        
        son=new boolean[getArity()];
        for (int i=0; i<getArity();i++)
            son[i]=false;
        
        
        while(run){
            String n_State,n_AP;
            int n_sc;
            
            int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
            //System.out.println(choosenNumber);
            if ((choosenNumber>=myNumber) && (state.compareTo(active)==0)) {
                state=passive;
                Integer sC=new Integer(sc);
                String affiche=new String(my_State+",Pa,"+sC.toString());
                putProperty("label",affiche);
            }
            
            synchro=synchronization();
            
            sendTo(synchro,new StringMessage((String) getProperty("label"),labels));
            neighbourValue=((StringMessage) receiveFrom(synchro)).data();
            
            n_State=new String(neighbourValue.substring(0,1));
            n_AP=new String(neighbourValue.substring(2,4));
            n_sc=((Integer) new Integer(neighbourValue.substring(5))).intValue();
            
            if ((my_State.compareTo("N")==0) && (n_State.compareTo("N")!=0) &&
            (n_AP.compareTo(active)==0)) {
                my_State=new String("M");
                state=active;
                father=new Integer(synchro);
                setDoorState(new MarkedState(true),synchro);
            }
            else
                if ((my_State.compareTo("N")!=0) &&
                (n_State.compareTo("N")==0) && (state.compareTo(active)==0)) {
                    sc++;
                    son[synchro]=true;
                }
                else
                    if ((my_State.compareTo("N")!=0) && (state.compareTo(passive)==0) &&
                    (sc!=0) && (n_AP.compareTo(active)==0))
                        state=active;
                    else
                        if ((my_State.compareTo("N")!=0) && (state.compareTo(passive)==0)
                        && (sc==0) && (father.intValue()==synchro)) {
                            state=passive;
                            my_State=new String("N");
                            father=null;
                            setDoorState(new MarkedState(false),synchro);
                        }
                        else
                            if ((my_State.compareTo("N")!=0) &&
                            (n_AP.compareTo(passive)==0) && (n_sc==0)
                            && (son[synchro])) {
                                sc--;
                                son[synchro]=false;
                            }
            
            Integer sC=new Integer(sc);
            String affiche=new String(my_State+","+state+","+sC.toString());
            putProperty("label",affiche);
            
            if ((my_State.compareTo("A")==0) && (sc==0) && (state.compareTo(passive)==0)){
                run=false;
                putProperty("label",new String("END"));
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
        return new Dijkstra_Scholten_RDV();
    }
}

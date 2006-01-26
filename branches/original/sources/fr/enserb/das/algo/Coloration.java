package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;

public class Coloration extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType color = new MessageType("color", true);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(color);
        //typesList.add(booleen);
        return typesList;
    }
    
    
    public void init(){
        String myColor=new String("X");
        int myC=0;
        int synchro;
        String neighbours[];
        
        neighbours=new String[getArity()];
        
        putProperty("label",myColor);
        
        while (true) {
            synchro=starSynchro();
            if (synchro==starCenter ){
                for (int i=0;i<getArity();i++)
                    neighbours[i]=((StringMessage) receiveFrom(i)).data();
                
                if ((neighbours[0].compareTo(myColor)==0) &&
                (neighbours[1].compareTo(myColor)==0)) {
                    myC=(myC+1)%3;
                    myColor=getNewColor(myC);
                }
                else
                    while ((neighbours[0].compareTo(myColor)==0) ||
                    (neighbours[1].compareTo(myColor)==0)) {
                        myC=(myC+1)%3;
                        myColor=getNewColor(myC);
                    }
                
                putProperty("label",myColor);
                
                breakSynchro();
                
            }
            else {
                if (synchro != notInTheStar) {
                    sendTo(synchro,new StringMessage(myColor,color));
                }
            }
        }
    }
    private String getNewColor(int colors) {
        if (colors==0)
            return new String("X");
        else
            if (colors==1)
                return new String("Y");
            else
                return new String("Z");
    }
    
    public int starSynchro(){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for (int i=0;i<arite;i++)
            sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
        
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
            
        }
        
        
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        for (int i=0;i<arite;i++)
            sendTo(i,new IntegerMessage(new Integer(max),synchronization));
        
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
            for( int door = 0; door < getArity(); door++){
                setDoorState(new SyncState(true),door);
            }
            
            for (int i=0;i<arite;i++)
                sendTo(i,new IntegerMessage(1,synchronization));
            
            for (int i=0;i<arite;i++) {
                Message msg=receiveFrom(i);
                
            }
            return starCenter;
        }
        else {
            int inTheStar=notInTheStar;
            
            for (int i=0;i<arite;i++)
                sendTo(i,new IntegerMessage(0,synchronization));
            
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
        return new Coloration();
    }
}

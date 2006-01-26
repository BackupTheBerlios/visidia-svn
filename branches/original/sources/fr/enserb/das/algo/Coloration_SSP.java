package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;

public class Coloration_SSP extends Algorithm {
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
        String myColor=new String("X");
        String myState=new String("X,F,-1");
        int myC=0;
        int synchro;
        String neighbours[];
        boolean finishedNode[], run=true;
        String m_V=new String("F");
        int m_sc=-1;
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        neighbours=new String[getArity()];
        
        putProperty("label",myState);
        
        while (run) {
            synchro=starSynchro(finishedNode);
            if (synchro==starCenter ){
                String label[],v[];
                int sc[];
                
                label=new String[getArity()];
                v=new String[getArity()];
                sc=new int[getArity()];
                
                for (int i=0;i<getArity();i++)
                    if (! finishedNode[i]) {
                        label[i]=((StringMessage) receiveFrom(i)).data();
                        
                        neighbours[i]=new String(label[i].substring(0,1));
                        v[i]=new String(label[i].substring(2,3));
                        sc[i]=((Integer) new Integer(label[i].substring(4))).intValue();
                    }
                    else
                        sc[i]=getNetSize();
                
                if (m_V.compareTo("F")==0) {
                    while ((neighbours[0].compareTo(myColor)==0) ||
                    (neighbours[1].compareTo(myColor)==0)) {
                        myC=(myC+1)%3;
                        myColor=getNewColor(myC);
                    }
                    
                    m_V=new String("T");
                    if (sc[0]<sc[1])
                        m_sc=sc[0]+1;
                    else
                        m_sc=sc[1]+1;
                }
                else
                    if (sc[0]<sc[1])
                        m_sc=sc[0]+1;
                    else
                        m_sc=sc[1]+1;
                
                if (m_sc>=getNetSize())
                    run=false;
                
                String ssc=new String((new Integer(m_sc)).toString());
                myState=myColor+","+m_V+","+ssc;
                putProperty("label",myState);
                
                
                breakSynchro();
                
            }
            else {
                if (synchro != notInTheStar) {
                    sendTo(synchro,new StringMessage(myState,labels));
                }
            }
        }
        
        for( int i = 0; i < getArity(); i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(-1),synchronization));
            }
        }
    }
    
    private String getNewColor(int color) {
        if (color==0)
            return new String("X");
        else
            if (color==1)
                return new String("Y");
            else
                return new String("Z");
    }
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
                if (answer[i]==-1)
                    finishedNode[i]=true;
            }
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if (! finishedNode[i]) {
                if( answer[i] >= max )
                    max = answer[i];
            }
        }
        
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
            }
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                if (! finishedNode[door])
                    setDoorState(new SyncState(true),door);
            }
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                }
            }
            
            for (int i=0;i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                }
            }
            
            return starCenter;
        }
        else {
            int inTheStar=notInTheStar;
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(0),synchronization));
                }
            }
            
            for (int i=0; i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                    if  (((IntegerMessage)msg).value() == 1) {
                        inTheStar=i;
                    }
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < getArity(); door++){
            setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Coloration_SSP();
    }
}

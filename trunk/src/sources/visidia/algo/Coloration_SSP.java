package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

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
        
        finishedNode=new boolean[this.getArity()];
        for (int i=0;i<this.getArity();i++) {
            finishedNode[i]=false;
        }
        
        neighbours=new String[this.getArity()];
        
        this.putProperty("label",myState);
        
        while (run) {
            synchro=this.starSynchro(finishedNode);
            if (synchro==this.starCenter ){
                String label[],v[];
                int sc[];
                
                label=new String[this.getArity()];
                v=new String[this.getArity()];
                sc=new int[this.getArity()];
                
                for (int i=0;i<this.getArity();i++)
                    if (! finishedNode[i]) {
                        label[i]=((StringMessage) this.receiveFrom(i)).data();
                        
                        neighbours[i]=new String(label[i].substring(0,1));
                        v[i]=new String(label[i].substring(2,3));
                        sc[i]=(new Integer(label[i].substring(4))).intValue();
                    }
                    else
                        sc[i]=this.getNetSize();
                
                if (m_V.compareTo("F")==0) {
                    while ((neighbours[0].compareTo(myColor)==0) ||
                    (neighbours[1].compareTo(myColor)==0)) {
                        myC=(myC+1)%3;
                        myColor=this.getNewColor(myC);
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
                
                if (m_sc>=this.getNetSize())
                    run=false;
                
                String ssc=new String((new Integer(m_sc)).toString());
                myState=myColor+","+m_V+","+ssc;
                this.putProperty("label",myState);
                
                
                this.breakSynchro();
                
            }
            else {
                if (synchro != this.notInTheStar) {
                    this.sendTo(synchro,new StringMessage(myState,labels));
                }
            }
        }
        
        for( int i = 0; i < this.getArity(); i++){
            if (! finishedNode[i]) {
                this.sendTo(i,new IntegerMessage(new Integer(-1),synchronization));
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
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                this.sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = this.receiveFrom(i);
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
                this.sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = this.receiveFrom(i);
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
                    this.setDoorState(new SyncState(true),door);
            }
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    this.sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                }
            }
            
            for (int i=0;i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=this.receiveFrom(i);
                }
            }
            
            return this.starCenter;
        }
        else {
            int inTheStar=this.notInTheStar;
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    this.sendTo(i,new IntegerMessage(new Integer(0),synchronization));
                }
            }
            
            for (int i=0; i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=this.receiveFrom(i);
                    if  (((IntegerMessage)msg).value() == 1) {
                        inTheStar=i;
                    }
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new  SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Coloration_SSP();
    }
}

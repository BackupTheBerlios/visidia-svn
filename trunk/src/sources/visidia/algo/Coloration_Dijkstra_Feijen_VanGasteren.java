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

public class Coloration_Dijkstra_Feijen_VanGasteren extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    final StringMessage end=new StringMessage(new String("End"),labels);
    final StringMessage active=new StringMessage(new String("Ac"),labels);
    
    public Collection getListTypes(){
        Collection<MessageType> typesList = new LinkedList<MessageType>();
        typesList.add(synchronization);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    
    
    
    public void init(){
        String myColor=new String("X");
        int myC=0;
        int synchro;//,myFather=-1;
        String neighbours[];
        String myState=new String("X,M,Pa,b,NT");
        boolean run=true, myToken=false;//, myCoD=true;
        String myLabelD=new String("M"), myAP=new String("Pa"), myColorD=new String("b");
        boolean finishedNode[];
        int arite = this.getArity() ;
        
        finishedNode=new boolean[arite];
        for (int i=0;i<arite;i++) {
            finishedNode[i]=false;
        }
        
        if (((String) this.getProperty("label")).compareTo("A")==0){
            myState=new String("X,A,Ac,w,HT");
            myLabelD=new String("A");
            myAP=new String("Ac");
            myColorD=new String("w");
            myToken=true;
            //myCoD=false;
        }
        
        neighbours=new String[this.getArity()];
        
        this.putProperty("label",myState);
        
        while (run) {
            synchro=this.starSynchro(finishedNode);
            if (synchro==this.starCenter ){
                String label[],labelD[],aP[], cD[], tD[];
                
                label=new String[this.getArity()];
                labelD=new String[this.getArity()];
                aP=new String[this.getArity()];
                cD=new String[this.getArity()];
                tD=new String[this.getArity()];
                
                for (int i=0;i<this.getArity();i++)
                    if (! finishedNode[i]) {
                        neighbours[i]=((StringMessage) this.receiveFrom(i)).data();
                        
                        label[i]=new String(neighbours[i].substring(0,1));
                        labelD[i]=new String(neighbours[i].substring(2,3));
                        aP[i]=new String(neighbours[i].substring(4,6));
                        cD[i]=new String(neighbours[i].substring(7,8));
                        tD[i]=new String(neighbours[i].substring(9,11));
                        
                        //if (labelD[i].compareTo("N")==0)
                        //  existN=i;
                        //System.out.println(neighbours[i]+" = "+label[i]+","+labelD[i]+","+aP[i]);
                    }
                if (!finishedNode[0] && !finishedNode[1]) {
                    while ((label[0].compareTo(myColor)==0) ||
                    (label[1].compareTo(myColor)==0)) {
                        myC=(myC+1)%3;
                        myColor=this.getNewColor(myC);
                        myAP=new String("Ac");
                        myColorD=new String("b");
                        this.sendAll(this.active);
                    }
                }
                
                if (myToken) {
                    if (aP[this.nextDoor()].compareTo("Pa")==0) {
                        this.sendTo(this.nextDoor(),new StringMessage(new String(myColorD),labels));
                        myColorD=new String("w");
                        myToken=false;
                    }
                }
                if (!finishedNode[0] && !finishedNode[1])
                    if ((label[0].compareTo(myColor)!=0) &&
                    (label[1].compareTo(myColor)!=0))
                        myAP=new String("Pa");
                
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        this.sendTo(i,this.end);
                    }
                }
                
                myState=myColor+","+myLabelD+","+myAP+","+myColorD+",";
                
                if (myToken)
                    myState=myState+"HT";
                else
                    myState=myState+"NT";
                
                this.putProperty("label",myState);
                
                this.breakSynchro();
                
            }
            else {
                if (synchro != this.notInTheStar) {
                    String son;
                    
                    this.sendTo(synchro,new StringMessage(myState,labels));
                    son=((StringMessage)this.receiveFrom(synchro)).data();
                    
                    while (son.compareTo(this.end.data())!=0) {
                        if (son.compareTo(this.active.data())!=0) {
                            if (myLabelD.compareTo("A")!=0)
                                if (son.compareTo("b")==0)
                                    myColorD=new String("b");
                            if (myLabelD.compareTo("A")==0)
                                if (son.compareTo("b")==0)
                                    myColorD=new String("w");
                                else
                                    if (myColorD.compareTo("w")==0)
                                        run =false;
                            myToken=true;
                        }
                        else
                            myAP=new String("Ac");
                        son=((StringMessage)this.receiveFrom(synchro)).data();
                    }
                    
                    myState=myColor+","+myLabelD+","+myAP+","+myColorD+",";
                    
                    if (myToken)
                        myState=myState+"HT";
                    else
                        myState=myState+"NT";
                    
                    this.putProperty("label",myState);
                }
            }
        }
        
        this.putProperty("label",myColor+","+"END");
        /*
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(-1)));
            }
            }*/
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
                    /*Message msg=*/this.receiveFrom(i);
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
            this.setDoorState(new SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Coloration_Dijkstra_Feijen_VanGasteren();
    }
}

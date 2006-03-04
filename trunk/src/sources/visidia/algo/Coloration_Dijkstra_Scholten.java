package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;


public class Coloration_Dijkstra_Scholten extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    final StringMessage mes=new StringMessage(new String("mes"),labels);
    final StringMessage noMes=new StringMessage(new String("No Mes"),labels);
    final StringMessage sig=new StringMessage(new String("sig"),labels);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    
    
    public void init(){
        String myColor=new String("X");
        int myC=0;
        int synchro,myFather=-1;
        String neighbours[];
        String myState=new String("X,N,Pa,0");
        boolean run=true;
        String myLabelD=new String("N"), myAP=new String("Pa");
        int mySc=0;
        boolean finishedNode[];
        int arite = getArity() ;
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        if (((String) getProperty("label")).compareTo("A")==0){
            myState=new String("X,A,Ac,0");
            myLabelD=new String("A");
            myAP=new String("Ac");
        }
        
        neighbours=new String[getArity()];
        
        putProperty("label",myState);
        
        while (run) {
            synchro=starSynchro(finishedNode);
            if (synchro==starCenter ){
                String label[],labelD[],aP[];
                int sc[];
                int existN=-1;
                
                label=new String[getArity()];
                labelD=new String[getArity()];
                aP=new String[getArity()];
                sc=new int[getArity()];
                
                for (int i=0;i<getArity();i++)
                    if (! finishedNode[i]) {
                        neighbours[i]=((StringMessage) receiveFrom(i)).data();
                        
                        label[i]=new String(neighbours[i].substring(0,1));
                        labelD[i]=new String(neighbours[i].substring(2,3));
                        aP[i]=new String(neighbours[i].substring(4,6));
                        sc[i]=((Integer) new Integer(neighbours[i].substring(7))).intValue();
                        if (labelD[i].compareTo("N")==0)
                            existN=i;
                        //System.out.println(neighbours[i]+" = "+label[i]+","+labelD[i]+","+aP[i]);
                    }
                if (!finishedNode[0] && !finishedNode[1]) {
                    while ((label[0].compareTo(myColor)==0) ||
                    (label[1].compareTo(myColor)==0)) {
                        myC=(myC+1)%3;
                        myColor=getNewColor(myC);
                    }
                }
                
                if (myAP.compareTo("Ac")==0) {
                    if (existN!=-1) {
                        sendTo(existN,mes);
                        mySc++;
                        setDoorState(new MarkedState(true),existN);
                    }
                    else
                        if ((mySc==0) && (myLabelD.compareTo("A")!=0)) {
                            myAP="Pa";
                            sendTo(myFather,sig);
                            setDoorState(new MarkedState(false),myFather);
                            run =false;
                        }
                }
                if ((mySc==0) && (myLabelD.compareTo("A")==0) && (existN==-1))
                    run=false;
                
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        sendTo(i,noMes);
                    }
                }
                
                String ssc=new String((new Integer(mySc)).toString());
                myState=myColor+","+myLabelD+","+myAP+","+ssc;
                putProperty("label",myState);
                
                breakSynchro();
                
            }
            else {
                if (synchro != notInTheStar) {
                    String son;
                    
                    sendTo(synchro,new StringMessage(myState,labels));
                    son=((StringMessage)receiveFrom(synchro)).data();
                    
                    while (son.compareTo(noMes.data())!=0) {
                        if (son.compareTo(mes.data())==0) {
                            myFather=synchro;
                            myLabelD="M";
                            myAP="Ac";
                        }
                        else
                            if (son.compareTo(sig.data())==0) {
                                mySc--;
                            }
                        son=((StringMessage)receiveFrom(synchro)).data();
                    }
                    
                    String ssc=new String((new Integer(mySc)).toString());
                    myState=myColor+","+myLabelD+","+myAP+","+ssc;
                    putProperty("label",myState);
                }
            }
        }
        
        for( int i = 0; i < arite; i++){
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
            setDoorState(new SyncState(false),door);
        }
    }
    
    public Object clone(){
        return new Coloration_Dijkstra_Scholten();
    }
}

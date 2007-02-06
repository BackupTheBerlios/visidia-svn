
package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class Sequential_Spanning_Tree_ID extends Algorithm {
    
    /* R1: (A,i)-0-(X,j)  ---> (M,i)-1-(A,i)  // j<i; X=A,N,M,F
       R2: (A,i)-a-(Y,k)  ---> (N,i)-a-(Y,k)  // k>i; Y=A,M,N
       R3: (M,i)-1-(A,i)  ---> (A,i)-1-(F,i)
       R1, R2 > R3 */
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    static MessageType termination = new MessageType("termination", false, java.awt.Color.green);
    static MessageType labels = new MessageType("labels", true);
    
    final int starCenter=-1;
    final int notInTheStar=-2;
    final int nNode=0;//new String("A");
    final int mNode=1;//new String("M");
    final int fNode=2;//new String("F");
    final int nodeN=3;//new String("N");
    
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        typesList.add(termination);
        return typesList;
    }
    
    public void init(){
        
        //int graphS=getNetSize(); /* la taille du graphe */
        int synchro;
        boolean run=true; /* booleen de fin  de l'algorithme */
        Vector neighboursLabel[];
        boolean finishedNode[];
        int neighboursLink[]=new int[this.getArity()];
        Vector name=new Vector(2);
        
        for (int i=0; i<this.getArity();i++)
            neighboursLink[i]=0;
        
        neighboursLabel=new Vector[this.getArity()];
        
        finishedNode= new boolean[this.getArity()];
        for (int i=0;i<this.getArity();i++)
            finishedNode[i]=false;
        
        
        name.add(this.getId());
        name.add(new Integer(this.nNode));
        
        String disp=new String("(A , "+name.elementAt(0)+")");
        this.putProperty("label",new String(disp));
        
        while(run){
            
            synchro=this.starSynchro(finishedNode);
            if (synchro==-3)
                run=false;
            else
                if (synchro==this.starCenter){
                    int neighbourX=-1;
                    int neighbourM=-1;
                    int nbreN=0;
                    int nbreF=0;
                    boolean existHigher=false;
                    //boolean existLower=false;
                    
                    for (int door=0;door<this.getArity();door++)
                        if (!finishedNode[door]) {
                            neighboursLabel[door]=new Vector(((VectorMessage) this.receiveFrom(door)).data());
                            
                            if (((Integer)neighboursLabel[door].elementAt(0)).intValue()<((Integer)name.elementAt(0)).intValue())
                                neighbourX=door;
                            else
                                if (((Integer)neighboursLabel[door].elementAt(0)).intValue()>((Integer)name.elementAt(0)).intValue())
                                    existHigher=true;
                            
                            
                            if (((Integer)neighboursLabel[door].elementAt(1)).intValue()==this.nNode)
                                nbreN++;
                            
                            if (((Integer)neighboursLabel[door].elementAt(1)).intValue()==this.fNode)
                                nbreF++;
                            
                            if ((((Integer)neighboursLabel[door].elementAt(1)).intValue()==this.mNode) &&
                            (neighboursLink[door]==((Integer)name.elementAt(0)).intValue()))
                                neighbourM=door;
                            
                        }
                    if ((((Integer)name.elementAt(1)).intValue()==this.nNode) &&
                    (nbreF==this.getArity()))
                        run=false;
                    
                    if ((((Integer)name.elementAt(1)).intValue()==this.nNode) &&
                    (neighbourX !=-1) && (! existHigher)) {
                        String display;
                        display=new String("(M , "+name.elementAt(0)+")");
                        this.putProperty("label",new String(display));
                        this.setDoorState(new MarkedState(true),neighbourX);
                        name.setElementAt(new Integer(this.mNode),1);
                        
                        neighboursLink[neighbourX]=((Integer)name.elementAt(0)).intValue();
                        
                        for (int door=0;door<this.getArity();door++)
                            if (!finishedNode[door]) {
                                if (door!=neighbourX)
                                    this.sendTo(door,new VectorMessage(neighboursLabel[door],labels));
                                else {
                                    Vector trans=new Vector(2);
                                    trans.addElement(name.elementAt(0));
                                    trans.addElement(new Integer(this.nNode));
                                    
                                    this.sendTo(door,new VectorMessage(trans,labels));
                                }
                            }
                    }
                    else {
                        if ((((Integer)name.elementAt(1)).intValue()==this.nNode) &&
                        (neighbourM !=-1) && (! existHigher)) {
                            
                            String display;
                            display=new String("(F , "+name.elementAt(0)+")");
                            this.putProperty("label",new String(display));
                            name.setElementAt(new Integer(this.fNode),1);
                            
                            for (int door=0;door<this.getArity();door++)
                                if (!finishedNode[door]) {
                                    if (door!=neighbourM)
                                        this.sendTo(door,new VectorMessage(neighboursLabel[door],labels));
                                    else {
                                        Vector trans=new Vector(2);
                                        trans.addElement(name.elementAt(0));
                                        trans.addElement(new Integer(this.nNode));
                                        
                                        this.sendTo(door,new VectorMessage(trans,labels));
                                    }
                                }
                        }
                        else
                            if ((((Integer)name.elementAt(1)).intValue()==this.nNode) &&
                            (existHigher)) {
                                
                                String display;
                                display=new String("(N , "+name.elementAt(0)+")");
                                this.putProperty("label",new String(display));
                                name.setElementAt(new Integer(this.nodeN),1);
                                for (int door=0;door<this.getArity();door++)
                                    if (!finishedNode[door])
                                        this.sendTo(door,new VectorMessage(neighboursLabel[door],labels));
                            }
                            else
                                for (int door=0;door<this.getArity();door++)
                                    if (!finishedNode[door])
                                        this.sendTo(door,new VectorMessage(neighboursLabel[door],labels));
                    }
                    
                    this.breakSynchro();
                }
                else
                    if (synchro!=this.notInTheStar) {
                        Vector newName;
                        
                        this.sendTo(synchro,new VectorMessage((Vector)name.clone(),labels));
                        newName=((VectorMessage) this.receiveFrom(synchro)).data();
                        if (((Integer)newName.elementAt(0)).intValue()!=((Integer)name.elementAt(0)).intValue())
                            neighboursLink[synchro]=((Integer)newName.elementAt(0)).intValue();
                        name=new Vector(newName);
                        String display;
                        String nodes;
                        if (((Integer)name.elementAt(1)).intValue()==this.nNode)
                            nodes=new String("A");
                        else
                            if(((Integer)name.elementAt(1)).intValue()==this.fNode)
                                nodes=new String("F");
                            else
                                if (((Integer)name.elementAt(1)).intValue()==this.mNode)
                                    nodes=new String("M");
                                else
                                    nodes=new String("N");
                        
                        display=new String("("+nodes+" , "+name.elementAt(0)+")");
                        this.putProperty("label",new String(display));
                        for (int door=0;door<this.getArity();door++)
                            if (neighboursLink[door]<((Integer)name.elementAt(0)).intValue())
                                this.setDoorState(new MarkedState(false),door);
                    }
        }
        this.sendAll(new IntegerMessage(new Integer(-3),termination));
    }
    
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = this.getArity() ;
        int[] answer = new int[arite] ;
        boolean theEnd=false;
        
        
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
                if (answer[i]==-3) {
                    finishedNode[i]=true;
                    theEnd=true;
                }
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
        if (theEnd)
            max=-3;
        
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
                if (answer[i]==-3) {
                    theEnd=true;
                }
            }
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if (! finishedNode[i])
                if( answer[i] >= max )
                    max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                if (! finishedNode[door])
                    this.setDoorState(new SyncState(true),door);
            }
            
            
            if (! theEnd) {
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
                for( int i = 0; i < arite; i++){
                    if (! finishedNode[i]) {
                        this.sendTo(i,new IntegerMessage(new Integer(-3),termination));
                    }
                }
                
                for (int i=0;i<arite;i++) {
                    if (! finishedNode[i]) {
                        Message msg=this.receiveFrom(i);
                    }
                }
                
                return -3;
            }
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
                    else
                        if (((IntegerMessage)msg).value() == -3) {
                            finishedNode[i]=true;
                            theEnd=true;
                        }
                }
            }
            if (inTheStar!=this.notInTheStar)
                return inTheStar;
            else
                if (theEnd)
                    return -3;
                else
                    return this.notInTheStar;
            
        }
    }
    
    public void breakSynchro() {
        
        for( int door = 0; door < this.getArity(); door++){
            this.setDoorState(new SyncState(false),door);
        }
    }
    
    
    public Object clone(){
        return new Sequential_Spanning_Tree_ID();
    }
}

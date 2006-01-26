package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;
import java.awt.Color;

public class Exclusion2T extends Algorithm {

    final int starCenter=-1;
    final int notInTheStar=-2;

    static int synchroNumber=0;
    static float timeNumber=0;
    static int cE=0;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.green);
    //    static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);

    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(synchronization);
	typesList.add(labels);
	//typesList.add(booleen);
	return typesList;
    }
   
    public void init(){
	final int arity=getArity();
	
	boolean finish=false;
	int syncNumber=0;
	boolean finishedNode[];

	Vector synchro;
	boolean run=true;
	int count = -1;
	int countE=0;
	boolean result=false;
	boolean result1=false;
	//String myHungryNode = new String("H,"+count);
	String myThinkingNode = new String("T,-1");
	
	int myNumber = Math.abs(SynchronizedRandom.nextInt());
	putProperty("label",myThinkingNode);

	finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
	if (((Integer) getId()).intValue()==0)
	    timeNumber=0;
	cE=0;
	
	while(run){
	    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	    String theHungryNode = new String("H,"+count);
	    String theThinkingNode = new String("T,"+count);
	    String theEatingNode = new String("E,"+count);
	    
	    if (choosenNumber>=myNumber) {
		
		/*if(getProperty("label").toString().substring(0,1).compareTo("T")==0){
		    count=0;
		    putProperty("label",theHungryNode);
		    }*/
		if((getProperty("label").toString().substring(0,1).compareTo("E")==0) && 
		   (countE>=2)){
		    count=-1;
		    putProperty("label",myThinkingNode);
		}
	    }
	    countE++;
	    synchro=starSynchro(finishedNode);
	      if (synchro != null ){
		  syncNumber++;
                if (((Integer) synchro.elementAt(0)).intValue()==-1) {
		    StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()));
		    String myLabelString = nodeLabel.data().substring(0,1);
		    int myLabelInt = ((Integer)new Integer(nodeLabel.data().substring(2))).intValue();
		    int maxInt=-1;
		    int minInt=count;
		    boolean existE=false;

		    for(int i=0;i<arity;i++)
			if (! finishedNode[i]) {
			    String msg = ((StringMessage)receiveFrom(i)).data();
			    String neighLabelString = msg.substring(0,1);
			    int neighLabelInt = ((Integer)new Integer(msg.substring(2))).intValue();
			
			    if ((neighLabelString.compareTo("H")==0) &&
				(neighLabelInt>maxInt))
				maxInt=neighLabelInt;

			    if ((neighLabelString.compareTo("H")==0) &&
				(neighLabelInt<minInt))
				minInt=neighLabelInt;

			    if (neighLabelString.compareTo("E")==0)
				existE=true;
			}
		    if ((myLabelString.compareTo("T")==0) &&
			(choosenNumber>=myNumber)) {
			count=maxInt+1;
			theHungryNode = new String("H,"+count);
			putProperty("label",theHungryNode);
			syncNumber=0;
		    }

		    if ((myLabelString.compareTo("H")==0) &&
			(! existE) && ((minInt>=count) || (maxInt==-1))) {
			theEatingNode = new String("E,"+count);
			putProperty("label",theEatingNode);
			countE=0;
			if (timeNumber>0) {
			    if (cE<getNetSize())
				timeNumber=(timeNumber+syncNumber)/2;
			}
			else
			    timeNumber=syncNumber;
			
			if (!finish) {
			    finish=true;
			    cE++;
			}
		    }
		    
		    breakSynchro();
		}
		else {
		    StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()),labels);
		    for (int i=0;i<synchro.size();i++)
                        sendTo(((Integer) synchro.elementAt(i)).intValue(),nodeLabel);
		    
		}
	      }
	      if (cE>=getNetSize())
		  run=false;
	}

	sendAll(new IntegerMessage(new Integer(-1),synchronization));
	
	if (((Integer) getId()).intValue()==0)
	    System.out.println(getId()+" : Nombre de starSynchro = "+synchroNumber+
			       "   temps moyens = "+timeNumber);
        
    }
 /**
     * renvoie <code>true</code> si le noeud est centre d'une étoile
     */
    public Vector starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        Vector neighbourCenter;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        //sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
         for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }

        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++)
	    if (! finishedNode[i]) {
		Message msg = receiveFrom(i);
		answer[i]= ((IntegerMessage)msg).value();
		if (answer[i]==-1)
                    finishedNode[i]=true;
	    }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++)
	    if (! finishedNode[i]) {
		if( answer[i] >= max )
		    max = answer[i];
	    }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < getArity(); door++)
		if (! finishedNode[door]) {
		    setDoorState(new SyncState(true),door);
		}
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    sendTo(i,new IntegerMessage(new Integer(1),synchronization));
		}
	    }
            
            for (int i=0;i<arite;i++)
		if (! finishedNode[i]) {
		    Message msg=receiveFrom(i);
		}
	    
            neighbourCenter=new Vector();
            neighbourCenter.add(new Integer(-1));
	    
            synchroNumber++;
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            for( int i = 0; i < arite; i++){
		if (! finishedNode[i]) {
		    sendTo(i,new IntegerMessage(new Integer(0),synchronization));
		}
	    }
	    
            for (int i=0; i<arite;i++) 
		if (! finishedNode[i]) {
		    Message msg=receiveFrom(i);
		    if  (((IntegerMessage)msg).value() == 1) {
			neighbourCenter.add(new Integer(i));
		    }
		}
	    
            if (neighbourCenter.size()==0)
                neighbourCenter=null;
            
            return neighbourCenter;
            
        }
    }

    public void breakSynchro() {
	
	for( int door = 0; door < getArity(); door++){
	    setDoorState(new SyncState(false),door);
	    //setEdgeColor(door, new ColorState(Color.black));
	}
    }
    
    public Object clone(){
	return new Exclusion2T();
    }
}





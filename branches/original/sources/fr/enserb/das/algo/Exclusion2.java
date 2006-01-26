package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;
import java.awt.Color;

public class Exclusion2 extends Algorithm {

    final int starCenter=-1;
    final int notInTheStar=-2;

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
	    synchro=starSynchro();
	      if (synchro != null ){
                if (((Integer) synchro.elementAt(0)).intValue()==-1) {
		    StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()));
		    String myLabelString = nodeLabel.data().substring(0,1);
		    int myLabelInt = ((Integer)new Integer(nodeLabel.data().substring(2))).intValue();
		    int maxInt=-1;
		    int minInt=count;
		    boolean existE=false;

		    for(int i=0;i<arity;i++){
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
		    }

		    if ((myLabelString.compareTo("H")==0) &&
			(! existE) && ((minInt>=count) || (maxInt==-1))) {
			theEatingNode = new String("E,"+count);
			putProperty("label",theEatingNode);
			countE=0;
		    }
		    
		    breakSynchro();
		}
		else {
		    StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()),labels);
		     for (int i=0;i<synchro.size();i++)
                        sendTo(((Integer) synchro.elementAt(i)).intValue(),nodeLabel);
		    
		}
	      }
	}
    }
 /**
     * renvoie <code>true</code> si le noeud est centre d'une étoile
     */
    public Vector starSynchro(){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        Vector neighbourCenter;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        sendAll(new IntegerMessage(new Integer(choosenNumber),synchronization));
        
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            Message msg = receiveFrom(i);
            answer[i]= ((IntegerMessage)msg).value();
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < getArity(); door++){
                setDoorState(new SyncState(true),door);
            }
            
            sendAll(new IntegerMessage(new Integer(1),synchronization));
            
            for (int i=0;i<arite;i++) {
                Message msg=receiveFrom(i);
            }
            neighbourCenter=new Vector();
            neighbourCenter.add(new Integer(-1));
            
            return neighbourCenter;
        }
        else {
            
            neighbourCenter=new Vector();
            
            sendAll(new IntegerMessage(new Integer(0),synchronization));
            
            for (int i=0; i<arite;i++) {
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
	return new Exclusion2();
    }
}





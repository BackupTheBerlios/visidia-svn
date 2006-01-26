package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;
import java.awt.Color;

public class Exclusion extends Algorithm {

    final int starCenter=-1;
    final int notInTheStar=-2;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.green);
    static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);

    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(synchronization);
	typesList.add(labels);
	typesList.add(booleen);
	return typesList;
    }
   
    public void init(){
	final int arity=getArity();
	
	int synchro;
	boolean run=true;
	int count = 0;
	boolean result=false;
	boolean result1=false;
	//String myHungryNode = new String("H,"+count);
	String myThinkingNode = new String("T,0");
	String myEatingNode = new String("E,0");
	int myNumber = Math.abs(SynchronizedRandom.nextInt());
	putProperty("label",myThinkingNode);

	while(run){
	    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	    String theHungryNode = new String("H,"+count);
	    String theThinkingNode = new String("T,"+count);
	    String theEatingNode = new String("E,"+count);
	    if (choosenNumber>=myNumber) {
		if(getProperty("label").toString().substring(0,1).compareTo("H")==0){
		    count++;
		    putProperty("label",theHungryNode);
		}
		if(getProperty("label").toString().substring(0,1).compareTo("T")==0){
		    count=0;
		    putProperty("label",theHungryNode);
		}
		if(getProperty("label").toString().substring(0,1).compareTo("E")==0){
		    putProperty("label",theEatingNode);
		}
	    }
	    if(choosenNumber<=myNumber) {
		if(getProperty("label").toString().substring(0,1).compareTo("E")==0){
		    count = 0;
		    //   for(int i=0; i<arity;i++)
		    //setEdgeColor(i, new ColorState(Color.black));
		    putProperty("label",theThinkingNode);  
		}
		if(getProperty("label").toString().substring(0,1).compareTo("T")==0){
		    putProperty("label",theThinkingNode);
		}
		if(getProperty("label").toString().substring(0,1).compareTo("H")==0){
		    count++;
		    putProperty("label",theHungryNode);
		}
	    }
	    
	    synchro=starSynchro();
	    if(synchro==starCenter){
		StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()));
		String myLabelString = nodeLabel.data().substring(0,1);
		int myLabelInt = ((Integer)new Integer(nodeLabel.data().substring(2))).intValue();

		for(int i=0;i<arity;i++){
		    String msg = ((StringMessage)receiveFrom(i)).data();
		    String neighLabelString = msg.substring(0,1);
		    int neighLabelInt = ((Integer)new Integer(msg.substring(2))).intValue();
		    if(myLabelString.compareTo("H")==0){
			if(neighLabelString.compareTo("H")==0){
			    if(neighLabelInt <= myLabelInt){
				result = true;			
			    }
			    else{
				result1 = true;				
			    }
			}
			if(neighLabelString.compareTo("T")==0){
			    result = true;			    
			}
			if(neighLabelString.compareTo("E")==0){
			    result1 = true;
			}
		    }
		    if(myLabelString.compareTo("T")==0 || myLabelString.compareTo("E")==0){
			result = false;
			result1 = false;
		    }
		}
		if(result == true && result1 == false){
		    //for(int i=0;i<arity;i++)
		    //setEdgeColor(i, new ColorState(Color.red));
		    putProperty("label",myEatingNode);
		}
		
		breakSynchro();
	    }
	    else {
		if (synchro != notInTheStar) {
		    StringMessage nodeLabel = new StringMessage(new String(getProperty("label").toString()),labels);
		    sendTo(synchro,nodeLabel);
		}
	    }
	}
    }
  
 /**
     * renvoie <code>true</code> si le noeud est centre d'une étoile
     */
    public int starSynchro(){
	
	int arite = getArity() ;
	int[] answer = new int[arite] ;

	/*random */
	int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	
	/*Send to all neighbours */
	sendAll(new IntegerMessage(new Integer(choosenNumber), synchronization));
	
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
	
	sendAll(new IntegerMessage(new Integer(max), synchronization));
	
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
           for( int door = 0; door < arite; door++){
		    setDoorState(new SyncState(true),door);
		}

	   
	   sendAll(new IntegerMessage(1, booleen));

	   for (int i=0;i<arite;i++) {
	       Message msg=receiveFrom(i);
	   }

	   return starCenter;
	}
	else {
	    int inTheStar=notInTheStar;
	    
	    sendAll(new IntegerMessage(0, booleen));
	    
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
	    //setEdgeColor(door, new ColorState(Color.black));
	}
    }
    
    public Object clone(){
	return new Exclusion();
    }
}





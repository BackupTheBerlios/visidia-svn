package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;
import java.awt.Color;

public class ExclusionToken extends Algorithm {

    final int starCenter=-1;
    final int notInTheStar=-2;
    static int nbr_finish=0;
    
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

	final StringMessage end=new StringMessage(new String("END"),labels);
        final StringMessage toKen=new StringMessage(new String("TOKEN"),labels);

	int synchro;
	boolean run=true;
	int count = 0;
	int countToken=0;
	boolean result=false;
	boolean token=false;
	int myNumber = Math.abs(SynchronizedRandom.nextInt());
	String myState=new String("T");
	String state;
	/*
	if (((String) getProperty("label")).compareTo("A")==0) {
	    token=true;
	    state =new String(myState+","+count+","+countToken+",HT");
	}
	else
	    state =new String(myState+","+count+","+countToken+",NT"); 
	putProperty("label",state);
	*/
	if (getId().intValue()==0) {
	    token=true;
	    state =new String(myState+","+count+","+countToken+",HT");
	}
	else
	    state =new String(myState+","+count+","+countToken+",NT"); 
	
	putProperty("label",state);

	while(run){
	    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	   
	    if(choosenNumber<=myNumber) 
		if(myState.compareTo("E")==0){
		    myState=new String("T");
		    state =new String(myState+","+count+","+countToken+",HT"); 
		    putProperty("label",state);
		    
		}
	    
	    
	    synchro=starSynchro();
	    if(synchro==starCenter){
		String nLabel[]=new String[getArity()];
		int nCount[]=new int[getArity()];
		int nCToken[]=new int[getArity()];
		boolean existLower=false;
		int nextDoor=-1;
		int lowerCToken;
		int maxInt=-1;

		for(int i=0;i<arity;i++){
		    nLabel[i] = ((StringMessage)receiveFrom(i)).data();
		    nCount[i] = (((IntegerMessage)receiveFrom(i)).data()).intValue();
		    nCToken[i] = (((IntegerMessage)receiveFrom(i)).data()).intValue();
		   
		    if(myState.compareTo("H")==0){
			if(nLabel[i].compareTo("H")==0){
			    if (nCount[i] < count){
				existLower = true;			
			    }

			    if (nCount[i]>maxInt)
				maxInt=nCount[i];
			}
		    }
		 
		    if(nLabel[i].compareTo("H")==0){
			if (nCount[i]>maxInt)
			    maxInt=nCount[i];
		    }
		}
		
		if (token) {
		    if ((!existLower) && (myState.compareTo("H")==0)) {
			myState=new String("E");
			count=0;
			if (!result) {
			    nbr_finish++;
			    result=true;
			}
		    }
		    else {
			lowerCToken=nCToken[0];
			nextDoor=0;
			for (int j=1;j<arity;j++)
			    if (nCToken[j]<lowerCToken) {
				lowerCToken=nCToken[j];
				nextDoor=j;
			    }
			    else {
				if (nCToken[j]==lowerCToken) 
				    if (nCount[j]<nCount[nextDoor]) 
					nextDoor=j;
			    }
			token=false;
			sendTo(nextDoor,toKen);
		    }
		    //countToken++;
		}
		
		if ((myState.compareTo("T")==0) && (choosenNumber>=myNumber)) {
		    count=maxInt+1;
		    myState=new String("H");
		}
		
		sendAll(end);
		if (token)
		    state =new String(myState+","+count+","+countToken+",HT"); 
		else
		    state =new String(myState+","+count+","+countToken+",NT"); 
		
		putProperty("label",state);

		breakSynchro();
	    }
	    else {
		if (synchro != notInTheStar) {
		    StringMessage nodeLabel = new StringMessage(myState,labels);
		    sendTo(synchro,nodeLabel);

		    IntegerMessage nodeCount = new IntegerMessage(new Integer(count),labels);
		    sendTo(synchro,nodeCount);

		    IntegerMessage nodeCToken = new IntegerMessage(new Integer(countToken),labels);
		    sendTo(synchro,nodeCToken);
		    
		    String ans=((StringMessage) receiveFrom(synchro)).data();
		    
		    while (ans.compareTo(end.data())!=0) {
			if (ans.compareTo(toKen.data())==0){
			    token=true;
			    countToken++;
			}
			ans=((StringMessage) receiveFrom(synchro)).data();
		    }
		    
		    if (token)
			state =new String(myState+","+count+","+countToken+",HT"); 
		    else
			state =new String(myState+","+count+","+countToken+",NT"); 
		    
		    putProperty("label",state);

		}
	    }

	    if (nbr_finish>getNetSize())
		run=false; 
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
	return new ExclusionToken();
    }
}





package fr.enserb.das.algo;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.*;
import java.awt.Color;

public class ExclusionToken2p extends Algorithm {

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
	//int count = 0;
	int countToken=0;
	float cV=0;
	boolean result=false;
	boolean token=false;
	int myNumber = Math.abs(SynchronizedRandom.nextInt());
	String myState=new String("T");
	String state;
	int syc=0;
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
	    state =new String(myState+","+cV+",HT");
	}
	else
	    state =new String(myState+","+cV+",NT"); 
	
	putProperty("label",state);

	while(run){
	    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	   
	    if(choosenNumber<=myNumber) {
		if (myState.compareTo("E")==0) {
		    int choosenNumber2 = Math.abs(SynchronizedRandom.nextInt());
		    
		    if ((choosenNumber2<=myNumber) || (syc>=3)) {
			myState=new String("T");
			state =new String(myState+","+cV+",HT"); 
			putProperty("label",state);
			syc=0;
		    }
		}
		else
		    if ((myState.compareTo("T")==0) && (syc>=1)) {
			myState=new String("H");
			if (token)
			    state =new String(myState+","+cV+",HT"); 
			else
			    state =new String(myState+","+cV+",NT");
			putProperty("label",state);
			syc=0;
		    
		    }
	    }	    

	    synchro=starSynchro();
	    if(synchro==starCenter){
		syc++;

		String nLabel[]=new String[getArity()];
		//int nCount[]=new int[getArity()];
		int nCToken;
		int nc;
		float nCV[]=new float[getArity()];
		boolean existLower=false;
		int nextDoor=-1;
		float lowerCV;
		int maxInt=-1;

		for(int i=0;i<arity;i++){
		    nLabel[i] = ((StringMessage)receiveFrom(i)).data();
		    nCToken = (((IntegerMessage)receiveFrom(i)).data()).intValue();
		    nc = (((IntegerMessage)receiveFrom(i)).data()).intValue();
		    nCV[i]=((float) nCToken)/nc;
		}
		
		if (token) {
		    if (myState.compareTo("H")==0) {
			myState=new String("E");
			//syc=0;
			if (!result) {
			    nbr_finish++;
			    result=true;
			}
		    }
		    else {
			if (myState.compareTo("E")!=0) {
			    lowerCV=nCV[0];
			    Vector v=new Vector();
			    v.add(new Integer(0));
			    for (int j=1;j<arity;j++)
				if (nCV[j]<lowerCV) {
				    lowerCV=nCV[j];
				    v=new Vector();
				    v.add(new Integer(j));
				}
				else
				    if (nCV[j]==lowerCV) {
					v.add(new Integer(j));
				    }
			    nextDoor=choice(v);
			    token=false;
			    sendTo(nextDoor,toKen);
			}
			//else
			//  syc++;
			//countToken++;
		    }
		   
		}
		
		
		sendAll(end);

		cV=((float) countToken)/arity;

		if (token)
		    state =new String(myState+","+cV+",HT"); 
		else
		    state =new String(myState+","+cV+",NT"); 
		
		putProperty("label",state);

		breakSynchro();
	    }
	    else {
		if (synchro != notInTheStar) {
		    StringMessage nodeLabel = new StringMessage(myState,labels);
		    sendTo(synchro,nodeLabel);

		    IntegerMessage nodeCToken = new IntegerMessage(new Integer(countToken),labels);
		    sendTo(synchro,nodeCToken);

		    IntegerMessage nodeArity = new IntegerMessage(new Integer(arity),labels);
		    sendTo(synchro,nodeArity);

		    String ans=((StringMessage) receiveFrom(synchro)).data();
		    
		    while (ans.compareTo(end.data())!=0) {
			if (ans.compareTo(toKen.data())==0) {
			    token=true;
			    countToken++;
			}
			ans=((StringMessage) receiveFrom(synchro)).data();
		    }
		    
		    cV=((float) countToken)/arity;

		    if (token)
			state =new String(myState+","+cV+",HT"); 
		    else
			state =new String(myState+","+cV+",NT"); 
		    
		    putProperty("label",state);

		}
	    }

	    //if (nbr_finish>getNetSize())
	    //	; 
	}
    }

    public int choice(Vector v) {
	Random generator = new Random();
	int choosen=Math.abs((generator.nextInt()))% v.size();
	return ((Integer) v.elementAt(choosen)).intValue();
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
	return new ExclusionToken2p();
    }
}





package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class SynchroDist extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    static int synchroNumber=0;

    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    static MessageType termination = new MessageType("termination", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(synchronization);
        typesList.add(labels);
        typesList.add(termination);
        return typesList;
    }
    
    
    
    
    public void init(){ 
	
	final String nodeN=new String("N");
        final String nodeA=new String("A");
	final String nodeL=new String("L");
        final String nodeF=new String("F");

	int mySynchro=0;
        String myState=new String("N,0");
        String myL=nodeN;
        //Vector synchro;
        boolean neighbours[];
        //boolean finishedNode[];
        boolean run=true;
        //int round=0;
	String label[];
	int father=-1;
	String ssy;

	neighbours=new boolean[this.getArity()];
	
        for (int i=0;i<this.getArity();i++) {
            neighbours[i]=false;
        }
        
	label=new String[this.getArity()];
		
	if ((this.getId()).intValue()==0) {
	    myL=nodeA;
	    mySynchro=1;
	    myState=new String("A,1");
	}
	
        this.putProperty("label",myState);
        
        while (run) {
	    
	    father=-1;
	    int countA=0,count=0,countL;
	    Door door=new Door();
	    int i;
	    boolean fir=false;
	    
	    do {
	       
		if ((myL.compareTo(nodeA)==0) && (!fir)) {
		    if (father==-1)
			this.sendAll(new StringMessage(myL,labels));
		    else
			for (int j=0;j<this.getArity();j++)
			    if (j!=father)
				this.sendTo(j,new StringMessage(myL,labels));
		    fir=true;
		}
		
		String la=(((StringMessage) this.receive(door)).data());
		i=door.getNum();
		
		label[i]=la;
		
		if (label[i].compareTo(nodeF)==0) {
		    count--;
		    /*    int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
		    sendTo(i,new IntegerMessage(new Integer(choosenNumber),labels));
		    int res=(((IntegerMessage) receiveFrom(i)).data()).intValue();
		    
		    if (choosenNumber>res) {
			myL=nodeA;
		    */
		    neighbours[i]=false;
		    this.setDoorState(new MarkedState(false),i);
			
		    //mySynchro++;
		    //}
		    
		    ssy=new String((new Integer(mySynchro)).toString());
		    myState=myL+","+ssy;
		    this.putProperty("label",myState);
		}
		
		if (label[i].compareTo(nodeA)==0) {
		    countA++;
		    if (father==-1)
			father=i;
		}
		
		if (label[i].compareTo(nodeL)==0) {
		    neighbours[i]=true;
		}
		
		if ((myL.compareTo(nodeN)==0) && (father!=-1)) {
		    myL=nodeA;
		    mySynchro++;
		    neighbours[father]=true;

		    ssy=new String((new Integer(mySynchro)).toString());
		    myState=myL+","+ssy;
		    this.putProperty("label",myState);
		    
		    this.sendTo(father,new StringMessage(nodeL,labels));
		    this.setDoorState(new MarkedState(true),father);
		}
		count++;
	    }
	    while (count<this.getArity());
	    
	   
	    
	    if (countA==this.getArity())
		myL=nodeN;
	    
	    fir=false;

	    countL=0;
	    
	    for (int j=0;j<this.getArity();j++)
		if (neighbours[j])
		    countL++;
	    
	    if (countL==1)
		myL=nodeN;

	    ssy=new String((new Integer(mySynchro)).toString());
	    myState=myL+","+ssy;
	    this.putProperty("label",myState);

	    
		
	    do {
		if ((myL.compareTo(nodeN)==0) && (!fir) && (father!=-1)) {
		    /*int j=0;
		    while (!neighbours[j])
			j++;
		    if (j<getArity()) {
			sendTo(j,new StringMessage(nodeF,labels));
		    */
		    //if (father!=-1) {
		    this.sendTo(father,new StringMessage(nodeF,labels));
		    neighbours[father]=false;
		    countL--;
			//}
		    fir=true;
		    //}
		}
		
		if (!fir) {
		    String la=(((StringMessage) this.receive(door)).data());
		    i=door.getNum();
		    
		    if (neighbours[i]) {
			neighbours[i]=false;
			countL--;
		    }
		    this.setDoorState(new MarkedState(false),i);
		    
		    if (countL==1)
			myL=nodeN;
		    if (countL==0) {
			myL=nodeA;
			mySynchro++;
			fir=true;
		    }
		}
	    }
	    while(!fir);

	    ssy=new String((new Integer(mySynchro)).toString());
	    myState=myL+","+ssy;
	    this.putProperty("label",myState); 
	}
    }
    public Object clone(){
        return new SynchroDist();
    }
}

package visidia.algoRMI;
import visidia.misc.IntegerMessage;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.AlgorithmDist;


public class ColorationRmi extends AlgorithmDist {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7361958588127066919L;
	final int starCenter=-1;
    final int notInTheStar=-2;
    
    public ColorationRmi() {
	this.addMessageType(MessageType.defaultMessageType);
    }

    public void init(){
	String myColor=new String("X");
	int myC=0;
	int synchro;
	String neighbours[];
	
	neighbours=new String[this.getArity()];

	this.putProperty("label",myColor);
	
	while (true) {
	    synchro=this.starSynchro();
	    if (synchro==this.starCenter ){  
		for (int i=0;i<this.getArity();i++)
		    neighbours[i]=((StringMessage) this.receiveFrom(i)).data();

		if ((neighbours[0].compareTo(myColor)==0) && 
		    (neighbours[1].compareTo(myColor)==0)) {
		    myC=(myC+1)%3;
		    myColor=this.getNewColor(myC);
		}
		else 
		    while ((neighbours[0].compareTo(myColor)==0) || 
			   (neighbours[1].compareTo(myColor)==0)) {
			myC=(myC+1)%3;
			myColor=this.getNewColor(myC);
		    }
		
		this.putProperty("label",myColor);

		this.breakSynchro();
		    
	    }
	    else {
		if (synchro != this.notInTheStar) {  
		    this.sendTo(synchro,new StringMessage(myColor));
		} 
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
    
    public int starSynchro(){
	
	int arite = this.getArity() ;
	int[] answer = new int[arite] ;

	/*random */
	int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	
	/*Send to all neighbours */
	for (int i=0;i<arite;i++)
	    this.sendTo(i,new IntegerMessage(new Integer(choosenNumber)));
	
	/*receive all numbers from neighbours */
	for( int i = 0; i < arite; i++){
	    Message msg = this.receiveFrom(i);
	    answer[i]= ((IntegerMessage)msg).value();
	    
	}


	int max = choosenNumber;
	for( int i=0;i < arite ; i++){
	    if( answer[i] >= max )
		max = answer[i];
	}
	
	for (int i=0;i<arite;i++)
	    this.sendTo(i,new IntegerMessage(new Integer(max)));
	
	/*get alla answers from neighbours */
	for( int i = 0; i < arite; i++){
	    Message msg = this.receiveFrom(i);
	    answer[i]= ((IntegerMessage)msg).value();
	}
	
	/*get the max */
        max =choosenNumber;
	for( int i=0;i < arite ; i++){
	    if( answer[i] >= max )
		max = answer[i];
	}
	
	if (choosenNumber >= max) {
	    for( int door = 0; door < this.getArity(); door++){
		this.setDoorState(new MarkedState(true),door);
	    }
	    
	    for (int i=0;i<arite;i++)
		this.sendTo(i,new IntegerMessage(1));
	     
	    for (int i=0;i<arite;i++) {
		Message msg=this.receiveFrom(i);
		
	    }
	    return this.starCenter;
	}
	else {
	    int inTheStar=this.notInTheStar;

	    for (int i=0;i<arite;i++)
		this.sendTo(i,new IntegerMessage(0));

	    for (int i=0; i<arite;i++) {
		Message msg=this.receiveFrom(i);
		if  (((IntegerMessage)msg).value() == 1) {
		    inTheStar=i;
		}
	    }
	    return inTheStar;
	   
	}
    }

    public void breakSynchro() {
	
	for( int door = 0; door < this.getArity(); door++){
	    this.setDoorState(new MarkedState(false),door);
	}
    }

    public Object clone(){
	return new ColorationRmi();
    }
}

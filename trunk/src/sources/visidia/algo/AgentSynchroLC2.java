package visidia.algo;
import visidia.simulation.*;
import visidia.misc.*;
import java.util.*;

public class AgentSynchroLC2 extends Algorithm {
    
    static Object notReady = new Object();
    static boolean notReadyBool = true;
    static int[] initialLocation;
    static int numberRdv = 0;
    

    static MessageType round1 = new MessageType("round1", true, java.awt.Color.blue);
    static MessageType round2 = new MessageType("round2", true, java.awt.Color.green);
    static MessageType round3 = new MessageType("round3", true, java.awt.Color.yellow);
    //static MessageType booleen = new MessageType("booleen", false, java.awt.Color.blue);
    static MessageType labels = new MessageType("labels", true);
    
    public Collection getListTypes(){
        Collection typesList = new LinkedList();
        typesList.add(round1);
        typesList.add(round2);
	typesList.add(round3);
        typesList.add(labels);
        //typesList.add(booleen);
        return typesList;
    }
    
    public void init(){
	
	int numberOfPresentAgent = 0 ;
	boolean agentPresent = false;

	int arite = getArity() ;

	/*
	if (((String) getProperty("label")).compareTo("A")==0){
	    agentPresent = true;
	    numberOfPresentAgent = 1;
	}
	*/
	
	if( getId().intValue() == 0){
	    //System.out.println("OK");
	    int n = getNetSize();
	    System.out.println("la taille du graphe est :"+n);
	    initialLocation = new int[n];
	    double _k = Math.log(1.5)*n;
	    //System.out.println(_k);
	    int k = (int)_k;
	    //System.out.println("le nombre d agents utilise pour la simulation est :"+ k);
	    Random generator = new Random();
	    while(k>0) {
		int choosenNode= Math.abs((generator.nextInt()))% n ;
		//System.out.println(choosenNode);
		initialLocation[choosenNode] += 1;
		k--;
	    }
	    try{
		synchronized(notReady){
		    notReadyBool = false;
		    notReady.notifyAll();
		}
	    } catch (Exception e) {}
	    
	} else {
	    try{
		synchronized(notReady){
		    if(notReadyBool)
			notReady.wait();
		}
	    } catch (Exception e) {}
	    
	    numberOfPresentAgent= initialLocation[getId().intValue()];
	    if(numberOfPresentAgent > 0) {
		agentPresent = true;
	    }
	}
	
	
	for(int nbr = 0; nbr < 1000; nbr++){
	    Random generator = new Random();
	    
	    if(agentPresent) {
		putProperty("label",new String("A"));
		boolean tentative = true;
		
		/*
		  j'envoi 1 a tous mes voisins
		*/
				
		for(int i=0; i < arite; i++){
		    sendTo(i, new IntegerMessage(new Integer(1),round1));
		}
		
		/*
		  Symetriquement, je recois les message envoye par les autres
		*/
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i);
		}
		
		
		/*
		  j'envoi a tout le monde 0 pour dire (a ceux qui m'ont choisi) Non
		*/
		for( int i = 0; i < arite; i++){
		    sendTo(i, new IntegerMessage(new Integer(0),round2));
		}
		
		/*
		  Symetriquement je recois les reponse des voisins
		*/
		
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i);
		    IntegerMessage smsg = (IntegerMessage) msg;
		    if(smsg.value() == 0) 
			tentative = false;
		}
		
		if (tentative) {
		    for( int door = 0; door < arite; door++){
			setDoorState(new SyncState(true),door);
		    }
		    numberRdv += 1;
		}
		
		/*
		  j'envoi tous mes jeton 1 vers des nouvelles direction et 0 aux autres
		*/
		int go=0;
		int[] goDirection = new int[arite];
		for(int i = 0; i < numberOfPresentAgent; i++) {
		    //generator = new Random();
		    int reste = Math.abs((generator.nextInt()))% 2 ;
		    if(reste == 1) {
			//generator = new Random();
			int choosenDirection = Math.abs((generator.nextInt()))% arite ;
			goDirection[choosenDirection]+=1;
			go=go+1;
		    }
		}
		
		numberOfPresentAgent= numberOfPresentAgent - go;
		
		for(int i=0; i < arite; i++){
			sendTo(i,new IntegerMessage(new Integer(goDirection[i]),labels));
		}
		

		
		
		/*
		  Symetriquement je regarde s'il y'a de nouveau jeton
		*/
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    numberOfPresentAgent+=smsg.value();
		}
		
		if(numberOfPresentAgent > 0)
		    agentPresent = true;
		else 
		    agentPresent = false;
		
		for( int door = 0; door < arite; door++){
		    setDoorState(new SyncState(false),door);
		}
		
	    } else {
		putProperty("label",new String("N"));
		Vector ilMontChoisit = new Vector();
		
		// j'envoi 0 a tout le monde
		for(int i=0; i < arite; i++){
		    sendTo(i, new IntegerMessage(new Integer(0),round1));
		}
		
		// je recois les requetes des voisins
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    // un jeton est arrive
		    if(smsg.value() == 1) {
			ilMontChoisit.addElement(new Integer(i));
		    }
		}
		
		// j'envoi 1 a tout ceux qui m'ont choisit
		if(ilMontChoisit.size() != 0) {
		    int choosenLC2 = Math.abs((generator.nextInt())) % (ilMontChoisit.size());

		    sendTo(choosenLC2, new IntegerMessage(new Integer(1),round2));
		    

		    for(int i=0; i < arite; i++){
			if(i != choosenLC2)
			    sendTo(i, new IntegerMessage(new Integer(0),round2));
		    }	
		} else {
		    for(int i=0; i < arite; i++){
			sendTo(i, new IntegerMessage(new Integer(0),round2));
		    }
		}
		
		// symetriquement je recoit les reponses
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i);
		    IntegerMessage smsg = (IntegerMessage) msg;
		}
		
		// j'envoi 0
		for( int i = 0; i < arite; i++){
		    sendTo(i, new IntegerMessage(new Integer(0),round3));
		}
		
		// je recois les nouveau jeton
		for( int i = 0; i < arite; i++){
		    Message msg = receiveFrom(i);
		    IntegerMessage smsg = (IntegerMessage) msg;
		    numberOfPresentAgent+=smsg.value();
		}
		
		if(numberOfPresentAgent > 0)
		    agentPresent = true;
		else 
		    agentPresent = false;
		
	    }
	    
	}
	
	if(getId().intValue() == 0) {
	    System.out.println("nombre de RDV : "+numberRdv);
	    //System.out.println("##############################################");
	    notReadyBool = true;
	    numberRdv = 0;
	}
    }


    public Object clone(){
	return new AgentSynchroLC2();
    }
}


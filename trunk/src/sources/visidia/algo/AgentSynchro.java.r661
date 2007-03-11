package visidia.algo;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import visidia.misc.IntegerMessage;
import visidia.misc.IntegerMessageCriterion;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.simulation.Algorithm;

public class AgentSynchro extends Algorithm {
    
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

	int arite = this.getArity() ;

	/*
	if (((String) getProperty("label")).compareTo("A")==0){
	    agentPresent = true;
	    numberOfPresentAgent = 1;
	}
	*/
	
	if( this.getId().intValue() == 0){
	    //System.out.println("OK");
	    int n = this.getNetSize();
	    System.out.println("la taille du graphe est :"+n);
	    initialLocation = new int[n];
	    double _k = Math.log(2)*n;
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
	    
	    numberOfPresentAgent= initialLocation[this.getId().intValue()];
	    if(numberOfPresentAgent > 0) {
		agentPresent = true;
	    }
	}
	
	
	for(int nbr = 0; nbr < 1000; nbr++){
	    Random generator = new Random();
			
	    if(agentPresent) {
		this.putProperty("label",new String("A"));
		boolean tentative = false;
		
		/*
		  je choisi un et je lui envoi 0, tout les autres je leur envoi 2
		*/
		/*choice of the neighbour*/
		int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;
		
		this.sendTo(choosenNeighbour,new IntegerMessage(new Integer(1),labels));
		for(int i=0; i < arite; i++){
		    if( i != choosenNeighbour)
			this.sendTo(i, new IntegerMessage(new Integer(2),round1));
		}
		
		/*
		  Symetriquement, je recois les message envoye par les autres
		*/
		for( int i = 0; i < arite; i++){
		    /*Message msg =*/ this.receiveFrom(i,new IntegerMessageCriterion());
		}
		
		
		/*
		  j'envoi a tout le monde 0 pour dire (a ceux qui m'ont choisi) Non
		*/
		for( int i = 0; i < arite; i++){
		    this.sendTo(i, new IntegerMessage(new Integer(0),round2));
		}
		
		/*
		  Symetriquement je recois les reponse des voisins
		*/
		
		for( int i = 0; i < arite; i++){
		    Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    if((i == choosenNeighbour) && (smsg.value() == 1)) 
			tentative = true;
		}
		if (tentative) {
		    this.setDoorState(new MarkedState(true),choosenNeighbour);
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
			this.sendTo(i,new IntegerMessage(new Integer(goDirection[i]),labels));
		}
		
		/*
		  Symetriquement je regarde s'il y'a de nouveau jeton
		*/
		for( int i = 0; i < arite; i++){
		    Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    numberOfPresentAgent+=smsg.value();
		}
		
		if(numberOfPresentAgent > 0)
		    agentPresent = true;
		else 
		    agentPresent = false;
		
		this.setDoorState(new MarkedState(false),choosenNeighbour);
		
		
	    } else {
		this.putProperty("label",new String("N"));
		Vector ilMontChoisit = new Vector();
		
		// j'envoi 0 a tout le monde
		for(int i=0; i < arite; i++){
		    this.sendTo(i, new IntegerMessage(new Integer(0),round1));
		}
		
		// je recois les requetes des voisins
		for( int i = 0; i < arite; i++){
		    Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    // un jeton est arrive
		    if(smsg.value() == 1) {
			ilMontChoisit.addElement(new Integer(i));
		    }
		}
		
		// je renvoi 0 ou 1 si il y'a un voisin qui m'a envoye 1 (un jeton)
		if (ilMontChoisit.size() != 0) {
		    //Random generator = new Random();
		    int choosenNeighbour= Math.abs((generator.nextInt()))% ilMontChoisit.size() ;
		    
		    this.sendTo(((Integer) (ilMontChoisit.elementAt(choosenNeighbour))).intValue(),new IntegerMessage(new Integer(1),labels));
		    
		    for(int i=0; i < arite; i++){
			if( i != ((Integer) (ilMontChoisit.elementAt(choosenNeighbour))).intValue())
			    this.sendTo(i, new IntegerMessage(new Integer(0),round2));
		    }	
		} else {
		    for( int i = 0; i < arite; i++){
			this.sendTo(i, new IntegerMessage(new Integer(0),round2));
		    }
		}
		
		// symetriquement je recoit les reponse
		for( int i = 0; i < arite; i++){
		    /*Message msg =*/ this.receiveFrom(i,new IntegerMessageCriterion());
		}
		
		// j'envoi 0
		for( int i = 0; i < arite; i++){
		    this.sendTo(i, new IntegerMessage(new Integer(0),round3));
		}
		
		// je recois les nouveau jeton
		for( int i = 0; i < arite; i++){
		    Message msg = this.receiveFrom(i,new IntegerMessageCriterion());
		    IntegerMessage smsg = (IntegerMessage) msg;
		    numberOfPresentAgent+=smsg.value();
		}
		
		if(numberOfPresentAgent > 0)
		    agentPresent = true;
		else 
		    agentPresent = false;
		
	    }
	    
	}
	if(this.getId().intValue() == 0) {
	    System.out.println("nombre de RDV : "+numberRdv);
	    //System.out.println("##############################################");
	    notReadyBool = true;
	    numberRdv = 0;
	}
    }


    public Object clone(){
	return new AgentSynchro();
    }
}
    

package fr.enserb.das.algo;

import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import java.util.Random;


public abstract class SynchroAlgo extends Algorithm
{

    /**
     * essaie de synchroniser le noeud.retourne uniquement 
     * si la synchronisation est réussie.
     */
    public int synchronization(){
	int i = -1;
	int a =getArity();
	
	//interface graphique:je ne suis plus synchro
	for(int door=0;door < a;door++)
	setDoorState(new SyncState(false),door);

	while(i <0){
	    i = trySynchronize();
	}
	//interface graphique: je suis synchro sur la porte i
	setDoorState(new SyncState(true),i);
	return i;	
    }
    
    
    /**
     * Un round de la synchronisation.
     */
    private int trySynchronize(){
	int arite = getArity() ;
	int[] answer = new int[arite] ;

	/*choice of the neighbour*/
	Random generator = new Random();
	int choosenNeighbour= Math.abs((generator.nextInt()))% arite ;

	sendTo(choosenNeighbour,new SyncMessage(new Integer(1)));
	for(int i=0; i < arite; i++){
	    if( i != choosenNeighbour)
		sendTo(i, new SyncMessage(new Integer(0)));
	  
	}
    
	for( int i = 0; i < arite; i++){
	    Message msg = receiveFrom(i,new SyncMessageCriterion());
	    SyncMessage smsg = (SyncMessage) msg;
	  
	    answer[i]= smsg.value();
	}

	if (answer[choosenNeighbour] == 1){
	    return choosenNeighbour;
	}
	else {
	    return -1 ;
	}
    }
}





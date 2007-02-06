package visidia.algo;

import visidia.simulation.*;
import visidia.misc.*;

public class RendezVous extends Algorithm{

    public void init(){

	int neighbourCount = this.getArity();
	if(neighbourCount == 0){
	    //nothing to do
	    return;
	}

        while(true){
	    int rendezVousNeighbour = this.chooseNeigbour();

	    //send 1 to rendezVousNeighbour
	    this.sendTo(rendezVousNeighbour, new IntegerMessage(1));

	    //and send 0 to others
	    for(int i = 0; i < neighbourCount; i++){
		if(i == rendezVousNeighbour) continue;

		this.sendTo(i, new IntegerMessage(0));
	    }


	    boolean rendezVousIsAccepted = false;

	    //receive all neighbour rendez-vous message
	    //and check whether the selected neigbour accept
	    //the rendez-vous.
	    for(int i = 0; i < neighbourCount; i++){
		IntegerMessage mesg = (IntegerMessage) this.receiveFrom(i);
		if((i == rendezVousNeighbour) && (mesg.value() == 1)){
		    rendezVousIsAccepted = true;
		}
	    }

	    //send "HELLO" to the selectedNeigbour if the rendez-vous is
	    // accepted.
	    if(rendezVousIsAccepted){
                this.setDoorState(new MarkedState(true),rendezVousNeighbour);

		this.sendTo(rendezVousNeighbour, new StringMessage("Hello !!!"));
		this.receiveFrom(rendezVousNeighbour);

                this.setDoorState(new MarkedState(false),rendezVousNeighbour);
	    }
	}
    }


    private int chooseNeigbour(){
	return Math.abs((SynchronizedRandom.nextInt())) % this.getArity() ;
    }

    public Object clone(){
	return new RendezVous();
    }
    
}

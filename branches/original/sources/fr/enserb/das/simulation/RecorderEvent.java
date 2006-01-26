package fr.enserb.das.simulation;

import java.util.Calendar;
import java.io.*;
import fr.enserb.das.tools.Queue;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;


public class RecorderEvent implements Runnable, Cloneable {

    private Queue evtIn, evtOut;
    private ObjectWriter writer;

    public RecorderEvent (Queue evtIn_, Queue evtOut_, ObjectWriter writer_){
	evtIn = evtIn_;
	evtOut = evtOut_;
	writer = writer_;
    }

    public void run () {
	while (true) {
	    SimulEvent simEvt = null;

	    try{
		simEvt = (SimulEvent) evtIn.get();
	    }
	    catch(ClassCastException e){
		e.printStackTrace();
	    }
	    catch (InterruptedException e) {
		break;		
	    }

	    writer.writeObject(simEvt);

	    try {
		evtOut.put(simEvt);
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
		continue;		
	    }

	}
    }
}











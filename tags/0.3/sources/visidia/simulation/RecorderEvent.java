package visidia.simulation;

import java.util.Calendar;
import java.io.*;
import visidia.tools.VQueue;
import visidia.simulation.*;
import visidia.misc.*;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;


public class RecorderEvent implements Runnable, Cloneable {

    private VQueue evtIn, evtOut;
    private ObjectWriter writer;

    public RecorderEvent (VQueue evtIn_, VQueue evtOut_, ObjectWriter writer_){
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











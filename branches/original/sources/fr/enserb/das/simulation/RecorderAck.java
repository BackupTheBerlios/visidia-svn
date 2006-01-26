package fr.enserb.das.simulation;

import java.text.DateFormat;
import java.io.*;
import fr.enserb.das.tools.Queue;
import fr.enserb.das.simulation.SimulAck;
import fr.enserb.das.simulation.SimulEvent;
import fr.enserb.das.simulation.*;
import fr.enserb.das.misc.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;


public class RecorderAck implements Runnable, Cloneable {

    private Queue ackIn, ackOut;
    private ObjectWriter writer;

    public RecorderAck (Queue ackIn_, Queue ackOut_, ObjectWriter writer_){
	ackIn = ackIn_;
	ackOut = ackOut_;
	writer = writer_;
    }
    
    public void run () {
	while (true) {
	    SimulAck simAck = null;

	    try {
		simAck = (SimulAck) ackIn.get();
	    }
	    catch (ClassCastException e) {
		e.printStackTrace();
		continue;
	    }
	    catch (InterruptedException e) {
		break;		
	    }

	    writer.writeObject(simAck);

	    try {
		ackOut.put(simAck);
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
		continue;		
	    }
	}
    }
}

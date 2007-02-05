package visidia.simulation;

import visidia.tools.VQueue;
import visidia.simulation.SimulAck;

public class RecorderAck implements Runnable, Cloneable {

    private VQueue ackIn, ackOut;
    private ObjectWriter writer;

    public RecorderAck (VQueue ackIn_, VQueue ackOut_, ObjectWriter writer_){
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

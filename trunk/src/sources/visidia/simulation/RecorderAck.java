package visidia.simulation;

import visidia.tools.VQueue;
import visidia.simulation.SimulAck;

public class RecorderAck implements Runnable, Cloneable {

    private VQueue ackIn, ackOut;
    private ObjectWriter writer;

    public RecorderAck (VQueue ackIn_, VQueue ackOut_, ObjectWriter writer_){
	this.ackIn = ackIn_;
	this.ackOut = ackOut_;
	this.writer = writer_;
    }
    
    public void run () {
	while (true) {
	    SimulAck simAck = null;

	    try {
		simAck = (SimulAck) this.ackIn.get();
	    }
	    catch (ClassCastException e) {
		e.printStackTrace();
		continue;
	    }
	    catch (InterruptedException e) {
		break;		
	    }

	    this.writer.writeObject(simAck);

	    try {
		this.ackOut.put(simAck);
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
		continue;		
	    }
	}
    }
}

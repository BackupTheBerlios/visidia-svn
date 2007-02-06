package visidia.simulation;

import java.io.*;
import visidia.tools.VQueue;
import java.util.Vector;
import visidia.simulation.SimulAck;
import visidia.simulation.SimulEvent;
import visidia.tools.Element;

public class Reader implements Runnable, Cloneable {
    private Vector messages = new Vector();
    private Vector stringMessages = new Vector();
    private VQueue ackOut = null;
    private VQueue evtOut = null;
    private File file;

    public Reader (VQueue ao, VQueue eo, File f) {
	this.evtOut = eo;
	this.ackOut = ao;
	this.file = f;
    }

    private void initialize () {
	FileInputStream fileIS = null;
	ObjectInputStream objectIS = null;

	try {
	    fileIS = new FileInputStream(this.file);
	    objectIS = new ObjectInputStream(fileIS);

	    while(fileIS.available() > 0){
		Object o = null;
		o = objectIS.readObject();
		if (o instanceof SimulAck)
		    this.messages.add(new Element((SimulAck) o));
		else if (o instanceof SimulEvent)
		    this.messages.add(new Element((SimulEvent) o));
	    }
	    objectIS.close();
 	}
	catch (FileNotFoundException e){
	    e.printStackTrace();
	}
	catch (IOException e){
	    e.printStackTrace();
	}
	catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
   }

    private int contains(Vector pipe, Element a) {
	int i=0;
	if (pipe.size() != 0) {
	    while (pipe.size() > i){ 
		if (a.getSimulAck().number().equals(((SimulAck) pipe.elementAt(i)).number()))
		    return i;
		else
		    i++;
	    }
	}
	
	return -1;
    }
    
    public void run(){
	//int i=0;
	//int j=0;
	Vector ackRcv = new Vector();

	while (this.messages.size() != 0){ // while there are messages to send
	    if (((Element) this.messages.firstElement()).isEvent()){
		try {
		    this.evtOut.put(((Element) this.messages.firstElement()).getSimulEvent());
		}
		catch (InterruptedException e) {
		    // e.printStackTrace();
		    break;
		}
		this.messages.remove(0);
	    }
	    else{
		int position = -1;
		if ((position = this.contains(ackRcv, (Element) this.messages.elementAt(0))) == -1)
		    while ((position = this.contains(ackRcv, (Element) this.messages.elementAt(0))) == -1) {
			try {
			    ackRcv.add(this.ackOut.get());
			}
			catch (InterruptedException e) {
			    //			    e.printStackTrace();
			    return;
			}
		    }
		ackRcv.remove(position);
		this.messages.remove(0);
	    }
	}
    }

    public void read() {
	this.initialize();
    }
}








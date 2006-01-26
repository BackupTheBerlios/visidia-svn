package fr.enserb.das.simulation;

import java.io.*;
import fr.enserb.das.tools.Queue;
import java.util.Vector;
import java.util.StringTokenizer;
import fr.enserb.das.simulation.SimulConstants;
import fr.enserb.das.misc.Message;
import fr.enserb.das.simulation.SimulAck;
import fr.enserb.das.simulation.SimulEvent;
import fr.enserb.das.tools.Element;
import fr.enserb.das.gui.metier.Graphe;

public class Reader implements Runnable, Cloneable {
    private String trace = null;
    private Vector messages = new Vector();
    private Vector stringMessages = new Vector();
    private Queue ackOut = null;
    private Queue evtOut = null;
    private File file;

    public Reader (Queue ao, Queue eo, File f) {
	evtOut = eo;
	ackOut = ao;
	file = f;
    }

    private void initialize () {
	FileInputStream fileIS = null;
	ObjectInputStream objectIS = null;

	try {
	    fileIS = new FileInputStream(file);
	    objectIS = new ObjectInputStream(fileIS);

	    while(fileIS.available() > 0){
		Object o = null;
		o = objectIS.readObject();
		if (o instanceof SimulAck)
		    messages.add(new Element((SimulAck) o));
		else if (o instanceof SimulEvent)
		    messages.add(new Element((SimulEvent) o));
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
	int i=0, j=0;
	Vector ackRcv = new Vector();

	while (messages.size() != 0){ // while there are messages to send
	    if (((Element) messages.firstElement()).isEvent()){
		try {
		    evtOut.put(((Element) messages.firstElement()).getSimulEvent());
		}
		catch (InterruptedException e) {
		    // e.printStackTrace();
		    break;
		}
		messages.remove(0);
	    }
	    else{
		int position = -1;
		if ((position = contains(ackRcv, (Element) messages.elementAt(0))) == -1)
		    while ((position = contains(ackRcv, (Element) messages.elementAt(0))) == -1) {
			try {
			    ackRcv.add((SimulAck) ackOut.get());
			}
			catch (InterruptedException e) {
			    //			    e.printStackTrace();
			    return;
			}
		    }
		ackRcv.remove(position);
		messages.remove(0);
	    }
	}
    }

    public void read() {
	initialize();
    }
}








package visidia.tools;

//import visidia.misc.Message;
import visidia.simulation.*;



public class Element {

    private SimulAck sa=null;
    private SimulEvent se=null;
    private boolean event;
    
    
    public Element(SimulAck sa_){
	sa = sa_;
	event=false;
    }

    public Element(SimulEvent se_){
	se = se_;
	event=true;
    }
    
    public SimulEvent getSimulEvent() {
	if (isEvent())
	    return se;
	else
	    return null;
    }

    public SimulAck getSimulAck() {
	if (!isEvent())
	    return sa;
	else
	    return null;
    }

    public boolean isEvent() {
	return event;
    }
}


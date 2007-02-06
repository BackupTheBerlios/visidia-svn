package visidia.tools;

import visidia.simulation.*;



public class Element {

    private SimulAck sa=null;
    private SimulEvent se=null;
    private boolean event;
    
    
    public Element(SimulAck sa_){
	this.sa = sa_;
	this.event=false;
    }

    public Element(SimulEvent se_){
	this.se = se_;
	this.event=true;
    }
    
    public SimulEvent getSimulEvent() {
	if (this.isEvent())
	    return this.se;
	else
	    return null;
    }

    public SimulAck getSimulAck() {
	if (!this.isEvent())
	    return this.sa;
	else
	    return null;
    }

    public boolean isEvent() {
	return this.event;
    }
}


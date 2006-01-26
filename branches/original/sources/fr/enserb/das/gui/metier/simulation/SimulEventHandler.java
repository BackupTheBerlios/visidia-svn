package fr.enserb.das.gui.metier.simulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import fr.enserb.das.misc.*;
import fr.enserb.das.simulation.*;
import fr.enserb.das.tools.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
  
/** This class is responsible of dealing with the 
    events coming from the simulator */

public class SimulEventHandler extends Thread {

    // instance variables
    protected FenetreDeSimulation fenetreSimul;
    /* events file from the simulator
     * that will be traduce to the graphics interface
     * by a state change or a message sent */
    protected fr.enserb.das.tools.Queue evtPipe ;
    /* ackitment file of events treated by the graphics interface */
    protected fr.enserb.das.tools.Queue ackPipe ;

    boolean stopped = false;
   
    public SimulEventHandler(FenetreDeSimulation f, fr.enserb.das.tools.Queue evtPipe, fr.enserb.das.tools.Queue ackPipe){	
	this.evtPipe = evtPipe;
	this.ackPipe = ackPipe;
	this.fenetreSimul = f;
    }

    public void abort(){
	stopped = true;
	interrupt();
    }
    
    public void run(){
	try{
	    while(!stopped){
		SimulEvent simEvt = null;
		SimulAck simAck = null;
		try{
		    simEvt = (SimulEvent) evtPipe.get();
		}
		catch(ClassCastException e){
		    e.printStackTrace();
		    continue;
		}
		
		switch(simEvt.type()){
		    
		case SimulConstants.EDGE_STATE_CHANGE:
		    handleEdgeStateChangeEvt(simEvt);
		    break;
		    
		case SimulConstants.MESSAGE_SENT :
		    handleMessageSentEvt(simEvt);	     
		    break;

		case SimulConstants.NODE_PROPERTY_CHANGE :
		    handleNodePropertyChangeEvt(simEvt);
		    break; 

		case SimulConstants.ALGORITHM_END :
		    handleAlgorithmEndEvent(simEvt);
		    break;

		case SimulConstants.EDGE_COLOR_CHANGE:
		    handleEdgeColorChangeEvt(simEvt);
		    break;

		}
	    }
		
	}
	catch(InterruptedException e){
	    // break;
	}
	
    }
  

    
    public void handleNodePropertyChangeEvt(SimulEvent se) throws InterruptedException{
	NodePropertyChangeEvent npce = (NodePropertyChangeEvent) se;
	Hashtable tableSommet = ((fenetreSimul.getVueGraphe()).rechercherSommet(npce.nodeId().toString())).getStateTable();
	    tableSommet.put(npce.getKey(),npce.getValue());
	    
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(npce.eventNumber());
	    ackPipe.put(npca); 		
    }

    public void handleEdgeStateChangeEvt(SimulEvent se) throws InterruptedException{
	EdgeStateChangeEvent esce = (EdgeStateChangeEvent) se;
	
	if(esce.state() instanceof MarkedState){
	    MarkedState state = (MarkedState)esce.state();
	    ((fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).setEtat(state.isMarked());
	    
	}
	else if(esce.state() instanceof SyncState){
	    SyncState state = (SyncState)esce.state();
	    ((fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).enluminer(state.isSynchronized());
	    ((fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId1().toString())).enluminer(state.isSynchronized());
	    ((fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId2().toString())).enluminer(state.isSynchronized());
	    
	}
	EdgeStateChangeAck esca = new EdgeStateChangeAck(esce.eventNumber());
	ackPipe.put(esca); 
	
    }

    /**
     * This method changes the color of an edge according to the event it receives
     * @param se The Simul Event
     * @throws java.lang.InterruptedException
     */
    public void handleEdgeColorChangeEvt(SimulEvent se) throws InterruptedException{
	EdgeColorChangeEvent ecce = (EdgeColorChangeEvent)se;
	
	if(ecce.color() instanceof ColorState){
	    ColorState color = (ColorState)ecce.color();
	    ((fenetreSimul.getVueGraphe()).rechercherArete(ecce.nodeId1().toString(),ecce.nodeId2().toString())).changerCouleurTrait(color.isColored());
	}
	EdgeColorChangeAck ecca = new EdgeColorChangeAck(ecce.eventNumber());
	ackPipe.put(ecca); 
    }
    
    public void handleMessageSentEvt(SimulEvent se){
	MessageSendingEvent mse = (MessageSendingEvent) se;
	fenetreSimul.simulationPanel().animate(mse);
    }
    
    public void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
	fenetreSimul.simulationPanel().pause();
	JOptionPane.showMessageDialog(fenetreSimul,"Algorithms are terminated");
	throw new InterruptedException();	
    }

    
}








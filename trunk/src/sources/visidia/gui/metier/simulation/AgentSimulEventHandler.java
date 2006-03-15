package visidia.gui.metier.simulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import visidia.misc.*;
import visidia.simulation.*;
import visidia.tools.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.AreteDessin;
  
/** This class is responsible of dealing with the 
    events coming from the simulator */

public class AgentSimulEventHandler extends Thread {

    // instance variables
    protected AgentsSimulationWindow agentsSimulationWindow;
    /* events file from the simulator
     * that will be traduce to the graphics interface
     * by a state change or a message sent */
    protected VQueue evtPipe ;
    /* ackitment file of events treated by the graphics interface */
    protected VQueue ackPipe ;

    boolean stopped = false;

    boolean synchroSimul = false;
	
	
    public AgentSimulEventHandler(AgentsSimulationWindow w, VQueue evtPipe, 
                                  VQueue ackPipe){	
	this.evtPipe = evtPipe;
	this.ackPipe = ackPipe;
	this.agentsSimulationWindow = w;
    }

    public void abort(){
	stopped = true;
	interrupt();
    }
    
    public void run(){
	try{
	    while(!stopped){
		SimulEvent simEvt = null;
		try{ 
		    simEvt = (SimulEvent) evtPipe.get();
		}
		catch(ClassCastException e){
		    e.printStackTrace();
		    continue;
		}
		
		switch(simEvt.type()){
		    
		case SimulConstants.MESSAGE_SENT :
		    handleMessageSentEvt(simEvt);
		    break;
                case SimulConstants.ALGORITHM_END :
                    handleAlgorithmEndEvent(simEvt);
		    break;
                case SimulConstants.EDGE_STATE_CHANGE :
                    handleEdgeStateChangeEvt(simEvt);
		    break;
		case SimulConstants.AGENT_MOVED :
		    handleAgentMovedEvt(simEvt);
		}
	    }
	}
	catch(InterruptedException e){
	    // break;
	}
		
    }

    public void handleMessageSentEvt(SimulEvent se){
	MessageSendingEvent mse = (MessageSendingEvent) se;
	//	System.out.println("agentsSimulationWindow.simulationPanel().animate(mse)");
        agentsSimulationWindow.simulationPanel().animate(mse);
    }
    
    public void handleAlgorithmEndEvent(SimulEvent se) 
        throws InterruptedException {

        agentsSimulationWindow.but_pause();
        JOptionPane.showMessageDialog(agentsSimulationWindow,
                                      "Algorithms are terminated");
        throw new InterruptedException();	
    }

    public void handleEdgeStateChangeEvt(SimulEvent se) 
        throws InterruptedException {

        EdgeStateChangeEvent event = (EdgeStateChangeEvent) se;

        if(event.state() instanceof MarkedState) {
            MarkedState state = (MarkedState)event.state();
            VueGraphe vue = agentsSimulationWindow.getVueGraphe();
            AreteDessin arete;
            arete = vue.rechercherArete(event.nodeId1().toString(),
                                        event.nodeId2().toString());
            arete.setEtat(state.isMarked());
            
        }
        else {
            throw new RuntimeException("Other states are not implemented");
        }
        
        EdgeStateChangeAck ack;
        ack = new EdgeStateChangeAck(event.eventNumber());

        ackPipe.put(ack);
    }

    public void handleAgentMovedEvt(SimulEvent se){
	AgentMovedEvent ame = (AgentMovedEvent) se;


	if(ame.nbrAg().intValue() == 0)
	    agentsSimulationWindow.getVueGraphe().
		rechercherSommet(ame.vertexId().toString()).
		changerCouleurFond(Color.white);
	else
	    agentsSimulationWindow.getVueGraphe().
		rechercherSommet(ame.vertexId().toString()).
		changerCouleurFond(Color.red);
    }
       
}



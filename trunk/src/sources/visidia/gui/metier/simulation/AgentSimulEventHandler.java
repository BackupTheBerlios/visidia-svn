package visidia.gui.metier.simulation;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import visidia.misc.*;
import visidia.simulation.*;
import visidia.tools.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.SommetDessin;
  
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
	this.stopped = true;
	this.interrupt();
    }
    
    public void run(){
	try{
	    while(!this.stopped){
		SimulEvent simEvt = null;
		try{ 
		    simEvt = (SimulEvent) this.evtPipe.get();
		}
		catch(ClassCastException e){
		    e.printStackTrace();
		    continue;
		}
		
		switch(simEvt.type()){
		    
		case SimulConstants.MESSAGE_SENT :
		    this.handleMessageSentEvt(simEvt);
		    break;
                case SimulConstants.ALGORITHM_END :
                    this.handleAlgorithmEndEvent(simEvt);
		    break;
                case SimulConstants.EDGE_STATE_CHANGE :
                    this.handleEdgeStateChangeEvt(simEvt);
		    break;
		case SimulConstants.AGENT_MOVED :
		    this.handleAgentMovedEvt(simEvt);
                    break;
		case SimulConstants.LABEL_CHANGE :
		    this.handleLabelChangeEvt(simEvt);
                    break;
		case SimulConstants.NEXT_PULSE :
		    this.handleNextPulse(simEvt);
		    break;

		}
	    }
	}
	catch(InterruptedException e){
	    // break;
	}

    }

    public void handleMessageSentEvt(SimulEvent se){
	MessageSendingEvent mse = (MessageSendingEvent) se;

        

        this.agentsSimulationWindow.simulationPanel().animate(mse);
    }
    
    public void handleAlgorithmEndEvent(SimulEvent se) 
        throws InterruptedException {

        this.agentsSimulationWindow.but_pause();
        JOptionPane.showMessageDialog(this.agentsSimulationWindow,
                                      "Algorithms are terminated");
        this.agentsSimulationWindow.but_stop();
        throw new InterruptedException();	
    }

    public void handleEdgeStateChangeEvt(SimulEvent se) 
        throws InterruptedException {

        EdgeStateChangeEvent event = (EdgeStateChangeEvent) se;

        if(event.state() instanceof MarkedState) {
            MarkedState state = (MarkedState)event.state();
            VueGraphe vue = this.agentsSimulationWindow.getVueGraphe();
            AreteDessin arete;
            arete = vue.rechercherArete(event.nodeId1().toString(),
                                        event.nodeId2().toString());
            arete.setEtat(state.isMarked());
            // BILEL
	    this.agentsSimulationWindow.simulationPanel().repaint();
        } else if (event.state() instanceof SyncState) {
            SyncState state = (SyncState)event.state();
            VueGraphe vue = this.agentsSimulationWindow.getVueGraphe();
	    (vue.rechercherArete(event.nodeId1().toString(),event.nodeId2().toString())).enluminerBis(state.isSynchronized());
            //arete.eluminerBis(state.isSynchronized());
	    vue.rechercherSommet(event.nodeId1().toString()).enluminerBis(state.isSynchronized());
	    vue.rechercherSommet(event.nodeId2().toString()).enluminerBis(state.isSynchronized());
	    // BILEL
	    this.agentsSimulationWindow.simulationPanel().repaint(); 
	} else {
	    throw new RuntimeException("Other states are not implemented");
	}
	
	//  ne sert à priori à rien d'acquitter, si pas de waitForAnswer
        EdgeStateChangeAck ack;
        ack = new EdgeStateChangeAck(event.eventNumber());

        this.ackPipe.put(ack);
    }

    public void handleAgentMovedEvt(SimulEvent se)
	throws InterruptedException {
	AgentMovedEvent ame = (AgentMovedEvent) se;

        SommetDessin vert = this.agentsSimulationWindow.getVueGraphe().
            rechercherSommet(ame.vertexId().toString());
	
	int nbr = ame.nbrAg().intValue();
	String nbrStr = new String();
	nbrStr = nbrStr.valueOf(nbr);

        // Updating the AgentBoxChangingVertexState
        this.agentsSimulationWindow.updateVertexState(vert);

	if(nbr == 0)
	    vert.changerCouleurFond(Color.white);
	else
	    vert.changerCouleurFond(Color.red);

	vert.setNbr(nbrStr);
	//((SommetCarre)vert).setNbr(nbrStr);
	
	// Bilel : ne sert à priori à rien d'aqcuitter, si pas de waitForAnswer
	this.agentsSimulationWindow.simulationPanel().repaint();

	AgentMovedAck ack;
        ack = new AgentMovedAck(ame.eventNumber());

        this.ackPipe.put(ack);
	
    }

    public void handleLabelChangeEvt(SimulEvent se)
	    throws InterruptedException {

	LabelChangeEvent lce = (LabelChangeEvent) se;

	Hashtable tableSommet = ((this.agentsSimulationWindow.getVueGraphe()).
				 rechercherSommet(lce.vertexId().toString())).getStateTable();
	tableSommet.put("label",lce.label());
	    
    }

    public void handleNextPulse(SimulEvent se) {

	int pulse = ((NextPulseEvent)se).pulse();
	this.agentsSimulationWindow.setPulse(pulse);
    }
}



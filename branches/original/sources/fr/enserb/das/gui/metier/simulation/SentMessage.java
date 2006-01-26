package fr.enserb.das.gui.metier.simulation;

import java.awt.*;
import fr.enserb.das.tools.MovableObject;
import fr.enserb.das.simulation.MessageSendingEvent;
import fr.enserb.das.misc.MessageType;
import fr.enserb.das.graph.*;

public class SentMessage extends MovableObject {
    private String mesg;
    private MessageSendingEvent event;
    
    /**
     * for dealing with the action made on sending message
     */
    public SentMessage(MessageSendingEvent event, Point a, Point b, double step){
	super(a, b, step);
	this.event = event;
	this.mesg = event.message().toString();
    }
    
    public MessageSendingEvent getEvent(){
	return event;
    }
    
    
    /**
     *
     */
    public void paint(Graphics g){
	MessageType messageType = event.message().getType();
	if (messageType.getToPaint()){
	   
	    if ((event.message()).getVisualization()) {
		Point p = currentLocation();
		g.setColor(messageType.getColor());
		g.drawString(mesg, p.x, p.y);
	    }
	}
    }
}

package visidia.gui.metier.simulation;

import java.awt.Graphics;
import java.awt.Point;

import visidia.misc.MessageType;
import visidia.simulation.MessageSendingEvent;
import visidia.tools.MovableObject;

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
	return this.event;
    }
    
    
    /**
     *
     */
    public void paint(Graphics g){
	
	MessageType messageType = this.event.message().getType();
	if (messageType.getToPaint()){
	   
	    if ((this.event.message()).getVisualization()) {
		Point p = this.currentLocation();
		g.setColor(messageType.getColor());
		g.drawString(this.mesg, p.x, p.y);
	    }
	}
    }
}

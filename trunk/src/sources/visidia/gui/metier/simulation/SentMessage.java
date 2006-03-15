package visidia.gui.metier.simulation;

import java.awt.*;
import visidia.tools.MovableObject;
import visidia.simulation.MessageSendingEvent;
import visidia.misc.MessageType;
import visidia.graph.*;
import javax.swing.ImageIcon;

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
	
	ImageIcon monIcone = new ImageIcon("visidia/gui/donnees/images/aide.gif");
	Image monImage = monIcone.getImage();

	MessageType messageType = event.message().getType();
	if (messageType.getToPaint()){
	   
	    if ((event.message()).getVisualization()) {
		Point p = currentLocation();
		g.setColor(messageType.getColor());
		g.drawString(mesg, p.x, p.y);
		g.drawImage(monImage,p.x,p.y,null,null);
	    }
	}
    }
}

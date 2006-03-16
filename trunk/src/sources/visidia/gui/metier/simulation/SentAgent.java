package visidia.gui.metier.simulation;

import java.awt.*;
import visidia.tools.MovableObject;
import visidia.simulation.MessageSendingEvent;
import visidia.misc.MessageType;
import visidia.graph.*;
import visidia.gui.donnees.TableImages;


public class SentAgent extends MovableObject {
    private String mesg;
    private MessageSendingEvent event;
    
    /**
     * for dealing with the action made on sending message
     */
    public SentAgent(MessageSendingEvent event, Point a, Point b, double step){
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
	
	Image img = TableImages.getImage("homme");
	int height = img.getHeight(null);
	int width = img.getWidth(null);

	MessageType messageType = event.message().getType();
	if (messageType.getToPaint()){
	   
	    if ((event.message()).getVisualization()) {
		Point p = currentLocation();
		g.setColor(messageType.getColor());
		g.drawString(mesg, p.x-(width/2), p.y+(height/2));
		g.drawImage(img,p.x-(width/2),p.y-(height/2),null,null);
	    }
	}
    }
}

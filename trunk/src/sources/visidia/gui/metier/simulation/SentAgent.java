package visidia.gui.metier.simulation;

import java.awt.*;
import visidia.tools.MovableObject;
import visidia.simulation.MessageSendingEvent;
import visidia.misc.MessageType;
import visidia.gui.donnees.TableImages;


public class SentAgent extends MovableObject {
    private String mesg;
    private MessageSendingEvent event;
    private boolean moveForward;
    
    /**
     * for dealing with the action made on sending message
     */
    public SentAgent(MessageSendingEvent event, Point a, Point b, double step){
	super(a, b, step);
	this.event = event;
	this.mesg = event.message().toString();

	if(a.getX() < b.getX())
	    this.moveForward = true;
	else
	    this.moveForward = false;
    }
    
    public MessageSendingEvent getEvent(){
	return this.event;
    }
    
    
    /**
     *
     */
    public void paint(Graphics g){

	Image img;

	if(this.moveForward)
	    img = TableImages.getImage("miroirHomme");
	else
	    img = TableImages.getImage("homme");
	    
	int imgHeight = img.getHeight(null);
	int imgWidth = img.getWidth(null);
	int stringSize = (int)(g.getFontMetrics().
			       getStringBounds(this.mesg,g).getWidth());
	

	MessageType messageType = this.event.message().getType();
	if (messageType.getToPaint()){
	   
	    if ((this.event.message()).getVisualization()) {
		Point p = this.currentLocation();
		g.setColor(messageType.getColor());
		g.drawString(this.mesg, p.x-(stringSize/2), p.y+(imgHeight/2));
		g.drawImage(img,p.x-(imgWidth/2),p.y-(imgHeight/2),null,null);
	    }
	}
    }
}

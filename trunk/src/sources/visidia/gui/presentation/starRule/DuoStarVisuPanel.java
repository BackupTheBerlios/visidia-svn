package visidia.gui.presentation.starRule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import visidia.gui.presentation.*;

/**
 * Visualize and permits to compose two stars at the same time.
 * Any modifications of structure on one are applied on the other.
 * Indeed, when an event occurs, it is applied to the VueGraphe where the
 * mouse is located, and if it does not imply structure alteration, it is
 * applied to the other VueGraphe.

 * The parent must add the instance to its MouseListener and MouseMotionListener
 */
public class DuoStarVisuPanel extends MouseAdapter implements MouseMotionListener {
    
    VueGraphe vgLeft, vgRight;
    int xPanelCenter;
    int ray, distBetweenCenters;
    JPanel parent;    

    StarVisuPanel svpLeft, svpRight;

    public DuoStarVisuPanel(JPanel parent,
			    VueGraphe vgLeft, VueGraphe vgRight, 
			    Point centerLeft, Point centerRight, 
			    int distBetweenCenters, int ray, boolean isSimpleRule) {
	this.vgLeft = vgLeft;
	this.vgRight = vgRight;
	this.ray = ray;
	this.distBetweenCenters = distBetweenCenters;
	this.parent = parent;

	this.launchStarVisuPanels(centerLeft, centerRight, isSimpleRule);
    }

    /**
     * Can be used in order to change the locations
     */
    public void launchStarVisuPanels(Point centerLeft, Point centerRight, 
				     boolean isSimpleRule) {
    	this.xPanelCenter = (centerLeft.x + centerRight.x) / 2;
	this.svpLeft = new StarVisuPanel(this.vgLeft, this.ray, centerLeft, this.parent, isSimpleRule);
	this.svpRight = new StarVisuPanel(this.vgRight, this.ray, centerRight, this.parent, isSimpleRule);
    }

    
    public void reorganizeVertex() {
	this.svpLeft.reorganizeVertex();
	this.svpRight.reorganizeVertex();
    }

    // Returns the same event with a x position available to the left or to the
    // right side of the panel
    private MouseEvent makeMouseEvent(MouseEvent evt, boolean left) {
	int x = evt.getX();
	int y = evt.getY();
	if ((left && (x < this.xPanelCenter)) 
	    || (!left && (x >= this.xPanelCenter))) {
	    return evt;
	} else {
	    return new MouseEvent(evt.getComponent(), evt.getID(),
				  evt.getWhen(), evt.getModifiers(), 
				  (left 
				   ? x - this.distBetweenCenters 
				   : x + this.distBetweenCenters), y, 
				  evt.getClickCount(), evt.isPopupTrigger());
	}
    }
    
    public void mousePressed(MouseEvent evt) {
	int modifiers = evt.getModifiers();
	//System.out.println ("WWW " + evt.getX() + " " + (evt.getX() - xPanelCenter));
	if (evt.getX() < this.xPanelCenter) {
	    this.svpLeft.mousePressed(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		this.svpRight.mousePressed(this.makeMouseEvent(evt, false));
	} else {
	    this.svpRight.mousePressed(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		this.svpLeft.mousePressed(this.makeMouseEvent(evt, true));
	}
    } 	
    
    public void mouseDragged(MouseEvent evt) {
	this.svpLeft.mouseDragged(this.makeMouseEvent(evt, true));
	this.svpRight.mouseDragged(this.makeMouseEvent(evt, false));
    }

    public void mouseReleased(MouseEvent evt) {
	int modifiers = evt.getModifiers();
	
	if (evt.getX() < this.xPanelCenter) {
	    this.svpLeft.mouseReleased(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		this.svpRight.mouseReleased(this.makeMouseEvent(evt, false));
	} else {
	    this.svpRight.mouseReleased(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		this.svpLeft.mouseReleased(this.makeMouseEvent(evt, true));
	}
    }
	
    public void mouseMoved(MouseEvent evt) {}    
}

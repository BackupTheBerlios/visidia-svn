package visidia.gui.presentation.starRule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.border.*;
import visidia.gui.presentation.*;
//import visidia.gui.metier.Graphe;
//import visidia.gui.presentation.boite.EtatPanel;
//import visidia.gui.presentation.boite.VueEtatPanel;
//import visidia.gui.donnees.TableCouleurs;
//import java.util.*;

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

	launchStarVisuPanels(centerLeft, centerRight, isSimpleRule);
    }

    /**
     * Can be used in order to change the locations
     */
    public void launchStarVisuPanels(Point centerLeft, Point centerRight, 
				     boolean isSimpleRule) {
    	xPanelCenter = (centerLeft.x + centerRight.x) / 2;
	svpLeft = new StarVisuPanel(vgLeft, ray, centerLeft, parent, isSimpleRule);
	svpRight = new StarVisuPanel(vgRight, ray, centerRight, parent, isSimpleRule);
    }

    
    public void reorganizeVertex() {
	svpLeft.reorganizeVertex();
	svpRight.reorganizeVertex();
    }

    // Returns the same event with a x position available to the left or to the
    // right side of the panel
    private MouseEvent makeMouseEvent(MouseEvent evt, boolean left) {
	int x = evt.getX();
	int y = evt.getY();
	if ((left && x < xPanelCenter) 
	    || (!left && x >= xPanelCenter)) {
	    return evt;
	} else {
	    return new MouseEvent(evt.getComponent(), evt.getID(),
				  evt.getWhen(), evt.getModifiers(), 
				  (left 
				   ? x - distBetweenCenters 
				   : x + distBetweenCenters), y, 
				  evt.getClickCount(), evt.isPopupTrigger());
	}
    }
    
    public void mousePressed(MouseEvent evt) {
	int modifiers = evt.getModifiers();
	//System.out.println ("WWW " + evt.getX() + " " + (evt.getX() - xPanelCenter));
	if (evt.getX() < xPanelCenter) {
	    svpLeft.mousePressed(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		svpRight.mousePressed(makeMouseEvent(evt, false));
	} else {
	    svpRight.mousePressed(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		svpLeft.mousePressed(makeMouseEvent(evt, true));
	}
    } 	
    
    public void mouseDragged(MouseEvent evt) {
	svpLeft.mouseDragged(makeMouseEvent(evt, true));
	svpRight.mouseDragged(makeMouseEvent(evt, false));
    }

    public void mouseReleased(MouseEvent evt) {
	int modifiers = evt.getModifiers();
	
	if (evt.getX() < xPanelCenter) {
	    svpLeft.mouseReleased(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		svpRight.mouseReleased(makeMouseEvent(evt, false));
	} else {
	    svpRight.mouseReleased(evt);
	    if (modifiers != InputEvent.BUTTON3_MASK) //Not right click
		svpLeft.mouseReleased(makeMouseEvent(evt, true));
	}
    }
	
    public void mouseMoved(MouseEvent evt) {}    
}

package visidia.gui.presentation.starRule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.boite.EtatPanel;
import visidia.gui.presentation.boite.VueEtatPanel;
import visidia.gui.donnees.TableCouleurs;
import java.util.*;

/**
 * Visualizes and permits to compose a star as a VueGraph.
 * The parent must add the instance to its MouseListener and MouseMotionListener
 */
public class StarVisuPanel implements MouseListener, MouseMotionListener {

    // Used for drag'n drop a vertex
    SommetDessin drag_n_drop_sommet = null;
    Point ancien_pos = null;
    
    // Used to change vertex label
    JPopupMenu vertexPopup;
    SommetDessin sommetC;
    
    VueGraphe vg;
    boolean isSimpleRule;
    int vertexNumber;
    int ray;
    Point center;
    JPanel parent;
    
    /**
     * The center of the star is located at "center" ; if the star is from a
     * simple rule, just two vertexes can compose the VueGraphe. 
     */
    public StarVisuPanel(VueGraphe vg, int ray, Point center, JPanel parent,
			 boolean isSimpleRule) {
	this.isSimpleRule = isSimpleRule;
	this.vg = vg;
	this.ray = ray;
	this.center = center;
	this.parent = parent;
	sommetC = (SommetDessin) vg.en_dessous(center.x, center.y);
	vertexPopup = new JPopupMenu();
	vertexPopup.setBorder(BorderFactory.createRaisedBevelBorder());
	vertexNumber = vg.nbObjets();
	if (vertexNumber > 1) {
	    // Edges must be deduted
	    vertexNumber = vertexNumber - (vertexNumber / 2);
	}
    }

    // Returns SommetDessin s as s.getEtiquette().equals(id)
    private SommetDessin getVertex(String id) {
	for (Enumeration e = vg.listeAffichage(); e.hasMoreElements(); ) {
	    FormeDessin f = (FormeDessin) e.nextElement();
	    if (f instanceof SommetDessin) {
		if (((SommetDessin) f).getEtiquette().equals(id))
		    return (SommetDessin) f;
	    }
	}
	return null;
    }
    
    /**
     * Places the vertexes (not the center one) in a regular way
     */
    public void reorganizeVertex() {
	if (isSimpleRule) {
	    SommetDessin s = getVertex("1");
	    sommetC.placer(center.x, center.y);
	    if (s != null)
		s.placer(center.x + ray, center.y);
	} else {
	    double alpha = 0;
	    double step = 2 * Math.PI / (vertexNumber - 1);
	    for (int i = 1; i < vertexNumber; i++) {
		SommetDessin s = getVertex("" + i);
		int x = center.x + (int) (Math.sin(alpha) * ray);
		int y = center.y - (int) (Math.cos(alpha) * ray);
		s.placer(x, y);
		alpha += step;
	    }
	}
	parent.repaint();
    }
    
    // Clockwise renumbering
    private void renumeberVertex() {
	double alpha = 0;
	SommetDessin s = null;
	int number = 1;
	sommetC.setEtiquette("0");
	for (alpha = 0.0; alpha < 2 * Math.PI - 0.2; alpha += 0.2) {
	    int x = center.x + (int) (Math.sin(alpha) * ray);
	    int y = center.y - (int) (Math.cos(alpha) * ray);
	    try {
		SommetDessin s2 = (SommetDessin) vg.en_dessous(x, y, s);
		//System.out.println ("Sommet " + s2 
		//   + " trouve a alpha " + alpha + " " + x + " " + y);
		s2.setEtiquette(Integer.toString(number++));
		s = s2;
	    } catch (NoSuchElementException e) {
	    }
	}
    }

    // Returns null if (x, y) is not near the circle or
    // returns the nearest point on the circle
    // If isSimpleRule == true, returns the point of the second vertex or null
    private Point getRoundedPosition(int x, int y) {
	if (isSimpleRule) {
	    if ((x > center.x + ray - 25) && (x < center.x + ray + 25)
		&& (y > center.y - 50) && (y < center.y + 50)) 
		return new Point (center.x + ray, center.y);
	    else
		return null;
	} else {
	    double r2 = Math.sqrt (Math.pow(x - center.x, 2) 
				   + Math.pow (y - center.y, 2));
	    if (Math.abs(r2 - ray) > 10) 
		return null;
	    return new Point ((int) (center.x + (ray * (x - center.x)) 
				     / r2),
			      (int) (center.y + (ray * (y - center.y)) 
				     / r2));
	}
    }
    
    /**
     * Left and center buttons change the structure
     * Right button change the associated data
     */
    public void mousePressed(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY(); 
	int modifiers = evt.getModifiers();
	
	//System.out.println (x + " " + y);
	
	//Left click
	if ((modifiers == InputEvent.BUTTON1_MASK) 
	    && (evt.getClickCount() == 2)) {
	    Point p = getRoundedPosition(x, y);
	    //System.out.println (p);
			
	    if (p != null) {
		try { // Delete the vertex
		    SommetDessin s = (SommetDessin) vg.en_dessous(x, y);
		    AreteDessin a = vg.rechercherArete(s.getEtiquette(),
						       sommetC.getEtiquette());
		    vg.delObject(s);
		    vg.delObject(a);
		    vertexNumber--;
		} catch (NoSuchElementException e) { // Add a new vertex
		    SommetDessin s = vg.creerSommet(p.x, p.y);
		    vg.creerArete((SommetDessin) vg.en_dessous(center.x, center.y), s);
		    vertexNumber++;
		}
		renumeberVertex();
		parent.repaint();
	    }
	} else if ((modifiers == InputEvent.BUTTON2_MASK) ||
		   ((modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK))
		   && ! isSimpleRule)) {
	    //Center click or left + shift
	    try {
		if (getRoundedPosition(x, y) != null)
		drag_n_drop_sommet = (SommetDessin) vg.en_dessous(x, y);
	    } catch (NoSuchElementException e) {
		drag_n_drop_sommet = null;
		ancien_pos = new Point(x, y);
	    }
	} else if (modifiers == InputEvent.BUTTON3_MASK) {
	    // Right click
	    try {
		FormeDessin f = vg.en_dessous(x, y);
		if ((f instanceof AreteDessin) || (f instanceof SommetDessin))
		    maybeShowPopup(evt, f);
	    } catch (NoSuchElementException e) {
		
	    }
	}
    }
    
    public void mouseClicked(MouseEvent evt) {}
    
    public void mouseEntered(MouseEvent evt) {}
    
    public void mouseExited(MouseEvent evt) {}
    
    public void mouseMoved(MouseEvent evt) {}
    
    private void maybeShowPopup(MouseEvent e, final FormeDessin f) {
        if (e.isPopupTrigger()) {
	    final boolean estSommet =  (f instanceof SommetDessin);
	    VueEtatPanel vueEtatPanel = 
		new VueEtatPanel() {
		    public void elementModified(String str) {
			if (estSommet)
			    ((SommetDessin) f).setEtat(str);
			else
			    ((AreteDessin) f).setEtat(str);
			vertexPopup.setVisible(false);
			parent.repaint();
		    }
		};
	    vertexPopup.removeAll();
	    String etat = (estSommet 
			   ? ((SommetDessin) f).getEtat() 
			   : ((AreteDessin) f).getEtatStr());
	    
	    if (! estSommet) {
		final boolean isMarked = ((AreteDessin) f).getEtat();
		JMenuItem edgePopUpItem =
		    new JMenuItem(isMarked ? "Dismark" : "Mark");
		edgePopUpItem.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
			    ((AreteDessin) f).setEtat(! isMarked);
			    vertexPopup.setVisible(false);
			    parent.repaint();
			}
		    });
		vertexPopup.add(edgePopUpItem);
		if (etat != null) {
		    edgePopUpItem = new JMenuItem("No label");
		    edgePopUpItem.addActionListener(new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
				((AreteDessin) f).setEtat(null);
				vertexPopup.setVisible(false);
				parent.repaint();
			    }
			});
		    vertexPopup.add(edgePopUpItem);
		}
		vertexPopup.addSeparator();
	    }
	    if (etat == null) 
		etat = "N";
	    EtatPanel etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(), 
						vueEtatPanel, etat, true);
	    vertexPopup.add(etatPanel);
	    vertexPopup.show(e.getComponent(), e.getX(), e.getY());
	    etatPanel.requestFocus();
	}
    }
    
    /**
     * Left and center buttons just modify the structure
     */
    public void mouseReleased(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	int modifiers = evt.getModifiers();
	
	if (modifiers == InputEvent.BUTTON3_MASK) {
	    try {
		FormeDessin f = vg.en_dessous(x, y);
		maybeShowPopup(evt, f);
	    } catch (NoSuchElementException e) {
	    }
	} else if (drag_n_drop_sommet != null) {
	    //On verifie s'il ne faut pas fusionner deux sommets
	    try {
		SommetDessin s = vg.sommet_en_dessous(x, y, drag_n_drop_sommet);
		AreteDessin a = vg.rechercherArete(s.getEtiquette(),
						   sommetC.getEtiquette());
		vg.delObject(a);
		vg.delObject(s);
		renumeberVertex();
		vertexNumber--;
		parent.repaint();
	    } catch (NoSuchElementException e) {
		drag_n_drop_sommet = null;
	    }
	}
    }
    
    /**
     * Modify the structure
     */
    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	int modifiers = evt.getModifiers();
	
	if ((modifiers == InputEvent.BUTTON2_MASK) ||
	    (modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK))) {
	    if (drag_n_drop_sommet != null) {
		Point p = getRoundedPosition(x, y);
		if (p != null) {
		    try {
			//S'il y a un sommet dessous, il est aspire
			SommetDessin s = vg.sommet_en_dessous(x, y, drag_n_drop_sommet);
			ancien_pos = new Point(s.centreX(), s.centreY());
			drag_n_drop_sommet.placer(ancien_pos.x, ancien_pos.y);
		    } catch (NoSuchElementException e) {
			//Sinon il est simplement deplace
			drag_n_drop_sommet.placer(p.x, p.y);
			ancien_pos = new Point(x, y);
		    }
		    renumeberVertex();
		    parent.repaint();
		}
	    }
	}
    }
}

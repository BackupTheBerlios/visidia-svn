package visidia.gui.presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

import visidia.gui.presentation.userInterfaceEdition.undo.DeselectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.SelectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;


public class SelectionUnit extends MouseAdapter implements MouseMotionListener {

    private final static Color RECT_SELECTION_COLOR = Color.gray;
    
    /* Used to update data */
    protected SelectionGetData selectionGetData;
    /* You must call the updateDate method before unsing those 3 fields */
    protected SelectionDessin selection;
    protected UndoInfo undoInfo;
    protected RecoverableObject recoverableObject;
    
    protected JPanel parentPanel;

    private boolean carre_selection;
    private int selection_x, selection_x1, selection_x2;
    private int selection_y, selection_y1, selection_y2;

    public SelectionUnit (SelectionGetData selectionGetData, JPanel parentPanel) {
	this.selectionGetData = selectionGetData;
	this.parentPanel = parentPanel;
    }
    
    public void mousePressed(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY(); 
	
	switch(evt.getModifiers()) {
	    // Bouton droit
	case InputEvent.BUTTON3_MASK:
	    this.appuiBoutonDroit(x, y);
	    break;
	    // shift + bouton droit
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    this.appuiShiftBoutonDroit(x, y);
	    break;
	}
    }
    
    public void mouseReleased(MouseEvent evt) {
	//int x = evt.getX();
	//int y = evt.getY();
	//boolean changed = false;
     
	if (this.carre_selection) {
	    this.updateData();
	    
	    /* Selection des objets dans la zone rectangulaire : 
	     *  c'est un nouveau undoGroup. */
	    Enumeration e = 
		this.recoverableObject.objetsDansRegion(this.selection_x1, this.selection_y1, 
						   this.selection_x2, this.selection_y2);
	
	    if (e.hasMoreElements()) {
		if (this.undoInfo != null)
		    this.undoInfo.newGroup("Unselect elements in rectangular area", 
				      "Select elements in rectangular area");
		
		while (e.hasMoreElements()) {
		    FormeDessin formeDessin = (FormeDessin) e.nextElement();
		    this.selection.insererElement(formeDessin);
		    if (this.undoInfo != null)
			this.undoInfo.addInfo(new SelectFormeDessin(this.selection, 
							       formeDessin));
		}
	    }
	    this.carre_selection = false;
	    this.parentPanel.repaint();
	}
    }

    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();

	switch(evt.getModifiers()) {
	    // Bouton droit ou (shift + bouton droit)
	case InputEvent.BUTTON3_MASK:
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    this.glisseBoutonDroit(x, y);
	    break;
	}
    }

    public void mouseMoved (MouseEvent evt) { }

    public Rectangle selectionRectangle () {
	return null;
    }

    /**
     * L'appui du Bouton droit de la souris permet de selectionner un objet
     * ou tous les objets d'une zone rectangulaire.
     **/
    private void appuiBoutonDroit (int x, int y) {
	// Remise a zero de la selection
	FormeDessin objet_sous_souris;

	try {
	    
	    this.updateData();
	    objet_sous_souris = this.recoverableObject.en_dessous(x, y);
	    
	    /* On vide la selection : c'est un nouvel undoGroup. 
	     * Il est cree dans la commande de vidage de la selection. */
	    if (! this.selection.estVide()) {
		this.deSelect();
		this.parentPanel.repaint ();
	    }
	    
	    /*...et on selectionne l'objet : c'est un nouvel undoGroup. */
	    if (this.undoInfo != null) {
		this.undoInfo.newGroup("Unselect object", "Select object");
		this.undoInfo.addInfo(new SelectFormeDessin(this.selection, objet_sous_souris));
	    }
	    this.selection.insererElement(objet_sous_souris);
	    this.parentPanel.repaint();
	    
	} catch(NoSuchElementException e) {
	    /* On vide la selection, si elle ne l'est pas deja : nouvel undoGroup.
	     * Il est cree dans la commande de vidage de la selection.  */
	    if (! this.selection.estVide()) {
		this.deSelect();
		this.parentPanel.repaint(0);
	    }
	    
	    // Carre de selection
	    this.carre_selection = true;
	    this.selection_x = this.selection_x1 = this.selection_x2 = x;
	    this.selection_y = this.selection_y1 = this.selection_y2 = y;
	}
    }


    /* L'appui de Shift + bouton droit permet la selection additive.  */
    private void appuiShiftBoutonDroit(int x, int y) {
	try {
	    
	    this.updateData();
	    FormeDessin objet_sous_souris = this.recoverableObject.en_dessous(x, y);
	    
	    if (this.selection.contient(objet_sous_souris)) {
		//Suppression d'un element de la selection : nouvel undoGroup.
		if (this.undoInfo != null) {
		    this.undoInfo.newGroup("Select all", "Deselect object");      
		    this.undoInfo.addInfo(new DeselectFormeDessin(this.selection, 
							     objet_sous_souris));
		}
		this.selection.supprimerElement(objet_sous_souris);

	    } else {
		//Ajout d'un element a la selection : nouvel undoGroup.
		if (this.undoInfo != null) {
		    this.undoInfo.newGroup("Remove object from selection", 
				      "Add object to selection");
		    this.undoInfo.addInfo(new SelectFormeDessin(this.selection, 
							   objet_sous_souris));
		}
		this.selection.insererElement(objet_sous_souris);
		
	    }
	    this.parentPanel.repaint();

	} catch (NoSuchElementException e) {
	    // Carre de selection
	    this.carre_selection = true;
	    this.selection_x = this.selection_x1 = this.selection_x2 = x;
	    this.selection_y = this.selection_y1 = this.selection_y2 = y;
	}
    }

    /* Permet de creer un rectangle de selection au cas ou on maintient
     * le bouton droit de la souris appuye. */
    private void glisseBoutonDroit(int x, int y) {
	if (this.carre_selection) {
	    if (x > this.selection_x) {
		this.selection_x1 = this.selection_x;
		this.selection_x2 = x;
	    } else {
		this.selection_x1 = x;
		this.selection_x2 = this.selection_x;
	    }
	    if (y > this.selection_y) {
		this.selection_y1 = this.selection_y;
		this.selection_y2 = y;
	    } else {
		this.selection_y1 = y;
		this.selection_y2 = this.selection_y;
	    }
	    Dimension size = this.parentPanel.getPreferredSize ();
	    if ((x > 0) && (y > 0) && (x < size.width) && (y < size.height)) {
		this.parentPanel.setAutoscrolls (false);
		System.out.println (x + " " + y + " -- " + 
				    size.width + " " + size.height);
		this.parentPanel.scrollRectToVisible (new Rectangle(x, y, 1, 1));
	    }
	    this.parentPanel.repaint();
	}
    }

    
    private void deSelect () {
	this.updateData();
	
	if (this.undoInfo != null) {
	    Enumeration e_undo = this.selection.elements();
	    this.undoInfo.newGroup("Reselect Objects", "Deselect Objects");
	    FormeDessin elt;
	    while (e_undo.hasMoreElements()) {
		elt = (FormeDessin)e_undo.nextElement();
		this.undoInfo.addInfo(new DeselectFormeDessin(this.selection, elt));
	    }
	}
	this.selection.deSelect();
    }
    
    public void drawSelection (Graphics g) {
	if (this.carre_selection) {
	    g.setColor(RECT_SELECTION_COLOR);
	    g.drawRect(this.selection_x1, this.selection_y1, 
		       this.selection_x2 - this.selection_x1, this.selection_y2 - this.selection_y1);
	}
    }

    /**
     *  Must be called before using undoInfo, selectionDessin or recoverableObject
     */
    protected void updateData () {
	try {
	    this.undoInfo = this.selectionGetData.getUndoInfo ();
	} catch (NoSuchMethodException e) {
	    this.undoInfo = null;
	}
	this.selection = this.selectionGetData.getSelectionDessin ();
	this.recoverableObject = this.selectionGetData.getRecoverableObject ();
    }
}

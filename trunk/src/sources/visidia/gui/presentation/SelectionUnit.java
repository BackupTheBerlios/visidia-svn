package visidia.gui.presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.util.*;
//import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.undo.*;
//import java.io.*;


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
	    appuiBoutonDroit(x, y);
	    break;
	    // shift + bouton droit
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    appuiShiftBoutonDroit(x, y);
	    break;
	}
    }
    
    public void mouseReleased(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	boolean changed = false;
     
	if (carre_selection) {
	    updateData();
	    
	    /* Selection des objets dans la zone rectangulaire : 
	     *  c'est un nouveau undoGroup. */
	    Enumeration e = 
		recoverableObject.objetsDansRegion(selection_x1, selection_y1, 
						   selection_x2, selection_y2);
	
	    if (e.hasMoreElements()) {
		if (undoInfo != null)
		    undoInfo.newGroup("Unselect elements in rectangular area", 
				      "Select elements in rectangular area");
		
		while (e.hasMoreElements()) {
		    FormeDessin formeDessin = (FormeDessin) e.nextElement();
		    selection.insererElement(formeDessin);
		    if (undoInfo != null)
			undoInfo.addInfo(new SelectFormeDessin(selection, 
							       formeDessin));
		}
	    }
	    carre_selection = false;
	    parentPanel.repaint();
	}
    }

    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();

	switch(evt.getModifiers()) {
	    // Bouton droit ou (shift + bouton droit)
	case InputEvent.BUTTON3_MASK:
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    glisseBoutonDroit(x, y);
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
	    
	    updateData();
	    objet_sous_souris = recoverableObject.en_dessous(x, y);
	    
	    /* On vide la selection : c'est un nouvel undoGroup. 
	     * Il est cree dans la commande de vidage de la selection. */
	    if (! selection.estVide()) {
		deSelect();
		parentPanel.repaint ();
	    }
	    
	    /*...et on selectionne l'objet : c'est un nouvel undoGroup. */
	    if (undoInfo != null) {
		undoInfo.newGroup("Unselect object", "Select object");
		undoInfo.addInfo(new SelectFormeDessin(selection, objet_sous_souris));
	    }
	    selection.insererElement(objet_sous_souris);
	    parentPanel.repaint();
	    
	} catch(NoSuchElementException e) {
	    /* On vide la selection, si elle ne l'est pas deja : nouvel undoGroup.
	     * Il est cree dans la commande de vidage de la selection.  */
	    if (! selection.estVide()) {
		deSelect();
		parentPanel.repaint(0);
	    }
	    
	    // Carre de selection
	    carre_selection = true;
	    selection_x = selection_x1 = selection_x2 = x;
	    selection_y = selection_y1 = selection_y2 = y;
	}
    }


    /* L'appui de Shift + bouton droit permet la selection additive.  */
    private void appuiShiftBoutonDroit(int x, int y) {
	try {
	    
	    updateData();
	    FormeDessin objet_sous_souris = recoverableObject.en_dessous(x, y);
	    
	    if (selection.contient(objet_sous_souris)) {
		//Suppression d'un element de la selection : nouvel undoGroup.
		if (undoInfo != null) {
		    undoInfo.newGroup("Select all", "Deselect object");      
		    undoInfo.addInfo(new DeselectFormeDessin(selection, 
							     objet_sous_souris));
		}
		selection.supprimerElement(objet_sous_souris);

	    } else {
		//Ajout d'un element a la selection : nouvel undoGroup.
		if (undoInfo != null) {
		    undoInfo.newGroup("Remove object from selection", 
				      "Add object to selection");
		    undoInfo.addInfo(new SelectFormeDessin(selection, 
							   objet_sous_souris));
		}
		selection.insererElement(objet_sous_souris);
		
	    }
	    parentPanel.repaint();

	} catch (NoSuchElementException e) {
	    // Carre de selection
	    carre_selection = true;
	    selection_x = selection_x1 = selection_x2 = x;
	    selection_y = selection_y1 = selection_y2 = y;
	}
    }

    /* Permet de creer un rectangle de selection au cas ou on maintient
     * le bouton droit de la souris appuye. */
    private void glisseBoutonDroit(int x, int y) {
	if (carre_selection) {
	    if (x > selection_x) {
		selection_x1 = selection_x;
		selection_x2 = x;
	    } else {
		selection_x1 = x;
		selection_x2 = selection_x;
	    }
	    if (y > selection_y) {
		selection_y1 = selection_y;
		selection_y2 = y;
	    } else {
		selection_y1 = y;
		selection_y2 = selection_y;
	    }
	    Dimension size = parentPanel.getPreferredSize ();
	    if (x > 0 && y > 0 && x < size.width && y < size.height) {
		parentPanel.setAutoscrolls (false);
		System.out.println (x + " " + y + " -- " + 
				    size.width + " " + size.height);
		parentPanel.scrollRectToVisible (new Rectangle(x, y, 1, 1));
	    }
	    parentPanel.repaint();
	}
    }

    
    private void deSelect () {
	updateData();
	
	if (undoInfo != null) {
	    Enumeration e_undo = selection.elements();
	    undoInfo.newGroup("Reselect Objects", "Deselect Objects");
	    FormeDessin elt;
	    while (e_undo.hasMoreElements()) {
		elt = (FormeDessin)e_undo.nextElement();
		undoInfo.addInfo(new DeselectFormeDessin(selection, elt));
	    }
	}
	selection.deSelect();
    }
    
    public void drawSelection (Graphics g) {
	if (carre_selection) {
	    g.setColor(RECT_SELECTION_COLOR);
	    g.drawRect(selection_x1, selection_y1, 
		       selection_x2 - selection_x1, selection_y2 - selection_y1);
	}
    }

    /**
     *  Must be called before using undoInfo, selectionDessin or recoverableObject
     */
    protected void updateData () {
	try {
	    undoInfo = selectionGetData.getUndoInfo ();
	} catch (NoSuchMethodException e) {
	    undoInfo = null;
	}
	selection = selectionGetData.getSelectionDessin ();
	recoverableObject = selectionGetData.getRecoverableObject ();
    }
}

package visidia.gui.presentation.userInterfaceEdition;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.donnees.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.undo.*;

/**
 * Un GrapheVisuPanel est un JPanel sur lequel le graphe est affiché
 * et qui permet d'interagir avec le graphe.  <BR> Pour bien
 * comprendre la séparation des taches entre les classes Editeur et
 * GrapheVisuPanel, on peut imaginer plusieurs objets GrapheVisuPanel
 * associés a une seule instance de Editeur pour éditer plusieurs
 * parties d'un même graphe en même temps.
 **/
public class GrapheVisuPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    
    /** Couleur de fond par défaut du grapheVisuPanel **/
    private static final Color RECT_SELECTION_COULEUR = Color.gray;
    
    /** L'éditeur auquel est associé le GrapheVisuPanel */
    protected Editeur editeur;
    
    protected MediaTracker tracker;
    
    /** Dimension du graphique */
    protected Dimension size=new Dimension(0,0);
    
    /** Abscisse de l'ancienne position de la souris au debut d'un drag-and-drop **/
    protected int x_ancien;
    /** Ordonnée de l'ancienne position de la souris au debut d'un
     * drag-and-drop **/
    protected int y_ancien;
    /** Objet situé à la position à laquelle est la souris **/
    private FormeDessin objet_sous_souris;
    /** Signifie si un drag-and-drop d'un sommet est en court **/
    private boolean drag_n_drop_sommet;
    /** Signifie si un drag-and-drop d'un sommet est en court, mais
     * avec déplacement d'un objet déàja existant **/
    private boolean drag_n_drop_sommet_existant;
    private int dx;
    private int dy;
    /** Signifie si un drag-and-drop d'une sélection est en court **/
    private boolean drag_n_drop_selection;
    /** Signifie si un drag-and-drop du graphe entier est en court **/
    private boolean drag_n_drop_graph;
    /** Abscisse de la position initiale de la souris lors d'une sélection **/
    private int x_origine;
    /** Ordonnee de la position initiale de la souris lors d'une sélection **/
    private int y_origine;
    /** Signifie si une sélection par un rectangle est en court **/
    private boolean carre_selection;
    private int selection_x, selection_x1, selection_x2;
    private int selection_y, selection_y1, selection_y2;
    private Ensemble formeDessin_a_deplacer;
    private FormeDessin drag_n_dropVertex;
    private int drag_n_dropVertex_X;
    private int drag_n_dropVertex_Y;
    protected Vector vecteurImages;
    private SommetDessin ancien_sommet_sous_souris; //ww
    
    /**
     * Instancie un GrapheVisuPanel associé à l'editeur passé en argument.
     **/
    public GrapheVisuPanel(Editeur un_editeur) {
	tracker = new MediaTracker(this);
	vecteurImages = new Vector();
	vecteurImages.add(TableImages.getImage("image1"));
	vecteurImages.add(TableImages.getImage("image2"));
	vecteurImages.add(TableImages.getImage("image3"));
	vecteurImages.add(TableImages.getImage("image4"));
	vecteurImages.add(TableImages.getImage("image5"));
	vecteurImages.add(TableImages.getImage("image6"));		    
	
	
	for(int i=0;i<6;i++)
	    tracker.addImage((Image)vecteurImages.elementAt(i),0);
	try
	    {
		tracker.waitForID(0);    
	    } 
	catch (InterruptedException e) {System.out.println("probleme");}
	editeur = un_editeur;
	if(editeur.graph().ordre()!= 0)
	    {
		
		size = editeur.getVueGraphe().donnerDimension();
	    }
	else size =new Dimension(0,0);
	objet_sous_souris = null;
	drag_n_drop_sommet = false;
	drag_n_drop_sommet_existant = false;
	drag_n_drop_selection = false;
	drag_n_drop_graph = false;
	carre_selection = false;
	formeDessin_a_deplacer = null;
	this.addMouseListener(this);
	this.addMouseMotionListener(this);
	this.addKeyListener(this);
	
	setBackground(new Color(0xe6e6fa));
    }
    
    
    /**
     * Redessine les elements du graphique.
     **/
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	
	editeur.getVueGraphe().dessiner(this,g);
    
	if (tracker.statusAll(false) != MediaTracker.COMPLETE)
	    {
		g.drawString("probleme de chargement d'image", 50, 100);
		return;
	    }
	
	
	if(carre_selection)
	    dessinerCarre(g, selection_x1, selection_y1, selection_x2, selection_y2);
    }
    
    /**
     * Methode appellée quand on appuie sur un bouton de la
     * souris. Suivant ce que l'on trouve sous la souris, on appelle
     * les autres méthodes de la classe.
     **/
    public void mousePressed(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY(); 
	
	size = this.getPreferredSize();
	
	switch(evt.getModifiers()) {
	    // Bouton du milieu
	case InputEvent.BUTTON2_MASK:
	case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK):  // Pour les souris sans boutton du milieu
	    appuiBoutonMilieu(x, y);
	    break;
	    
	    // Bouton droit
	case InputEvent.BUTTON3_MASK:
	    appuiBoutonDroit(x, y);
	    break;
	    
	    // shift + bouton droit
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    appuiShiftBoutonDroit(x, y);
	    break;
	    
	    // Bouton gauche
	case InputEvent.BUTTON1_MASK:
	    appuiBoutonGauche(x, y);
	    break;
	    
	default:
	    
	}
    }
    
    /**
     * Methode appellee quand on relache un bouton de la souris.
     **/
    public void mouseReleased(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	boolean changed = false;
	
	if(drag_n_drop_sommet) {
	    try {
		/* Si on fusionne deux sommets, il faut retirer le
		 * sommet cree de la liste */ 
		/* des informations de undo, sinon, il sera recree
		 * lors d'un redo eventuel!*/
		SommetDessin sommet_en_dessous =
		    editeur.getVueGraphe().sommet_en_dessous(x, y,objet_sous_souris);
		
		//ww : On efface l'ancienne arete si OUI, puisque une
		//autre arete a ete cree, mais si l'utilisateur boucle
		//sur le meme sommet, ceci n'efface pas la
		//pseudo-arete.
		if (editeur.getVueGraphe().rechercherArete(((SommetDessin)ancien_sommet_sous_souris).getEtiquette(), ((SommetDessin)sommet_en_dessous).getEtiquette()) != null)
		    {
			editeur.getVueGraphe().delObject((AreteDessin)((editeur.getVueGraphe()).rechercherArete(((SommetDessin)ancien_sommet_sous_souris).getEtiquette(),((SommetDessin)sommet_en_dessous).getEtiquette())));
		    }
		
		//ww: ceci efface l'arete en cas de bouclage sur un meme sommet
		if ((editeur.getVueGraphe().rechercherArete(((SommetDessin)ancien_sommet_sous_souris).getEtiquette(), (((SommetDessin)objet_sous_souris).getEtiquette())) != null)
		    &&( ((SommetDessin)ancien_sommet_sous_souris).getEtiquette() == (((SommetDessin)sommet_en_dessous).getEtiquette())))
		    {
			editeur.getVueGraphe().delObject((AreteDessin)((editeur.getVueGraphe()).rechercherArete(((SommetDessin)ancien_sommet_sous_souris).getEtiquette(),
														((SommetDessin)objet_sous_souris).getEtiquette())));
		    }
		//ww: end
		
		
		sommet_en_dessous.fusionner((SommetDessin)objet_sous_souris);
		editeur.undoInfo.removeObject((SommetDessin)drag_n_dropVertex);
		
		
		
	    }
	    catch(NoSuchElementException e) {
	    }
	    drag_n_drop_sommet = false;
	} else if (drag_n_drop_sommet_existant) {
	    try {
		/* On fusionne deux sommets existants par deplacement
		 * de l'un d'eux. */
		SommetDessin sommet_en_dessous =
		    editeur.getVueGraphe().sommet_en_dessous(x, y,objet_sous_souris);
		editeur.undoInfo.newGroup("Separate merged vertices", "Merge vertices");
		
		editeur.undoInfo.addInfo(new FusionneSommet((SommetDessin)objet_sous_souris,sommet_en_dessous,drag_n_dropVertex_X,
							    drag_n_dropVertex_Y));								
		
		sommet_en_dessous.fusionner((SommetDessin)objet_sous_souris);
	    }
	    catch(NoSuchElementException e) {
		editeur.undoInfo.newGroup("Move vertex to old position", "Move vertex to new position");
		editeur.undoInfo.addInfo(new DeplaceObjets((SommetDessin)objet_sous_souris,
							   x - drag_n_dropVertex_X,
							   y - drag_n_dropVertex_Y));
	    }
	    drag_n_drop_sommet_existant = false;
	} else if (drag_n_drop_selection) { 
	    editeur.undoInfo.newGroup("Move selection to old position", "Move selection to new position");
	    editeur.undoInfo.addInfo(new DeplaceObjets(formeDessin_a_deplacer,
						       x - x_origine,
						       y - y_origine));
	    drag_n_drop_selection = false;
	} else if (drag_n_drop_graph) {
	    editeur.undoInfo.newGroup("Move graph to old position", "Move graph to new position");
	    editeur.undoInfo.addInfo(new DeplaceObjets(formeDessin_a_deplacer,
						       x - x_origine,
						       y - y_origine));
	    drag_n_drop_graph = false;
	} else if (carre_selection) {
	    //selection des objets dans la zone rectangulaire : c'est
	    //un nouveau undoGroup.
	    Enumeration e = editeur.getVueGraphe().objetsDansRegion(selection_x1,
								    selection_y1, selection_x2, selection_y2);
	    
	    if (e.hasMoreElements()) {
		editeur.undoInfo.newGroup("Unselect elements in rectangular area", "Select elements in rectangular area");
		
		while(e.hasMoreElements()) {
		    FormeDessin formeDessin = (FormeDessin)e.nextElement();
		    editeur.selection.insererElement(formeDessin);
		    editeur.undoInfo.addInfo(new SelectFormeDessin(editeur.selection, formeDessin));
		    //formeDessin.enluminer(true);
		}
	    }
	    carre_selection = false;
	}
	
	// On reinitialise les variables utilisees
	formeDessin_a_deplacer = null;
	objet_sous_souris = null;
	
	if(x+20 > size.width)
	    {
		size.width = x+20; changed=true;
		
	    }
	if(y+20 > size.height)
	    {
		size.height = y+20; changed=true;      	
	    }
	if(changed) {
	    editeur.changerDimensionGraphe(size);
	    setPreferredSize(size);
	    revalidate();
	}
	this.scrollRectToVisible(new Rectangle(x-10,y-10,30,30));
	editeur.setUndo();    
	repaint();
    }
    
    public void mouseClicked(MouseEvent evt) {}
    
    public void mouseEntered(MouseEvent evt) {}
    
    public void mouseExited(MouseEvent evt) {}
    
    /**
     * L'appui du bouton du milieu de la souris permet de déplacer un objet,
     * la sélection ou la zone visible du plan de travail.
     **/
    public void appuiBoutonMilieu(int x, int y) {
	x_ancien = x;
	y_ancien = y;
	
	try {
	    objet_sous_souris = editeur.getVueGraphe().en_dessous(x, y);
      
	    if (editeur.selection.contient(objet_sous_souris)) {
		// Deplacement de la selection

		// on ne deplace pas les aretes, mais leurs sommets incidents.
		formeDessin_a_deplacer =
		    Traitements.sommetsTotaux(editeur.selection.elements());
	
		//formeDessin_a_deplacer.inserer(editeur.selection.autresObjetsVisu());
		drag_n_drop_selection = true;
	  
		x_origine = x;
		y_origine = y;
	    } else if (objet_sous_souris.type().equals("vertex")) {
		  
		drag_n_drop_sommet_existant = true;
		// Quand on drag un sommet, on veut un positionnement precis par
		// rapport au curseur, on sauvegarde donc la position relative
		// du sommet par rapport au curseur.
		drag_n_dropVertex_X = ((SommetDessin)objet_sous_souris).centreX();
		drag_n_dropVertex_Y = ((SommetDessin)objet_sous_souris).centreY();
	  
	  
		dx = ((SommetDessin)objet_sous_souris).centreX() - x;
		dy = ((SommetDessin)objet_sous_souris).centreY() - y;
	    }
	} catch(NoSuchElementException e) {
	    drag_n_drop_graph = true;
	    x_origine = x;
	    y_origine = y;
	}
    }
    
    /**
     * L'appui du Bouton droit de la souris permet de selectionner un objet
     * ou tous les objets d'une zone rectangulaire.
     **/
    public void appuiBoutonDroit(int x, int y) {
	// Remise a zero de la selection
	x_ancien = x;
	y_ancien = y;
	try {
	    objet_sous_souris = editeur.getVueGraphe().en_dessous(x, y);
	    
	    //On vide la selection : c'est un nouvel undoGroup. Il est cree dans la commande de vidage de la selection.
	    if (!editeur.selection.estVide()) {
		editeur.setDescription("Reselect object(s)", "Deselect object(s)");
		editeur.commandeViderSelection(true);
	    }
	    
	    //objet_sous_souris.enluminer(true);
	    //...et on selectionne l'objet : c'est un nouvel undoGroup.
	    editeur.undoInfo.newGroup("Unselect object", "Select object");
	    editeur.selection.insererElement(objet_sous_souris);
	    editeur.undoInfo.addInfo(new SelectFormeDessin(editeur.selection, objet_sous_souris));
	    repaint();
	} catch(NoSuchElementException e) {
	    // On vide la selection, si elle ne l'est pas deja : nouvel undoGroup.  Il est cree dans la commande de vidage de la selection.
	    if(!editeur.selection.estVide()) {
		editeur.setDescription("Reselect object(s)", "Deselect object(s)");
		editeur.commandeViderSelection(true);
		repaint(0);
	    }
	    // Carre de selection
	    carre_selection = true;
	    selection_x = selection_x1 = selection_x2 = x;
	    selection_y = selection_y1 = selection_y2 = y;
	}
	editeur.setUndo();
    }
    
    
    /**
     * L'appui de Shift + bouton droit permet la sélection additive.
     **/
    public void appuiShiftBoutonDroit(int x, int y) {
	x_ancien = x;
	y_ancien = y;
	try {
	    objet_sous_souris = editeur.getVueGraphe().en_dessous(x, y);
	    if (editeur.selection.contient(objet_sous_souris)) {
		//Suppression d'un element de la selection : nouvel undoGroup.
		editeur.undoInfo.newGroup("Select all", "Deselect object");      
		editeur.selection.supprimerElement(objet_sous_souris);
		editeur.undoInfo.addInfo(new DeselectFormeDessin(editeur.selection, objet_sous_souris));
		//objet_sous_souris.enluminer(false);
	    } else {
		//Ajout d'un element a la selection : nouvel undoGroup.
		editeur.undoInfo.newGroup("Remove object from selection", "Add object to selection");      
		editeur.selection.insererElement(objet_sous_souris);
		editeur.undoInfo.addInfo(new SelectFormeDessin(editeur.selection, objet_sous_souris));
		//objet_sous_souris.enluminer(true);
	    }
	    repaint();
	} catch (NoSuchElementException e) {
	    // Carre de selection
	    carre_selection = true;
	    selection_x = selection_x1 = selection_x2 = x;
	    selection_y = selection_y1 = selection_y2 = y;
	}
	editeur.setUndo();
    }

    /**
     * L'appui du bouton gauche de la souris permet de créer un nouveau
     * sommet (avec arete si on clique sur un sommet déjà existant).
     **/
    public void appuiBoutonGauche(int x, int y) {
	x_ancien = x;
	y_ancien = y;
 
	try {
	    objet_sous_souris = editeur.getVueGraphe().sommet_en_dessous(x, y);
	} catch(NoSuchElementException e) {
	    //Creation d'un sommet : nouvel undoGroup.
	    editeur.undoInfo.newGroup("Delete newly created vertex", "Create new vertex");
	    objet_sous_souris = editeur.getVueGraphe().creerSommet(x, y);
	    editeur.undoInfo.addInfo(new AjouteObjet(objet_sous_souris));
	    //((SommetDessin)objet_sous_souris).changerImage(new ImageIcon((Image)vecteurImages.firstElement()));
	    repaint();
	}
	editeur.setUndo();
    }

    /**
     * Implémentation de MouseMotionListener.
     **/
    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();

	switch(evt.getModifiers()) {
	    // Bouton du milieu
	case InputEvent.BUTTON2_MASK:
	case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK):  // Pour les souris sans boutton du milieu
	    glisseBoutonMilieu(x, y);
	    break;
	
	    // Bouton droit ou (shift + bouton droit)
	case InputEvent.BUTTON3_MASK:
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    glisseBoutonDroit(x, y);
	    break;

	    // Bouton gauche
	case InputEvent.BUTTON1_MASK:
	case 0:
	    glisseBoutonGauche(x, y);
	    break;

	default:
	}
    }

    public void mouseMoved(MouseEvent evt) {}

    /* Fin d'implementation de MouseMotionListener */

    /** 
     * Permet de déplacer l'objet sous le curseur en même temps que la
     * souris au cas ou on maintient le bouton du milieu appuyé.
     **/
    public void glisseBoutonMilieu(int x, int y) {
	// deplacer objet sous la souris
	if (objet_sous_souris != null) {
	    if (formeDessin_a_deplacer != null) {
		// on deplace la selection

		VueGraphe.deplacerFormeDessin(formeDessin_a_deplacer.elements(),
					      x - x_ancien,
					      y - y_ancien);
		x_ancien = x;
		y_ancien = y;
	    } else if (drag_n_drop_sommet || drag_n_drop_sommet_existant) {
		// on deplace un sommet
		glisseSommet(x, y);
	    } else {
		// on deplace un formeDessin quelconque
		objet_sous_souris.deplacer(x - x_ancien, y - y_ancien);
		x_ancien = x;
		y_ancien = y;
	    }
	} else {
	    boolean changed = false;

	    // Rien sous la souris
	    // On va donc translater tout le graphe

	    formeDessin_a_deplacer =
		Traitements.sommetsTotaux(editeur.getVueGraphe().listeAffichage());
	    if(formeDessin_a_deplacer != null) {
		VueGraphe.deplacerFormeDessin(
					      formeDessin_a_deplacer.elements(),
					      x - x_ancien,
					      y - y_ancien);
		x_ancien = x;
		y_ancien = y;
	    }
	    Enumeration objets_deplaces = formeDessin_a_deplacer.elements();
	    while (objets_deplaces.hasMoreElements()) {
		SommetDessin objet = (SommetDessin)objets_deplaces.nextElement();
		if (objet.centreX() + 20 > size.width) {
		    size.width = objet.centreX()+20; 
		    changed=true;
		}
		if (objet.centreY() + 20 > size.height) {
		    size.height = objet.centreY()+20; 
		    changed=true;
		}
	    }
	    if(changed) {
		editeur.changerDimensionGraphe(size);
		setPreferredSize(size);
		revalidate();
	    }
	    this.scrollRectToVisible(new Rectangle(Math.min(x,size.width-20)-10,
						   Math.min(y,size.height-20)-10,
						   30,30));
	}
	repaint();
    }

    /**
     * Permet de creer un rectangle de selection au cas ou on maintient
     * le bouton droit de la souris appuye.
     **/
    public void glisseBoutonDroit(int x, int y) {
	if(carre_selection) {
	    if(x > selection_x) {
		selection_x1 = selection_x;
		selection_x2 = x;
	    } else {
		selection_x1 = x;
		selection_x2 = selection_x;
	    }
	    if(y > selection_y) {
		selection_y1 = selection_y;
		selection_y2 = y;
	    } else {
		selection_y1 = y;
		selection_y2 = selection_y;
	    }
	    this.scrollRectToVisible(new Rectangle(Math.min(x,size.width-20)-10,
						   Math.min(y,size.height-20)-10,
						   30,30));
	    this.repaint();
	}
    }

    /**
     * Permet de créer un sommet et une arête quand on maintient le
     * bouton gauche de la souris appuyé. Si on relache la souris au
     * dessus d'un sommet existant, la méthode <b>mouseRelease</b>
     * fusionne ce sommet avec le nouveau sommet cree.
     **/
    public void glisseBoutonGauche(int x, int y) {
	boolean changed = false;
	//AreteDessin areteCree = null;

	if(objet_sous_souris != null) {
	    if(drag_n_drop_sommet)
		glisseSommet(x, y);
	    else {
		if(objet_sous_souris.appartient(x, y))
		    return;
		else {
		    // creer un nouveau sommet et l'arete qui va avec : c'est un nouvel undoGroup.
		    this.ancien_sommet_sous_souris =
			(SommetDessin)objet_sous_souris;
		    editeur.undoInfo.newGroup("Delete newly created vertex and edge", "Create new vertex and edge");
	  
		    objet_sous_souris = editeur.getVueGraphe().creerSommet(x, y);
		    editeur.undoInfo.addInfo(new AjouteObjet(objet_sous_souris));
	
		    // On sauvegarde l'objet UndoObject contenant ce
		    // sommet : on peut en effet avoir a le supprimer
		    // en cas de fusion avec un sommet existant.
		    drag_n_dropVertex = objet_sous_souris;

		    AreteDessin a = editeur.getVueGraphe().creerArete(ancien_sommet_sous_souris,
								      (SommetDessin)objet_sous_souris);
		    editeur.undoInfo.addInfo(new AjouteObjet(a));

		    drag_n_drop_sommet = true;
		    // Quand on drag un sommet, on veut un
		    // positionnement precis par rapport au curseur,
		    // on sauvegarde donc la position relative du
		    // sommet par rapport au curseur, ici elle est
		    // nulle.
		    dx = dy = 0;
		}
	    }
	    this.scrollRectToVisible(new Rectangle(x-10,y-10,30,30));
	    if(x+20 > size.width)
		{size.width = x+20; changed=true;}
	    if(y+20 > size.height)
		{size.height = y+20; changed=true;}
	    if(changed) {
		editeur.changerDimensionGraphe(size);
		setPreferredSize(size);
		revalidate();
	    }
	    repaint();
	}
	editeur.setUndo();
    }

    /**
     * Implementation de KeyListener.
     **/
    public void keyPressed(KeyEvent evt) {
	switch(evt.getKeyCode()) {
	    // Delete
	case KeyEvent.VK_DELETE:
	case KeyEvent.VK_BACK_SPACE:
	    editeur.commandeSupprimer();
	    repaint();
	    break;

	default:
	}
    }

    public void keyReleased(KeyEvent evt) {}

    public void keyTyped(KeyEvent evt) {}

    /* Fin d'implementation de KeyListener */

    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    // Methodes privees

    /**
     * Méthode privée qui gère le déplacement d'un sommet avec
     * effet d'aspiration par un autre sommet.
     **/
    private void glisseSommet(int x, int y) {
	// On regarde si notre sommet en recouvre un autre
	// le try leve une exception si il n'y a pas de recouverture
	try {
 
	    SommetDessin sommet_en_dessous =
		(SommetDessin)editeur.getVueGraphe().sommet_en_dessous(x, y,
								       objet_sous_souris);
    

	    // Oui: le sommet deplace est aspire
	    x_ancien = sommet_en_dessous.centreX();
	    y_ancien = sommet_en_dessous.centreY();
	    ((SommetDessin)objet_sous_souris).placer(x_ancien, y_ancien);
    

	} catch(NoSuchElementException e) {
	    // Non: on se contente de deplacer le sommet
	    ((SommetDessin)objet_sous_souris).placer(x + dx, y + dy);
     
	    x_ancien = x;
	    y_ancien = y;
	}

	// remarque: dans le cas d'un drag-n-drop, on n'utilise pas
	// x_ancien et y_ancien, mais dx et dy pour assurer un
	// positionnement plus précis (éviter les derivés entre la
	// position du curseur et celle de l'objet déplacé), on
	// actualise cependant les variables x_ancien et y_ancien pour
	// homogeneiser avec le cas général.
    }

    /**
     * Méthode de dessin du carré de sélection.
     **/
    private void dessinerCarre(Graphics g, int x1, int y1, int x2, int y2) {
	g.setColor(RECT_SELECTION_COULEUR);
	g.drawRect(x1, y1, x2 - x1, y2 - y1);
    }
}

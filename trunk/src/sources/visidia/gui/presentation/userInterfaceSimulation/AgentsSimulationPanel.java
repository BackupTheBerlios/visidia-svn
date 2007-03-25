package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.JPanel;

import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.metier.simulation.SentAgent;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.RecoverableObject;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SelectionGetData;
import visidia.gui.presentation.SelectionUnit;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;
import visidia.simulation.MessageSendingAck;
import visidia.simulation.MessageSendingEvent;

/**
 * 
 * Contenu par la class AgentsSimulationWindow C'est dans ce panel que s'affiche
 * le graphe et les agents C'est cette fenêtre qui gère les actions (souris,
 * clavier) de l'utilisateur sur les éléments du graphe
 * 
 */
public class AgentsSimulationPanel extends JPanel implements ActionListener,
	MouseListener, MouseMotionListener, KeyListener {

    private static final long serialVersionUID = 6633924693735717321L;

    /** Couleur de fond par defaut du grapheVisuPanel * */
    private AgentsSimulationWindow agentsSimulationWindow;

    protected FormeDessin objet_sous_souris;

    protected int PAS_PAR_DEFAUT = 10;

    /**
         * le pas de visulaisation : vitesse de visulaisation. Bilel.
         */
    protected int lePas;

    protected javax.swing.Timer timer;

    protected int x_ancien;

    protected int y_ancien;

    int current_x = 0;

    int current_y = 0;

    /** Signifie si un drag-and-drop d'un sommet est en court * */
    private boolean drag_n_drop_sommet;

    // Stocke l'élément courant qu'on deplace
    private Ensemble formeDessin_a_deplacer; 
    /***********************************************************************
         * Signifie si un drag-and-drop d'un sommet est en court, mais avec
         * déplacement d'un objet déàja existant
         **********************************************************************/
    private boolean drag_n_drop_sommet_existant;

    private int dx;

    private int dy;

    private SommetDessin ancien_sommet_sous_souris;

    /** Signifie si un drag-and-drop d'une sélection est en court * */
    private boolean drag_n_drop_selection;
    /** Signifie si un drag-and-drop du graphe entier est en court * */
    private boolean drag_n_drop_graph;
    private FormeDessin drag_n_dropVertex;
    private int drag_n_dropVertex_X;
    private int drag_n_dropVertex_Y;
    /** Abscisse de la position initiale de la souris lors d'une sélection * */
	private int x_origine;

	/** Ordonnee de la position initiale de la souris lors d'une sélection * */
	private int y_origine;

    /** Dimension du graphique */
    protected Dimension size;

    protected SelectionUnit selectionUnit;

    protected Vector sentAgentVector = new Vector(10, 10);

    /**
         * Cet objet sert à avoir un accès concurrent aux évènements à
         * visualiser. Bilel.
         * 
         */
    private final Object concurrentObject = new Object();

    /**
         * Instancie un SimulationPanel associe a la fenetre de simulation
         * passee en argument.
         */

    public AgentsSimulationPanel(AgentsSimulationWindow simulation) {
	this.agentsSimulationWindow = simulation;
	this.objet_sous_souris = null;
	this.lePas = this.PAS_PAR_DEFAUT;

	if (simulation.getVueGraphe().getGraphe().ordre() != 0) {
	    this.size = simulation.getVueGraphe().donnerDimension();
	    this.setPreferredSize(this.size);
	    this.revalidate();
	} else {
	    this.size = new Dimension(0, 0);
	}

	this.selectionUnit = new SelectionUnit(new SelectionGetData() {
	    public SelectionDessin getSelectionDessin() {
		return AgentsSimulationPanel.this.agentsSimulationWindow.selection;
	    }

	    public UndoInfo getUndoInfo() throws NoSuchMethodException {
		throw new NoSuchMethodException("undo processing not used");
	    }

	    public RecoverableObject getRecoverableObject() {
		return AgentsSimulationPanel.this.agentsSimulationWindow
			.getVueGraphe();
	    }
	}, this);

	this.addMouseListener(this.selectionUnit);
	this.addMouseListener(this);
	this.addMouseMotionListener(this.selectionUnit);
	this.addMouseMotionListener(this);
	this.addKeyListener(this);

	this.timer = new javax.swing.Timer(30, this);
	this.setBackground(new Color(0xe6e6fa));
    }

    public int pas() {
	return this.lePas;
    }

    public void updatePas(int p) {
	this.lePas = p;
	synchronized (this.concurrentObject) {
	    // SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    for (int i = 0; i < size; i++) {
		((SentAgent) this.sentAgentVector.elementAt(i)).setStep(p);
	    }
	}
    }

    /**
         * Redessine les elements du graphique.
         */
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	this.agentsSimulationWindow.getVueGraphe().dessiner(this, g, "agents");

	g.setColor(Color.red);

	synchronized (this.concurrentObject) {
	    SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    for (int i = 0; i < size; i++) {
		sentAgent = (SentAgent) this.sentAgentVector.elementAt(i);
		sentAgent.paint(g);
	    }
	}

	this.selectionUnit.drawSelection(g);
    }

    public void actionPerformed(ActionEvent e) {

	synchronized (this.concurrentObject) {
	    SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    Vector tmpVect = new Vector(size);
	    for (int i = 0; i < size; i++) {
		sentAgent = (SentAgent) this.sentAgentVector.elementAt(i);
		if (sentAgent.isIntoBounds()) {
		    sentAgent.moveForward();
		    tmpVect.add(sentAgent);
		} else {
		    MessageSendingAck msa = new MessageSendingAck(sentAgent
			    .getEvent().eventNumber());

		    // envoyer un message d'acquitement de fin d'animation
		    try {
			this.agentsSimulationWindow.getAckPipe().put(msa);
		    } catch (InterruptedException exp) {
			exp.printStackTrace();
			return;
		    }
		}
	    }
	    this.sentAgentVector = tmpVect;
	}
	this.repaint();
    }

    public boolean isRunning() {
	if (this.timer.isRunning()) {
	    return true;
	} else {
	    return false;
	}
    }

    public void start() {
	this.timer.start();
    }

    public void pause() {
	this.timer.stop();
    }

    public void stop() {
	this.timer.stop();
	synchronized (this.concurrentObject) {
	    this.sentAgentVector = new Vector(10, 10);
	}
    }

    /*
         * Gestion des clicks gauches pour la modification des graphes
         * 
         */
    public void mousePressed(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();

	this.size = this.getPreferredSize();

	switch (evt.getModifiers()) {
	
	case InputEvent.BUTTON2_MASK:
	case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK): 
	    	// Pour les souris sans boutton du milieu
		this.appuiBoutonMilieu(x, y);
		break;
	// Bouton gauche
	case InputEvent.BUTTON1_MASK:
	    this.appuiBoutonGauche(x, y);
	    break;
	default:

	}
    }

    /**
         * L'appui du bouton gauche de la souris permet de créer un nouveau
         * sommet (avec arete si on clique sur un sommet déjà existant).
         */
    public void appuiBoutonGauche(int x, int y) {
	this.x_ancien = x;
	this.y_ancien = y;

	try {
	    this.objet_sous_souris = this.agentsSimulationWindow.getVueGraphe()
		    .sommet_en_dessous(x, y);
	} catch (NoSuchElementException e) {
	    // Vertex added to the visualisation graph
	    this.objet_sous_souris = this.agentsSimulationWindow.getVueGraphe()
		    .creerSommet(x, y);
	    // Vertex added to the simulation graph

	    // Si le simulateur n'est pas encore créé, on ajoute seulement
	    // le sommet au graphe dessin,
	    // le graphe du simulateur sera initialisé en conséquence lors
	    // du début de la simulation
	    if (this.agentsSimulationWindow.sim != null) {
		this.addVertexSimpleGraph((SommetDessin) objet_sous_souris);
	    }

	    this.repaint();

	}
    }

    /**
         * L'appui du bouton du milieu de la souris permet de déplacer un objet,
         * la sélection ou la zone visible du plan de travail.
         */
    public void appuiBoutonMilieu(int x, int y) {
	this.x_ancien = x;
	this.y_ancien = y;

	try {
	    this.objet_sous_souris = this.agentsSimulationWindow.getVueGraphe().en_dessous(x,
		    y);

	    if (this.agentsSimulationWindow.selection.contient(this.objet_sous_souris)) {
		// Deplacement de la selection

		// on ne deplace pas les aretes, mais leurs sommets incidents.
		this.formeDessin_a_deplacer = Traitements
			.sommetsTotaux(this.agentsSimulationWindow.selection.elements());

		// formeDessin_a_deplacer.inserer(editeur.selection.autresObjetsVisu());
		this.drag_n_drop_selection = true;

		this.x_origine = x;
		this.y_origine = y;
	    } else if (this.objet_sous_souris.type().equals("vertex")) {

		this.drag_n_drop_sommet_existant = true;
		// Quand on drag un sommet, on veut un positionnement precis par
		// rapport au curseur, on sauvegarde donc la position relative
		// du sommet par rapport au curseur.
		this.drag_n_dropVertex_X = ((SommetDessin) this.objet_sous_souris)
			.centreX();
		this.drag_n_dropVertex_Y = ((SommetDessin) this.objet_sous_souris)
			.centreY();

		this.dx = ((SommetDessin) this.objet_sous_souris).centreX() - x;
		this.dy = ((SommetDessin) this.objet_sous_souris).centreY() - y;
	    }
	} catch (NoSuchElementException e) {
	    this.drag_n_drop_graph = true;
	    this.x_origine = x;
	    this.y_origine = y;
	}
    }

    /**
	 * Permet de déplacer l'objet sous le curseur en même temps que la souris au
	 * cas ou on maintient le bouton du milieu appuyé.
	 */
	public void glisseBoutonMilieu(int x, int y) {
		// deplacer objet sous la souris
		if (this.objet_sous_souris != null) {
			if (this.formeDessin_a_deplacer != null) {
				// on deplace la selection

				VueGraphe.deplacerFormeDessin(this.formeDessin_a_deplacer
						.elements(), x - this.x_ancien, y - this.y_ancien);
				this.x_ancien = x;
				this.y_ancien = y;
			} else if (this.drag_n_drop_sommet
					|| this.drag_n_drop_sommet_existant) {
				// on deplace un sommet
				this.glisseSommet(x, y);
			} else {
				// on deplace un formeDessin quelconque
				this.objet_sous_souris.deplacer(x - this.x_ancien, y
						- this.y_ancien);
				this.x_ancien = x;
				this.y_ancien = y;
			}
		} else {
			boolean changed = false;

			// Rien sous la souris
			// On va donc translater tout le graphe

			this.formeDessin_a_deplacer = Traitements
					.sommetsTotaux(this.agentsSimulationWindow.getVueGraphe().listeAffichage());
			if (this.formeDessin_a_deplacer != null) {
				VueGraphe.deplacerFormeDessin(this.formeDessin_a_deplacer
						.elements(), x - this.x_ancien, y - this.y_ancien);
				this.x_ancien = x;
				this.y_ancien = y;
			}
			Enumeration objets_deplaces = this.formeDessin_a_deplacer
					.elements();
			while (objets_deplaces.hasMoreElements()) {
				SommetDessin objet = (SommetDessin) objets_deplaces
						.nextElement();
				if (objet.centreX() + 20 > this.size.width) {
					this.size.width = objet.centreX() + 20;
					changed = true;
				}
				if (objet.centreY() + 20 > this.size.height) {
					this.size.height = objet.centreY() + 20;
					changed = true;
				}
			}
			if (changed) {
				
				this.setPreferredSize(this.size);
				this.revalidate();
			}
			this.scrollRectToVisible(new Rectangle(Math.min(x,
					this.size.width - 20) - 10, Math.min(y,
					this.size.height - 20) - 10, 30, 30));
		}
		this.repaint();
	}

    /**
         * Ajout du sommet au graph du simulateur pendant la simulation
         * 
         * @param numVertex
         *                Le sommet du dessin à ajouter au simulateur
         */
    public void addVertexSimpleGraph(SommetDessin sommetD) {

	this.agentsSimulationWindow.sim.graph.put(new Integer(sommetD
		.getEtiquette()), sommetD.getWhiteBoardTable());
	this.agentsSimulationWindow.sim.graph.vertex(
		new Integer(sommetD.getEtiquette())).setData(
		sommetD.getStateTable().clone());

    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseMoved(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	boolean changed = false;

	if (this.drag_n_drop_sommet) {
	    try {

		SommetDessin sommet_en_dessous = this.agentsSimulationWindow
			.getVueGraphe().sommet_en_dessous(x, y,
				this.objet_sous_souris);

		/*
                 * On efface l'ancienne arete si une autre arete a ete cree Si
                 * l'utilisateur boucle sur le meme sommet, ceci n'efface pas la
                 * pseudo-arete.
                 */

		boolean passuppr = true;

		if (this.agentsSimulationWindow.getVueGraphe().rechercherArete(
			(this.ancien_sommet_sous_souris).getEtiquette(),
			(sommet_en_dessous).getEtiquette()) != null) {
		    // l'arrete existe déjà
		    this.agentsSimulationWindow
			    .getVueGraphe()
			    .delObject(
				    ((this.agentsSimulationWindow
					    .getVueGraphe()).rechercherArete(
					    (this.ancien_sommet_sous_souris)
						    .getEtiquette(),
					    (sommet_en_dessous).getEtiquette())));

		    passuppr = false;
		}

		// En cas d'ajout d'une arrête entre deux sommets existants
		// L'extrémité de l'arrête ajoutée lors du début du drag est le
                // sommet
		// qui va êter fusionné et qui n'est donc pas valide
		// On supprimé donc l'arrête avec le sommet invalide et on la
                // rajoute
		// avec le sommet actualisé
		if (this.agentsSimulationWindow.getVueGraphe().rechercherArete(
			(this.ancien_sommet_sous_souris).getEtiquette(),
			((SommetDessin) (this.objet_sous_souris))
				.getEtiquette()) != null) {
		    // l'arrete existe déjà
		    this.agentsSimulationWindow
			    .getVueGraphe()
			    .delObject(
				    ((this.agentsSimulationWindow
					    .getVueGraphe())
					    .rechercherArete(
						    (this.ancien_sommet_sous_souris)
							    .getEtiquette(),
						    ((SommetDessin) (this.objet_sous_souris))
							    .getEtiquette())));

		    this.agentsSimulationWindow.getVueGraphe().creerArete(
			    this.ancien_sommet_sous_souris, sommet_en_dessous);

		}

		// ww: ceci efface l'arete en cas de bouclage sur un meme sommet
		if ((this.agentsSimulationWindow
			.getVueGraphe()
			.rechercherArete(
				(this.ancien_sommet_sous_souris).getEtiquette(),
				(((SommetDessin) this.objet_sous_souris)
					.getEtiquette())) != null)
			&& ((this.ancien_sommet_sous_souris).getEtiquette() == ((sommet_en_dessous)
				.getEtiquette()))) {
		    this.ancien_sommet_sous_souris
			    .getVueGraphe()
			    .delObject(
				    ((this.ancien_sommet_sous_souris
					    .getVueGraphe())
					    .rechercherArete(
						    (this.ancien_sommet_sous_souris)
							    .getEtiquette(),
						    ((SommetDessin) this.objet_sous_souris)
							    .getEtiquette())));
		    passuppr = false;
		}

		sommet_en_dessous
			.fusionner((SommetDessin) this.objet_sous_souris);

		// ww: end
		if (passuppr) {
		    // Si le simulateur n'est pas encore créé, on ajoute
		    // seulement
		    // le sommet au graphe dessin,
		    // le graphe du simulateur sera initialisé en
		    // conséquence lors
		    // du début de la simulation
		    if (this.agentsSimulationWindow.sim != null) {

			// Edges between the old vertex and the new vertex added
			// to the simumation graph and ...
			this.agentsSimulationWindow.sim.graph
				.link(
					Integer.valueOf(sommet_en_dessous
						.getEtiquette()),
					Integer
						.valueOf((this.ancien_sommet_sous_souris)
							.getEtiquette()));
		    }
		}

	    } catch (NoSuchElementException e) {
		// Si le simulateur n'est pas encore créé, on ajoute seulement
		// le sommet au graphe dessin,
		// le graphe du simulateur sera initialisé en conséquence lors
		// du début de la simulation
		if (this.agentsSimulationWindow.sim != null) {

		    this
			    .addVertexSimpleGraph((SommetDessin) this.objet_sous_souris);

		    System.out.println(Integer
			    .valueOf(((SommetDessin) this.objet_sous_souris)
				    .getEtiquette()));

		    // Edges between the old vertex and the new vertex added
		    // to the simumation graph and ...
		    this.agentsSimulationWindow.sim.graph.link(new Integer(
			    ((SommetDessin) this.objet_sous_souris)
				    .getEtiquette()), new Integer(
			    (this.ancien_sommet_sous_souris).getEtiquette()));

		}
	    }
	    this.drag_n_drop_sommet = false;
	} else if (this.drag_n_drop_sommet_existant) {
	    try {
		/*
                 * On fusionne deux sommets existants par deplacement de l'un
                 * d'eux.
                 */
		SommetDessin sommet_en_dessous = this.ancien_sommet_sous_souris
			.getVueGraphe().sommet_en_dessous(x, y,
				this.objet_sous_souris);

		sommet_en_dessous
			.fusionner((SommetDessin) this.objet_sous_souris);

	    } catch (NoSuchElementException e) {

	    }
	    this.drag_n_drop_sommet_existant = false;
	}

	// On reinitialise les variables utilisees
	// this.formeDessin_a_deplacer = null;
	this.objet_sous_souris = null;

	if (x + 20 > this.size.width) {
	    this.size.width = x + 20;
	    changed = true;

	}
	if (y + 20 > this.size.height) {
	    this.size.height = y + 20;
	    changed = true;
	}
	if (changed) {
	    // this.editeur.changerDimensionGraphe(this.size);
	    this.setPreferredSize(this.size);
	    this.revalidate();
	}
	this.scrollRectToVisible(new Rectangle(x - 10, y - 10, 30, 30));
	// this.editeur.setUndo();
	this.repaint();
    }

    /**
         * Implémentation de MouseMotionListener.
         */
    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();

	switch (evt.getModifiers()) {
	// Bouton du milieu
	case InputEvent.BUTTON2_MASK:
	case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK): // Pour les
		// souris sans
		// boutton du
		// milieu
		this.glisseBoutonMilieu(x, y);
		break;
		
	// Bouton gauche
	case InputEvent.BUTTON1_MASK:
	case 0:
	    this.glisseBoutonGauche(x, y);
	    break;

	default:
	}
    }

    /**
         * Permet de créer un sommet et une arête quand on maintient le bouton
         * gauche de la souris appuyé. Si on relache la souris au dessus d'un
         * sommet existant, la méthode <b>mouseRelease</b> fusionne ce sommet
         * avec le nouveau sommet cree.
         */
    public void glisseBoutonGauche(int x, int y) {
	boolean changed = false;
	// AreteDessin areteCree = null;

	if (this.objet_sous_souris != null) {
	    if (this.drag_n_drop_sommet) {
		this.glisseSommet(x, y);
	    } else {
		// creer un nouveau sommet et l'arete qui va avec
		this.ancien_sommet_sous_souris = (SommetDessin) this.objet_sous_souris;

		this.objet_sous_souris = this.agentsSimulationWindow
			.getVueGraphe().creerSommet(x, y);

		// to the vizualization graph
		this.agentsSimulationWindow.getVueGraphe().creerArete(
			this.ancien_sommet_sous_souris,
			(SommetDessin) this.objet_sous_souris);

		this.drag_n_drop_sommet = true;
		// Quand on drag un sommet, on veut un
		// positionnement precis par rapport au curseur,
		// on sauvegarde donc la position relative du
		// sommet par rapport au curseur, ici elle est
		// nulle.
		this.dx = this.dy = 0;
	    }
	    this.scrollRectToVisible(new Rectangle(x - 10, y - 10, 30, 30));
	    if (x + 20 > this.size.width) {
		this.size.width = x + 20;
		changed = true;
	    }
	    if (y + 20 > this.size.height) {
		this.size.height = y + 20;
		changed = true;
	    }
	    if (changed) {
		this.agentsSimulationWindow.editeur
			.changerDimensionGraphe(this.size);
		this.setPreferredSize(this.size);
		this.revalidate();
	    }
	    this.repaint();
	}
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    /**
         * Implementation de KeyListener.
         */
    public void keyPressed(KeyEvent evt) {
    }

    public void keyReleased(KeyEvent evt) {
    }

    public void keyTyped(KeyEvent evt) {
    }

    /* Fin d'implementation de KeyListener */

    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    public void animate(MessageSendingEvent mse) {

	synchronized (this.concurrentObject) {
	    SentAgent sentAgent;

	    sentAgent = new SentAgent(mse, this.agentsSimulationWindow
		    .getVueGraphe().rechercherSommet(mse.sender().toString())
		    .centre(), this.agentsSimulationWindow.getVueGraphe()
		    .rechercherSommet(mse.receiver().toString()).centre(),
		    this.lePas);
	    this.sentAgentVector.add(sentAgent);

	}
    }

    /**
         * Méthode privée qui gère le déplacement d'un sommet avec effet
         * d'aspiration par un autre sommet.
         */
    private void glisseSommet(int x, int y) {
	// On regarde si notre sommet en recouvre un autre
	// le try leve une exception si il n'y a pas de recouverture
	try {

	    SommetDessin sommet_en_dessous = this.agentsSimulationWindow
		    .getVueGraphe().sommet_en_dessous(x, y,
			    this.objet_sous_souris);

	    // Oui: le sommet deplace est aspire
	    this.x_ancien = sommet_en_dessous.centreX();
	    this.y_ancien = sommet_en_dessous.centreY();
	    ((SommetDessin) this.objet_sous_souris).placer(this.x_ancien,
		    this.y_ancien);

	} catch (NoSuchElementException e) {
	    // Non: on se contente de deplacer le sommet
	    ((SommetDessin) this.objet_sous_souris).placer(x + this.dx, y
		    + this.dy);

	    this.x_ancien = x;
	    this.y_ancien = y;
	}

	// remarque: dans le cas d'un drag-n-drop, on n'utilise pas
	// x_ancien et y_ancien, mais dx et dy pour assurer un
	// positionnement plus précis (éviter les derivés entre la
	// position du curseur et celle de l'objet déplacé), on
	// actualise cependant les variables x_ancien et y_ancien pour
	// homogeneiser avec le cas général.
    }

}

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
//import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.JPanel;

import visidia.gui.metier.simulation.SentAgent;
//import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.RecoverableObject;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SelectionGetData;
import visidia.gui.presentation.SelectionUnit;
import visidia.gui.presentation.SommetDessin;
//import visidia.gui.presentation.userInterfaceEdition.undo.AjouteObjet;
//import visidia.gui.presentation.userInterfaceEdition.undo.DeplaceObjets;
//import visidia.gui.presentation.userInterfaceEdition.undo.FusionneSommet;
//import visidia.gui.presentation.userInterfaceEdition.undo.SelectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;
import visidia.simulation.MessageSendingAck;
import visidia.simulation.MessageSendingEvent;

/**
 * 
 * Contenu par la class AgentsSimulationWindow
 * C'est dans ce panel que s'affiche le graphe et les agents
 * C'est cette fenêtre qui gère les actions (souris, clavier) de l'utilisateur
 * sur les éléments du graphe
 *
 */
public class AgentsSimulationPanel extends JPanel implements ActionListener,
		MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 
	 */
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

	/***************************************************************************
	 * Signifie si un drag-and-drop d'un sommet est en court, mais avec
	 * déplacement d'un objet déàja existant
	 **************************************************************************/
	private boolean drag_n_drop_sommet_existant;

	private int dx;

	private int dy;

	private SommetDessin ancien_sommet_sous_souris;

	/** Signifie si un drag-and-drop d'une sélection est en court * */
	//private boolean drag_n_drop_selection;

	/** Signifie si un drag-and-drop du graphe entier est en court * */
	//private boolean drag_n_drop_graph;

	//private FormeDessin drag_n_dropVertex;

	//private int drag_n_dropVertex_X;

	//private int drag_n_dropVertex_Y;

	/** Abscisse de la position initiale de la souris lors d'une sélection * */

	/** Dimension du graphique */
	protected Dimension size;

	protected SelectionUnit selectionUnit;

	protected Vector sentAgentVector = new Vector(10, 10);

	/**
	 * Cet objet sert à avoir un accès concurrent aux évènements à visualiser.
	 * Bilel.
	 * 
	 */
	private final Object concurrentObject = new Object();

	/**
	 * Instancie un SimulationPanel associe a la fenetre de simulation passee en
	 * argument.
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
		if (this.timer.isRunning())
			return true;
		else
			return false;
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
		// Bouton gauche
		case InputEvent.BUTTON1_MASK:
			this.appuiBoutonGauche(x, y);
			break;

		default:

		}
	}

	/**
	 * L'appui du bouton gauche de la souris permet de créer un nouveau sommet
	 * (avec arete si on clique sur un sommet déjà existant).
	 */
	public void appuiBoutonGauche(int x, int y) {
		this.x_ancien = x;
		this.y_ancien = y;

		try {
			this.objet_sous_souris = this.agentsSimulationWindow.getVueGraphe()
					.sommet_en_dessous(x, y);
		} catch (NoSuchElementException e) {
			this.objet_sous_souris = this.agentsSimulationWindow.getVueGraphe()
					.creerSommet(x, y);

			this.repaint();
		}
	}

	/** Solution non fonctionnelle
	 * Met à jour, à partir du graphe de l'interface graphique de classe Graphe
	 * le graph du simulateur de type SimpleGraph
	 
	public void updateSimulationGraphe() {
		//visidia.gui.metier.simulation.Convertisseur c = new visidia.gui.metier.simulation.Convertisseur();
		if(this.agentsSimulationWindow != null) {
			this.agentsSimulationWindow.updateSimulationGraphe();
		}
	}*/
	
	
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

				// ww : On efface l'ancienne arete si OUI, puisque une
				// autre arete a ete cree, mais si l'utilisateur boucle
				// sur le meme sommet, ceci n'efface pas la
				// pseudo-arete.
				if (this.agentsSimulationWindow.getVueGraphe().rechercherArete(
						(this.ancien_sommet_sous_souris).getEtiquette(),
						(sommet_en_dessous).getEtiquette()) != null) {
					this.agentsSimulationWindow
							.getVueGraphe()
							.delObject(
									((this.agentsSimulationWindow
											.getVueGraphe()).rechercherArete(
											(this.ancien_sommet_sous_souris)
													.getEtiquette(),
											(sommet_en_dessous).getEtiquette())));
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
				}
				// ww: end

				sommet_en_dessous
						.fusionner((SommetDessin) this.objet_sous_souris);

			} catch (NoSuchElementException e) {
			}
			this.drag_n_drop_sommet = false;
		} else if (this.drag_n_drop_sommet_existant) {
			try {
				/*
				 * On fusionne deux sommets existants par deplacement de l'un
				 * d'eux.
				 */
				SommetDessin sommet_en_dessous = this.ancien_sommet_sous_souris.getVueGraphe()
						.sommet_en_dessous(x, y, this.objet_sous_souris);
		

				sommet_en_dessous
						.fusionner((SommetDessin) this.objet_sous_souris);
			} catch (NoSuchElementException e) {

			}
			this.drag_n_drop_sommet_existant = false;
		}
		

		// On reinitialise les variables utilisees
		//this.formeDessin_a_deplacer = null;
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
			//this.editeur.changerDimensionGraphe(this.size);
			this.setPreferredSize(this.size);
			this.revalidate();
		}
		this.scrollRectToVisible(new Rectangle(x - 10, y - 10, 30, 30));
		//this.editeur.setUndo();
		this.repaint();
	}

	/**
	 * Implémentation de MouseMotionListener.
	 */
	public void mouseDragged(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();

		switch (evt.getModifiers()) {
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
	 * gauche de la souris appuyé. Si on relache la souris au dessus d'un sommet
	 * existant, la méthode <b>mouseRelease</b> fusionne ce sommet avec le
	 * nouveau sommet cree.
	 */
	public void glisseBoutonGauche(int x, int y) {
		boolean changed = false;
		// AreteDessin areteCree = null;

		if (this.objet_sous_souris != null) {
			if (this.drag_n_drop_sommet)
				this.glisseSommet(x, y);

			else {
				// creer un nouveau sommet et l'arete qui va avec : c'est un
				// nouvel undoGroup.
				this.ancien_sommet_sous_souris = (SommetDessin) this.objet_sous_souris;

				this.objet_sous_souris = this.agentsSimulationWindow
						.getVueGraphe().creerSommet(x, y);

				// On sauvegarde l'objet UndoObject contenant ce
				// sommet : on peut en effet avoir a le supprimer
				// en cas de fusion avec un sommet existant.
				//this.drag_n_dropVertex = this.objet_sous_souris;

				/*AreteDessin a = */ this.agentsSimulationWindow.getVueGraphe()
						.creerArete(this.ancien_sommet_sous_souris,
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

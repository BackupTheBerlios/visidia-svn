package visidia.gui.presentation.userInterfaceEdition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
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

import visidia.gui.donnees.TableImages;
import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.userInterfaceEdition.undo.AjouteObjet;
import visidia.gui.presentation.userInterfaceEdition.undo.DeplaceObjets;
import visidia.gui.presentation.userInterfaceEdition.undo.DeselectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.FusionneSommet;
import visidia.gui.presentation.userInterfaceEdition.undo.SelectFormeDessin;

/**
 * 
 * Un GrapheVisuPanel est un JPanel sur lequel le graphe est affiché et qui
 * permet d'interagir avec le graphe. <BR>
 * Pour bien comprendre la séparation des taches entre les classes Editeur et
 * GrapheVisuPanel, on peut imaginer plusieurs objets GrapheVisuPanel associés a
 * une seule instance de Editeur pour éditer plusieurs parties d'un même graphe
 * en même temps.
 * 
 * RQ : Panel utilisé lors de la création du graphe et non pas lors de la
 * simulation
 */
public class GrapheVisuPanel extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6415374030017341894L;

	/** Couleur de fond par défaut du grapheVisuPanel * */
	private static final Color RECT_SELECTION_COULEUR = Color.gray;

	/** L'éditeur auquel est associé le GrapheVisuPanel */
	protected Editeur editeur;

	protected MediaTracker tracker;

	/** Dimension du graphique */
	protected Dimension size = new Dimension(0, 0);

	/** Abscisse de l'ancienne position de la souris au debut d'un drag-and-drop * */
	protected int x_ancien;

	/***************************************************************************
	 * Ordonnée de l'ancienne position de la souris au debut d'un drag-and-drop
	 **************************************************************************/
	protected int y_ancien;

	/** Objet situé à la position à laquelle est la souris * */
	private FormeDessin objet_sous_souris;

	/** Signifie si un drag-and-drop d'un sommet est en court * */
	private boolean drag_n_drop_sommet;

	/***************************************************************************
	 * Signifie si un drag-and-drop d'un sommet est en court, mais avec
	 * déplacement d'un objet déàja existant
	 **************************************************************************/
	private boolean drag_n_drop_sommet_existant;

	private int dx;

	private int dy;

	/** Signifie si un drag-and-drop d'une sélection est en court * */
	private boolean drag_n_drop_selection;

	/** Signifie si un drag-and-drop du graphe entier est en court * */
	private boolean drag_n_drop_graph;

	/** Abscisse de la position initiale de la souris lors d'une sélection * */
	private int x_origine;

	/** Ordonnee de la position initiale de la souris lors d'une sélection * */
	private int y_origine;

	/** Signifie si une sélection par un rectangle est en court * */
	private boolean carre_selection;

	private int selection_x, selection_x1, selection_x2;

	private int selection_y, selection_y1, selection_y2;

	private Ensemble formeDessin_a_deplacer;

	private FormeDessin drag_n_dropVertex;

	private int drag_n_dropVertex_X;

	private int drag_n_dropVertex_Y;

	protected Vector<Image> vecteurImages;

	private SommetDessin ancien_sommet_sous_souris; // ww

	/**
	 * Instancie un GrapheVisuPanel associé à l'editeur passé en argument.
	 */
	public GrapheVisuPanel(Editeur un_editeur) {
		this.tracker = new MediaTracker(this);
		this.vecteurImages = new Vector<Image>();
		this.vecteurImages.add(TableImages.getImage("image1"));
		this.vecteurImages.add(TableImages.getImage("image2"));
		this.vecteurImages.add(TableImages.getImage("image3"));
		this.vecteurImages.add(TableImages.getImage("image4"));
		this.vecteurImages.add(TableImages.getImage("image5"));
		this.vecteurImages.add(TableImages.getImage("image6"));

		for (int i = 0; i < 6; i++) {
			this.tracker.addImage((Image) this.vecteurImages.elementAt(i), 0);
		}
		try {
			this.tracker.waitForID(0);
		} catch (InterruptedException e) {
			System.out.println("probleme");
		}
		this.editeur = un_editeur;
		if (this.editeur.graph().ordre() != 0) {

			this.size = this.editeur.getVueGraphe().donnerDimension();
		} else {
			this.size = new Dimension(0, 0);
		}
		this.objet_sous_souris = null;
		this.drag_n_drop_sommet = false;
		this.drag_n_drop_sommet_existant = false;
		this.drag_n_drop_selection = false;
		this.drag_n_drop_graph = false;
		this.carre_selection = false;
		this.formeDessin_a_deplacer = null;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);

		this.setBackground(new Color(0xe6e6fa));
	}

	/**
	 * Redessine les elements du graphique.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.editeur.getVueGraphe().dessiner(this, g);

		if (this.tracker.statusAll(false) != MediaTracker.COMPLETE) {
			g.drawString("Probleme de chargement d'image", 50, 100);
			return;
		}

		if (this.carre_selection) {
			this.dessinerCarre(g, this.selection_x1, this.selection_y1,
					this.selection_x2, this.selection_y2);
		}
	}

	/**
	 * Methode appellée quand on appuie sur un bouton de la souris. Suivant ce
	 * que l'on trouve sous la souris, on appelle les autres méthodes de la
	 * classe.
	 */
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();

		this.size = this.getPreferredSize();

		switch (evt.getModifiers()) {
		// Bouton du milieu
		case InputEvent.BUTTON2_MASK:
		case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK): // Pour les
			// souris sans
			// boutton du
			// milieu
			this.appuiBoutonMilieu(x, y);
			break;

		// Bouton droit
		case InputEvent.BUTTON3_MASK:
			this.appuiBoutonDroit(x, y);
			break;

		// shift + bouton droit
		case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
			this.appuiShiftBoutonDroit(x, y);
			break;

		// Bouton gauche
		case InputEvent.BUTTON1_MASK:
			this.appuiBoutonGauche(x, y);
			break;

		default:

		}
	}

	/**
	 * Methode appellee quand on relache un bouton de la souris.
	 */
	public void mouseReleased(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		boolean changed = false;

		if (this.drag_n_drop_sommet) {
			try {
				/*
				 * Si on fusionne deux sommets, il faut retirer le sommet cree
				 * de la liste
				 */
				/*
				 * des informations de undo, sinon, il sera recree lors d'un
				 * redo eventuel!
				 */
				SommetDessin sommet_en_dessous = this.editeur.getVueGraphe()
						.sommet_en_dessous(x, y, this.objet_sous_souris);

				// ww : On efface l'ancienne arete si OUI, puisque une
				// autre arete a ete cree, mais si l'utilisateur boucle
				// sur le meme sommet, ceci n'efface pas la
				// pseudo-arete.
				if (this.editeur.getVueGraphe().rechercherArete(
						(this.ancien_sommet_sous_souris).getEtiquette(),
						(sommet_en_dessous).getEtiquette()) != null) {
					this.editeur.getVueGraphe().delObject(
							((this.editeur.getVueGraphe()).rechercherArete(
									(this.ancien_sommet_sous_souris)
											.getEtiquette(),
									(sommet_en_dessous).getEtiquette())));
				}

				// ww: ceci efface l'arete en cas de bouclage sur un meme sommet
				if ((this.editeur
						.getVueGraphe()
						.rechercherArete(
								(this.ancien_sommet_sous_souris).getEtiquette(),
								(((SommetDessin) this.objet_sous_souris)
										.getEtiquette())) != null)
						&& ((this.ancien_sommet_sous_souris).getEtiquette() == ((sommet_en_dessous)
								.getEtiquette()))) {
					this.editeur.getVueGraphe().delObject(
							((this.editeur.getVueGraphe()).rechercherArete(
									(this.ancien_sommet_sous_souris)
											.getEtiquette(),
									((SommetDessin) this.objet_sous_souris)
											.getEtiquette())));
				}
				// ww: end

				sommet_en_dessous
						.fusionner((SommetDessin) this.objet_sous_souris);
				this.editeur.undoInfo.removeObject(this.drag_n_dropVertex);

			} catch (NoSuchElementException e) {
			}
			this.drag_n_drop_sommet = false;
		} else if (this.drag_n_drop_sommet_existant) {
			try {
				/*
				 * On fusionne deux sommets existants par deplacement de l'un
				 * d'eux.
				 */
				SommetDessin sommet_en_dessous = this.editeur.getVueGraphe()
						.sommet_en_dessous(x, y, this.objet_sous_souris);
				this.editeur.undoInfo.newGroup("Separate merged vertices",
						"Merge vertices");

				this.editeur.undoInfo.addInfo(new FusionneSommet(
						(SommetDessin) this.objet_sous_souris,
						sommet_en_dessous, this.drag_n_dropVertex_X,
						this.drag_n_dropVertex_Y));

				sommet_en_dessous
						.fusionner((SommetDessin) this.objet_sous_souris);
			} catch (NoSuchElementException e) {
				this.editeur.undoInfo.newGroup("Move vertex to old position",
						"Move vertex to new position");
				this.editeur.undoInfo.addInfo(new DeplaceObjets(
						(SommetDessin) this.objet_sous_souris, x
								- this.drag_n_dropVertex_X, y
								- this.drag_n_dropVertex_Y));
			}
			this.drag_n_drop_sommet_existant = false;
		} else if (this.drag_n_drop_selection) {
			this.editeur.undoInfo.newGroup("Move selection to old position",
					"Move selection to new position");
			this.editeur.undoInfo.addInfo(new DeplaceObjets(
					this.formeDessin_a_deplacer, x - this.x_origine, y
							- this.y_origine));
			this.drag_n_drop_selection = false;
		} else if (this.drag_n_drop_graph) {
			this.editeur.undoInfo.newGroup("Move graph to old position",
					"Move graph to new position");
			this.editeur.undoInfo.addInfo(new DeplaceObjets(
					this.formeDessin_a_deplacer, x - this.x_origine, y
							- this.y_origine));
			this.drag_n_drop_graph = false;
		} else if (this.carre_selection) {
			// selection des objets dans la zone rectangulaire : c'est
			// un nouveau undoGroup.
			Enumeration e = this.editeur.getVueGraphe().objetsDansRegion(
					this.selection_x1, this.selection_y1, this.selection_x2,
					this.selection_y2);

			if (e.hasMoreElements()) {
				this.editeur.undoInfo.newGroup(
						"Unselect elements in rectangular area",
						"Select elements in rectangular area");

				while (e.hasMoreElements()) {
					FormeDessin formeDessin = (FormeDessin) e.nextElement();
					this.editeur.selection.insererElement(formeDessin);
					this.editeur.undoInfo.addInfo(new SelectFormeDessin(
							this.editeur.selection, formeDessin));
					// formeDessin.enluminer(true);
				}
			}
			this.carre_selection = false;
		}

		// On reinitialise les variables utilisees
		this.formeDessin_a_deplacer = null;
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
			this.editeur.changerDimensionGraphe(this.size);
			this.setPreferredSize(this.size);
			this.revalidate();
		}
		this.scrollRectToVisible(new Rectangle(x - 10, y - 10, 30, 30));
		this.editeur.setUndo();
		this.repaint();
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	/**
	 * L'appui du bouton du milieu de la souris permet de déplacer un objet, la
	 * sélection ou la zone visible du plan de travail.
	 */
	public void appuiBoutonMilieu(int x, int y) {
		this.x_ancien = x;
		this.y_ancien = y;

		try {
			this.objet_sous_souris = this.editeur.getVueGraphe().en_dessous(x,
					y);

			if (this.editeur.selection.contient(this.objet_sous_souris)) {
				// Deplacement de la selection

				// on ne deplace pas les aretes, mais leurs sommets incidents.
				this.formeDessin_a_deplacer = Traitements
						.sommetsTotaux(this.editeur.selection.elements());

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
	 * L'appui du Bouton droit de la souris permet de selectionner un objet ou
	 * tous les objets d'une zone rectangulaire.
	 */
	public void appuiBoutonDroit(int x, int y) {
		// Remise a zero de la selection
		this.x_ancien = x;
		this.y_ancien = y;
		try {
			this.objet_sous_souris = this.editeur.getVueGraphe().en_dessous(x,
					y);

			// On vide la selection : c'est un nouvel undoGroup. Il est cree
			// dans la commande de vidage de la selection.
			if (!this.editeur.selection.estVide()) {
				this.editeur.setDescription("Reselect object(s)",
						"Deselect object(s)");
				this.editeur.commandeViderSelection(true);
			}

			// objet_sous_souris.enluminer(true);
			// ...et on selectionne l'objet : c'est un nouvel undoGroup.
			this.editeur.undoInfo.newGroup("Unselect object", "Select object");
			this.editeur.selection.insererElement(this.objet_sous_souris);
			this.editeur.undoInfo.addInfo(new SelectFormeDessin(
					this.editeur.selection, this.objet_sous_souris));
			this.repaint();
		} catch (NoSuchElementException e) {
			// On vide la selection, si elle ne l'est pas deja : nouvel
			// undoGroup. Il est cree dans la commande de vidage de la
			// selection.
			if (!this.editeur.selection.estVide()) {
				this.editeur.setDescription("Reselect object(s)",
						"Deselect object(s)");
				this.editeur.commandeViderSelection(true);
				this.repaint(0);
			}
			// Carre de selection
			this.carre_selection = true;
			this.selection_x = this.selection_x1 = this.selection_x2 = x;
			this.selection_y = this.selection_y1 = this.selection_y2 = y;
		}
		this.editeur.setUndo();
	}

	/**
	 * L'appui de Shift + bouton droit permet la sélection additive.
	 */
	public void appuiShiftBoutonDroit(int x, int y) {
		this.x_ancien = x;
		this.y_ancien = y;
		try {
			this.objet_sous_souris = this.editeur.getVueGraphe().en_dessous(x,
					y);
			if (this.editeur.selection.contient(this.objet_sous_souris)) {
				// Suppression d'un element de la selection : nouvel undoGroup.
				this.editeur.undoInfo.newGroup("Select all", "Deselect object");
				this.editeur.selection.supprimerElement(this.objet_sous_souris);
				this.editeur.undoInfo.addInfo(new DeselectFormeDessin(
						this.editeur.selection, this.objet_sous_souris));
				// objet_sous_souris.enluminer(false);
			} else {
				// Ajout d'un element a la selection : nouvel undoGroup.
				this.editeur.undoInfo.newGroup("Remove object from selection",
						"Add object to selection");
				this.editeur.selection.insererElement(this.objet_sous_souris);
				this.editeur.undoInfo.addInfo(new SelectFormeDessin(
						this.editeur.selection, this.objet_sous_souris));
				// objet_sous_souris.enluminer(true);
			}
			this.repaint();
		} catch (NoSuchElementException e) {
			// Carre de selection
			this.carre_selection = true;
			this.selection_x = this.selection_x1 = this.selection_x2 = x;
			this.selection_y = this.selection_y1 = this.selection_y2 = y;
		}
		this.editeur.setUndo();
	}

	/**
	 * L'appui du bouton gauche de la souris permet de créer un nouveau sommet
	 * (avec arete si on clique sur un sommet déjà existant).
	 */
	public void appuiBoutonGauche(int x, int y) {
		this.x_ancien = x;
		this.y_ancien = y;

		try {
			this.objet_sous_souris = this.editeur.getVueGraphe()
					.sommet_en_dessous(x, y);
		} catch (NoSuchElementException e) {
			// Creation d'un sommet : nouvel undoGroup.
			this.editeur.undoInfo.newGroup("Delete newly created vertex",
					"Create new vertex");
			this.objet_sous_souris = this.editeur.getVueGraphe().creerSommet(x,
					y);
			this.editeur.undoInfo.addInfo(new AjouteObjet(
					this.objet_sous_souris));
			// ((SommetDessin)objet_sous_souris).changerImage(new
			// ImageIcon((Image)vecteurImages.firstElement()));
			this.repaint();
		}
		this.editeur.setUndo();
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

		// Bouton droit ou (shift + bouton droit)
		case InputEvent.BUTTON3_MASK:
		case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
			this.glisseBoutonDroit(x, y);
			break;

		// Bouton gauche
		case InputEvent.BUTTON1_MASK:
		case 0:
			this.glisseBoutonGauche(x, y);
			break;

		default:
		}
	}

	public void mouseMoved(MouseEvent evt) {
	}

	/* Fin d'implementation de MouseMotionListener */

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
					.sommetsTotaux(this.editeur.getVueGraphe().listeAffichage());
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
				this.editeur.changerDimensionGraphe(this.size);
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
	 * Permet de creer un rectangle de selection au cas ou on maintient le
	 * bouton droit de la souris appuye.
	 */
	public void glisseBoutonDroit(int x, int y) {
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
			this.scrollRectToVisible(new Rectangle(Math.min(x,
					this.size.width - 20) - 10, Math.min(y,
					this.size.height - 20) - 10, 30, 30));
			this.repaint();
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
			if (this.drag_n_drop_sommet) {
				this.glisseSommet(x, y);
			} else {
				if (this.objet_sous_souris.appartient(x, y)) {
					return;
				} else {
					// creer un nouveau sommet et l'arete qui va avec : c'est un
					// nouvel undoGroup.
					this.ancien_sommet_sous_souris = (SommetDessin) this.objet_sous_souris;
					this.editeur.undoInfo.newGroup(
							"Delete newly created vertex and edge",
							"Create new vertex and edge");

					this.objet_sous_souris = this.editeur.getVueGraphe()
							.creerSommet(x, y);
					this.editeur.undoInfo.addInfo(new AjouteObjet(
							this.objet_sous_souris));

					// On sauvegarde l'objet UndoObject contenant ce
					// sommet : on peut en effet avoir a le supprimer
					// en cas de fusion avec un sommet existant.
					this.drag_n_dropVertex = this.objet_sous_souris;

					AreteDessin a = this.editeur.getVueGraphe().creerArete(
							this.ancien_sommet_sous_souris,
							(SommetDessin) this.objet_sous_souris);
					this.editeur.undoInfo.addInfo(new AjouteObjet(a));

					this.drag_n_drop_sommet = true;
					// Quand on drag un sommet, on veut un
					// positionnement precis par rapport au curseur,
					// on sauvegarde donc la position relative du
					// sommet par rapport au curseur, ici elle est
					// nulle.
					this.dx = this.dy = 0;
				}
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
				this.editeur.changerDimensionGraphe(this.size);
				this.setPreferredSize(this.size);
				this.revalidate();
			}
			this.repaint();
		}
		this.editeur.setUndo();
	}

	/**
	 * Implementation de KeyListener.
	 */
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		// Delete
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			this.editeur.commandeSupprimer();
			this.repaint();
			break;

		default:
		}
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

	/* Fin d'implementation de KeyListener */

	public Dimension getMinimumSize() {
		return new Dimension(20, 20);
	}

	// Methodes privees

	/**
	 * Méthode privée qui gère le déplacement d'un sommet avec effet
	 * d'aspiration par un autre sommet.
	 */
	private void glisseSommet(int x, int y) {
		// On regarde si notre sommet en recouvre un autre
		// le try leve une exception si il n'y a pas de recouverture
		try {

			SommetDessin sommet_en_dessous = this.editeur.getVueGraphe()
					.sommet_en_dessous(x, y, this.objet_sous_souris);

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

	/**
	 * Méthode de dessin du carré de sélection.
	 */
	private void dessinerCarre(Graphics g, int x1, int y1, int x2, int y2) {
		g.setColor(GrapheVisuPanel.RECT_SELECTION_COULEUR);
		g.drawRect(x1, y1, x2 - x1, y2 - y1);
	}
}

package visidia.gui.presentation.userInterfaceEdition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import visidia.graph.SimpleGraph;
import visidia.graph.Vertex;
import visidia.gui.DistributedAlgoSimulator;
import visidia.gui.donnees.TableImages;
import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.donnees.conteneurs.MultiEnsemble;
import visidia.gui.metier.Graphe;
import visidia.gui.metier.inputOutput.GMLParser;
import visidia.gui.metier.inputOutput.NewGraph;
import visidia.gui.metier.inputOutput.OpenGraph;
import visidia.gui.metier.inputOutput.SaveFile;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.boite.BoiteChangementNombreOperations;
import visidia.gui.presentation.boite.BoiteChangingEdgeShape;
import visidia.gui.presentation.boite.BoiteChangingVertexShape;
import visidia.gui.presentation.boite.BoiteChoix;
import visidia.gui.presentation.boite.BoiteFormeDessin;
import visidia.gui.presentation.boite.BoiteGraphe;
import visidia.gui.presentation.boite.BoiteSelection;
import visidia.gui.presentation.userInterfaceEdition.undo.AjouteObjet;
import visidia.gui.presentation.userInterfaceEdition.undo.DeselectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.SelectFormeDessin;
import visidia.gui.presentation.userInterfaceEdition.undo.SupprimeObjet;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

/**
 * La classe Editeur est particulièrement importante, elle implémente la fenêtre
 * (une "JFrame") dans laquelle les graphes sont édités (un graphe est un objet
 * de classe Graphe). C'est dans cette classe que sont implémentés les menus et
 * les méthodes de "haut niveau" appelées lors de l'execution d'une commande par
 * l'utilisateur. Par contre l'affichage "interactif" du graphe est assuré par
 * la classe GrapheVisuPanel.<BR>
 * Parmi les variables d'instance de cette classe, il faut bien distinguer 2
 * catégories distinctes: les variables qui decrivent l'état courant de
 * l'editeur (comme par exemple les objets selectionnes, les objets
 * visuablisables servant de modele a la creation de nouveaux sommets et de
 * nouvelle aretes, ...) et les variables decrivant l'etat du document édité
 * (parmi lesquelles on trouve biensur le VueGraphe, mais aussi la taille de la
 * zone de travail, la couleur de fond, ...). Seules les variables de la seconde
 * catégorie sont sauvegardées sur disque, les autres sont toujours initialisées
 * à des valeurs par defaut lors de l'ouverture d'un document.
 */

public class Editeur extends Fenetre implements ActionListener, WindowListener,
		ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6342392372397824641L;

	/***************************************************************************
	 * Objet <b>UndoInfo</b> qui permet de réaliser les opérations de "undo" et
	 * "redo".
	 **************************************************************************/
	protected UndoInfo undoInfo;

	/** Panel ou est dessiné le Graphe */
	protected GrapheVisuPanel grapheVisuPanel;

	private FenetreDeSimulation fenetreDeSimulation;

	// * * * ************************************************************

	/**
	 * Number of operations for annulation or restauration annulees with
	 * undo/redo by group.
	 */
	protected int nb_op = 5;

	/** La description de l'annulation d'une opération effectuée. */
	protected String undoDescription = "";

	/** La description de la restauration d'une operation effectuée. */
	protected String redoDescription = "";

	protected JMenuBar menuBar;

	protected JMenu file, edition, transformation, option;

	protected JMenuItem file_new, file_open, file_save, file_save_as,
			file_gml_export, file_gml_import;

	protected JMenuItem file_refresh, file_help, file_close, file_quit;

	protected JMenuItem transformation_complete, transformation_cut_edges,
			transformation_renumeroter;

	protected JMenuItem transformation_change_shape_edges,
			transformation_change_shape_vertices;

	protected JMenuItem edition_select_all, edition_duplication, edition_cut,
			edition_properties;

	protected JMenuItem edition_undo, edition_redo, edition_undoBySet,
			edition_redoBySet, edition_setNbOp;

	protected JToolBar toolBar;

	protected JButton but_new, but_open, but_save, but_duplicate, but_info,
			but_help, but_undo, but_redo, but_simulation,
			but_agents_simulation, but_simulation_distribue;

	protected JCheckBoxMenuItem option_labels;

	protected JMenuItem option_dictionnaire;

	JFileChooser gmlFileChooser = null;

	/**
	 * Cree une nouvelle fenetre d'edition contenant un document vierge: un
	 * Graphe vide et des caracteristiques (taille de la zone de travail,
	 * couleur de fond, ...) ayant des valeurs par defaut.
	 */
	public Editeur() {
		this(new Graphe(), Fenetre.COULEUR_FOND_PAR_DEFAUT,
				Fenetre.DIM_X_PAR_DEFAUT, Fenetre.DIM_Y_PAR_DEFAUT);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(Fenetre.DIM_X_PAR_DEFAUT, Fenetre.DIM_Y_PAR_DEFAUT);
		this.pack();
		this.setVisible(true);

	}

	/**
	 * Un constructeur dont les arguments permettent de spécifier completement
	 * le document edité. Des données telles que le modèle de sommet (ou
	 * d'arête), le fichier édité, les objets selectionnés,... ne font pas
	 * partie des arguments car il ne s'agit pas de données propres au document
	 * mais des données définissant l'état courant de l'éditeur.
	 */
	protected Editeur(Graphe graphe_edite, Color couleur_fond, int dim_x,
			int dim_y) {

		super();

		// Le manager de composants
		this.content = new JPanel();
		this.content.setLayout(new BorderLayout());

		this.fichier_edite = null;
		this.mettreAJourTitreFenetre();

		// La barre de menus
		this.addMenu();
		// Donnees courantes de l'édition

		// Couleur d'arrière plan du GrapheVisuPanel
		this.couleur_de_fond = couleur_fond;

		// Le graphe édite et l'objet sélection qui contient les
		// objets sélectionnes graphe = graphe_edite;
		this.vueGraphe = graphe_edite.getVueGraphe();
		this.selection = new SelectionDessin();

		// Le panel ou est dessine le graphe
		this.grapheVisuPanel = this.creerGrapheVisuPanel();

		// pour le undo :
		this.undoInfo = new UndoInfo();

		super.setSize(dim_x, dim_y);
		// un setSize est a faire avant l'ajout de composants pour éviter
		// les warnings
		this.scroller = new JScrollPane(this.grapheVisuPanel);
		this.scroller.setPreferredSize(new Dimension(dim_x, dim_y));
		this.scroller.setOpaque(true);
		this.content.add(this.scroller, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Editeur.this.commandeClose();
			}
		});
		// La barre d'outils
		this.addToolBar();

		// On disable les items non-valide pour une applet
		if (!DistributedAlgoSimulator.estStandalone()) {
			this.disableButtonForApplet();
		}

		this.setContentPane(this.content);
	}

	public VueGraphe getGraphClone() {
		return this.vueGraphe.cloner();
	}

	/**
	 * Cette méthode ajoute la barre de menu, ses menus et sous_menus a
	 * l'éditeur.
	 */
	protected void addMenu() {

		this.menuBar = new JMenuBar();
		this.menuBar.setOpaque(true);
		this.menuBar.setPreferredSize(new Dimension(Fenetre.DIM_X_PAR_DEFAUT,
				20));

		// Build the menu File
		this.file = new JMenu("File");
		this.file.getPopupMenu().setName("PopFile");
		this.file.setMnemonic('F');
		this.file_new = new JMenuItem("New", KeyEvent.VK_N);
		this.file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		this.file_new.addActionListener(this);
		this.file.add(this.file_new);
		this.file_open = new JMenuItem("Open...", KeyEvent.VK_O);
		this.file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		this.file_open.addActionListener(this);
		this.file.add(this.file_open);
		this.file_save = new JMenuItem("Save", KeyEvent.VK_S);
		this.file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		this.file_save.addActionListener(this);
		this.file.add(this.file_save);
		this.file_save_as = new JMenuItem("Save as...");
		this.file_save_as.addActionListener(this);
		this.file.add(this.file_save_as);

		this.file.addSeparator();
		this.file_gml_import = new JMenuItem("Import from GML");
		this.file_gml_import.addActionListener(this);
		this.file.add(this.file_gml_import);
		this.file_gml_export = new JMenuItem("Export in GML");
		this.file_gml_export.addActionListener(this);
		this.file.add(this.file_gml_export);

		this.file.addSeparator();
		this.file_refresh = new JMenuItem("Refresh", KeyEvent.VK_E);
		this.file_refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.CTRL_MASK));
		this.file_refresh.addActionListener(this);
		this.file.add(this.file_refresh);
		this.file_help = new JMenuItem("Help", KeyEvent.VK_H);
		this.file_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		this.file_help.addActionListener(this);
		this.file.add(this.file_help);
		this.file.addSeparator();
		this.file_close = new JMenuItem("Close", KeyEvent.VK_C);
		this.file_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		this.file_close.addActionListener(this);
		this.file.add(this.file_close);
		this.file_quit = new JMenuItem("Quit", KeyEvent.VK_Q);
		this.file_quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		this.file_quit.addActionListener(this);
		this.file.add(this.file_quit);
		this.file.addActionListener(this);
		this.menuBar.add(this.file);

		// Build the menu Edition.
		this.edition = new JMenu("Edition");
		this.edition.getPopupMenu().setName("PopEdition");
		this.edition.setMnemonic('E');
		this.edition_select_all = new JMenuItem("Select all", KeyEvent.VK_A);
		this.edition_select_all.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		this.edition_select_all.addActionListener(this);
		this.edition.add(this.edition_select_all);

		this.edition_duplication = new JMenuItem("Duplicate", KeyEvent.VK_D);
		this.edition_duplication.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		this.edition_duplication.addActionListener(this);
		this.edition.add(this.edition_duplication);
		this.edition_cut = new JMenuItem("Delete");
		this.edition_cut.addActionListener(this);
		this.edition.add(this.edition_cut);

		this.edition.addSeparator();
		this.edition_properties = new JMenuItem("Properties", KeyEvent.VK_P);
		this.edition_properties.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		this.edition_properties.addActionListener(this);
		this.edition.add(this.edition_properties);

		this.edition.addActionListener(this);
		this.edition.addSeparator();
		this.edition_undo = new JMenuItem("Undo", KeyEvent.VK_U);
		this.edition_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.CTRL_MASK));
		this.edition_undo.addActionListener(this);
		this.edition_undo.setEnabled(false);
		this.edition.add(this.edition_undo);

		this.edition_redo = new JMenuItem("Redo", KeyEvent.VK_R);
		this.edition_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		this.edition_redo.addActionListener(this);
		this.edition_redo.setEnabled(false);
		this.edition.add(this.edition_redo);

		this.edition_undoBySet = new JMenuItem("Undo last " + this.nb_op);
		this.edition_undoBySet.addActionListener(this);
		this.edition_undoBySet.setEnabled(false);
		this.edition.add(this.edition_undoBySet);

		this.edition_redoBySet = new JMenuItem("Redo last " + this.nb_op);
		this.edition_redoBySet.addActionListener(this);
		this.edition_redoBySet.setEnabled(false);
		this.edition.add(this.edition_redoBySet);

		this.edition_setNbOp = new JMenuItem("Number of undo/redo");
		this.edition_setNbOp.addActionListener(this);
		this.edition.add(this.edition_setNbOp);

		this.menuBar.add(this.edition);

		// Build the menu Transformation.
		this.transformation = new JMenu("Transformation");
		this.transformation.getPopupMenu().setName("PopTransformation");
		this.transformation.setMnemonic('T');

		this.transformation_complete = new JMenuItem("Complete");
		this.transformation_complete.addActionListener(this);
		this.transformation.add(this.transformation_complete);

		this.transformation_cut_edges = new JMenuItem("Delete edges");
		this.transformation_cut_edges.addActionListener(this);
		this.transformation.add(this.transformation_cut_edges);

		this.transformation.addSeparator();

		this.transformation_change_shape_edges = new JMenuItem(
				"Change edge shape");
		this.transformation_change_shape_edges.addActionListener(this);
		this.transformation.add(this.transformation_change_shape_edges);

		this.transformation_change_shape_vertices = new JMenuItem(
				"Change vertex shape");
		this.transformation_change_shape_vertices.addActionListener(this);
		this.transformation.add(this.transformation_change_shape_vertices);
		this.transformation.addSeparator();

		this.transformation_renumeroter = new JMenuItem("vertex renumbering");
		this.transformation_renumeroter.addActionListener(this);
		this.transformation.add(this.transformation_renumeroter);

		this.transformation.addActionListener(this);

		this.menuBar.add(this.transformation);

		// Build the menu Options.
		this.option = new JMenu("Options");
		this.option.getPopupMenu().setName("PopOption");
		this.option.setMnemonic('O');
		this.option_labels = new JCheckBoxMenuItem("Labels");
		this.option_labels.setState(true);
		this.option_labels.addItemListener(this);
		this.option.add(this.option_labels);
		this.option.addSeparator();
		this.option_dictionnaire = new JMenuItem("Dictionnaire");
		this.option_dictionnaire.addActionListener(this);
		this.option.add(this.option_dictionnaire);

		this.option.addActionListener(this);
		this.menuBar.add(this.option);
		this.setJMenuBar(this.menuBar);

	}

	/**
	 * Cette methode ajoute la barre d'outils et ses boutons a l'editeur.
	 */
	protected void addToolBar() {

		this.toolBar = new JToolBar();
		this.toolBar.setBackground(new Color(120, 120, 120));
		this.toolBar.setOpaque(true);
		this.toolBar.setPreferredSize(new Dimension(Fenetre.DIM_X_PAR_DEFAUT,
				40));

		// Build buttons on the tool bar
		this.but_new = new JButton(new ImageIcon(TableImages.getImage("new")));
		this.but_new.setToolTipText("New");
		this.but_new.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_new.addActionListener(this);
		this.toolBar.add(this.but_new);

		this.but_open = new JButton(new ImageIcon(TableImages.getImage("open")));
		this.but_open.setToolTipText("Open");
		this.but_open.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_open.addActionListener(this);
		this.toolBar.add(this.but_open);

		this.but_save = new JButton(new ImageIcon(TableImages.getImage("disk")));
		this.but_save.setToolTipText("Save");
		this.but_save.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_save.addActionListener(this);
		this.toolBar.add(this.but_save);

		this.toolBar.addSeparator();

		this.but_duplicate = new JButton(new ImageIcon(TableImages
				.getImage("duplicate")));
		this.but_duplicate.setToolTipText("Duplicate");
		this.but_duplicate.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_duplicate.addActionListener(this);
		this.toolBar.add(this.but_duplicate);

		this.but_undo = new JButton(new ImageIcon(TableImages.getImage("undo")));// "visidia/gui/donnees/images/undo.gif"));
		this.but_undo.setToolTipText("Undo");
		this.but_undo.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_undo.addActionListener(this);
		this.but_undo.setEnabled(false);
		this.toolBar.add(this.but_undo);

		this.but_redo = new JButton(new ImageIcon(TableImages.getImage("redo")));// "visidia/gui/donnees/images/redo.gif"));
		this.but_redo.setToolTipText("Redo");
		this.but_redo.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_redo.addActionListener(this);
		this.but_redo.setEnabled(false);
		this.toolBar.add(this.but_redo);

		this.but_info = new JButton(new ImageIcon(TableImages.getImage("info")));// "visidia/gui/donnees/images/info.gif"));
		this.but_info.setToolTipText("Properties");
		this.but_info.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_info.addActionListener(this);
		this.toolBar.add(this.but_info);

		this.toolBar.addSeparator();

		this.but_help = new JButton(new ImageIcon(TableImages.getImage("help")));// "visidia/gui/donnees/images/aide.gif"));
		this.but_help.setToolTipText("Help");
		this.but_help.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_help.addActionListener(this);
		this.toolBar.add(this.but_help);

		this.toolBar.addSeparator();

		this.but_simulation = new JButton("simulation");
		this.but_simulation.setToolTipText("simulation");
		this.but_simulation.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_simulation.addActionListener(this);
		this.toolBar.add(this.but_simulation);

		this.toolBar.addSeparator();

		this.but_agents_simulation = new JButton("Agent Simulation");
		this.but_agents_simulation.setToolTipText("Agent Simulation");
		this.but_agents_simulation.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_agents_simulation.addActionListener(this);
		this.toolBar.add(this.but_agents_simulation);

		this.toolBar.addSeparator();

		this.but_simulation_distribue = new JButton("Network Simulation");
		this.but_simulation_distribue.setToolTipText("Network Simualtion");
		this.but_simulation_distribue.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.but_simulation_distribue.addActionListener(this);
		this.toolBar.add(this.but_simulation_distribue);

		this.content.add(this.toolBar, BorderLayout.NORTH);

	}

	// disable the button not used for the applet
	private void disableButtonForApplet() {
		this.file_open.setEnabled(false);
		this.file_save.setEnabled(false);
		this.file_gml_export.setEnabled(false);
		this.file_gml_import.setEnabled(false);
		this.file_save_as.setEnabled(false);
		this.file_quit.setEnabled(false);
		this.file_new.setEnabled(false);
		this.transformation_change_shape_edges.setEnabled(false);
		this.transformation_change_shape_vertices.setEnabled(false);
		this.but_open.setEnabled(false);
		this.but_save.setEnabled(false);
		this.but_new.setEnabled(false);
	}

	/** Cree un GrapheVisuPanel rattache a l'editeur courant. * */
	public GrapheVisuPanel creerGrapheVisuPanel() {
		return new GrapheVisuPanel(this);
	}

	/** Retourne le GrapheVisuPanel servant a l'edition.* */
	public GrapheVisuPanel grapheVisuPanel() {
		return this.grapheVisuPanel;
	}

	public Graphe graph() {
		return super.getVueGraphe().getGraphe();
	}

	public void remplaceSelection(SelectionDessin nouvelle_selection) {
		// On vide et on supprime la selection initiale et on la remplace par la
		// nouvelle
		// commandeViderSelection(true);
		this.selection = nouvelle_selection;
		// selection.select();
		// vueGraphe.enluminerFormeDessin(selection.elements());
	}

	/**
	 * Cette méthode retourne une chaine qui est utilisée pour construire le
	 * titre de la fenêtre et pour savoir dans quel type d'editeur le graphe est
	 * édite.
	 */
	protected String titre() {
		return "Graph Editor";
	}

	public String type() {
		return "Editor";
	}

	// Implementation des Listener

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
		this.grapheVisuPanel.repaint();
		this.content.repaint();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Implémentation de l'interface ActionListener traitement de l'appui sur
	 * les menus déroulants et les boutons de la barre d'outils.
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JMenuItem) {
			this.action_menu((JMenuItem) evt.getSource());
		} else if (evt.getSource() instanceof JButton) {
			this.action_toolbar((JButton) evt.getSource());
		}
	}

	/**
	 * Méthode de redirection du traitement suivant le menu dans lequel se
	 * trouve la fonctionnalité selectionnée.
	 */
	public void action_menu(JMenuItem mi) {
		String le_menu = ((JPopupMenu) mi.getParent()).getName();

		if (le_menu == "PopFile") {
			this.menuFile(mi);
		} else if (le_menu == "PopEdition") {
			this.menuEdition(mi);
		} else if (le_menu == "PopTransformation") {
			this.menuTransformation(mi);
		} else if (le_menu == "PopOption") {
			this.menuOption(mi);
		}
	}

	/**
	 * Méthode de redirection du traitement suivant le bouton selectionné dans
	 * la barre d'outils.
	 */

	public void action_toolbar(JButton b) {
		if (b == this.but_new) {
			NewGraph.newGraphe(this);
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_duplicate) {

			this.commandeDupliquer();
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_undo) {
			this.undoInfo.undo();
			this.setUndo();
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_redo) {
			this.undoInfo.redo();
			this.setUndo();
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_simulation) {
			this.commandeRenumeroter();
			this.creerFenetreSimulation();
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_agents_simulation) {
			this.commandeRenumeroter();
			this.createAgentsSimulationWindow();
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_simulation_distribue) {
			this.commandeRenumeroter();
			this.creerFenetreSimulationDist();
			this.grapheVisuPanel.repaint();
		}

		else if (b == this.but_info) {
			this.commandeProprietes();
		} else if (b == this.but_help) {
			JOptionPane
					.showMessageDialog(
							this,
							"DistributedAlgoSimulator, v2\n"
									+ "To create a new vertex:\n"
									+ "    Left mouse button\n"
									+ " \n"
									+ "To create a new edge:\n"
									+ "    Drag'n Drop with left mouse button\n"
									+ " \n"
									+ "To move a vertex, an edge or the selection:\n"
									+ "    Drag'nDrop with middle mouse button\n"
									+ " \n"
									+ "To move the visible part of the graph:\n"
									+ "    Drag'nDrop with the middle mouse button on\n"
									+ "    the desk area or use scrollbars\n"
									+ " \n"
									+ "To select objects in the graph:\n"
									+ "    Right mouse button\n"

					);
		} else if (b == this.but_open) {

			OpenGraph.open(this);
			this.grapheVisuPanel.setPreferredSize(this.vueGraphe
					.donnerDimension());
			this.grapheVisuPanel.revalidate();

			this.grapheVisuPanel.scrollRectToVisible(new Rectangle(
					Fenetre.DIM_X_PAR_DEFAUT, Fenetre.DIM_Y_PAR_DEFAUT, 0, 0));
			this.grapheVisuPanel.repaint();
		} else if (b == this.but_save) {
			SaveFile.save(this, this.vueGraphe.getGraphe());
		}
	}

	/** Méthode de traitement des fonctionnalités du menu File. * */

	public void menuFile(JMenuItem mi) {
		if (mi == this.file_open) {
			OpenGraph.open(this);
			this.grapheVisuPanel.setPreferredSize(this.vueGraphe
					.donnerDimension());
			this.grapheVisuPanel.revalidate();
			this.grapheVisuPanel.scrollRectToVisible(new Rectangle(
					Fenetre.DIM_X_PAR_DEFAUT, Fenetre.DIM_Y_PAR_DEFAUT, 0, 0));
			this.grapheVisuPanel.repaint();

		} else if (mi == this.file_new) {
			NewGraph.newGraphe(this);
			this.grapheVisuPanel.repaint();
		} else if (mi == this.file_save) {
			SaveFile.save(this, this.vueGraphe.getGraphe());
		} else if (mi == this.file_save_as) {
			this.fichier_edite = null;
			SaveFile.saveAs(this, this.vueGraphe.getGraphe());
		} else if (mi == this.file_gml_export) {
			GMLParser.export(this, this.vueGraphe.getGraphe());
		} else if (mi == this.file_gml_import) {
			this.importGMLGraph();
		} else if (mi == this.file_refresh) {
			this.grapheVisuPanel.repaint();
		} else if (mi == this.file_help) {
			JOptionPane
					.showMessageDialog(
							this,
							"DistributedAlgoSimulator, v2\n"
									+ "A graph editor in Java for \n"
									+ "random walk simulation on a graph\n"
									+ "To create a new vertex:\n"
									+ "    Left mouse button\n"
									+ " \n"
									+ "To create a new edge:\n"
									+ "    Drag'nDrop with left mouse button\n"
									+ " \n"
									+ "To move a vertex, an edge or the selection:\n"
									+ "    Drag'nDrop with middle mouse button\n"
									+ " \n"
									+ "To move the visible part of the graph:\n"
									+ "    Drag'nDrop with the middle mouse button on\n"
									+ "    the desk area or use scrollbars\n"
									+ " \n"
									+ "To select objects in the graph:\n"
									+ "    Right mouse button\n"
									+ " \n"
									+ "Attention:\n"
									+ "* Items 'Properties...' and 'Simulate...' of menus\n"
									+ "  'Edition' and 'Simulate' doesn't work on ctwm\n"
									+ "* Input/Output (ie save,open, ...)\n"
									+ "  are not available if the editor works as an\n"
									+ "  Applet: to use it, the programme must be use in\n"
									+ "  standalone !!\n"
									+ "* select a vertex before using\n"
									+ "  random walk simulation\n" + " \n");
		} else if (mi == this.file_close) {
			this.commandeClose();
		} else if (mi == this.file_quit) {
			System.exit(0);
		}
	}

	/** Methode de traitement des fonctionnalites du menu Edition. * */
	public void menuEdition(JMenuItem mi) {
		if (mi == this.edition_properties) {
			this.commandeProprietes();
		} else if (mi == this.edition_duplication) {
			this.commandeDupliquer();
		} else if (mi == this.edition_cut) {
			this.commandeSupprimer();
		} else if (mi == this.edition_select_all) {
			this.commandeToutSelectionner();
		} else if (mi == this.edition_undo) {
			this.undoInfo.undo();
			this.setUndo();
		} else if (mi == this.edition_redo) {
			this.undoInfo.redo();
			this.setUndo();
		} else if (mi == this.edition_undoBySet) {
			this.undoInfo.undo(this.nb_op);
			this.setUndo();
		} else if (mi == this.edition_redoBySet) {
			this.undoInfo.redo(this.nb_op);
			this.setUndo();
		} else if (mi == this.edition_setNbOp) {
			this.commandeChangerNombreOperations();
		}
		this.grapheVisuPanel.repaint();
	}

	/** Methode de traitement des fonctionnalites du menu Transformation. * */
	public void menuTransformation(JMenuItem mi) {
		if (mi == this.transformation_complete) {
			this.commandeCompleter();
		} else if (mi == this.transformation_cut_edges) {
			this.commandeSupprimerAretes();
		} else if (mi == this.transformation_renumeroter) {
			this.commandeRenumeroter();
		} else if (mi == this.transformation_change_shape_edges) {
			this.deSelect(this.selection);
			BoiteChangingEdgeShape changeBox = new BoiteChangingEdgeShape(this,
					this.vueGraphe);
			changeBox.show();
		}

		else if (mi == this.transformation_change_shape_vertices) {
			this.deSelect(this.selection);
			BoiteChangingVertexShape changeBox = new BoiteChangingVertexShape(
					this, this.vueGraphe);
			changeBox.show();
		} else {
			return;
		}

		this.grapheVisuPanel.repaint();
	}

	/** Methode de traitement des fonctionnalites du menu Options. * */
	public void menuOption(JMenuItem mi) {
		if (mi == this.option_labels) {
			this.vueGraphe.afficherEtiquettes(this.option_labels.getState());
		} else if (mi == this.option_dictionnaire) {

			BoiteChoix boiteChoix = new BoiteChoix(this,
					"tableau de choix des couleurs");
			boiteChoix.show(this);
		}
		this.grapheVisuPanel.repaint();
	}

	// Les commandes
	// Remarque: les commandes ne font theoriquement
	// pas elles-meme les repaint() necessaires

	/** Commande de suppression des éléments de la sélection courante. * */
	public void commandeSupprimer() { // Penser au repaint()

		// Suppression des elements de la selection
		if (!this.selection.estVide()) {
			this.undoInfo.newGroup("Recreate deleted selection",
					"Delete selection");
			// on cree l'enumeration avec tous les elements a supprimer
			Enumeration e = Traitements.elementsTotaux(
					this.selection.elements()).elements();
			while (e.hasMoreElements()) {
				FormeDessin s = (FormeDessin) e.nextElement();
				this.undoInfo.addInfo(new SupprimeObjet(s));
				s.delete();
				this.undoInfo
						.addInfo(new DeselectFormeDessin(this.selection, s));
			}
			this.selection.deSelect();
		}
		this.setUndo();
	}

	/** Commande de fermeture de l'éditeur courant. * */
	public void commandeClose() {
		this.setVisible(false);
		this.dispose();

		// On en profite pour collecter les ordures
		// que l'on vient de creer
		Runtime.getRuntime().gc();
	}

	/**
	 * Commande d'affichage des propriétés :
	 * 
	 * du ou des éléments de la sélection du graphe tout entier, si la sélection
	 * est vide.
	 * 
	 */

	public void commandeProprietes() {
		if (this.selection.estVide()) {
			BoiteGraphe.show(this);
		} else {
			int taille_selection = this.selection.nbElements();
			Enumeration e = this.selection.elements();

			if (taille_selection == 1) {
				Object selected = e.nextElement();
				// pour les boites de dialogue
				// undoInfo.undoAndRemove();
				BoiteFormeDessin boite_de_proprietes = ((FormeDessin) selected)
						.proprietes(this);
				boite_de_proprietes.show(this);
				this.setUndo();

			} else {
				MultiEnsemble table_des_types = new MultiEnsemble();
				while (e.hasMoreElements()) {
					table_des_types.inserer(((FormeDessin) e.nextElement())
							.type());
				}

				BoiteSelection.show(this, taille_selection, table_des_types);
			}
		}
	}

	/**
	 * Commande de rv_enumerotation des sommets du graphe.
	 */
	public void commandeRenumeroter()// Penser au repaint()
	{
		this.vueGraphe.renumeroterSommets();
	}

	/**
	 * Commande qui permet de rendre complet :
	 * 
	 * le graphe tout entier si la selection est vide; la partie du graphe
	 * selectionnée sinon.
	 * 
	 */

	public void commandeCompleter() { // Penser au repaint()
		Enumeration e;
		if (this.selection.estVide()) {
			this.undoInfo
					.newGroup("Restore uncomplete graph", "Complete graph");
			e = Traitements
					.completersousGraphe(this.vueGraphe.listeAffichage());
		} else {
			this.undoInfo.newGroup("Restore uncomplete selection",
					"Complete selection");
			e = Traitements.completersousGraphe(this.selection.elements());
		}
		while (e.hasMoreElements()) {
			this.undoInfo
					.addInfo(new AjouteObjet((FormeDessin) e.nextElement()));
		}
		this.setUndo();
	}

	/**
	 * Commande de suppression :
	 * 
	 * <ul>
	 * <li>des arêtes contenues dans la selection; </li>
	 * <li>de toutes les arêtes du graphe si la selection est vide.</li>
	 * </ul>
	 */
	public void commandeSupprimerAretes() { // Penser au repaint()
		if (this.selection.estVide()) {
			/* On va utiliser une pile intermédiaire pour pouvoir supprimer */
			/* les éléments dans l'ordre (sinon, on ne peut pas : la méthode */
			/* nextElement plante!! */
			Enumeration e = this.vueGraphe.listeAffichage();
			Stack<AreteDessin> pileTmp = new Stack<AreteDessin>();
			while (e.hasMoreElements()) {
				FormeDessin f = (FormeDessin) e.nextElement();
				if (f.type().equals("edge")) {
					pileTmp.push((AreteDessin) f);
				}
			}
			if (!pileTmp.isEmpty()) {
				this.undoInfo.newGroup("Restore edges in the graph",
						"Delete edges from graph");
				while (!pileTmp.isEmpty()) {
					AreteDessin arete = (AreteDessin) pileTmp.pop();
					this.undoInfo.addInfo(new SupprimeObjet(arete));
					arete.delete();
				}
			}
		} else {
			Enumeration e = this.selection.elements();
			Stack<AreteDessin> pileTmp = new Stack<AreteDessin>();
			while (e.hasMoreElements()) {
				FormeDessin f = (FormeDessin) e.nextElement();
				if (f.type().equals("edge")) {
					pileTmp.push((AreteDessin) f);
				}
			}
			if (!pileTmp.isEmpty()) {
				this.undoInfo.newGroup("Restore edges in the graph",
						"Delete edges from graph");
				while (!pileTmp.isEmpty()) {
					AreteDessin arete = (AreteDessin) pileTmp.pop();
					this.undoInfo.addInfo(new SupprimeObjet(arete));
					arete.delete();
				}
			}
		}

		this.setUndo();
	}

	/**
	 * Commande qui permet de vider la sélection courante.
	 */
	public void commandeViderSelection(boolean deselect) { // Penser au
		// repaint()
		if (!this.selection.estVide()) {
			if (deselect) {
				this.deSelect(this.selection); // deselection with creating
				// undo group
			}
		}
		this.setUndo();
	}

	/**
	 * Commande de sélection de la totalité du graphe édité.
	 */
	public void commandeToutSelectionner() { // Penser au repaint()
		int i = 0;
		Enumeration e = this.vueGraphe.listeAffichage();
		if (e.hasMoreElements()) {
			this.undoInfo.newGroup("Deselect all", "Select all");
			while (e.hasMoreElements()) {
				FormeDessin objetVisu = (FormeDessin) e.nextElement();
				this.selection.insererElement(objetVisu);
				this.undoInfo.addInfo(new SelectFormeDessin(this.selection,
						objetVisu));
				// objetVisu.enluminer(true);
				i++;
			}
		}
		this.setUndo();
	}

	/**
	 * Commande de duplication :
	 * 
	 * <ul>
	 * <li>des élémants contenus dans la sélection; </li>
	 * <li>du graphe tout entier si la selection est vide. </li>
	 * </ul>
	 * Les duplicata deviennent les nouveaux objets selectionnes.
	 */
	public void commandeDupliquer() {
		// On duplique tous les éléments de la sélection, et les duplicata
		// deviennent les nouveaux objets selectionnés
		this.redoDescription = "Duplicate";
		this.undoDescription = "Cancel duplication";
		if (!this.selection.estVide()) {
			Traitements.dupliquerSelectionDessin(this.selection);

			// on sélectionne la copie
			this.select(this.selection);

			// On deplace les clones
			Ensemble objetVisu_a_deplacer = Traitements
					.sommetsTotaux(this.selection.elements());
			// objetVisu_a_deplacer.inserer(selection.autresObjetsVisu());
			VueGraphe.deplacerFormeDessin(objetVisu_a_deplacer.elements(), 30,
					30);

			this.undoInfo.newGroup("Cancel duplication", "Duplicate");
			Enumeration e = this.selection.elements();
			while (e.hasMoreElements()) {
				this.undoInfo.addInfo(new AjouteObjet((FormeDessin) e
						.nextElement()));
			}
			this.setUndo();
		}
	}

	/**
	 * Commande de sélection du sous-graphe connexe contenant les éléments de la
	 * sélection courante.
	 */
	public void commandesousGrapheConnexe() {
		Enumeration sel = this.selection.elements();
		if (sel.hasMoreElements()) {
			this.undoDescription = "Unselect connected subgraph";
			this.redoDescription = "Select connected subgraph";
			this.selection = Traitements.sousGrapheConnexe(sel);
			this.select(this.selection);
		} else {
			this.undoDescription = "Unselect connected subgraph";
			this.redoDescription = "Select connected subgraph";
			this.commandeViderSelection(true);
		}
	}

	/**
	 * Commande de sélection du sous-graphe connexe orienté contenant les
	 * éléments de la sélection courante.
	 */
	public void commandesousGrapheConnexeOriente() {
		Enumeration les_sommets = Traitements.areteDessin(this.selection
				.elements());
		if (les_sommets.hasMoreElements()) {
			this.redoDescription = "Select connected oriented subgraph";
			this.undoDescription = "Unselect connected oriented subgraph";
			this.selection = Traitements.sousGrapheConnexeOriente(les_sommets);
			this.select(this.selection);
		} else {
			this.redoDescription = "Select connected oriented subgraph";
			this.undoDescription = "Unselect connected oriented subgraph";
			this.commandeViderSelection(true);
		}
	}

	/**
	 * Commande d'ouverture d'une boite de dialogue permettant d'ouvrir un
	 * nouvel editeur du type desire.
	 */

	/**
	 * Commande de changement du nombre d'opérations à annuler ou à restaurer
	 * grace aux commandes de undo/redo par groupes.
	 */
	public void commandeChangerNombreOperations() {
		// BoiteChangementNombreOperations b =
		new BoiteChangementNombreOperations(this, this.nb_op);
	}

	/**
	 * Cette méthode met <b>nb_op</b> à la valeur "i" et réactualise le texte
	 * des éléments de menus <b>edition_undoBySet</b> et d'édition_redoBySet</b>
	 */
	public void setNbOp(int i) {
		this.nb_op = i;
		this.edition_undoBySet.setText("Undo last " + i);
		this.edition_redoBySet.setText("Redo last " + i);
	}

	/**
	 * Commande de remise à jour du dessin du graphe.
	 */
	public void rafraichir() {
		this.grapheVisuPanel.repaint();
	}

	/**
	 * Commande de modification de la couleur de fond de l'éditeur.
	 */
	public void change_couleur_de_fond(Color new_color) {
		this.couleur_de_fond = new_color;
		this.grapheVisuPanel.setBackground(this.couleur_de_fond);
	}

	/**
	 * Renvoie la valeur de la largeur courante de la zone de dessin.
	 */
	public int drawableWidth() {
		return this.grapheVisuPanel.getPreferredSize().width;
	}

	/**
	 * Renvoie la valeur de la hauteur courante de la zone de dessin.
	 */
	public int drawableHeight() {
		return this.grapheVisuPanel.getPreferredSize().height;
	}

	/**
	 * Renvoie le nom de la classe de l'éditeur.
	 */
	public String className() {
		return new String("editeur.Editeur");
	}

	/**
	 * Commande de reinitialisation de l'éditeur. <BR>
	 * Appelée avant l'ouverture d'un fichier si celui-ci contient un graphe du
	 * même type que l'éditeur courant.
	 */
	public void initialize() {
		Graphe graphe = new Graphe();
		this.vueGraphe = graphe.getVueGraphe();
		this.but_undo.setEnabled(false);
		this.but_redo.setEnabled(false);
		this.selection = new SelectionDessin();
	}

	/**
	 * Permet de donner a <b>description</b> la valeur passée en argument.
	 */
	public void setDescription(String newUndoDesc, String newRedoDesc) {
		this.undoDescription = newUndoDesc;
		this.redoDescription = newRedoDesc;
	}

	/**
	 * Permet d'activer ou de désactiver les boutons "Undo" et "Redo" suivant
	 * l'état des "piles" d'actions undo et redo.
	 */
	public void setUndo() {
		// Si le groupe de undo cree est vide, on le supprime!
		this.undoInfo.removeEmptyGroup();
		this.but_undo.setEnabled(this.undoInfo.undoMore());
		this.but_undo.setToolTipText(this.undoInfo.undoDescription());
		this.edition_undo.setEnabled(this.undoInfo.undoMore());
		this.edition_undoBySet.setEnabled(this.undoInfo.undoMore());
		this.but_redo.setEnabled(this.undoInfo.redoMore());
		this.but_redo.setToolTipText(this.undoInfo.redoDescription());
		this.edition_redo.setEnabled(this.undoInfo.redoMore());
		this.edition_redoBySet.setEnabled(this.undoInfo.redoMore());
	}

	public void changerPanel(GrapheVisuPanel unGrapheVisuPanel) {
		this.content.remove(this.scroller);
		this.grapheVisuPanel = unGrapheVisuPanel;
		this.scroller = new JScrollPane(this.grapheVisuPanel);
		this.scroller.setPreferredSize(new Dimension(Fenetre.DIM_X_PAR_DEFAUT,
				Fenetre.DIM_Y_PAR_DEFAUT));
		this.scroller.setOpaque(true);
		this.content.add(this.scroller, BorderLayout.CENTER);
	}

	public void changerVueGraphe(VueGraphe graphe) {
		this.vueGraphe = graphe;
		this.changerPanel(new GrapheVisuPanel(this));
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() instanceof JCheckBoxMenuItem) {
			this.menuOption((JMenuItem) e.getSource());
		}
	}

	public void creerFenetreSimulation() {
		try {
			this.selection.deSelect();
			this.fenetreDeSimulation = new FenetreDeSimulation(this.vueGraphe
					.cloner(), this.fichier_edite, this);
			this.fenetreDeSimulation.setVisible(true);

		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
			excpt.printStackTrace();
		}

	}

	public void createAgentsSimulationWindow() {
		try {
			this.selection.deSelect();
			AgentsSimulationWindow agentsSimulationWindow = new AgentsSimulationWindow(
					this.vueGraphe.cloner(), this.fichier_edite, this);
			agentsSimulationWindow.setVisible(true);

		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
			excpt.printStackTrace();
		}

	}

	public void creerFenetreSimulationDist() {
		try {
			this.selection.deSelect();
			FenetreDeSimulationDist fenetreDeSimulationDist = new FenetreDeSimulationDist(
					this.vueGraphe.cloner(), this.fichier_edite);
			fenetreDeSimulationDist.setVisible(true);

		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
			excpt.printStackTrace();
		}

	}

	public void changerDimensionGraphe(Dimension size) {
		this.vueGraphe.changerDimension(size);
	}

	// pour gerer la selection, on a une methode qui permet de creer la liste
	// undo correspondante.
	private void select(SelectionDessin selection) {
		selection.select();
		Enumeration e_undo = selection.elements();
		FormeDessin elt;
		while (e_undo.hasMoreElements()) {
			elt = (FormeDessin) e_undo.nextElement();
			this.undoInfo.addInfo(new SelectFormeDessin(selection, elt));
		}
	}

	private void deSelect(SelectionDessin selection) {
		Enumeration e_undo = selection.elements();
		this.undoInfo.newGroup("Reselect Objects", "Deselect Objects");
		FormeDessin elt;
		while (e_undo.hasMoreElements()) {
			elt = (FormeDessin) e_undo.nextElement();
			this.undoInfo.addInfo(new DeselectFormeDessin(selection, elt));
		}
		selection.deSelect();
		this.setUndo();
	}

	public UndoInfo getUndoInfo() {
		return this.undoInfo;
	}

	public JFileChooser getGMLFileChooser() {
		if (this.gmlFileChooser != null) {
			return this.gmlFileChooser;
		}

		javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String filename = f.getName();
				int index = filename.lastIndexOf(".");
				if (index == -1) {
					return false;
				}

				if (filename.substring(index + 1).equals("gml")) {
					return true;
				}

				return false;
			}

			public String getDescription() {
				return "GML format file (.gml)";
			}
		};

		this.gmlFileChooser = new JFileChooser(".");
		this.gmlFileChooser.setFileFilter(filter);
		this.gmlFileChooser.setDialogTitle("Import from GML");
		this.gmlFileChooser.setApproveButtonText("Import");

		return this.gmlFileChooser;
	}

	public void importGMLGraph() {
		JFileChooser fileChooser = this.getGMLFileChooser();
		int option = fileChooser.showOpenDialog(this);

		if (option == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			this.mettreAJourTitreFenetre(f.getName());

			InputStream inStream = null;
			try {
				inStream = new BufferedInputStream(new FileInputStream(f));
				visidia.gml.GMLParser parser = new visidia.gml.GMLParser(
						inStream);
				SimpleGraph graph = parser.Input();

				this.initialize();
				this.undoInfo = new UndoInfo();
				this.undoInfo.newGroup("", "");

				Hashtable hash = new Hashtable();
				Enumeration v_enum = graph.vertices();
				while (v_enum.hasMoreElements()) {
					Vertex vertex = (Vertex) v_enum.nextElement();
					visidia.gml.GMLNode gmlNode = (visidia.gml.GMLNode) vertex
							.getData();
					visidia.gml.GMLNodeGraphics graphics = gmlNode
							.getGraphics();
					Rectangle rect = graphics.getArea().getBounds();
					SommetDessin sommet = this.vueGraphe.creerSommet(rect.x,
							rect.y);
					hash.put(vertex.identity(), sommet);
				}

				v_enum = graph.vertices();
				while (v_enum.hasMoreElements()) {
					Vertex vertex = (Vertex) v_enum.nextElement();
					Enumeration neighbourEnum = vertex.neighbours();
					while (neighbourEnum.hasMoreElements()) {
						Vertex neighbourVertex = (Vertex) neighbourEnum
								.nextElement();
						if (vertex.identity().intValue() > neighbourVertex
								.identity().intValue()) {
							this.vueGraphe.creerArete((SommetDessin) hash
									.get(vertex.identity()),
									(SommetDessin) hash.get(neighbourVertex
											.identity()));
						}
					}
				}

			} catch (IOException e) {
				System.out.println("Problem: " + e);
			} catch (visidia.gml.ParseException e) {
				e.printStackTrace();
			} finally {
				try {
					inStream.close();
				} catch (IOException e) {
				}
			}

			this.grapheVisuPanel.repaint();
		}
	}
}

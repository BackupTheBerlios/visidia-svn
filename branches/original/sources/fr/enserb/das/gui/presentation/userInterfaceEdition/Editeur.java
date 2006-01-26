package fr.enserb.das.gui.presentation.userInterfaceEdition;

import fr.enserb.das.gui.DistributedAlgoSimulator;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import fr.enserb.das.graph.*;
import fr.enserb.das.gui.donnees.conteneurs.*;
import fr.enserb.das.gui.donnees.*;
import fr.enserb.das.gui.metier.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.gui.presentation.boite.*;
import fr.enserb.das.gui.metier.inputOutput.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.undo.*;
import fr.enserb.das.gui.presentation.*;


/**
 * La classe Editeur est particuli�rement importante, elle impl�mente la fen�tre
 * (une "JFrame") dans laquelle les graphes sont �dit�s (un graphe est un objet
 * de classe Graphe). C'est dans cette classe que sont impl�ment�s les menus
 * et les m�thodes de "haut niveau" appel�es lors de l'ex�cution d'une commande par
 * l'utilisateur. Par contre l'affichage "int�ractif" du graphe est assur� par la
 * classe GrapheVisuPanel.<BR>
 * Parmi les variables d'instance de cette classe, il faut bien distinguer 2 cat�gories
 * distinctes: les variables qui d�crivent l'�tat courant de l'�diteur (comme par
 * exemple les objets s�lectionn�s, les objets visuablisables servant de mod�le �
 * la cr�ation de nouveaux sommets et de nouvelle ar�tes, ...) et les variables
 * d�crivant l'�tat du document �dit� (parmi lesquelles on trouve biens�r le
 * VueGraphe, mais aussi la taille de la zone de travail, la couleur de fond, ...). Seules
 * les variables de la seconde cat�gorie sont sauvegard�es sur disque, les autres sont
 * toujours initialis�es � des valeurs par d�faut lors de l'ouverture d'un document.
 **/

public class Editeur extends Fenetre implements ActionListener, WindowListener ,ItemListener{
    
    
    /** Objet <b>UndoInfo</b> qui permet de r�aliser les op�rations de "undo" et "redo". **/
    protected UndoInfo undoInfo;
  
    /** Panel ou est dessine le Graphe*/
    protected GrapheVisuPanel grapheVisuPanel;
    

    // * * * ************************************************************
  
     
    /** Number of operations for annulation or restauration annul�es with undo/redo by group.*/
    protected int nb_op = 5;
    
    /** La description de l'annulation d'une op�ration effectu�e.*/
    protected String undoDescription = "";
    /** La description de la restauration d'une op�ration effectu�e.*/
    protected String redoDescription = "";
    
    
    protected JMenuBar menuBar;
    protected JMenu file, edition, transformation, option;
    
    protected JMenuItem file_new, file_open, file_save, file_save_as, file_gml_export, file_gml_import;
    protected JMenuItem file_refresh, file_help, file_close, file_quit;
    protected JMenuItem transformation_complete,transformation_cut_edges,transformation_renumeroter;
    protected JMenuItem transformation_change_shape_edges,transformation_change_shape_vertices;
    protected JMenuItem edition_select_all, edition_duplication,edition_cut, edition_properties;
    protected JMenuItem edition_undo, edition_redo, edition_undoBySet, edition_redoBySet, edition_setNbOp ;
    protected JToolBar toolBar;
    protected JButton but_new, but_open, but_save, but_duplicate, but_info, but_help, but_undo, but_redo , but_simulation;
    protected JCheckBoxMenuItem option_labels;
    protected JMenuItem option_dictionnaire ;

    JFileChooser gmlFileChooser = null;


    /**
     * Cr�e une nouvelle fen�tre d'�dition contenant un document vierge:
     * un Graphe vide et des caract�ristiques (taille de la zone de
     * travail, couleur de fond, ...) ayant des valeurs par d�faut.
     **/
    public Editeur() {
	this(new Graphe(), COULEUR_FOND_PAR_DEFAUT, DIM_X_PAR_DEFAUT,
	     DIM_Y_PAR_DEFAUT);
	this.addWindowListener(this);
	this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.setSize(650, 600);
	this.pack();
	this.setVisible(true);
	
    }
    
    
    /**
     * Un constructeur dont les arguments permettent de sp�cifier
     * compl�tement le document �dit�. Des donn�es telles que le
     * mod�le de sommet (ou d'ar�te), le fichier �dit�, les objets
     * s�lectionn�s,... ne font pas partie des arguments car il ne s'agit
     * pas de donn�es propres au document mais des donn�es d�finissant
     * l'�tat courant de l'�diteur.
     **/
    protected Editeur(Graphe graphe_edite, Color couleur_fond,
		      int dim_x, int dim_y) {
	
	super();
	
	// Le manager de composants
	content = new JPanel();
	content.setLayout(new BorderLayout());
	
	fichier_edite = null;
	mettreAJourTitreFenetre();
	
	
	// La barre de menus
	this.addMenu();
	// Donnees courantes de l'edition
	
	// Couleur d'arriere plan du GrapheVisuPanel
	couleur_de_fond = couleur_fond;
		
	// Le graphe edite et l'objet selection qui contient les objets selectionnes
	// graphe = graphe_edite;
	vueGraphe = graphe_edite.getVueGraphe();
	selection = new SelectionDessin();
	
	// Le panel ou est dessine le graphe
	grapheVisuPanel = creerGrapheVisuPanel();

	// pour le undo :
	undoInfo = new UndoInfo();
		
	super.setSize(dim_x,dim_y);
	// un setSize est a faire avant l'ajout de composants pour eviter
	// les warnings
	scroller = new JScrollPane(grapheVisuPanel);
	scroller.setPreferredSize(new Dimension(dim_x,dim_y));
	scroller.setOpaque(true);
	content.add(scroller, BorderLayout.CENTER);
	
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    commandeClose();
		}
	    });
	// La barre d'outils
	this.addToolBar();
	
	// On disable les items non-valide pour une applet
	if(!DistributedAlgoSimulator.estStandalone()) 
	    disableButtonForApplet();
	
	
		
	this.setContentPane(content);
    }
    
    
    /**
     * Cette m�thode ajoute la barre de menu, ses menus et sous_menus � l'�diteur.
     **/
    protected void addMenu() {
	
	menuBar = new JMenuBar();
	menuBar.setOpaque(true);
	menuBar.setPreferredSize(new Dimension(650, 20));
	
	// Build the menu File
	file = new JMenu("File");
	file.getPopupMenu().setName("PopFile");
	file.setMnemonic('F');
	file_new = new JMenuItem("New",KeyEvent.VK_N);
	file_new.setAccelerator(KeyStroke.getKeyStroke(
						       KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	file_new.addActionListener(this);
	file.add(file_new);
	file_open = new JMenuItem("Open...", KeyEvent.VK_O);
	file_open.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	file_open.addActionListener(this);
	file.add(file_open);
	file_save = new JMenuItem("Save", KeyEvent.VK_S);
	file_save.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	file_save.addActionListener(this);
	file.add(file_save);
	file_save_as = new JMenuItem("Save as...");
	file_save_as.addActionListener(this);
	file.add(file_save_as);
	
	file.addSeparator();
	file_gml_import = new JMenuItem("Import from GML");
	file_gml_import.addActionListener(this);
	file.add(file_gml_import);
	file_gml_export = new JMenuItem("Export in GML");
	file_gml_export.addActionListener(this);
	file.add(file_gml_export);
	
	file.addSeparator();
	file_refresh = new JMenuItem("Refresh", KeyEvent.VK_E);
	file_refresh.setAccelerator(KeyStroke.getKeyStroke(
							   KeyEvent.VK_E, ActionEvent.CTRL_MASK));
	file_refresh.addActionListener(this);
	file.add(file_refresh);
	file_help = new JMenuItem("Help", KeyEvent.VK_H);
	file_help.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_H, ActionEvent.CTRL_MASK));
	file_help.addActionListener(this);
	file.add(file_help);
	file.addSeparator();    
	file_close = new JMenuItem("Close", KeyEvent.VK_C);
	file_close.setAccelerator(KeyStroke.getKeyStroke(
							 KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	file_close.addActionListener(this);
	file.add(file_close);
	file_quit = new JMenuItem("Quit", KeyEvent.VK_Q);
	file_quit.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	file_quit.addActionListener(this);
	file.add(file_quit);
	file.addActionListener(this);
	menuBar.add(file);
	
	//Build the menu Edition.
	edition = new JMenu("Edition");
	edition.getPopupMenu().setName("PopEdition");
	edition.setMnemonic('E');
	edition_select_all = new JMenuItem("Select all", KeyEvent.VK_A);
	edition_select_all.setAccelerator(KeyStroke.getKeyStroke(
								 KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	edition_select_all.addActionListener(this);
	edition.add(edition_select_all);
	
	edition_duplication = new JMenuItem("Duplicate", KeyEvent.VK_D);
	edition_duplication.setAccelerator(KeyStroke.getKeyStroke(
								  KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	edition_duplication.addActionListener(this);
	edition.add(edition_duplication);
	edition_cut = new JMenuItem("Delete");
	edition_cut.addActionListener(this);
	edition.add(edition_cut);
	
	edition.addSeparator();
	edition_properties = new JMenuItem("Properties", KeyEvent.VK_P);
	edition_properties.setAccelerator(KeyStroke.getKeyStroke(
								 KeyEvent.VK_P, ActionEvent.CTRL_MASK));
	edition_properties.addActionListener(this);
	edition.add(edition_properties);
	
	edition.addActionListener(this);
	edition.addSeparator();
	edition_undo = new JMenuItem("Undo", KeyEvent.VK_U);
	edition_undo.setAccelerator(KeyStroke.getKeyStroke(
							   KeyEvent.VK_U, ActionEvent.CTRL_MASK));
	edition_undo.addActionListener(this);
	edition_undo.setEnabled(false);
	edition.add(edition_undo);
	
	edition_redo = new JMenuItem("Redo", KeyEvent.VK_R);
	edition_redo.setAccelerator(KeyStroke.getKeyStroke(
							   KeyEvent.VK_R, ActionEvent.CTRL_MASK));
	edition_redo.addActionListener(this);
	edition_redo.setEnabled(false);
	edition.add(edition_redo);
	
	edition_undoBySet = new JMenuItem("Undo last " + nb_op);
	edition_undoBySet.addActionListener(this);
	edition_undoBySet.setEnabled(false);
	edition.add(edition_undoBySet);
	
	edition_redoBySet = new JMenuItem("Redo last " + nb_op);
	edition_redoBySet.addActionListener(this);
	edition_redoBySet.setEnabled(false);
	edition.add(edition_redoBySet);
	
	edition_setNbOp = new JMenuItem("Number of undo/redo");
	edition_setNbOp.addActionListener(this);
	edition.add(edition_setNbOp);   
	
	menuBar.add(edition);	
	
	//Build the menu Transformation.
	transformation = new JMenu("Transformation");
	transformation.getPopupMenu().setName("PopTransformation");
	transformation.setMnemonic('T');
	
	transformation_complete = new JMenuItem("Complete");
	transformation_complete.addActionListener(this);
	transformation.add(transformation_complete);

	transformation_cut_edges = new JMenuItem("Delete edges");
	transformation_cut_edges.addActionListener(this);
	transformation.add(transformation_cut_edges);

	transformation.addSeparator();

	transformation_change_shape_edges = new JMenuItem("Change edges shape");
	transformation_change_shape_edges.addActionListener(this);
	transformation.add(transformation_change_shape_edges);

	transformation_change_shape_vertices = new JMenuItem("Change vertices shape");
	transformation_change_shape_vertices.addActionListener(this);
	transformation.add(transformation_change_shape_vertices);
	transformation.addSeparator();

	transformation_renumeroter = new JMenuItem("vertex renumbering");
	transformation_renumeroter.addActionListener(this);
	transformation.add(transformation_renumeroter);
	
	transformation.addActionListener(this);
	
	menuBar.add(transformation);	
	
	//Build the menu Options.
	option = new JMenu("Options");
	option.getPopupMenu().setName("PopOption");
	option.setMnemonic('O');
	option_labels = new JCheckBoxMenuItem("Labels");
	option_labels.setState(true);
	option_labels.addItemListener(this);
	option.add(option_labels);
	option.addSeparator();
	option_dictionnaire = new JMenuItem("Dictionnaire");
	option_dictionnaire.addActionListener(this);
	option.add(option_dictionnaire);
	
	option.addActionListener(this);
	menuBar.add(option);	
	this.setJMenuBar(menuBar);

    }
    
    /**
     * Cette m�thode ajoute la barre d'outils et ses boutons � l'�diteur.
     **/
    protected void addToolBar() {
	
	toolBar = new JToolBar();
	toolBar.setBackground(new Color(120, 120, 120));
	toolBar.setOpaque(true);
	toolBar.setPreferredSize(new Dimension(650, 40));
	
	//Build buttons on the tool bar
	but_new = new JButton(new ImageIcon(TableImages.getImage("new")));
	but_new.setToolTipText("New");
	but_new.setAlignmentY(CENTER_ALIGNMENT);
	but_new.addActionListener(this);
	toolBar.add(but_new);
	

	but_open = new JButton(new ImageIcon(TableImages.getImage("open")));
	but_open.setToolTipText("Open");
	but_open.setAlignmentY(CENTER_ALIGNMENT);
	but_open.addActionListener(this);
	toolBar.add(but_open);
	
	but_save = new JButton(new ImageIcon(TableImages.getImage("disk")));
	but_save.setToolTipText("Save");
	but_save.setAlignmentY(CENTER_ALIGNMENT);
	but_save.addActionListener(this);
	toolBar.add(but_save);
	
	toolBar.addSeparator();
	
	but_duplicate = new JButton(new ImageIcon(TableImages.getImage("duplicate")));
	but_duplicate.setToolTipText("Duplicate");
	but_duplicate.setAlignmentY(CENTER_ALIGNMENT);
	but_duplicate.addActionListener(this);
	toolBar.add(but_duplicate);
	    
	but_undo = new JButton(new ImageIcon(TableImages.getImage("undo")));//"fr/enserb/das/gui/donnees/images/undo.gif"));
	but_undo.setToolTipText("Undo");
	but_undo.setAlignmentY(CENTER_ALIGNMENT);
	but_undo.addActionListener(this);
	but_undo.setEnabled(false);
	toolBar.add(but_undo);
	
	but_redo = new JButton(new ImageIcon(TableImages.getImage("redo")));//"fr/enserb/das/gui/donnees/images/redo.gif"));
	but_redo.setToolTipText("Redo");
	but_redo.setAlignmentY(CENTER_ALIGNMENT);
	but_redo.addActionListener(this);
	but_redo.setEnabled(false);
	toolBar.add(but_redo);
	
	but_info = new JButton(new ImageIcon(TableImages.getImage("info")));//"fr/enserb/das/gui/donnees/images/info.gif"));
	but_info.setToolTipText("Properties");
	but_info.setAlignmentY(CENTER_ALIGNMENT);
	but_info.addActionListener(this);
	toolBar.add(but_info);
	
	toolBar.addSeparator();
	
	but_help = new JButton(new ImageIcon(TableImages.getImage("help")));//"fr/enserb/das/gui/donnees/images/aide.gif"));
	but_help.setToolTipText("Help");
	but_help.setAlignmentY(CENTER_ALIGNMENT);
	but_help.addActionListener(this);
	toolBar.add(but_help);
	
	toolBar.addSeparator();
	
	but_simulation = new JButton("simulation");
	but_simulation.setToolTipText("simulation");
	but_simulation.setAlignmentY(CENTER_ALIGNMENT);
	but_simulation.addActionListener(this);
	toolBar.add(but_simulation);
	
		
	content.add(toolBar, BorderLayout.NORTH);
    }
    
    // disable the button not used for the applet
    private void disableButtonForApplet(){
	file_open.setEnabled(false);
	file_save.setEnabled(false);
	file_gml_export.setEnabled(false);
	file_gml_import.setEnabled(false);
	file_save_as.setEnabled(false);
	file_quit.setEnabled(false);
	file_new.setEnabled(false);
	transformation_change_shape_edges.setEnabled(false);
	transformation_change_shape_vertices.setEnabled(false);
	but_open.setEnabled(false);
	but_save.setEnabled(false);
	but_new.setEnabled(false);
    }

    
    /** Cr�e un GrapheVisuPanel rattach� � l'�diteur courant. **/
    public GrapheVisuPanel creerGrapheVisuPanel() {
	return new GrapheVisuPanel(this);
    }
    
    /** Retourne le GrapheVisuPanel servant � l'�dition.**/
    public GrapheVisuPanel grapheVisuPanel() {
	return grapheVisuPanel;
    }
    public Graphe graph(){
	return super.getVueGraphe().getGraphe();
    }
    
    public void remplaceSelection(SelectionDessin nouvelle_selection) {
	// On vide et on supprime la selection initiale et on la remplace par la nouvelle
	// commandeViderSelection(true);
	selection = nouvelle_selection;
	//selection.select();
	//vueGraphe.enluminerFormeDessin(selection.elements());
    }
    
    /**
     * Cette m�thode retourne une cha�ne qui est utilis�e pour construire
     * le titre de la fen�tre et pour savoir dans quel type d'�diteur le
     * graphe est �dit�.
     **/
    protected String titre() {
	return "Graph Editor";
    }
    
    
    public String type(){
	return "Editor";
    }
    
    
    
    // Implementation des Listener
    
    public void windowOpened(WindowEvent e) {}
    
    public void windowClosing(WindowEvent e) {}
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowIconified(WindowEvent e) {}
    
    public void windowDeiconified(WindowEvent e) {}
    
    public void windowActivated(WindowEvent e) {
	grapheVisuPanel.repaint();
  	content.repaint();
    }
    
    public void windowDeactivated(WindowEvent e) {}
    
    /**
     * Impl�mentation de l'interface ActionListener
     * traitement de l'appui sur les menus d�roulants et
     * les boutons de la barre d'outils.
     **/
    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() instanceof JMenuItem)
	    action_menu((JMenuItem)evt.getSource());
	else if(evt.getSource() instanceof JButton)
	    action_toolbar((JButton)evt.getSource());
    }
    
    
    /**
     * M�thode de redirection du traitement suivant le menu dans lequel se
     * trouve la fonctionnalit� s�lectionn�e.
     **/
    public void action_menu(JMenuItem mi) {
	String le_menu = ((JPopupMenu)mi.getParent()).getName();
	
	if(le_menu == "PopFile") {
	    menuFile(mi);}
	else if(le_menu == "PopEdition")
	    menuEdition(mi);
	else if(le_menu == "PopTransformation")
	    menuTransformation(mi);
	else if(le_menu == "PopOption")
	    menuOption(mi);
    }
    
    /**
     * M�thode de redirection du traitement suivant le bouton s�lectionn�
     * dans la barre d'outils.
     **/


    public void action_toolbar(JButton b) {
	if (b == but_new)
	    {
		NewGraph.newGraphe(this);
		grapheVisuPanel.repaint();	
	    }
	else if (b == but_duplicate) {
	  

	    commandeDupliquer();
	    grapheVisuPanel.repaint();
	}
	else if (b == but_undo) {
	    undoInfo.undo();
	    setUndo();
	    grapheVisuPanel.repaint();
	}
	else if (b == but_redo) {
	    undoInfo.redo();
	    setUndo();
	    grapheVisuPanel.repaint();
	}
	else if(b == but_simulation){
	    commandeRenumeroter();
	    creerFenetreSimulation();
	    grapheVisuPanel.repaint();
	}
    	
	
	else if (b == but_info)
	    commandeProprietes();
	else if(b == but_help)
	    JOptionPane.showMessageDialog(this,
					  "DistributedAlgoSimulator, v2\n" +
					  "To create a new vertex:\n" +
					  "    Left mouse button\n" +
					  " \n" +
					  "To create a new edge:\n" +
					  "    Drag'n Drop with left mouse button\n" +
					  " \n" +
					  "To move a vertex, an edge or the selection:\n" +
					  "    Drag'nDrop with middle mouse button\n" +
					  " \n" +
					  "To move the visible part of the graph:\n" +
					  "    Drag'nDrop with the middle mouse button on\n" +
					  "    the desk area or use scrollbars\n" +
					  " \n" +
					  "To select objects in the graph:\n" +
					  "    Right mouse button\n"
					
					  );
	else if(b == but_open)
	    {
		


		OpenGraph.open(this);
		grapheVisuPanel.setPreferredSize(vueGraphe.donnerDimension());
		grapheVisuPanel.revalidate();
		
		grapheVisuPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
		grapheVisuPanel.repaint();
	    }
	else if(b == but_save)
	    SaveFile.save(this, vueGraphe.getGraphe());
    }
    
    
    
    /** M�thode de traitement des fonctionnalit�s du menu File. **/
    

	
    public void menuFile(JMenuItem mi) {
	if(mi == file_open){
	    OpenGraph.open(this);
	    grapheVisuPanel.setPreferredSize(vueGraphe.donnerDimension());
	    grapheVisuPanel.revalidate();
	    grapheVisuPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	    grapheVisuPanel.repaint();
	 
	}
	else if(mi == file_new) {
	    NewGraph.newGraphe(this);	
	    grapheVisuPanel.repaint();
      	}
	else if(mi == file_save)
	    SaveFile.save(this, vueGraphe.getGraphe());
	else if(mi == file_save_as) {
	    fichier_edite = null;
	    SaveFile.saveAs(this, vueGraphe.getGraphe());
	}
	else if(mi == file_gml_export) {
	    GMLParser.export(this, vueGraphe.getGraphe());	
	}
	else if(mi == file_gml_import) {
	    importGMLGraph();
	}
	else if(mi == file_refresh)
	    grapheVisuPanel.repaint();
	else if(mi == file_help) {
	    JOptionPane.showMessageDialog(this,
					  "DistributedAlgoSimulator, v2\n" +
					  "A graph editor in Java for \n" +
					  "random walk simulation on a graph\n" +
					  "To create a new vertex:\n" +
					  "    Left mouse button\n" +
					  " \n" +
					  "To create a new edge:\n" +
					  "    Drag'nDrop with left mouse button\n" +
					  " \n" +
					  "To move a vertex, an edge or the selection:\n" +
					  "    Drag'nDrop with middle mouse button\n" +
					  " \n" +
					  "To move the visible part of the graph:\n" +
					  "    Drag'nDrop with the middle mouse button on\n" +
					  "    the desk area or use scrollbars\n" +
					  " \n" +
					  "To select objects in the graph:\n" +
					  "    Right mouse button\n" +
					  " \n" +
					  "Attention:\n" +
					  "* Items 'Properties...' and 'Simulate...' of menus\n" +
					  "  'Edition' and 'Simulate' doesn't work on ctwm\n" +
					  "* Input/Output (ie save,open, ...)\n" +
					  "  are not available if the editor works as an\n" +
					  "  Applet: to use it, the programme must be use in\n" +
					  "  standalone !!\n" +
					  "* select a vertex before using\n" +
					  "  random walk simulation\n" +
					  " \n");
	}
	else if(mi == file_close)
	    commandeClose();
	else if(mi == file_quit)
	    System.exit(0);
    }
    
    
    /** M�thode de traitement des fonctionnalit�s du menu Edition. **/
    public void menuEdition(JMenuItem mi) {
	if(mi == edition_properties)
	    commandeProprietes();
	else if (mi == edition_duplication)
	    commandeDupliquer();
	else if (mi == edition_cut)
	    commandeSupprimer();
	else if (mi == edition_select_all)
	    commandeToutSelectionner();
	else if (mi == edition_undo) {
	    undoInfo.undo();
	    setUndo();
	}
	else if (mi == edition_redo) {
	    undoInfo.redo();
	    setUndo();
	}
	else if (mi == edition_undoBySet) {
	    undoInfo.undo(nb_op);
	    setUndo();
	}
	else if (mi == edition_redoBySet) {
	    undoInfo.redo(nb_op);
	    setUndo();
	}
	else if (mi == edition_setNbOp) {
	    commandeChangerNombreOperations();
	}
	grapheVisuPanel.repaint();
    }
    
    
    /** M�thode de traitement des fonctionnalit�s du menu Transformation. **/
    public void menuTransformation(JMenuItem mi) {
	if(mi == transformation_complete)
	    commandeCompleter();
	
	else if(mi == transformation_cut_edges)
	    commandeSupprimerAretes();
	
	else if(mi == transformation_renumeroter)
	    commandeRenumeroter();
	
	else if(mi == transformation_change_shape_edges){
	    deSelect(selection);
	    BoiteChangingEdgeShape changeBox = new BoiteChangingEdgeShape(this,vueGraphe);
	    changeBox.show();
	}

	else if(mi == transformation_change_shape_vertices){
	    deSelect(selection);
	    BoiteChangingVertexShape changeBox = new BoiteChangingVertexShape(this,vueGraphe);
	    changeBox.show();
	}

	else  return;
	
	grapheVisuPanel.repaint();
    }
    
    
    /** M�thode de traitement des fonctionnalit�s du menu Options. **/
    public void menuOption(JMenuItem mi) {
  	if(mi == option_labels)
	    vueGraphe.afficherEtiquettes(option_labels.getState());
	
        else if(mi == option_dictionnaire)
	    {
		
		BoiteChoix boiteChoix = new BoiteChoix(this,"tableau de choix des couleurs");
        	boiteChoix.show(this);
	    }
	grapheVisuPanel.repaint();
    }
    
    
    // Les commandes
    // Remarque: les commandes ne font theoriquement
    // pas elles-meme les repaint() necessaires
    

    /** Commande de suppression des �l�ments de la s�lection courante. **/
    public void commandeSupprimer() { // Penser au repaint()
	
	// Suppression des elements de la selection
	if(!selection.estVide()) {
	    undoInfo.newGroup("Recreate deleted selection", "Delete selection");
	    // on cree l'enumeration avec tous les elements a supprimer
	    Enumeration e = Traitements.elementsTotaux(selection.elements()).elements();
	    while (e.hasMoreElements()) {
		FormeDessin s = (FormeDessin)e.nextElement();
		undoInfo.addInfo(new SupprimeObjet(s));
		s.delete();
		undoInfo.addInfo(new DeselectFormeDessin(selection,s));
	    }
	    selection.deSelect();
	}
	setUndo();
    }
    
    /** Commande de fermeture de l'�diteur courant. **/
    public void commandeClose() {
	setVisible(false);
	dispose();
	
	// On en profite pour collecter les ordures
	// que l'on vient de creer
	Runtime.getRuntime().gc();
    }
    
    /**
     * Commande d'affichage des propri�t�s : 
     *
     *  du ou des �l�ments de la s�lection
     *  du graphe tout entier, si la s�lection est vide.
     * 
     */



    public void commandeProprietes() {
	if(selection.estVide()) {
	    BoiteGraphe.show(this);
	} else {
	    int taille_selection = selection.nbElements();
	    Enumeration e = selection.elements();
	  
	    if(taille_selection == 1) {
		Object selected = e.nextElement();
		// pour les boites de dialogue
		//undoInfo.undoAndRemove();
		BoiteFormeDessin boite_de_proprietes = ((FormeDessin)selected).proprietes(this);	      
		boite_de_proprietes.show(this);
		setUndo();
	      
	    } else {  
		MultiEnsemble table_des_types = new MultiEnsemble();
		while(e.hasMoreElements())
		    table_des_types.inserer(((FormeDessin)e.nextElement()).type());
	      
		BoiteSelection.show(this, taille_selection, table_des_types);
	    }
	}
    }
    

    /**
     * Commande de renum�rotation des sommets du graphe.
     **/
    public void commandeRenumeroter()// Penser au repaint()
    {
	vueGraphe.renumeroterSommets();
    }

    /**
     * Commande qui permet de rendre complet : 
     *
     * le graphe tout entier si la s�lection est vide; 
     * la partie du graphe s�lectionn�e sinon.
     * 
     */

    public void commandeCompleter() { // Penser au repaint()
	Enumeration e;
	if(selection.estVide()) {
	    undoInfo.newGroup("Restore uncomplete graph", "Complete graph");
	    e = Traitements.completersousGraphe(vueGraphe.listeAffichage());
	} else { 
	    undoInfo.newGroup("Restore uncomplete selection", "Complete selection");
	    e = Traitements.completersousGraphe(selection.elements());
	}
	while (e.hasMoreElements())
	    undoInfo.addInfo(new AjouteObjet((FormeDessin)e.nextElement()));
	setUndo();
    }

    /**
     * Commande de suppression : 
     *
     * <ul>
     * <li>des ar�tes contenues dans la s�lection; </li>
     * <li>de toutes les ar�tes du graphe si la s�lection est vide.</li>
     * </ul>
     **/
    public void commandeSupprimerAretes() { // Penser au repaint()
	if(selection.estVide()) {
	    /* On va utiliser une pile interm�diaire pour pouvoir supprimer */
	    /* les �l�ments dans l'ordre (sinon, on ne peut pas : la m�thode */
	    /* nextElement plante!! */
	    Enumeration e = vueGraphe.listeAffichage();
	    Stack pileTmp = new Stack();
	    while (e.hasMoreElements()) {
		FormeDessin f =(FormeDessin)e.nextElement();
		if (f.type().equals("edge"))
		    pileTmp.push((AreteDessin)f);
	    }
	    if (!pileTmp.isEmpty()) {
		undoInfo.newGroup("Restore edges in the graph", "Delete edges from graph");
		while (!pileTmp.isEmpty()) {
		    AreteDessin arete = (AreteDessin)pileTmp.pop();
		    undoInfo.addInfo(new SupprimeObjet(arete));
		    arete.delete();
		}
	    }
	} else {
	    Enumeration e = selection.elements();
	    Stack pileTmp = new Stack();
	    while (e.hasMoreElements()) {
		FormeDessin f =(FormeDessin)e.nextElement();
		if (f.type().equals("edge"))
		    pileTmp.push((AreteDessin)f);
	    }
	    if (!pileTmp.isEmpty()) {
		undoInfo.newGroup("Restore edges in the graph", "Delete edges from graph");
		while (!pileTmp.isEmpty()) {
		    AreteDessin arete = (AreteDessin)pileTmp.pop();
		    undoInfo.addInfo(new SupprimeObjet(arete));
		    arete.delete();
		}
	    }
	}
  
	setUndo();
    }
  
    /**
     * Commande qui permet de vider la s�lection courante.
     */
    public void commandeViderSelection(boolean deselect) { // Penser au repaint()
	if (!selection.estVide()) {
	    if (deselect) 
		deSelect(selection); // deselection with creating undo group
	}
	setUndo();
    }
    
    /**
     * Commande de s�lection de la totalit� du graphe �dit�.
     */
    public void commandeToutSelectionner() { // Penser au repaint()
	int i = 0;
	Enumeration e = vueGraphe.listeAffichage();
	if (e.hasMoreElements()) {
	    undoInfo.newGroup("Deselect all", "Select all");
	    while(e.hasMoreElements()) {
		FormeDessin objetVisu = (FormeDessin)e.nextElement();
		selection.insererElement(objetVisu);
		undoInfo.addInfo(new SelectFormeDessin(selection, objetVisu));
		//objetVisu.enluminer(true);
		i++;
	    }
	}
	setUndo();
    }

 
    
    /**
     * Commande de duplication : 
     *
     * <ul>
     * <li>des �l�mants contenus dans la s�lection; </li>
     * <li>du graphe tout entier si la s�lection est vide. </li>
     * </ul>
     * Les duplicata deviennent les nouveaux objets s�lectionn�s.
     **/
    public void commandeDupliquer() {
	// On duplique tous les elements de la selection, et les duplicata
	// deviennent les nouveaux objets selectionnes
	redoDescription = "Duplicate";
	undoDescription = "Cancel duplication"; 
	if (!selection.estVide()){
	    Traitements.dupliquerSelectionDessin(selection);

	    // on selectionne la copie
	    select(selection);

	    // On deplace les clones
	    Ensemble objetVisu_a_deplacer =
		Traitements.sommetsTotaux(selection.elements());
	    //objetVisu_a_deplacer.inserer(selection.autresObjetsVisu());
	    vueGraphe.deplacerFormeDessin(objetVisu_a_deplacer.elements(), 30, 30);
	

	    undoInfo.newGroup("Cancel duplication", "Duplicate");
	    Enumeration e = selection.elements();
	    while (e.hasMoreElements())
		undoInfo.addInfo(new AjouteObjet((FormeDessin)e.nextElement()));
	    setUndo();
	}
    }

    /**
     * Commande de s�lection du sous-graphe connexe contenant les �l�ments
     * de la s�lection courante.
     **/
    public void commandesousGrapheConnexe() {
	Enumeration sel = selection.elements();
	if(sel.hasMoreElements()) {
	    undoDescription = "Unselect connected subgraph";
	    redoDescription = "Select connected subgraph";
	    selection = Traitements.sousGrapheConnexe(sel);
	    select(selection);
	}
	else {
	    undoDescription = "Unselect connected subgraph";
	    redoDescription = "Select connected subgraph";
	    commandeViderSelection(true);
	}
    }

    /**
     * Commande de s�lection du sous-graphe connexe orient� contenant les
     * �l�ments de la s�lection courante.
     **/
    public void commandesousGrapheConnexeOriente() {
	Enumeration les_sommets = Traitements.areteDessin(selection.elements());
	if(les_sommets.hasMoreElements()) {
	    redoDescription = "Select connected oriented subgraph";
	    undoDescription = "Unselect connected oriented subgraph";
	    selection = Traitements.sousGrapheConnexeOriente(les_sommets);
	    select(selection);
	}
	else {
	    redoDescription = "Select connected oriented subgraph";
	    undoDescription = "Unselect connected oriented subgraph";
	    commandeViderSelection(true);
	}
    }

    /**
     * Commande d'ouverture d'une bo�te de dialogue permettant d'ouvrir un nouvel
     * �diteur du type d�sir�.
     **/
 
    /**
     * Commande de changement du nombre d'op�rations � annuler ou � 
     * restaurer gr�ce aux commandes de undo/redo par groupes.
     **/
    public void commandeChangerNombreOperations() {
	BoiteChangementNombreOperations b = new BoiteChangementNombreOperations(this, nb_op);
    }


    /**
     * Cette m�thode met <b>nb_op</b> � la valeur "i" et r�actualise le 
     * texte des �l�ments de menus <b>edition_undoBySet</b> et  b>edition_redoBySet</b>
     **/
    public void setNbOp(int i) {
	nb_op = i;
	edition_undoBySet.setText("Undo last " + i);
	edition_redoBySet.setText("Redo last " + i);    
    }

    /**
     * Commande de remise � jour du dessin du graphe.
     **/
    public void rafraichir() {
	grapheVisuPanel.repaint();
    }

    /**
     * Commande de modification de la couleur de fond de l'�diteur.
     **/
    public void change_couleur_de_fond(Color new_color) {
	couleur_de_fond = new_color;
	this.grapheVisuPanel.setBackground(couleur_de_fond);
    }

    /**
     * Renvoie la valeur de la largeur courante de la zone de dessin.
     **/
    public int drawableWidth() {
	return grapheVisuPanel.getPreferredSize().width;
    }

    /**
     * Renvoie la valeur de la hauteur courante de la zone de dessin.
     **/
    public int drawableHeight() {
	return grapheVisuPanel.getPreferredSize().height;
    }

    /**
     * Renvoie le nom de la classe de l'�diteur.
     **/
    public String className() {
	return new String("editeur.Editeur");
    }

    /**
     * Commande de r�initialisation de l'�diteur.
     * <BR> Appel�e avant l'ouverture d'un fichier si celui-ci contient
     * un graphe du m�me type que l'�diteur courant.
     **/
    public void initialize() {
	Graphe graphe = new Graphe();
	vueGraphe = graphe.getVueGraphe();
	but_undo.setEnabled(false);
	but_redo.setEnabled(false);
	selection = new SelectionDessin();
    }    

    /**
     * Permet de donner � <b>description</b> la valeur pass�e en argument.
     **/
    public void setDescription(String newUndoDesc, String newRedoDesc) {
	undoDescription = newUndoDesc;
	redoDescription = newRedoDesc;
    }

    /**
     * Permet d'activer ou de desactiver les boutons "Undo" et "Redo" suivant
     * l'�tat des "piles" d'actions undo et redo.
     **/
    public void setUndo() {
	//Si le groupe de undo cree est vide, on le supprime!
	undoInfo.removeEmptyGroup();
	but_undo.setEnabled(undoInfo.undoMore());
	but_undo.setToolTipText(undoInfo.undoDescription());
	edition_undo.setEnabled(undoInfo.undoMore());
	edition_undoBySet.setEnabled(undoInfo.undoMore());    
	but_redo.setEnabled(undoInfo.redoMore());
	but_redo.setToolTipText(undoInfo.redoDescription());
	edition_redo.setEnabled(undoInfo.redoMore());
	edition_redoBySet.setEnabled(undoInfo.redoMore());    
    }
  

 

    public void changerPanel(GrapheVisuPanel unGrapheVisuPanel){
	content.remove(scroller);
 	this.grapheVisuPanel = unGrapheVisuPanel;
	scroller = new JScrollPane(this.grapheVisuPanel);
        scroller.setPreferredSize(new Dimension(650,600));
        scroller.setOpaque(true);
        content.add(scroller, BorderLayout.CENTER);
    }

    public void changerVueGraphe(VueGraphe graphe){ 	
	this.vueGraphe = graphe;
	changerPanel( new GrapheVisuPanel(this));
    }
    public void itemStateChanged(ItemEvent e){
 	if(e.getSource() instanceof JCheckBoxMenuItem)
	    menuOption((JMenuItem)e.getSource());
    } 
 

    public void creerFenetreSimulation(){
	try {
	    selection.deSelect();
	    FenetreDeSimulation fenetreDeSimulation = 
		new FenetreDeSimulation(vueGraphe.cloner(),fichier_edite);
	    fenetreDeSimulation.show();
	    
	    
	} catch(Exception excpt) {
	    System.out.println("Problem: " + excpt);
	    excpt.printStackTrace();
	}
	
    }
    public void changerDimensionGraphe(Dimension size){
	vueGraphe.changerDimension(size);
    }


    // pour gerer la selection, on a une methode qui permet de creer la liste undo correspondante.
    private void select(SelectionDessin selection){
	selection.select();
	Enumeration e_undo = selection.elements();
	FormeDessin elt;
	while (e_undo.hasMoreElements()) {
	    elt = (FormeDessin)e_undo.nextElement();
	    undoInfo.addInfo(new SelectFormeDessin(selection, elt));
	}
    }

    private void deSelect(SelectionDessin selection){
	Enumeration e_undo = selection.elements();
	undoInfo.newGroup("Reselect Objects", "Deselect Objects");
	FormeDessin elt;
	while (e_undo.hasMoreElements()) {
	    elt = (FormeDessin)e_undo.nextElement();
	    undoInfo.addInfo(new DeselectFormeDessin(selection, elt));
	}
	selection.deSelect();
	setUndo();
    }


    public UndoInfo getUndoInfo(){
	return undoInfo;}

    public JFileChooser getGMLFileChooser(){
	if(gmlFileChooser != null){
	    return gmlFileChooser;
	}

	javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter(){
		public boolean accept(File f){
		    if(f.isDirectory()){
			return true;
		    }
		    
		    String filename = f.getName();
		    int index = filename.lastIndexOf(".");
		    if(index == -1){
			return false;
		    }

		    if(filename.substring(index+1).equals("gml")){
			return true;
		    }
		    
		    return false;
		}

		public String getDescription(){
		    return "GML format file (.gml)";
		}
	    };

	gmlFileChooser = new JFileChooser(".");
	gmlFileChooser.setFileFilter(filter);
	gmlFileChooser.setDialogTitle("Import from GML");
	gmlFileChooser.setApproveButtonText("Import");

	return gmlFileChooser;
    }

	
    public void importGMLGraph(){
	JFileChooser fileChooser = getGMLFileChooser();
	int option = fileChooser.showOpenDialog(this);
	
	if(option == JFileChooser.APPROVE_OPTION) {
	    File f = fileChooser.getSelectedFile();
	    
	    InputStream inStream = null;
	    try{
		inStream = new BufferedInputStream(new FileInputStream(f));
		fr.enserb.das.gml.GMLParser parser = new fr.enserb.das.gml.GMLParser(inStream);
		SimpleGraph graph = parser.Input();
		
		initialize();
		undoInfo = new UndoInfo();
		undoInfo.newGroup("","");
		
		Hashtable hash = new Hashtable();
		Enumeration enum = graph.vertices();
		while(enum.hasMoreElements()){
		    Vertex vertex = (Vertex) enum.nextElement();
		    fr.enserb.das.gml.GMLNode gmlNode = (fr.enserb.das.gml.GMLNode) vertex.getData();
		    fr.enserb.das.gml.GMLNodeGraphics graphics = gmlNode.getGraphics();
		    Rectangle rect = graphics.getArea().getBounds();
		    SommetDessin sommet = vueGraphe.creerSommet(rect.x, rect.y);
		    hash.put(vertex.identity(),sommet);
		}
		
		enum = graph.vertices();
		while(enum.hasMoreElements()){
		    Vertex vertex = (Vertex) enum.nextElement();
		    Enumeration neighbourEnum = vertex.neighbours();
		    while(neighbourEnum.hasMoreElements()){
			Vertex neighbourVertex = (Vertex) neighbourEnum.nextElement();
			if(vertex.identity().intValue() > neighbourVertex.identity().intValue()){
			    vueGraphe.creerArete((SommetDessin) hash.get(vertex.identity()), 
						 (SommetDessin) hash.get(neighbourVertex.identity()));
			}
		    }
		}
		
	    } 
	    catch(IOException e) {
		System.out.println("Problem: " + e);
	    }
	    catch(fr.enserb.das.gml.ParseException e){
		e.printStackTrace();
	    }
	    finally{
		try{
		    inStream.close();
		}
		catch(IOException e){}
	    }
	    
	    grapheVisuPanel.repaint();
	}
    }
}


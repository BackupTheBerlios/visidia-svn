package visidia.gui.presentation.starRule;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import visidia.gui.donnees.TableImages;
import visidia.gui.metier.Graphe;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.HelpDialog;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.userInterfaceSimulation.ApplyStarRulesSystem;
import visidia.rule.MyVector;
import visidia.rule.Neighbour;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.rule.Star;
import visidia.simulation.synchro.SynCT;

public class StarRuleFrame extends JFrame implements RuleTabbedPaneControl {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5246577019616997575L;

	JTabbedPane rule;

    //Relabeling system options
    JCheckBoxMenuItem optionTermination;
    //Synchronisation types
    JCheckBoxMenuItem synRdv, synLC1, synLC2, synNotSpecified;
    //Name of the save file
    String fileName = null;
    //Used by innner class
    JFrame finalThis;
    //Help input
    HelpDialog helpDialog;

    public StarRuleFrame(JFrame parent, final ApplyStarRulesSystem applyRules) {
	this.finalThis = this;
	this.rule = new JTabbedPane();
	this.rule.setBackground(StarData.ruleColor);
	Point p = parent.getLocation();
	this.helpDialog = new HelpDialog(this.finalThis, "Insert the rules description");
	this.helpDialog.setEditable(true);
	this.setLocation(p.x + 100, p.y + 100);
	this.setTitle();
	
	Container c = this.getContentPane();
	c.setBackground(new Color(175, 235, 235));
	c.add (this.rule);
	
	this.addWindowListener (new WindowAdapter() {
		public void windowClosing (WindowEvent e) {
		    StarRuleFrame.this.finalThis.setVisible(false);
		    StarRuleFrame.this.fileName = null;
		}
		});
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			   
	JMenuBar menuBar = new JMenuBar();
	this.setJMenuBar(menuBar);
	JMenu menuFile = new JMenu("File");
	menuFile.setMnemonic('F');
	JMenu menuSyn = new JMenu("Synchronisation");
	menuSyn.setMnemonic('S');
	JMenu menuOption = new JMenu("Options");
	menuOption.setMnemonic('O');
	menuBar.add(Box.createHorizontalStrut (5));
	menuBar.add(menuFile);
	menuBar.add(Box.createHorizontalStrut (5));
	menuBar.add(menuSyn);
	menuBar.add(Box.createHorizontalStrut (5));
	menuBar.add(menuOption);
	menuBar.add(Box.createHorizontalStrut (10));

	this.buildFileMenu(menuFile);

	ButtonGroup synGroup = new ButtonGroup();
	this.synRdv = new JCheckBoxMenuItem("Rendez-vous");
	this.synLC1 = new JCheckBoxMenuItem("LC1");
	this.synLC2 = new JCheckBoxMenuItem("LC2");
	this.synNotSpecified = new JCheckBoxMenuItem("Not specified");
	this.synNotSpecified.setSelected(true);
	synGroup.add(this.synRdv);
	synGroup.add(this.synLC1);
	synGroup.add(this.synLC2);
	synGroup.add(this.synNotSpecified);
	menuSyn.add(this.synNotSpecified);
	menuSyn.addSeparator();
	menuSyn.add(this.synRdv);
	menuSyn.add(this.synLC1);
	menuSyn.add(this.synLC2);
	
	this.optionTermination = new JCheckBoxMenuItem("Manage termination");
	menuOption.add(this.optionTermination);
	
	JButton butApply = new JButton("Apply");
	menuBar.add(butApply);
	butApply.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog
			(StarRuleFrame.this.finalThis, "Apply the rules to the simulation frame ?",
			 "Apply the rules", JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			applyRules.applyStarRulesSystem(StarRuleFrame.this.getRelabelingSystem());
		    }
		}
	    });
	ImageIcon imageHelp = new ImageIcon(TableImages.getImage("help"));
	JButton butHelp = new JButton(imageHelp);
	butHelp.setToolTipText("Help");
	butHelp.setAlignmentX(CENTER_ALIGNMENT);
	butHelp.setAlignmentY(CENTER_ALIGNMENT);
	Dimension dim = new Dimension(imageHelp.getIconWidth() + 8, 
				      imageHelp.getIconHeight() + 7);
	butHelp.setSize (dim);
	butHelp.setPreferredSize (dim);
	butHelp.setMaximumSize (dim);
	butHelp.setMinimumSize (dim);
	menuBar.add(Box.createHorizontalStrut (10));
	menuBar.add(butHelp);
	butHelp.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    StarRuleFrame.this.helpDialog.setVisible(true);
		    StarRuleFrame.this.pack();
		}
	    });
	
	this.newRelabelingSystem(null);
	this.pack();
    }
    
    public void setTitle() {
	String title = "Star rules system builder";
	if (this.fileName == null) {
	    super.setTitle(title + " " + "(untilted)");
	} else {
	    super.setTitle(title + " " + "(" + this.fileName + ")");
	}
    }
    
    public void setVisible(boolean v) {
	this.newRelabelingSystem(null);
	this.fileName = null;
	this.pack();
	super.setVisible(v);
    }

    //Load a new relabeling system (erases all rule tabbed panes)
    //If rSys is null, a new system is proposed
    private void newRelabelingSystem(RelabelingSystem rSys) {
	if (rSys == null) {
	    this.rule.removeAll();
	    this.rule.addTab ("Rule n 1", new RulePane(this, null));
	    this.optionTermination.setSelected(true);
	    this.synNotSpecified.setSelected(true);
	    this.helpDialog.setText("");
	} else {
	    this.rule.removeAll();
	    for (Iterator it = rSys.getRules(); it.hasNext(); ) {
		Rule r = (Rule) it.next();
		this.rule.addTab ("", new RulePane(this, r));
	    }
	    RSOptions rsOpt = rSys.getOptions();
	    this.optionTermination.setSelected(rsOpt.manageTerm);
	    int synType = rsOpt.defaultSynchronisation();
	    this.synNotSpecified.setSelected(true);
	    this.synRdv.setSelected(synType == SynCT.RDV);
	    this.synLC1.setSelected(synType == SynCT.LC1);
	    this.synLC2.setSelected(synType == SynCT.LC2);
	    this.helpDialog.setText(rSys.getDescription());
	}
	this.renameRule();
    }
    
    private void buildFileMenu(JMenu menuFile) {
	JMenuItem fileNew = new JMenuItem("New");
	JMenuItem fileOpen = new JMenuItem("Open");
	final JMenuItem fileSave = new JMenuItem("Save");
	JMenuItem fileSaveAs = new JMenuItem("Save as");
	JMenuItem fileClose = new JMenuItem("Close");
	menuFile.add(fileNew);
	menuFile.add(fileOpen);
	menuFile.add(fileSave);
	menuFile.add(fileSaveAs);
	menuFile.addSeparator();
	menuFile.add(fileClose);
	
	fileSave.setEnabled(false);
	
	final javax.swing.filechooser.FileFilter filter = 
	    new javax.swing.filechooser.FileFilter () {
		public boolean accept (File f) {
		    String n = f.getName ();
		    return n.endsWith ("srs");
		}
		public String getDescription () {
		    return "srs (star rules system) files";
		}
	    };

	fileSave.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (StarRuleFrame.this.fileName == null) 
			return;
		    try {
			FileOutputStream ostream = new FileOutputStream(StarRuleFrame.this.fileName);
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(StarRuleFrame.this.getRelabelingSystem());
			p.flush();
			ostream.close();
		    } catch (Exception exc) {
			System.out.println (exc);
		    }
		}
	    });
	
	fileNew.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog (StarRuleFrame.this.finalThis, 
						       "Begin new system ?",
						       "Begin new system",
						       JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			StarRuleFrame.this.newRelabelingSystem(null);
			StarRuleFrame.this.fileName = null;
			StarRuleFrame.this.setTitle();
			fileSave.setEnabled(false);
		    }
		}
	    });

	fileClose.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog (StarRuleFrame.this.finalThis, 
						       "Close the frame ?",
						       "Close the frame",
						       JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			StarRuleFrame.this.finalThis.setVisible(false);
			StarRuleFrame.this.fileName = null;
			fileSave.setEnabled(false);
		    }
		}
	    });

	fileSaveAs.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JFileChooser chooser = new JFileChooser ();
		    chooser.setDialogType (JFileChooser.SAVE_DIALOG);
		    chooser.setFileFilter (filter);
		    chooser.setCurrentDirectory (new File ("./"));
		    int returnVal = chooser.showSaveDialog (StarRuleFrame.this.finalThis);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
			    String fName = chooser.getSelectedFile ().getPath ();
			    if (!fName.endsWith ("srs"))
				fName += ".srs";
			    FileOutputStream ostream = new FileOutputStream(fName);
			    ObjectOutputStream p = new ObjectOutputStream(ostream);
			    p.writeObject(StarRuleFrame.this.getRelabelingSystem());
			    p.flush();
			    ostream.close();
			    StarRuleFrame.this.fileName = fName;
			    fileSave.setEnabled(true);
			    StarRuleFrame.this.setTitle();
			} catch (IOException ioe) {
			    System.out.println (ioe);
			}
		    }
		}
	    });
	
	fileOpen.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog (StarRuleFrame.this.finalThis, 
						       "Load file ?",
						       "Load file",
						       JOptionPane.YES_NO_OPTION);
		    if (res != JOptionPane.YES_OPTION)
			return;

		    JFileChooser chooser = new JFileChooser ();
		    chooser.setDialogType (JFileChooser.OPEN_DIALOG);
		    chooser.setFileFilter (filter);
		    chooser.setCurrentDirectory (new File ("./"));
		    int returnVal = chooser.showSaveDialog (StarRuleFrame.this.finalThis);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
			    String fName = chooser.getSelectedFile().getPath();
			    FileInputStream istream = new FileInputStream(fName);
			    ObjectInputStream p = new ObjectInputStream(istream);
			    RelabelingSystem rSys = (RelabelingSystem) p.readObject();
			    StarRuleFrame.this.newRelabelingSystem(rSys);
			    istream.close();
			    StarRuleFrame.this.fileName = fName;
			    fileSave.setEnabled(true);
			    StarRuleFrame.this.setTitle();
			} catch (IOException ioe) {
			    System.out.println (ioe);
			} catch (ClassNotFoundException cnfe) {
			    System.out.println (cnfe);
			}
		    }
		}
	    });
}
    
    //Give a name to the tab according to its index
    private void renameRule() {
	int count = this.rule.getTabCount();
	for (int i = 0; i < count; i++) {
	    this.rule.setTitleAt(i, "Rule " + (i + 1));
	    this.rule.setBackgroundAt(i, StarData.ruleColor);
	}
    }
    
    public void addNewRule() {
	this.rule.addTab("", new RulePane(this, null));
	this.renameRule();	
	this.rule.setSelectedIndex(this.rule.getTabCount() - 1);
    
    }
    public void deleteRule() {
	int p = this.rule.getSelectedIndex();
	int count = this.rule.getTabCount();
	if (count > 1) {
	    this.rule.remove(p);
	    this.renameRule();
	}
    }
    
    public void insertRule() {
	this.rule.insertTab("", null, 
		       new RulePane(this, null), null, this.rule.getSelectedIndex());
	this.renameRule();
    }

    public void switchLeft() {
	int pos = this.rule.getSelectedIndex();
	if (pos >= 1) {
	    RulePane r1 = (RulePane) this.rule.getSelectedComponent();
	    this.rule.remove(pos);
	    this.rule.insertTab("", null, r1, null, pos - 1);
	    this.rule.setSelectedIndex(pos);
	}
	this.renameRule();
	this.repaint();
    }

    public void switchRight() {
	int pos = this.rule.getSelectedIndex();
	if (pos < this.rule.getTabCount() - 1) {
	    RulePane r1 = (RulePane) this.rule.getSelectedComponent();
	    this.rule.remove(pos);
	    this.rule.insertTab("", null, r1, null, pos + 1);
	    this.rule.setSelectedIndex(pos);
	}
	this.renameRule();
    }

    /**
     * Returns true if the selected pane can switch with the right pane
     */ 
    public boolean canSwitchRight() {
	return (this.rule.getSelectedIndex() < (this.rule.getTabCount() - 1));
    }

    /**
     * Returns true if the selected pane can switch with the left pane
     */ 
    public boolean canSwitchLeft() {
	return (this.rule.getSelectedIndex() >= 1);
    }

    public RelabelingSystem getRelabelingSystem() {
	Vector v = new Vector();
	int count = this.rule.getTabCount();
	for (int i = 0; i < count; i++) {
	    v.add(((RulePane) this.rule.getComponent(i)).getRule());
	}
	RelabelingSystem rSys = new RelabelingSystem(v);
	//FIXME : quel est le choix pour type non specifie ?? (Mohammed)
	int synType = (this.synRdv.isSelected() ? SynCT.RDV 
		       : (this.synLC1.isSelected() ? SynCT.LC1 
			  : (this.synLC2.isSelected() ? SynCT.LC2 
			     : -1)));
	RSOptions opt = new RSOptions(synType, this.optionTermination.isSelected());
	rSys.setOptions(opt);
	rSys.setDescription(this.helpDialog.getText());
	return rSys;
    }
}

/**
 * Describes the functions needed by each rule pane proposed in a popup menu.
 */
interface RuleTabbedPaneControl {

    public void addNewRule();

    public void deleteRule();
	
    public void insertRule();

    public void switchLeft();
	
    public void switchRight();
    
    public boolean canSwitchRight();
	
    public boolean canSwitchLeft();
}

/**
 * A RulePane is a panel containing one rule and a tabbed pane of contexts.
 * It is inserted into the tabbed pane of the rules.
 */
class RulePane extends JPanel implements ContexTabbedPaneControl {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3496858951253633705L;
	final public static int NOT_TERMINIATION_RULE = 0;
    final public static int LOCAL_TERMINIATION_RULE = 1;
    final public static int GLOBAL_TERMINIATION_RULE = 2;
    final public static String TERMINATION[] = { "Not a termination rule",
						 "Local termination rule", 
						 "Global termination rule" };
    // The panel containing on rule
    BuildRulePane buildRulePane;
    // The contexts
    JTabbedPane context;
    int terminationType;

    public RulePane(RuleTabbedPaneControl tabbedPaneControl, Rule rule) {
	this.setBackground(StarData.ruleColor);
	Border b = (BorderFactory.createCompoundBorder 
		    (BorderFactory.createEmptyBorder (5, 5, 5, 5),
		     BorderFactory.createCompoundBorder
		     (BorderFactory.createLoweredBevelBorder(),
		      BorderFactory.createEmptyBorder (5, 10, 5, 10))));
	
	//That TabbedPane will store each context associated to the rule
	this.context = new JTabbedPane();
	this.context.setBorder(b);
	
	if (rule == null) { // There is no rule
	    this.context.addTab("Context 1", 
			   new BuildContextPane(this, true, null));
	    this.buildRulePane = new BuildRulePane(tabbedPaneControl, null, null, false);
	    this.terminationType = NOT_TERMINIATION_RULE;
	} else {
	    this.buildRulePane = 
		new BuildRulePane(tabbedPaneControl, 
				  rule.befor(), rule.after(), rule.isSimpleRule());
	    if (rule.forbContexts().size() == 0) {
		//System.out.println ("Zero forboedde");
		this.context.addTab("Context 1", new BuildContextPane(this, true, null));
	    } else {
		for (Iterator it = rule.forbContexts().iterator(); it.hasNext(); ) {
		    Star s = (Star) it.next();
		    this.context.addTab("", new BuildContextPane(this, false, s));
		}
	    }
	    int t = rule.getType();
	    if (t == SynCT.GENERIC) 
		this.terminationType = NOT_TERMINIATION_RULE;
	    else if (t == SynCT.LOCAL_END)
		this.terminationType = LOCAL_TERMINIATION_RULE;
	    else if (t == SynCT.GLOBAL_END)
		this.terminationType = GLOBAL_TERMINIATION_RULE;
	    this.renameContext();
	}
	
	//The combo box permits to choose the type of termination
	JPanel rulePaneWithCombo = new JPanel();
	rulePaneWithCombo.setBackground(StarData.ruleColor);
	JPanel comboPanel = new JPanel();
	comboPanel.setBackground(StarData.ruleColor);
	final JComboBox comboTermination = new JComboBox(TERMINATION);
	comboTermination.setBackground(new Color(185, 225, 215));
	comboTermination.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    RulePane.this.terminationType = comboTermination.getSelectedIndex();
		}
	    });
	comboPanel.add(comboTermination);
	comboTermination.setSelectedIndex(this.terminationType);
	
	rulePaneWithCombo.setLayout(new BorderLayout(0, 0));
	rulePaneWithCombo.add(this.buildRulePane, BorderLayout.CENTER);
	rulePaneWithCombo.add(comboPanel, BorderLayout.SOUTH);
	
	rulePaneWithCombo.setBorder(b);
	
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	this.setLayout(g);
	c.gridx = 0; c.gridy = 0;
	c.gridwidth = 3; c.gridheight = 1;
	c.weightx = 70; c.weighty = 0;
	c.fill = GridBagConstraints.BOTH;
	this.add(rulePaneWithCombo, c);
	c.gridx = 3; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 1;
	c.weightx = 30; c.weighty = 0;
	this.add(this.context, c);
    }
    
    public Rule getRule() {
	MyVector v = new MyVector();
	int count = this.context.getTabCount();
	for (int i = 0; i < count; i++) {
	    Star s = ((BuildContextPane) this.context.getComponent(i)).getStar();
	    if (s != null) {
		v.add(s);
	    }
	}
	Rule r = new Rule(this.buildRulePane.getLeftStar(), 
			  this.buildRulePane.getRightStar(), v);
	//Sets the type of the rule
	if (this.terminationType == NOT_TERMINIATION_RULE)
	    r.setType(SynCT.GENERIC);
	else if (this.terminationType == LOCAL_TERMINIATION_RULE) 
	    r.setType(SynCT.LOCAL_END);
	else if (this.terminationType == GLOBAL_TERMINIATION_RULE) 
	    r.setType(SynCT.GLOBAL_END);
	r.setSimpleRule(this.buildRulePane.getIsSimpleRule());
	
	return r;
    }

    //Give a name to the tab according to its index
    private void renameContext() {
	int count = this.context.getTabCount();
	for (int i = 0; i < count; i++) {
	    this.context.setTitleAt(i, "Context " + (i + 1));
	    this.context.setBackgroundAt(i, StarData.contextColor);
	}
    }

    /**
     * If the selected context is empty, it is deleted.
     * A new one is added.
     */
    public void addNewContext() {
	if (((BuildContextPane) this.context.getSelectedComponent()).isEmpty()) {
	    this.context.remove(0);
	}
	String s1 = this.buildRulePane.getLeftStar().centerState();
	Star centerContext = null;
	centerContext = new Star(s1);
	this.context.addTab("", new BuildContextPane(this, false, centerContext));
	this.renameContext();
	this.context.setSelectedIndex(this.context.getTabCount() - 1);
    }
    
    /**
     * If there is just one pane, it is replaced by an empty-one
     */
    public void deleteContext() {
	int p = this.context.getSelectedIndex();
	this.context.remove(p);
	int count = this.context.getTabCount();
	if (count == 0) {
	    this.context.addTab("", new BuildContextPane(this, true, null));
	}
	this.renameContext();
    }
    
    public void insertContext() {
	int pos;
	if (((BuildContextPane) this.context.getSelectedComponent()).isEmpty()) {
	    this.context.remove(0);
	    pos = 0;
	} else {
	    pos = this.context.getSelectedIndex();
	}
	this.context.insertTab("", null, 
			  new BuildContextPane(this, false, null),
			  null, pos);
	this.renameContext();
    }
}

class ConvertStarVueGraph {

    /**
     * The VueGraphe must already contain sommetC.
     * Adds to vg the data and the other vertexes stored into star.
     * The graphical position of the elements must be reorganized.
     */
    static public void star2StarVueGraphe(Star star, 
					  VueGraphe vg, SommetDessin sommetC) {
	sommetC.setEtat(star.centerState());
	sommetC.setEtiquette("0");
	for (Iterator it = star.neighbourhood().iterator(); it.hasNext(); ) {
	    Neighbour n = (Neighbour) it.next();
	    SommetDessin s = vg.creerSommet(10, 10);
	    s.setEtat(n.state());
	    //s.setEtiquette("" + (n.doorNum() + 1));
	    AreteDessin a = vg.creerArete(sommetC, s);
	    a.setEtat(n.mark());
	}
    }

    // Returns SommetDessin s as s.getEtiquette().equals(id)
    static private SommetDessin getVertex(VueGraphe vg, String id) {
	for (Enumeration e = vg.listeAffichage(); e.hasMoreElements(); ) {
	    FormeDessin f = (FormeDessin) e.nextElement();
	    if (f instanceof SommetDessin) {
		if (((SommetDessin) f).getEtiquette().equals(id))
		    return (SommetDessin) f;
	    }
	}
	return null;
    }

    static public Star starVueGraphe2Star(VueGraphe vg, SommetDessin sommetC) {
	int vertexNumber = vg.nbObjets();
	if (vertexNumber > 1) {
	    // Edge must be deduted
	    vertexNumber = vertexNumber - (vertexNumber / 2);
	}
	
	Star star = new Star(sommetC.getEtat());
	
	for (int i = 1; i < vertexNumber; i++) {
	    SommetDessin s = getVertex(vg, "" + i);
	    AreteDessin a = vg.rechercherArete(s.getEtiquette(), 
					       sommetC.getEtiquette());
	    star.addNeighbour(new Neighbour(s.getEtat(), a.getEtat()));
	}
	return star;
    }
}

/**
 * That left side panel is used to paint and to compose one rule
 * A popup menu proposes various actions (Add/remove/insert rule) 
 * implemented by StarRuleFrame.
 */
class BuildRulePane extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7494110680278206197L;
	SommetDessin sommetCLeft, sommetCRight;
    Graphe gLeft, gRight;
    VueGraphe vgLeft, vgRight;
    Point centerLeft, centerRight;
    RuleTabbedPaneControl tabbedPaneControl;
    boolean isSimpleRule;
    
    //Circle stroke
    float[] dash = {6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f};
    BasicStroke dashS = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, this.dash, 0.0f);
    
    // Used to visualize the two VueGraph
    DuoStarVisuPanel dsvp;
    JPopupMenu popup;
    JMenuItem deleteRuleMenuItem, addRuleMenuItem, insertRuleMenuItem;
    JMenuItem switchLeftMenuItem, switchRightMenuItem, reorganizeVertexMenuItem;
    JCheckBoxMenuItem simpleRuleMenuItem;
    
    public BuildRulePane(final RuleTabbedPaneControl tabbedPaneControl,
			 Star sLeft, Star sRight, boolean simpleRule) {

	this.tabbedPaneControl = tabbedPaneControl;
	this.isSimpleRule = simpleRule;
	
	this.setBackground(StarData.ruleColor);
	this.gLeft = new Graphe();
	this.gRight = new Graphe();
	this.vgLeft = new VueGraphe(this.gLeft);
	this.vgRight = new VueGraphe(this.gRight);
	

	this.setCenterPosition();
	
	this.sommetCLeft = this.vgLeft.creerSommet(this.centerLeft.x, this.centerLeft.y);
	this.sommetCRight = this.vgRight.creerSommet(this.centerRight.x, this.centerRight.y);
	
	if ((sLeft != null) && (sRight != null)) {
	    ConvertStarVueGraph.star2StarVueGraphe(sLeft, this.vgLeft, this.sommetCLeft);
	    ConvertStarVueGraph.star2StarVueGraphe(sRight, this.vgRight, this.sommetCRight);
	}
	
	this.dsvp = new DuoStarVisuPanel(this, this.vgLeft, this.vgRight,
				    this.centerLeft, this.centerRight, 
				    StarData.ray * 2 + StarData.rule_center,
				    StarData.ray, this.isSimpleRule);
	this.dsvp.reorganizeVertex();
	this.deleteRuleMenuItem = new JMenuItem("Delete rule");
	this.addRuleMenuItem = new JMenuItem("Add rule");
	this.insertRuleMenuItem = new JMenuItem("Insert rule");
	this.switchLeftMenuItem = new JMenuItem("Switch with left");
	this.switchRightMenuItem = new JMenuItem("Switch with right");
	this.reorganizeVertexMenuItem = new JMenuItem("Reoranize vertex");
	this.simpleRuleMenuItem = new JCheckBoxMenuItem("Simple rule");
	this.simpleRuleMenuItem.setSelected(this.isSimpleRule);
	
	this.deleteRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    for (Enumeration v_enum = BuildRulePane.this.vgLeft.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != BuildRulePane.this.sommetCLeft)
			    BuildRulePane.this.vgLeft.delObject(f);
		    }
		    for (Enumeration v_enum = BuildRulePane.this.vgRight.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != BuildRulePane.this.sommetCRight)
			    BuildRulePane.this.vgRight.delObject(f);
		    }
		    BuildRulePane.this.repaint();
		    tabbedPaneControl.deleteRule();
		}
	    });
	this.addRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.addNewRule();
		}
	    });
	this.insertRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.insertRule();
		}
	    });
	this.switchLeftMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.switchLeft();
		}		    
	    });
	this.switchRightMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.switchRight();
		}		    
	    });
	this.reorganizeVertexMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    BuildRulePane.this.dsvp.reorganizeVertex();
		}		    
	    });
	this.simpleRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if ((BuildRulePane.this.vgLeft.nbObjets() > 3) || (BuildRulePane.this.vgRight.nbObjets() > 3)) {
			BuildRulePane.this.simpleRuleMenuItem.setSelected(BuildRulePane.this.isSimpleRule);
			return;
		    }
		    BuildRulePane.this.isSimpleRule = BuildRulePane.this.simpleRuleMenuItem.isSelected();
		    BuildRulePane.this.setCenterPosition();
		    BuildRulePane.this.sommetCLeft.placer(BuildRulePane.this.centerLeft.x, BuildRulePane.this.centerLeft.y);
		    BuildRulePane.this.sommetCRight.placer(BuildRulePane.this.centerRight.x, BuildRulePane.this.centerRight.y);
		    BuildRulePane.this.dsvp.launchStarVisuPanels(BuildRulePane.this.centerLeft, BuildRulePane.this.centerRight, BuildRulePane.this.isSimpleRule);
		    BuildRulePane.this.dsvp.reorganizeVertex();
		    BuildRulePane.this.repaint();
		}		    
	    });
	
	this.popup = new JPopupMenu();
	this.popup.add(this.deleteRuleMenuItem);
	this.popup.add(this.addRuleMenuItem);
	this.popup.add(this.insertRuleMenuItem);
	this.popup.add(this.switchLeftMenuItem);
	this.popup.add(this.switchRightMenuItem);
	this.popup.addSeparator();
	this.popup.add(this.reorganizeVertexMenuItem);
	this.popup.add(this.simpleRuleMenuItem);
	this.popup.setBorder(BorderFactory.createRaisedBevelBorder());

	this.addMouseListener(new MouseAdapter() {
		private void callPopup(MouseEvent evt) {
		    int x = evt.getX();
		    int y = evt.getY(); 
		    int modifiers = evt.getModifiers();
		    if (modifiers == InputEvent.BUTTON3_MASK) {
			try {
			    //FormeDessin f1 = 
			    BuildRulePane.this.vgLeft.en_dessous(x, y);
			} catch (NoSuchElementException e1) {
			    try {
				//FormeDessin f2 = 
			    BuildRulePane.this.vgRight.en_dessous(x, y);
			    } catch (NoSuchElementException e2) {
				BuildRulePane.this.maybeShowPopup(evt);
			    }
			}
		    }
		}
		public void mousePressed(MouseEvent evt) {
		    this.callPopup(evt);
		}
		public void mouseReleased(MouseEvent evt) {
		    this.callPopup(evt);
		}
	    });
	this.addMouseListener(this.dsvp);
	this.addMouseMotionListener(this.dsvp);
    }
	
    private void setCenterPosition() {
	if (this.isSimpleRule) {
	    this.centerLeft = new Point(StarData.ray / 2 + StarData.rule_left,
				   StarData.ray + StarData.rule_top);
	    this.centerRight = new Point(this.centerLeft.x + StarData.ray * 2
				    + StarData.rule_center,
				    StarData.ray + StarData.rule_top);
	} else {
	    this.centerLeft = new Point(StarData.ray + StarData.rule_left,
				   StarData.ray + StarData.rule_top);
	    this.centerRight = new Point(this.centerLeft.x + StarData.ray * 2 
				    + StarData.rule_center,
				    StarData.ray + StarData.rule_top);
	}
    }

    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    this.switchRightMenuItem.setEnabled(this.tabbedPaneControl.canSwitchRight());
	    this.switchLeftMenuItem.setEnabled(this.tabbedPaneControl.canSwitchLeft());
	    this.popup.show(e.getComponent(), e.getX(), e.getY());
	}
    }
	
    public Dimension getPreferredSize() {
	return new Dimension(StarData.rule_left + StarData.ray * 2 
			     + StarData.rule_center + StarData.ray * 2 
			     + StarData.rule_left, 
			     StarData.rule_top + StarData.ray * 2
			     + StarData.rule_bottom);
    }

    public Star getLeftStar() {
	return ConvertStarVueGraph.starVueGraphe2Star(this.vgLeft, this.sommetCLeft);
    }

    public Star getRightStar() {
	return ConvertStarVueGraph.starVueGraphe2Star(this.vgRight, this.sommetCRight);
    }
    public boolean getIsSimpleRule() {
	return this.isSimpleRule;
    }

    public Dimension getMinimumSize() {
	return this.getPreferredSize();	
    }
	
    public Dimension getMaxmimumSize() {
	return this.getPreferredSize();	
    }
	
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(Color.black);
	Stroke tmp = ((Graphics2D) g).getStroke ();
	if (! this.isSimpleRule) {
	    // Indicates on the circle the degree 0
	    g.drawLine(this.centerLeft.x, this.centerLeft.y - StarData.ray - 2,
		       this.centerLeft.x, this.centerLeft.y - StarData.ray + 2);
	    g.drawLine(this.centerRight.x, this.centerRight.y - StarData.ray - 2,
		       this.centerRight.x, this.centerRight.y - StarData.ray + 2);
	}
	// Center arrow
	((Graphics2D) g).setStroke (new BasicStroke (3.0f));
	g.drawLine(StarData.arrow_x1, this.centerLeft.y,
		   StarData.arrow_x2, this.centerLeft.y);
		       
	g.drawLine(StarData.arrow_x2, this.centerLeft.y,
		   StarData.arrow_x2 - 5, this.centerLeft.y - 5);
	    
	g.drawLine(StarData.arrow_x2, this.centerLeft.y,
		   StarData.arrow_x2 - 5, this.centerLeft.y + 5);
	if (! this.isSimpleRule) {
	    // Circles
	    ((Graphics2D) g).setStroke(this.dashS);
	    g.drawArc(this.centerLeft.x - StarData.ray, this.centerLeft.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	    g.drawArc(this.centerRight.x - StarData.ray, this.centerRight.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	} else {
	    g.setColor(Color.gray);
	    g.drawLine(this.centerLeft.x + StarData.ray, this.centerLeft.y - 5, 
		       this.centerLeft.x + StarData.ray, this.centerLeft.y + 5);
	    g.drawLine(this.centerLeft.x + StarData.ray - 5, this.centerLeft.y, 
		       this.centerLeft.x + StarData.ray + 5, this.centerLeft.y);
	    g.drawLine(this.centerRight.x + StarData.ray, this.centerRight.y - 5, 
		       this.centerRight.x + StarData.ray, this.centerRight.y + 5);
	    g.drawLine(this.centerRight.x + StarData.ray - 5, this.centerRight.y, 
		       this.centerRight.x + StarData.ray + 5, this.centerRight.y);
	}
	((Graphics2D) g).setStroke (tmp);
	
	this.vgLeft.dessiner(this, g);
	this.vgRight.dessiner(this, g);
    }
}

/**
 * Describes functions used by a context pane and used by a popup menu
 */
interface ContexTabbedPaneControl {

    public void addNewContext();
    
    public void deleteContext();
	
    public void insertContext();
}


/**
 * A BuildContextPane is a panel where a context is composed.
 * A popup menu proposes various actions (Add/Delete/Insert context) 
 * implemented by RulePane.
 */
class BuildContextPane extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3926310804273678177L;
	Graphe g;
    VueGraphe vg;
    Point center;
    SommetDessin sommetC;

    StarVisuPanel svp;
    JPopupMenu popup;
    
    boolean empty;

    Color backCl = new Color(153, 255, 153);
    //Circle stroke
    float[] dash = {6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f};
    BasicStroke dashS = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, this.dash, 0.0f);
    
    /**
     * If empty is true, a message "NoContext" is displayed and no context
     * can be composed. Therefore that panel must be suppressed when the user
     * wants one.
     * "star" value can be null if the graph doesn't contains any vertex.
     */
    public BuildContextPane(final ContexTabbedPaneControl tabbedPaneControl, 
			    boolean empty, Star star) {
	this.empty = empty;
	this.setBackground(StarData.contextColor);
	this.center = new Point(StarData.ctxt_left + StarData.ray,
			   StarData.ctxt_top + StarData.ray);
	
	this.g = new Graphe();
	this.vg = new VueGraphe(this.g);	
	this.sommetC = this.vg.creerSommet(this.center.x, this.center.y);
	
	if (star != null) {
	    ConvertStarVueGraph.star2StarVueGraphe(star, this.vg, this.sommetC);
	}

	if (! empty) {
	    this.svp = new StarVisuPanel(this.vg, StarData.ray, this.center, this, false);
	    if (star != null) {
		this.svp.reorganizeVertex();
	    }
	}
	
	JMenuItem deleteContextMenuItem = new JMenuItem("Delete context");
	JMenuItem addContextMenuItem = new JMenuItem("Add context");
	JMenuItem insertContextMenuItem = new JMenuItem("Insert context");
	JMenuItem reorganizeVertexMenuItem = new JMenuItem("Reorganize vertexes");
	
	if (empty) {
	    deleteContextMenuItem.setEnabled(false);
	    reorganizeVertexMenuItem.setEnabled(false);
	}
		
	deleteContextMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    for (Enumeration v_enum = BuildContextPane.this.vg.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != BuildContextPane.this.sommetC)
			    BuildContextPane.this.vg.delObject(f);
		    }
		    BuildContextPane.this.repaint();
		    tabbedPaneControl.deleteContext();
		}
	    });
	addContextMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.addNewContext();
		}
	    });
	insertContextMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.insertContext();
		}
	    });
	reorganizeVertexMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    BuildContextPane.this.svp.reorganizeVertex();
		}
	    });
	this.popup = new JPopupMenu();
	this.popup.add(deleteContextMenuItem);
	this.popup.add(addContextMenuItem);
	this.popup.add(insertContextMenuItem);	
	this.popup.addSeparator();
	this.popup.add(reorganizeVertexMenuItem);
	this.popup.setBorder(BorderFactory.createRaisedBevelBorder());

	this.addMouseListener(this.svp);
	this.addMouseMotionListener(this.svp);
	this.addMouseListener(new MouseAdapter() {
		private void callPopup(MouseEvent evt) {
		    int x = evt.getX();
		    int y = evt.getY(); 
		    int modifiers = evt.getModifiers();
		    if (modifiers == InputEvent.BUTTON3_MASK) {
			try {
			    //FormeDessin f = 
			    BuildContextPane.this.vg.en_dessous(x, y);
			} catch (NoSuchElementException e) {
			    BuildContextPane.this.maybeShowPopup(evt);
			}
		    }	    
		}
		public void mousePressed(MouseEvent evt) {
		    this.callPopup(evt);
		}
		public void mouseReleased(MouseEvent evt) {
		    this.callPopup(evt);
		}
	    });
    }
    
    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    this.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
	    
    public Dimension getPreferredSize() {
	return new Dimension(StarData.ctxt_left + StarData.ray * 2 
			     + StarData.ctxt_right, 
			     StarData.ctxt_top + StarData.ray * 2
			     + StarData.ctxt_bottom);
    }
    
    public Dimension getMinimumSize() {
	return this.getPreferredSize();
    }
    
    public Dimension getMaxmimumSize() {
	return this.getPreferredSize();
    }

    public boolean isEmpty() {
	return this.empty;
    }

    public Star getStar() {
	if (this.isEmpty()) {
	    return null;
	} else {
	    return ConvertStarVueGraph.starVueGraphe2Star(this.vg, this.sommetC);
	}
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(Color.black);
	if (this.empty) {
	    g.drawString("No context", this.center.x - StarData.ray / 2, this.center.y);
	} else {
	    // Degree 0 of the circle
	    g.drawLine(this.center.x, this.center.y - StarData.ray - 2,
		       this.center.x, this.center.y - StarData.ray + 2);
	    
	    // Circle
	    Stroke tmp = ((Graphics2D) g).getStroke ();
	    ((Graphics2D) g).setStroke(this.dashS);
	    g.drawArc(this.center.x - StarData.ray, this.center.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	    ((Graphics2D) g).setStroke(tmp);
	    this.vg.dessiner(this, g);
	}
    }

}

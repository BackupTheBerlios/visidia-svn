package visidia.gui.presentation.starRule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import visidia.gui.presentation.*;
import visidia.gui.metier.Graphe;
//import visidia.gui.presentation.boite.EtatPanel;
//import visidia.gui.presentation.boite.VueEtatPanel;
//import visidia.gui.donnees.TableCouleurs;
import visidia.rule.*;
import visidia.simulation.synchro.SynCT;
import visidia.gui.presentation.userInterfaceSimulation.ApplyStarRulesSystem;
import visidia.gui.donnees.TableImages;
import java.util.*;
import java.io.*;

public class StarRuleFrame extends JFrame implements RuleTabbedPaneControl {
    
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
	finalThis = this;
	rule = new JTabbedPane();
	rule.setBackground(StarData.ruleColor);
	Point p = parent.getLocation();
	helpDialog = new HelpDialog(finalThis, "Insert the rules description");
	helpDialog.setEditable(true);
	setLocation(p.x + 100, p.y + 100);
	setTitle();
	
	Container c = getContentPane();
	c.setBackground(new Color(175, 235, 235));
	c.add (rule);
	
	addWindowListener (new WindowAdapter() {
		public void windowClosing (WindowEvent e) {
		    finalThis.setVisible(false);
		    fileName = null;
		}
		});
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			   
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);
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

	buildFileMenu(menuFile);

	ButtonGroup synGroup = new ButtonGroup();
	synRdv = new JCheckBoxMenuItem("Rendez-vous");
	synLC1 = new JCheckBoxMenuItem("LC1");
	synLC2 = new JCheckBoxMenuItem("LC2");
	synNotSpecified = new JCheckBoxMenuItem("Not specified");
	synNotSpecified.setSelected(true);
	synGroup.add(synRdv);
	synGroup.add(synLC1);
	synGroup.add(synLC2);
	synGroup.add(synNotSpecified);
	menuSyn.add(synNotSpecified);
	menuSyn.addSeparator();
	menuSyn.add(synRdv);
	menuSyn.add(synLC1);
	menuSyn.add(synLC2);
	
	optionTermination = new JCheckBoxMenuItem("Manage termination");
	menuOption.add(optionTermination);
	
	JButton butApply = new JButton("Apply");
	menuBar.add(butApply);
	butApply.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog
			(finalThis, "Apply the rules to the simulation frame ?",
			 "Apply the rules", JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			applyRules.applyStarRulesSystem(getRelabelingSystem());
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
		    helpDialog.setVisible(true);
		    pack();
		}
	    });
	
	newRelabelingSystem(null);
	pack();
    }
    
    public void setTitle() {
	String title = "Star rules system builder";
	if (fileName == null) {
	    super.setTitle(title + " " + "(untilted)");
	} else {
	    super.setTitle(title + " " + "(" + fileName + ")");
	}
    }
    
    public void setVisible(boolean v) {
	newRelabelingSystem(null);
	fileName = null;
	pack();
	super.setVisible(v);
    }

    //Load a new relabeling system (erases all rule tabbed panes)
    //If rSys is null, a new system is proposed
    private void newRelabelingSystem(RelabelingSystem rSys) {
	if (rSys == null) {
	    rule.removeAll();
	    rule.addTab ("Rule n 1", new RulePane(this, null));
	    optionTermination.setSelected(true);
	    synNotSpecified.setSelected(true);
	    helpDialog.setText("");
	} else {
	    rule.removeAll();
	    for (Iterator it = rSys.getRules(); it.hasNext(); ) {
		Rule r = (Rule) it.next();
		rule.addTab ("", new RulePane(this, r));
	    }
	    RSOptions rsOpt = rSys.getOptions();
	    optionTermination.setSelected(rsOpt.manageTerm);
	    int synType = rsOpt.defaultSynchronisation();
	    synNotSpecified.setSelected(true);
	    synRdv.setSelected(synType == SynCT.RDV);
	    synLC1.setSelected(synType == SynCT.LC1);
	    synLC2.setSelected(synType == SynCT.LC2);
	    helpDialog.setText(rSys.getDescription());
	}
	renameRule();
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
		    if (fileName == null) 
			return;
		    try {
			FileOutputStream ostream = new FileOutputStream(fileName);
			ObjectOutputStream p = new ObjectOutputStream(ostream);
			p.writeObject(getRelabelingSystem());
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
			JOptionPane.showConfirmDialog (finalThis, 
						       "Begin new system ?",
						       "Begin new system",
						       JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			newRelabelingSystem(null);
			fileName = null;
			setTitle();
			fileSave.setEnabled(false);
		    }
		}
	    });

	fileClose.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog (finalThis, 
						       "Close the frame ?",
						       "Close the frame",
						       JOptionPane.YES_NO_OPTION);
		    if (res == JOptionPane.YES_OPTION) {
			finalThis.setVisible(false);
			fileName = null;
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
		    int returnVal = chooser.showSaveDialog (finalThis);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
			    String fName = chooser.getSelectedFile ().getPath ();
			    if (!fName.endsWith ("srs"))
				fName += ".srs";
			    FileOutputStream ostream = new FileOutputStream(fName);
			    ObjectOutputStream p = new ObjectOutputStream(ostream);
			    p.writeObject(getRelabelingSystem());
			    p.flush();
			    ostream.close();
			    fileName = fName;
			    fileSave.setEnabled(true);
			    setTitle();
			} catch (IOException ioe) {
			    System.out.println (ioe);
			}
		    }
		}
	    });
	
	fileOpen.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    int res =
			JOptionPane.showConfirmDialog (finalThis, 
						       "Load file ?",
						       "Load file",
						       JOptionPane.YES_NO_OPTION);
		    if (res != JOptionPane.YES_OPTION)
			return;

		    JFileChooser chooser = new JFileChooser ();
		    chooser.setDialogType (JFileChooser.OPEN_DIALOG);
		    chooser.setFileFilter (filter);
		    chooser.setCurrentDirectory (new File ("./"));
		    int returnVal = chooser.showSaveDialog (finalThis);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
			    String fName = chooser.getSelectedFile().getPath();
			    FileInputStream istream = new FileInputStream(fName);
			    ObjectInputStream p = new ObjectInputStream(istream);
			    RelabelingSystem rSys = (RelabelingSystem) p.readObject();
			    newRelabelingSystem(rSys);
			    istream.close();
			    fileName = fName;
			    fileSave.setEnabled(true);
			    setTitle();
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
	int count = rule.getTabCount();
	for (int i = 0; i < count; i++) {
	    rule.setTitleAt(i, "Rule " + (i + 1));
	    rule.setBackgroundAt(i, StarData.ruleColor);
	}
    }
    
    public void addNewRule() {
	rule.addTab("", new RulePane(this, null));
	renameRule();	
	rule.setSelectedIndex(rule.getTabCount() - 1);
    
    }
    public void deleteRule() {
	int p = rule.getSelectedIndex();
	int count = rule.getTabCount();
	if (count > 1) {
	    rule.remove(p);
	    renameRule();
	}
    }
    
    public void insertRule() {
	rule.insertTab("", null, 
		       new RulePane(this, null), null, rule.getSelectedIndex());
	renameRule();
    }

    public void switchLeft() {
	int pos = rule.getSelectedIndex();
	if (pos >= 1) {
	    RulePane r1 = (RulePane) rule.getSelectedComponent();
	    rule.remove(pos);
	    rule.insertTab("", null, r1, null, pos - 1);
	    rule.setSelectedIndex(pos);
	}
	renameRule();
	repaint();
    }

    public void switchRight() {
	int pos = rule.getSelectedIndex();
	if (pos < rule.getTabCount() - 1) {
	    RulePane r1 = (RulePane) rule.getSelectedComponent();
	    rule.remove(pos);
	    rule.insertTab("", null, r1, null, pos + 1);
	    rule.setSelectedIndex(pos);
	}
	renameRule();
    }

    /**
     * Returns true if the selected pane can switch with the right pane
     */ 
    public boolean canSwitchRight() {
	return (rule.getSelectedIndex() < (rule.getTabCount() - 1));
    }

    /**
     * Returns true if the selected pane can switch with the left pane
     */ 
    public boolean canSwitchLeft() {
	return (rule.getSelectedIndex() >= 1);
    }

    public RelabelingSystem getRelabelingSystem() {
	Vector v = new Vector();
	int count = rule.getTabCount();
	for (int i = 0; i < count; i++) {
	    v.add(((RulePane) rule.getComponent(i)).getRule());
	}
	RelabelingSystem rSys = new RelabelingSystem(v);
	//FIXME : quel est le choix pour type non specifie ?? (Mohammed)
	int synType = (synRdv.isSelected() ? SynCT.RDV 
		       : (synLC1.isSelected() ? SynCT.LC1 
			  : (synLC2.isSelected() ? SynCT.LC2 
			     : -1)));
	RSOptions opt = new RSOptions(synType, optionTermination.isSelected());
	rSys.setOptions(opt);
	rSys.setDescription(helpDialog.getText());
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
	setBackground(StarData.ruleColor);
	Border b = (BorderFactory.createCompoundBorder 
		    (BorderFactory.createEmptyBorder (5, 5, 5, 5),
		     BorderFactory.createCompoundBorder
		     (BorderFactory.createLoweredBevelBorder(),
		      BorderFactory.createEmptyBorder (5, 10, 5, 10))));
	
	//That TabbedPane will store each context associated to the rule
	context = new JTabbedPane();
	context.setBorder(b);
	
	if (rule == null) { // There is no rule
	    context.addTab("Context 1", 
			   new BuildContextPane(this, true, null));
	    buildRulePane = new BuildRulePane(tabbedPaneControl, null, null, false);
	    terminationType = NOT_TERMINIATION_RULE;
	} else {
	    buildRulePane = 
		new BuildRulePane(tabbedPaneControl, 
				  rule.befor(), rule.after(), rule.isSimpleRule());
	    if (rule.forbContexts().size() == 0) {
		//System.out.println ("Zero forboedde");
		context.addTab("Context 1", new BuildContextPane(this, true, null));
	    } else {
		for (Iterator it = rule.forbContexts().iterator(); it.hasNext(); ) {
		    Star s = (Star) it.next();
		    context.addTab("", new BuildContextPane(this, false, s));
		}
	    }
	    int t = rule.getType();
	    if (t == SynCT.GENERIC) 
		terminationType = NOT_TERMINIATION_RULE;
	    else if (t == SynCT.LOCAL_END)
		terminationType = LOCAL_TERMINIATION_RULE;
	    else if (t == SynCT.GLOBAL_END)
		terminationType = GLOBAL_TERMINIATION_RULE;
	    renameContext();
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
		    terminationType = comboTermination.getSelectedIndex();
		}
	    });
	comboPanel.add(comboTermination);
	comboTermination.setSelectedIndex(terminationType);
	
	rulePaneWithCombo.setLayout(new BorderLayout(0, 0));
	rulePaneWithCombo.add(buildRulePane, BorderLayout.CENTER);
	rulePaneWithCombo.add(comboPanel, BorderLayout.SOUTH);
	
	rulePaneWithCombo.setBorder(b);
	
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	setLayout(g);
	c.gridx = 0; c.gridy = 0;
	c.gridwidth = 3; c.gridheight = 1;
	c.weightx = 70; c.weighty = 0;
	c.fill = GridBagConstraints.BOTH;
	add(rulePaneWithCombo, c);
	c.gridx = 3; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 1;
	c.weightx = 30; c.weighty = 0;
	add(context, c);
    }
    
    public Rule getRule() {
	MyVector v = new MyVector();
	int count = context.getTabCount();
	for (int i = 0; i < count; i++) {
	    Star s = ((BuildContextPane) context.getComponent(i)).getStar();
	    if (s != null) {
		v.add(s);
	    }
	}
	Rule r = new Rule(buildRulePane.getLeftStar(), 
			  buildRulePane.getRightStar(), v);
	//Sets the type of the rule
	if (terminationType == NOT_TERMINIATION_RULE)
	    r.setType(SynCT.GENERIC);
	else if (terminationType == LOCAL_TERMINIATION_RULE) 
	    r.setType(SynCT.LOCAL_END);
	else if (terminationType == GLOBAL_TERMINIATION_RULE) 
	    r.setType(SynCT.GLOBAL_END);
	r.setSimpleRule(buildRulePane.getIsSimpleRule());
	
	return r;
    }

    //Give a name to the tab according to its index
    private void renameContext() {
	int count = context.getTabCount();
	for (int i = 0; i < count; i++) {
	    context.setTitleAt(i, "Context " + (i + 1));
	    context.setBackgroundAt(i, StarData.contextColor);
	}
    }

    /**
     * If the selected context is empty, it is deleted.
     * A new one is added.
     */
    public void addNewContext() {
	if (((BuildContextPane) context.getSelectedComponent()).isEmpty()) {
	    context.remove(0);
	}
	String s1 = buildRulePane.getLeftStar().centerState();
	Star centerContext = null;
	centerContext = new Star(s1);
	context.addTab("", new BuildContextPane(this, false, centerContext));
	renameContext();
	context.setSelectedIndex(context.getTabCount() - 1);
    }
    
    /**
     * If there is just one pane, it is replaced by an empty-one
     */
    public void deleteContext() {
	int p = context.getSelectedIndex();
	context.remove(p);
	int count = context.getTabCount();
	if (count == 0) {
	    context.addTab("", new BuildContextPane(this, true, null));
	}
	renameContext();
    }
    
    public void insertContext() {
	int pos;
	if (((BuildContextPane) context.getSelectedComponent()).isEmpty()) {
	    context.remove(0);
	    pos = 0;
	} else {
	    pos = context.getSelectedIndex();
	}
	context.insertTab("", null, 
			  new BuildContextPane(this, false, null),
			  null, pos);
	renameContext();
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
	
    SommetDessin sommetCLeft, sommetCRight;
    Graphe gLeft, gRight;
    VueGraphe vgLeft, vgRight;
    Point centerLeft, centerRight;
    RuleTabbedPaneControl tabbedPaneControl;
    boolean isSimpleRule;
    
    //Circle stroke
    float[] dash = {6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f};
    BasicStroke dashS = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    
    // Used to visualize the two VueGraph
    DuoStarVisuPanel dsvp;
    JPopupMenu popup;
    JMenuItem deleteRuleMenuItem, addRuleMenuItem, insertRuleMenuItem;
    JMenuItem switchLeftMenuItem, switchRightMenuItem, reorganizeVertexMenuItem;
    JCheckBoxMenuItem simpleRuleMenuItem;
    
    public BuildRulePane(final RuleTabbedPaneControl tabbedPaneControl,
			 Star sLeft, Star sRight, boolean simpleRule) {

	this.tabbedPaneControl = tabbedPaneControl;
	isSimpleRule = simpleRule;
	
	setBackground(StarData.ruleColor);
	gLeft = new Graphe();
	gRight = new Graphe();
	vgLeft = new VueGraphe(gLeft);
	vgRight = new VueGraphe(gRight);
	

	setCenterPosition();
	
	sommetCLeft = vgLeft.creerSommet(centerLeft.x, centerLeft.y);
	sommetCRight = vgRight.creerSommet(centerRight.x, centerRight.y);
	
	if (sLeft != null && sRight != null) {
	    ConvertStarVueGraph.star2StarVueGraphe(sLeft, vgLeft, sommetCLeft);
	    ConvertStarVueGraph.star2StarVueGraphe(sRight, vgRight, sommetCRight);
	}
	
	dsvp = new DuoStarVisuPanel(this, vgLeft, vgRight,
				    centerLeft, centerRight, 
				    StarData.ray * 2 + StarData.rule_center,
				    StarData.ray, isSimpleRule);
	dsvp.reorganizeVertex();
	deleteRuleMenuItem = new JMenuItem("Delete rule");
	addRuleMenuItem = new JMenuItem("Add rule");
	insertRuleMenuItem = new JMenuItem("Insert rule");
	switchLeftMenuItem = new JMenuItem("Switch with left");
	switchRightMenuItem = new JMenuItem("Switch with right");
	reorganizeVertexMenuItem = new JMenuItem("Reoranize vertex");
	simpleRuleMenuItem = new JCheckBoxMenuItem("Simple rule");
	simpleRuleMenuItem.setSelected(isSimpleRule);
	
	deleteRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    for (Enumeration v_enum = vgLeft.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != sommetCLeft)
			    vgLeft.delObject(f);
		    }
		    for (Enumeration v_enum = vgRight.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != sommetCRight)
			    vgRight.delObject(f);
		    }
		    repaint();
		    tabbedPaneControl.deleteRule();
		}
	    });
	addRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.addNewRule();
		}
	    });
	insertRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.insertRule();
		}
	    });
	switchLeftMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.switchLeft();
		}		    
	    });
	switchRightMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    tabbedPaneControl.switchRight();
		}		    
	    });
	reorganizeVertexMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    dsvp.reorganizeVertex();
		}		    
	    });
	simpleRuleMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (vgLeft.nbObjets() > 3 || vgRight.nbObjets() > 3) {
			simpleRuleMenuItem.setSelected(isSimpleRule);
			return;
		    }
		    isSimpleRule = simpleRuleMenuItem.isSelected();
		    setCenterPosition();
		    sommetCLeft.placer(centerLeft.x, centerLeft.y);
		    sommetCRight.placer(centerRight.x, centerRight.y);
		    dsvp.launchStarVisuPanels(centerLeft, centerRight, isSimpleRule);
		    dsvp.reorganizeVertex();
		    repaint();
		}		    
	    });
	
	popup = new JPopupMenu();
	popup.add(deleteRuleMenuItem);
	popup.add(addRuleMenuItem);
	popup.add(insertRuleMenuItem);
	popup.add(switchLeftMenuItem);
	popup.add(switchRightMenuItem);
	popup.addSeparator();
	popup.add(reorganizeVertexMenuItem);
	popup.add(simpleRuleMenuItem);
	popup.setBorder(BorderFactory.createRaisedBevelBorder());

	addMouseListener(new MouseAdapter() {
		private void callPopup(MouseEvent evt) {
		    int x = evt.getX();
		    int y = evt.getY(); 
		    int modifiers = evt.getModifiers();
		    if (modifiers == InputEvent.BUTTON3_MASK) {
			try {
			    FormeDessin f1 = vgLeft.en_dessous(x, y);
			} catch (NoSuchElementException e1) {
			    try {
				FormeDessin f2 = vgRight.en_dessous(x, y);
			    } catch (NoSuchElementException e2) {
				maybeShowPopup(evt);
			    }
			}
		    }
		}
		public void mousePressed(MouseEvent evt) {
		    callPopup(evt);
		}
		public void mouseReleased(MouseEvent evt) {
		    callPopup(evt);
		}
	    });
	addMouseListener(dsvp);
	addMouseMotionListener(dsvp);
    }
	
    private void setCenterPosition() {
	if (isSimpleRule) {
	    centerLeft = new Point(StarData.ray / 2 + StarData.rule_left,
				   StarData.ray + StarData.rule_top);
	    centerRight = new Point(centerLeft.x + StarData.ray * 2
				    + StarData.rule_center,
				    StarData.ray + StarData.rule_top);
	} else {
	    centerLeft = new Point(StarData.ray + StarData.rule_left,
				   StarData.ray + StarData.rule_top);
	    centerRight = new Point(centerLeft.x + StarData.ray * 2 
				    + StarData.rule_center,
				    StarData.ray + StarData.rule_top);
	}
    }

    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    switchRightMenuItem.setEnabled(tabbedPaneControl.canSwitchRight());
	    switchLeftMenuItem.setEnabled(tabbedPaneControl.canSwitchLeft());
	    popup.show(e.getComponent(), e.getX(), e.getY());
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
	return ConvertStarVueGraph.starVueGraphe2Star(vgLeft, sommetCLeft);
    }

    public Star getRightStar() {
	return ConvertStarVueGraph.starVueGraphe2Star(vgRight, sommetCRight);
    }
    public boolean getIsSimpleRule() {
	return isSimpleRule;
    }

    public Dimension getMinimumSize() {
	return getPreferredSize();	
    }
	
    public Dimension getMaxmimumSize() {
	return getPreferredSize();	
    }
	
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(Color.black);
	Stroke tmp = ((Graphics2D) g).getStroke ();
	if (! isSimpleRule) {
	    // Indicates on the circle the degree 0
	    g.drawLine(centerLeft.x, centerLeft.y - StarData.ray - 2,
		       centerLeft.x, centerLeft.y - StarData.ray + 2);
	    g.drawLine(centerRight.x, centerRight.y - StarData.ray - 2,
		       centerRight.x, centerRight.y - StarData.ray + 2);
	}
	// Center arrow
	((Graphics2D) g).setStroke (new BasicStroke (3.0f));
	g.drawLine(StarData.arrow_x1, centerLeft.y,
		   StarData.arrow_x2, centerLeft.y);
		       
	g.drawLine(StarData.arrow_x2, centerLeft.y,
		   StarData.arrow_x2 - 5, centerLeft.y - 5);
	    
	g.drawLine(StarData.arrow_x2, centerLeft.y,
		   StarData.arrow_x2 - 5, centerLeft.y + 5);
	if (! isSimpleRule) {
	    // Circles
	    ((Graphics2D) g).setStroke(dashS);
	    g.drawArc(centerLeft.x - StarData.ray, centerLeft.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	    g.drawArc(centerRight.x - StarData.ray, centerRight.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	} else {
	    g.setColor(Color.gray);
	    g.drawLine(centerLeft.x + StarData.ray, centerLeft.y - 5, 
		       centerLeft.x + StarData.ray, centerLeft.y + 5);
	    g.drawLine(centerLeft.x + StarData.ray - 5, centerLeft.y, 
		       centerLeft.x + StarData.ray + 5, centerLeft.y);
	    g.drawLine(centerRight.x + StarData.ray, centerRight.y - 5, 
		       centerRight.x + StarData.ray, centerRight.y + 5);
	    g.drawLine(centerRight.x + StarData.ray - 5, centerRight.y, 
		       centerRight.x + StarData.ray + 5, centerRight.y);
	}
	((Graphics2D) g).setStroke (tmp);
	
	vgLeft.dessiner(this, g);
	vgRight.dessiner(this, g);
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
					BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    
    /**
     * If empty is true, a message "NoContext" is displayed and no context
     * can be composed. Therefore that panel must be suppressed when the user
     * wants one.
     * "star" value can be null if the graph doesn't contains any vertex.
     */
    public BuildContextPane(final ContexTabbedPaneControl tabbedPaneControl, 
			    boolean empty, Star star) {
	this.empty = empty;
	setBackground(StarData.contextColor);
	center = new Point(StarData.ctxt_left + StarData.ray,
			   StarData.ctxt_top + StarData.ray);
	
	g = new Graphe();
	vg = new VueGraphe(g);	
	sommetC = vg.creerSommet(center.x, center.y);
	
	if (star != null) {
	    ConvertStarVueGraph.star2StarVueGraphe(star, vg, sommetC);
	}

	if (! empty) {
	    svp = new StarVisuPanel(vg, StarData.ray, center, this, false);
	    if (star != null) {
		svp.reorganizeVertex();
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
		    for (Enumeration v_enum = vg.listeAffichage();
			 v_enum.hasMoreElements(); ) {
			FormeDessin f = (FormeDessin) v_enum.nextElement();
			if (f != sommetC)
			    vg.delObject(f);
		    }
		    repaint();
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
		    svp.reorganizeVertex();
		}
	    });
	popup = new JPopupMenu();
	popup.add(deleteContextMenuItem);
	popup.add(addContextMenuItem);
	popup.add(insertContextMenuItem);	
	popup.addSeparator();
	popup.add(reorganizeVertexMenuItem);
	popup.setBorder(BorderFactory.createRaisedBevelBorder());

	addMouseListener(svp);
	addMouseMotionListener(svp);
	addMouseListener(new MouseAdapter() {
		private void callPopup(MouseEvent evt) {
		    int x = evt.getX();
		    int y = evt.getY(); 
		    int modifiers = evt.getModifiers();
		    if (modifiers == InputEvent.BUTTON3_MASK) {
			try {
			    FormeDessin f = vg.en_dessous(x, y);
			} catch (NoSuchElementException e) {
			    maybeShowPopup(evt);
			}
		    }	    
		}
		public void mousePressed(MouseEvent evt) {
		    callPopup(evt);
		}
		public void mouseReleased(MouseEvent evt) {
		    callPopup(evt);
		}
	    });
    }
    
    private void maybeShowPopup(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
	    
    public Dimension getPreferredSize() {
	return new Dimension(StarData.ctxt_left + StarData.ray * 2 
			     + StarData.ctxt_right, 
			     StarData.ctxt_top + StarData.ray * 2
			     + StarData.ctxt_bottom);
    }
    
    public Dimension getMinimumSize() {
	return getPreferredSize();
    }
    
    public Dimension getMaxmimumSize() {
	return getPreferredSize();
    }

    public boolean isEmpty() {
	return empty;
    }

    public Star getStar() {
	if (isEmpty()) {
	    return null;
	} else {
	    return ConvertStarVueGraph.starVueGraphe2Star(vg, sommetC);
	}
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(Color.black);
	if (empty) {
	    g.drawString("No context", center.x - StarData.ray / 2, center.y);
	} else {
	    // Degree 0 of the circle
	    g.drawLine(center.x, center.y - StarData.ray - 2,
		       center.x, center.y - StarData.ray + 2);
	    
	    // Circle
	    Stroke tmp = ((Graphics2D) g).getStroke ();
	    ((Graphics2D) g).setStroke(dashS);
	    g.drawArc(center.x - StarData.ray, center.y - StarData.ray, 
		      StarData.ray * 2, StarData.ray * 2, 0, 360);
	    ((Graphics2D) g).setStroke(tmp);
	    vg.dessiner(this, g);
	}
    }

}

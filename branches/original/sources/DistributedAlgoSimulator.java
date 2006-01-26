import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.*;
import fr.enserb.das.gui.donnees.*;

public class DistributedAlgoSimulator extends JApplet implements ActionListener {

  public static final String nomDuProgramme = "DistributedAlgoSimulator";
  public static final short version_major = 2;
  public static final short version_minor = 0;
  public static final short patchLevel = 1;
  public static final String auteur = "Olivier FAUCHILLE/Sanaa ACHARGUI/Amah AHITE/Thierry VERDIER";
  public static final String date = "(Juin 2001)";

  public static final String names[] = {"Graph Editor"};
    //public static final String editeurs[] = {"fr.enserb.das.gui.presentation.userInterfaceEdition.Editeur"};

  protected static boolean est_standalone;
 // protected static int compteur_editeur;
  protected static JApplet japplet = new JApplet();
  protected JButton new_graph, help;
  
  private boolean inAnApplet = true;
  
  //Hack to avoid ugly message about system event access check.
  public DistributedAlgoSimulator() {
    this(true);
  }
  
  public DistributedAlgoSimulator(boolean inAnApplet) {
    this.inAnApplet = inAnApplet;
    if (inAnApplet) {
      getRootPane().putClientProperty("defeatSystemEventQueueCheck",
				      Boolean.TRUE);
    }
  }
  
  public String getAppletInfo() {
    return nomDuProgramme + " v" + version_major + "." + version_minor +
           "." + patchLevel + " by " + auteur + " " + date;
  }

  public String[][] getParameterInfo() {
    String pinfo[][] = {};
    return pinfo;
  }

  public static boolean estStandalone() {
    return est_standalone;
  }


  public static JApplet applet() {
    return japplet;
  }

   public void init() {
    est_standalone = false;
    TableImages.setTableImages(this); // fill the tables of images
    TableAlgo.setTableAlgo(this); // fill the table of  alforithms
        
    Font f1 = new Font("Helvetica", Font.BOLD, 18);

    new_graph = new JButton("New Graph");
    new_graph.setMnemonic('N');
    new_graph.setActionCommand("New Graph");
    new_graph.setFont(f1);
    
    help = new JButton("Help");
    help.setMnemonic('H');
    help.setActionCommand("Help");
    help.setFont(f1);
    
    //Listen for actions on buttons.
    new_graph.addActionListener(this);
    help.addActionListener(this);
    
    //Add Components to a JPanel, using GridLayout. 
    Container pane = this.getContentPane();
    pane.setLayout(new GridLayout(2,1));
    pane.add(new_graph);
    pane.add(help);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("New Graph")) {
    
	Editeur ed = new Editeur();
	ed.show();
	
    } else if (e.getActionCommand().equals("Help")) { 
      JOptionPane.showMessageDialog(this,
				    "DistributedAlgoSimulator, v1\n" +
				    "you edit a graph with the GraphEditor\n" +
				    "to simulate an algorithm on this graph \n"
				    + "you push on simulation button\n"+
				    "you must choose exclusively an algorithm\n "+
				    "or a list of rules \n"+
				    "finally you push on start\n");


    }
  }
  
  public static void main(String[] args) {
      TableImages.setTableImages(japplet.getToolkit()); // fill the tables of images
      est_standalone = true;
      Editeur ed = new Editeur();
      ed.show();
  }
}


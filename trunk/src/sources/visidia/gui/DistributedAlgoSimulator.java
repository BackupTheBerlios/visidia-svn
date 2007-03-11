package visidia.gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import visidia.gui.donnees.TableAlgo;
import visidia.gui.donnees.TableImages;
import visidia.gui.presentation.userInterfaceEdition.Editeur;

public class DistributedAlgoSimulator extends JApplet implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7028894819202792458L;

	public static final String nomDuProgramme = "DistributedAlgoSimulator";

	public static final short version_major = 2;

	public static final short version_minor = 0;

	public static final short patchLevel = 1;

	public static final String auteur = "Olivier FAUCHILLE/Sanaa ACHARGUI/Amah AHITE/Thierry VERDIER";

	public static final String date = "(Juin 2001)";

	public static final String names[] = { "Graph Editor" };

	// public static final String editeurs[] =
	// {"visidia.gui.presentation.userInterfaceEdition.Editeur"};

	protected static boolean est_standalone;

	// protected static int compteur_editeur;
	protected static JApplet japplet = new JApplet();

	protected JButton new_graph, help;

	// Hack to avoid ugly message about system event access check.
	public DistributedAlgoSimulator() {
		this(true);
	}

	public DistributedAlgoSimulator(boolean inAnApplet) {
		if (inAnApplet) {
			this.getRootPane().putClientProperty(
					"defeatSystemEventVQueueCheck", Boolean.TRUE);
		}
	}

	public String getAppletInfo() {
		return DistributedAlgoSimulator.nomDuProgramme + " v"
				+ DistributedAlgoSimulator.version_major + "."
				+ DistributedAlgoSimulator.version_minor + "."
				+ DistributedAlgoSimulator.patchLevel + " by "
				+ DistributedAlgoSimulator.auteur + " "
				+ DistributedAlgoSimulator.date;
	}

	public String[][] getParameterInfo() {
		String pinfo[][] = {};
		return pinfo;
	}

	public static boolean estStandalone() {
		return DistributedAlgoSimulator.est_standalone;
	}

	public static JApplet applet() {
		return DistributedAlgoSimulator.japplet;
	}

	public void init() {
		DistributedAlgoSimulator.est_standalone = false;
		TableImages.setTableImages(this); // fill the tables of images
		TableAlgo.setTableAlgo(this); // fill the table of alforithms

		Font f1 = new Font("Helvetica", Font.BOLD, 18);

		this.new_graph = new JButton("New Graph");
		this.new_graph.setMnemonic('N');
		this.new_graph.setActionCommand("New Graph");
		this.new_graph.setFont(f1);

		this.help = new JButton("Help");
		this.help.setMnemonic('H');
		this.help.setActionCommand("Help");
		this.help.setFont(f1);

		// Listen for actions on buttons.
		this.new_graph.addActionListener(this);
		this.help.addActionListener(this);

		// Add Components to a JPanel, using GridLayout.
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(2, 1));
		pane.add(this.new_graph);
		pane.add(this.help);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Graph")) {

			Editeur ed = new Editeur();
			ed.setVisible(true);

		} else if (e.getActionCommand().equals("Help")) {
			JOptionPane.showMessageDialog(this,
					"DistributedAlgoSimulator, v1\n"
							+ "you edit a graph with the GraphEditor\n"
							+ "to simulate an algorithm on this graph \n"
							+ "you push on simulation button\n"
							+ "you must choose exclusively an algorithm\n "
							+ "or a list of rules \n"
							+ "finally you push on start\n");

		}
	}

	public static void main(String[] args) {
		TableImages.setTableImages(DistributedAlgoSimulator.japplet
				.getToolkit()); // fill the tables
		// of images
		DistributedAlgoSimulator.est_standalone = true;
		Editeur ed = new Editeur();
		ed.setVisible(true);
	}
}

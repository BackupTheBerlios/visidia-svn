package visidia.gui.metier.inputOutput;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import visidia.gui.metier.Arete;
import visidia.gui.metier.Graphe;
import visidia.gui.metier.Sommet;
import visidia.gui.presentation.userInterfaceEdition.Fenetre;

/* this class permit to make the exportation of graph in GML format */

public class GMLParser extends JFileChooser implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2571602695974514717L;

	/** The parent window */
	protected Fenetre parent;

	/** a "valid" file : with the good extension */
	protected File validFile;

	/** the current indentation used in the file GML */
	protected static String indentString = new String("");

	/** The current path for exporting. */
	protected String path;

	/** the file writer to write */
	protected static FileWriter fileWriter;

	/** boolean to know if we display graphics informations */
	private static boolean graphicsInfo = true;

	/* button for the graphics properties */
	JRadioButton graphicsButton;

	/**
	 * Instancies a new GMLParser object, wich will pemit to export the graph
	 * which is in the "parent" window, in a file of the directory "path".
	 */

	public GMLParser(Fenetre parent, String path, Graphe graphe) {
		super(path);
		this.path = path;

		this.validFile = new File(path, "default.gml");
		this.setSelectedFile(this.validFile);
		this.parent = parent;

	}

	/**
	 * This method permit to get the name of the file chosen by the user We take
	 * care here of errors and warnings (existant file, extension errors...)
	 */

	public void approveSelection() {
		boolean save = true;
		File f = this.getSelectedFile();
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if ((i > 0) && (i < s.length() - 1)) {
			String extension = s.substring(i + 1).toLowerCase();
			if (!extension.equals("gml")) {
				JOptionPane.showMessageDialog(this, this.getName(f)
						+ " : this file has not a recognized\n"
						+ "extension. The required extension is '.gml ",
						"Warning", JOptionPane.WARNING_MESSAGE);
				this.setSelectedFile(this.validFile);
				save = false;

			}
		} else {
			if (i == -1) {
				this.setSelectedFile(new File(this.path, s + ".gml"));
				save = true;
			} else {
				this.setSelectedFile(new File(this.path, s + "gml"));
				save = true;
			}
		}

		if (this.getSelectedFile().exists()) {
			int overwrite = JOptionPane.showConfirmDialog(this, this
					.getName(this.getSelectedFile())
					+ " : this file aldready exists.\n"
					+ "Do you want to overwrite it ?", "Warning",
					JOptionPane.YES_NO_OPTION);
			if (overwrite == JOptionPane.YES_OPTION) {
				super.approveSelection();
			} else {
				this.setSelectedFile(this.validFile);
			}
		} else {
			if (save) {
				super.approveSelection();
			}
		}
	}

	/**
	 * Cancel the operation of exportation, or empty name file.
	 */

	public void cancelSelection() {
		if (this.getSelectedFile() == null) {
			JOptionPane.showMessageDialog(this,
					"You must choose a file to export your graph in !",
					"Warning", JOptionPane.WARNING_MESSAGE);
			this.setSelectedFile(this.validFile);
		} else {
			super.cancelSelection();
		}
	}

	/** Exporting a graphe in GML format */
	public static void export(Fenetre fenetre, Graphe graph) {

		GMLParser st = new GMLParser(fenetre, ".", graph);
		javax.swing.filechooser.FileFilter gmlFileFilter = new FileFilterGML();
		st.addChoosableFileFilter(gmlFileFilter);
		st.addTheButtons(st);
		st.setDialogTitle("Export in GML");
		st.setApproveButtonText("Export");
		st.setFileFilter(gmlFileFilter);

		int returnVal = st.showSaveDialog(fenetre);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = st.getSelectedFile();
			try {
				GMLParser.fileWriter = new FileWriter(f);
				GMLParser.creatingGMLFile(graph); // write in the object
													// output stream
			} catch (IOException e) {
				System.out.println("Problem: " + e);
			}
		}
	}

	// print the informations in the GML file
	private static void creatingGMLFile(Graphe graph) throws IOException {
		Enumeration e;
		GMLParser.write("graph [");
		GMLParser.indentMore();
		GMLParser.writeln("comment \"Graph make by VisDa\" ");
		// writeln("directed 1");
		e = graph.sommets();

		// for the nodes (the vertices)
		Sommet v;
		while (e.hasMoreElements()) {
			v = ((Sommet) e.nextElement());
			GMLParser.writeln("node [");
			GMLParser.indentMore();
			GMLParser.writeln("id " + v.getSommetDessin().getEtiquette());
			if (GMLParser.graphicsInfo) {
				GMLParser.writeln("graphics [");
				GMLParser.indentMore();
				GMLParser.writeln("x " + v.getSommetDessin().centreX());
				GMLParser.writeln("y " + v.getSommetDessin().centreY());
				GMLParser.indentLess();
				GMLParser.writeln("]");
			}
			GMLParser.indentLess();
			GMLParser.writeln("]");
		}

		// for the edges
		e = graph.aretes();
		Arete edg;
		while (e.hasMoreElements()) {
			edg = ((Arete) e.nextElement());
			GMLParser.writeln("edge [");
			GMLParser.indentMore();
			GMLParser.writeln("source "
					+ edg.origine().getSommetDessin().getEtiquette());
			GMLParser.writeln("target "
					+ edg.destination().getSommetDessin().getEtiquette());
			if (GMLParser.graphicsInfo) {
				GMLParser.writeln("graphics [");
				GMLParser.indentMore();

				GMLParser.writeln("Line [");
				GMLParser.indentMore();

				GMLParser.writeln("Point [");
				GMLParser.indentMore();
				GMLParser.writeln("x " + edg.getAreteDessin().origineX());
				GMLParser.writeln("y " + edg.getAreteDessin().origineY());
				GMLParser.indentLess();
				GMLParser.writeln("]");

				GMLParser.writeln("Point [");
				GMLParser.indentMore();
				GMLParser.writeln("x " + edg.getAreteDessin().destinationX());
				GMLParser.writeln("y " + edg.getAreteDessin().destinationY());
				GMLParser.indentLess();
				GMLParser.writeln("]");

				GMLParser.indentLess();
				GMLParser.writeln("]"); // for line

				GMLParser.indentLess();
				GMLParser.writeln("]"); // for graphics
			}
			GMLParser.indentLess();
			GMLParser.writeln("]"); // for edges
		}
		GMLParser.indentLess();
		GMLParser.writeln("]"); // for graph

		GMLParser.fileWriter.close();
	}

	// write the string in the buffer
	private static void write(String stringToWrite) throws IOException {
		GMLParser.fileWriter.write(stringToWrite);
		GMLParser.fileWriter.flush();
	}

	// write the string in a new line, with the rigth indentation
	private static void writeln(String stringToWrite) throws IOException {
		GMLParser.fileWriter.write("\n" + GMLParser.indentString
				+ stringToWrite);
		GMLParser.fileWriter.flush();
	}

	// add some spaces to the indent string
	private static void indentMore() {
		GMLParser.indentString = GMLParser.indentString.concat("   ");
	}

	// delete some spaces to the indent string
	private static void indentLess() {
		if (GMLParser.indentString.length() < 3) {
			GMLParser.indentString = new String("");
		} else {
			GMLParser.indentString = GMLParser.indentString.substring(0,
					GMLParser.indentString.length() - 3);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.graphicsButton) {
			GMLParser.graphicsInfo = this.graphicsButton.isSelected();
		}
	}

	private void addTheButtons(JFileChooser dialog) {
		JPanel buttonPane = new JPanel(new FlowLayout());
		JLabel labelGraphics = new JLabel("Print graphics coordinates ");

		this.graphicsButton = new JRadioButton();
		this.graphicsButton.setSelected(GMLParser.graphicsInfo); // is the
																	// button
		// selected ?
		this.graphicsButton.addActionListener(this);

		buttonPane.add(labelGraphics);
		buttonPane.add(this.graphicsButton);

		dialog.add(buttonPane, BorderLayout.SOUTH);
	}

}

package visidia.gui.metier.inputOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import visidia.gui.metier.Graphe;
import visidia.gui.presentation.userInterfaceEdition.Editeur;
import visidia.gui.presentation.userInterfaceEdition.Fenetre;

/**
 * This class permit to save graphs in text files
 */

public class SaveFile extends JFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8481113962193773634L;

	public static FileOutputStream fos;

	public static ObjectOutputStream oos;

	/** The parent window(from where the saving is called) */
	protected Fenetre parent;

	/** a valid file with good extension */
	protected File validFile;

	/** Number of file names already generated */
	protected static int nombre = 0;

	/** the path of the current directory for saving */
	protected String path;

	/** constructor to save the graph in the parent window and the path directory */
	public SaveFile(Fenetre parent, String path, Graphe graphe) {
		super(path);
		this.path = path;

		if (parent.type().equals("Editor")) {
			((Editeur) parent).commandeRenumeroter();
		}

		SaveFile.nombre++;
		this.validFile = new File(path, "noname_"
				+ Integer.toString(SaveFile.nombre) + ".graph");
		this.setSelectedFile(this.validFile);
		this.parent = parent;

	}

	/**
	 * this method permit to gave the file name given by the user Here we deal
	 * with errors and warnings (existing files, extension errors ...)
	 */
	public void approveSelection() {
		boolean save = true;
		File f = this.getSelectedFile();
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if ((i > 0) && (i < s.length() - 1)) {
			String extension = s.substring(i + 1).toLowerCase();
			if (!extension.equals("graph")) {
				JOptionPane.showMessageDialog(this, this.getName(f)
						+ " : this file has not a recognized\n"
						+ "extension. The required extension is '.graph ",
						"Warning", JOptionPane.WARNING_MESSAGE);
				this.setSelectedFile(this.validFile);
				save = false;

			}
		} else {
			if (i == -1) {
				this.setSelectedFile(new File(this.path, s + ".graph"));
				save = true;
			} else {
				this.setSelectedFile(new File(this.path, s + "graph"));
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
	 * Reaction when we use the "cancel" button or validation of an empty file
	 */
	public void cancelSelection() {
		SaveFile.nombre--;
		if (this.getSelectedFile() == null) {
			JOptionPane.showMessageDialog(this,
					"You must choose a file to save your graph in !",
					"Warning", JOptionPane.WARNING_MESSAGE);
			this.setSelectedFile(this.validFile);
		} else {
			super.cancelSelection();
		}
	}

	/** Method called for saving a graph */
	public static void save(Fenetre fenetre, Graphe graphe) {
		fenetre.selection.desenluminer();
		SaveFile st = new SaveFile(fenetre, ".", graphe);
		javax.swing.filechooser.FileFilter graphFileFilter = new FileFilterGraph();
		st.addChoosableFileFilter(graphFileFilter);
		st.setFileFilter(graphFileFilter);

		File f = fenetre.fichier_edite();

		if (f == null) {
			int returnVal = st.showSaveDialog(fenetre);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				f = st.getSelectedFile();
				try {
					SaveFile.fos = new FileOutputStream(f);
					SaveFile.oos = new ObjectOutputStream(SaveFile.fos);
					SaveFile.oos.writeObject(graphe);
					fenetre.mettreAJourTitreFenetre(f);
					SaveFile.oos.close();
					SaveFile.fos.close();

				} catch (IOException e) {
					System.out.println("Problem: " + e);
				}
			}
		} else {
			try {
				SaveFile.fos = new FileOutputStream(f);
				SaveFile.oos = new ObjectOutputStream(SaveFile.fos);
				SaveFile.oos.writeObject(graphe);
				fenetre.mettreAJourTitreFenetre(f);
				SaveFile.oos.close();
				SaveFile.fos.close();

			} catch (IOException e) {
				System.out.println("Problem: " + e);
			}
		}
		fenetre.selection.select();
	}

	public static void saveAs(Fenetre fenetre, Graphe graphe) {
		fenetre.selection.desenluminer();
		SaveFile st = new SaveFile(fenetre, ".", graphe);
		javax.swing.filechooser.FileFilter graphFileFilter = new FileFilterGraph();
		st.addChoosableFileFilter(graphFileFilter);
		st.setFileFilter(graphFileFilter);

		File f = fenetre.fichier_edite();
		int returnVal = st.showSaveDialog(fenetre);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = st.getSelectedFile();
			try {
				SaveFile.fos = new FileOutputStream(f);
				SaveFile.oos = new ObjectOutputStream(SaveFile.fos);
				SaveFile.oos.writeObject(graphe);
				fenetre.mettreAJourTitreFenetre(f);
				SaveFile.oos.close();
				SaveFile.fos.close();

			} catch (IOException e) {
				System.out.println("Problem: " + e);
			}

		}
		fenetre.selection.select();
	}

}

package visidia.gui.metier.inputOutput;

import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.JFileChooser;

import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;

public class OpenAgents implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -271522070707209586L;

	protected static final String dir = new String("visidia/agents");

	/**
	 * Open ".class" file for agents simulation agents are affected to chosen
	 * vertices
	 */
	public static boolean open(Enumeration e, AgentsSimulationWindow window) {
		File file_open = null;
		JFileChooser fc = new JFileChooser(OpenAgents.dir);
		javax.swing.filechooser.FileFilter classFileFilter = new FileFilterClass();
		fc.addChoosableFileFilter(classFileFilter);
		fc.setFileFilter(classFileFilter);

		int returnVal = fc.showOpenDialog(window);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file_open = fc.getSelectedFile();
		}

		String file_name = fc.getName(file_open);
		if (file_name == null) {
			return false; // if canceled
		}
		window.mettreAJourTitreFenetre(file_name);

		int index = file_name.lastIndexOf('.');
		String className = file_name.substring(0, index);

		try {
			Integer id;
			while (e.hasMoreElements()) {
				id = Integer.decode(((SommetDessin) e.nextElement())
						.getEtiquette());
				window.addAgents(id, className);
				// window.getVueGraphe().
				// rechercherSommet(id.toString()).
				// changerCouleurFond(Color.red);

			}
		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
		window.selection.deSelect();
		return true;
	}

}

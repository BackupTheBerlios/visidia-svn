package visidia.gui.metier.inputOutput;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.JFileChooser;

import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;
import visidia.simulation.agents.AgentChooser;

public class OpenAgentChooser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3894605768479026621L;

	protected static final String dir = new String(
			"visidia/agents/agentchooser");

	public static boolean open(AgentsSimulationWindow window) {

		File file_open = null;
		JFileChooser fc = new JFileChooser(OpenAgentChooser.dir);
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
		String className = "visidia.agents.agentchooser."
				+ file_name.substring(0, index);

		try {
			Class classChooser = Class.forName(className);
			Constructor constructor = classChooser.getConstructor();
			AgentChooser chooser = (AgentChooser) constructor.newInstance();
			Method placeAgentsMethod;

			placeAgentsMethod = (AgentChooser.class).getDeclaredMethod(
					"placeAgents", AgentsSimulationWindow.class);

			placeAgentsMethod.invoke(chooser, window);
		} catch (Exception excpt) {
			throw new RuntimeException("The agent chooser can't be created",
					excpt);
		}
		return true;
	}
}

package visidia.gui.metier.inputOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import visidia.tools.LocalNodeTable;

public class OpenConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3396865071254228328L;

	private static final String DEFAULT_URL_FOR_VISU = "Simulator";

	private Hashtable parameters = new Hashtable();

	private String visuHost;

	private String visuUrl;

	private int hostNummer = 0;

	/**
	 * Open ".config" file to set hosts for the Nodes
	 */

	// public void open(FenetreDeSimulationDist fenetre, VueGraphe vueGraphe){
	public void open(FenetreDeSimulationDist fenetre, int sizeOfTheGraph) {
		File file_open = null;
		JFileChooser fc = new JFileChooser("fr/enserb/das/config");
		javax.swing.filechooser.FileFilter hostFileFilter = new FileFilterConfig();
		fc.addChoosableFileFilter(hostFileFilter);
		fc.setFileFilter(hostFileFilter);

		int returnVal = fc.showOpenDialog(fenetre);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file_open = fc.getSelectedFile();
		}

		String file_name = fc.getName(file_open);
		if (file_name == null) {
			return; // must implement "cancel" button action
		}
		try {
			FileReader fr = new FileReader(file_open);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				if (this.visuHost == null) {
					this.setVisu(line);
				} else {
					this.handleLine(line);
				}
			}
			fr.close();

			fenetre.setNetworkParam(this.createNetworkParam(sizeOfTheGraph),
					this.visuHost, this.visuUrl);

		} catch (Exception excpt) {
			System.out.println("Problem : " + excpt);
			excpt.printStackTrace();
		}
	}

	private void setVisu(String ligne) {
		StringTokenizer st = new StringTokenizer(ligne);
		while (st.hasMoreTokens()) {
			if (this.visuHost == null) {
				this.visuHost = st.nextToken();
			} else if (this.visuUrl == null) {
				this.visuUrl = st.nextToken();
			}
		}
		if (this.visuUrl == null) {
			this.visuUrl = OpenConfig.DEFAULT_URL_FOR_VISU;
		}

	}

	private void handleLine(String ligne) {
		StringTokenizer st = new StringTokenizer(ligne);
		String host = null;

		// vecteur contenant les urls des noeuds locaux declare sur la machine
		// host
		Vector vect = new Vector();
		while (st.hasMoreTokens()) {
			if (host == null) {
				host = st.nextToken();
			} else {
				String url = st.nextToken();
				vect.addElement(url);
			}
		}
		if (vect.isEmpty()) {
			this.hostNummer += 1;
			vect.addElement(host);
			this.parameters.put(host, vect);
		} else {
			this.hostNummer += vect.size();
			this.parameters.put(host, vect);
		}
	}

	public LocalNodeTable createNetworkParam(int sizeOfTheGraph) {
		LocalNodeTable lnt = new LocalNodeTable();
		if (sizeOfTheGraph <= this.hostNummer) {
			int i = 0;
			Enumeration hosts = this.parameters.keys();
			while (hosts.hasMoreElements() && (i < sizeOfTheGraph)) {
				String aHost = (String) hosts.nextElement();
				Vector localNodes = (Vector) this.parameters.get(aHost);
				while (!localNodes.isEmpty() && (i < sizeOfTheGraph)) {
					String localNode = (String) localNodes.remove(0);
					Vector v = new Vector();
					v.addElement(new Integer(i));
					lnt.addLocalNode(aHost, localNode, v);
					i++;
				}
			}
		} else {
			int pas = sizeOfTheGraph / this.hostNummer;
			int reste = sizeOfTheGraph - this.hostNummer * pas;
			Enumeration hosts = this.parameters.keys();
			int current = 0;

			while (hosts.hasMoreElements()) {
				String aHost = (String) hosts.nextElement();
				Vector localNodes = (Vector) this.parameters.get(aHost);
				while (!localNodes.isEmpty()) {
					String localNode = (String) localNodes.remove(0);
					if (reste > 0) {
						Vector vect = new Vector();
						for (int j = current; j < current + pas + 1; j++) {
							vect.addElement(new Integer(j));
						}

						lnt.addLocalNode(aHost, localNode, vect);
						current = current + pas + 1;
						reste -= 1;
					} else {
						Vector vect = new Vector();
						for (int j = current; j < current + pas; j++) {
							vect.addElement(new Integer(j));
						}
						lnt.addLocalNode(aHost, localNode, vect);
						current = current + pas;
					}
				}
			}
		}
		return lnt;
	}
}

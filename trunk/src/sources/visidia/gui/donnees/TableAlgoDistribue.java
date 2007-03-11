package visidia.gui.donnees;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JApplet;

import visidia.algoRMI.ColorationRmi;
import visidia.algoRMI.ElectionRmi;
import visidia.simulation.AlgorithmDist;

public class TableAlgoDistribue {

	protected static Hashtable<String, AlgorithmDist> tableauAlgo;

	public static void setTableAlgo(JApplet uneApplet) {
		tableauAlgo = new Hashtable<String, AlgorithmDist>();

		// String jarAdress = new
		// String("jar:"+uneApplet.getCodeBase()+"DistributedAlgoSimulator.jar!/");

		try {
			tableauAlgo.put("Remote Election", new ElectionRmi());
			tableauAlgo.put("Remote Coloration", new ColorationRmi());
		} catch (Exception e) {
		}
	}

	// returns the image by the key parameter

	public static AlgorithmDist getAlgo(String key) {
		if (tableauAlgo.containsKey(key))
			return (AlgorithmDist) (tableauAlgo.get(key));
		else
			return null;
	}

	public static Vector getKeys() {
		Enumeration e = tableauAlgo.keys();
		Vector result = new Vector();

		while (e.hasMoreElements())
			result.add(e.nextElement());
		return result;
	}

}

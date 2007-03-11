package visidia.gui.metier.inputOutput;

import java.io.Serializable;

import visidia.gui.donnees.TableAlgoDistribue;
import visidia.gui.presentation.boite.BoiteAlgoAppletDistribue;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

public class OpenAlgoAppletDistribue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7091632679292105738L;

	public static FenetreDeSimulationDist fenetre;

	/**
	 * 
	 * Open ".class" file for a simulation algorithm from the applet the
	 * algorithm is affected to all the vertices
	 */

	public static void open(FenetreDeSimulationDist fenet) {
		fenetre = fenet;
		BoiteAlgoAppletDistribue box = new BoiteAlgoAppletDistribue(fenetre,
				TableAlgoDistribue.getKeys());
		box.show();
	}

	public static void setAlgorithm(String key) {
		try {
			fenetre.setAlgo(TableAlgoDistribue.getAlgo(key));
		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
	}

}

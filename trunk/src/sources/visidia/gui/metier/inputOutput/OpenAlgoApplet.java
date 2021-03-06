package visidia.gui.metier.inputOutput;

import java.io.Serializable;
import java.util.Enumeration;

import visidia.gui.donnees.TableAlgo;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.boite.BoiteAlgoApplet;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;

public class OpenAlgoApplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8588944438817119518L;

	public static FenetreDeSimulation fenetre;

	/**
	 * 
	 * Open ".class" file for a simulation algorithm from the applet the
	 * algorithm is affected to all the vertices
	 */
	public static void open(FenetreDeSimulation fenet) {
		OpenAlgoApplet.fenetre = fenet;
		BoiteAlgoApplet box = new BoiteAlgoApplet(OpenAlgoApplet.fenetre,
				TableAlgo.getKeys());
		box.show();
	}

	public static void setAlgorithm(String key) {
		try {
			OpenAlgoApplet.fenetre.getAlgorithms().putAlgorithmToAllVertices(
					TableAlgo.getAlgo(key));
			OpenAlgoApplet.fenetre.getMenuChoice().setListTypes(
					(TableAlgo.getAlgo(key)).getListTypes());
			System.err.println("Changement du menu reussi\n");
		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
	}

	/*
	 * OPENING ALGORITHM FOR AN ENUMERATION OF VERTICES
	 * 
	 */

	public static void openForVertices(Enumeration vertices,
			FenetreDeSimulation fenet) {
		OpenAlgoApplet.fenetre = fenet;
		BoiteAlgoApplet box = new BoiteAlgoApplet(OpenAlgoApplet.fenetre,
				TableAlgo.getKeys(), vertices);
		box.show();
	}

	public static void setAlgorithmForVertices(String key, Enumeration e) {
		try {
			String id;
			while (e.hasMoreElements()) {
				id = ((SommetDessin) e.nextElement()).getEtiquette();
				OpenAlgoApplet.fenetre.getAlgorithms().putAlgorithm(id,
						TableAlgo.getAlgo(key));
				OpenAlgoApplet.fenetre.getMenuChoice().addAtListTypes(
						(TableAlgo.getAlgo(key)).getListTypes());
			}
		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
	}

}

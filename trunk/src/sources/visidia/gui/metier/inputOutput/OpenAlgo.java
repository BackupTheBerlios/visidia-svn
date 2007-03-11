package visidia.gui.metier.inputOutput;

import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.JFileChooser;

import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;
import visidia.simulation.Algorithm;
import visidia.simulation.SyncAlgorithm;

public class OpenAlgo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 45214117913549595L;

	/**
	 * Open ".class" file for a simulation algorithm the algorithm is affected
	 * to all the vertices
	 */
	public static boolean open(FenetreDeSimulation fenetre) {
		File file_open = null;
		JFileChooser fc = new JFileChooser("visidia/algo");
		javax.swing.filechooser.FileFilter classFileFilter = new FileFilterClass();
		fc.addChoosableFileFilter(classFileFilter);
		fc.setFileFilter(classFileFilter);

		int returnVal = fc.showOpenDialog(fenetre);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file_open = fc.getSelectedFile();
		}

		String file_name = fc.getName(file_open);
		if (file_name == null) {
			return false; // if canceled
		}
		fenetre.mettreAJourTitreFenetre(file_name);

		int index = file_name.lastIndexOf('.');
		String className = file_name.substring(0, index);
		System.out.println(className);
		try {
			Algorithm a;
			try {
				a = (Algorithm) Class.forName(className).newInstance();
				fenetre.getAlgorithms().putAlgorithmToAllVertices(
						(Algorithm) a.clone());
				fenetre.getMenuChoice().setListTypes(
						((Algorithm) Class.forName(className).newInstance())
								.getListTypes());
			} catch (Exception e1) {
				// PFA2003
				try {
					a = (Algorithm) Class.forName("visidia.algo." + className)
							.newInstance();

					fenetre.getAlgorithms().putAlgorithmToAllVertices(
							(Algorithm) a.clone());
					fenetre.getMenuChoice().setListTypes(
							((Algorithm) Class.forName(
									"visidia.algo." + className).newInstance())
									.getListTypes());
					System.out.println(Class.forName(
							"visidia.algo." + className).newInstance()
							.getClass());
				} catch (Exception e) {
					a = (SyncAlgorithm) Class.forName(
							"visidia.algo.synchronous." + className)
							.newInstance();
					fenetre.getAlgorithms().putAlgorithmToAllVertices(
							(SyncAlgorithm) a.clone());
					fenetre.getMenuChoice().setListTypes(
							((SyncAlgorithm) Class.forName(
									"visidia.algo.synchronous." + className)
									.newInstance()).getListTypes());
					System.out.println(Class.forName(
							"visidia.algo.synchronous." + className)
							.newInstance().getClass());
				}
			}
		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
		return true;
	}

	/**
	 * Open ".class" file for a simulation algorithm Algorithm is affected to
	 * vertex number id
	 */
	public static boolean openForVertex(Enumeration e,
			FenetreDeSimulation fenetre) {

		File file_open = null;
		JFileChooser fc = new JFileChooser("visidia/algo");
		javax.swing.filechooser.FileFilter classFileFilter = new FileFilterClass();
		fc.addChoosableFileFilter(classFileFilter);
		fc.setFileFilter(classFileFilter);

		int returnVal = fc.showOpenDialog(fenetre);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file_open = fc.getSelectedFile();
		}

		String file_name = fc.getName(file_open);
		if (file_name == null) {
			return false; // if canceled
		}
		fenetre.mettreAJourTitreFenetre(file_name);

		int index = file_name.lastIndexOf('.');
		String className = file_name.substring(0, index);
		try {
			String id;
			while (e.hasMoreElements()) {
				id = ((SommetDessin) e.nextElement()).getEtiquette();
				System.out.println("Etiquette de sommet : " + id);
				fenetre.getAlgorithms().putAlgorithm(
						id,
						(Algorithm) Class.forName("visidia.algo." + className)
								.newInstance());
				fenetre.getMenuChoice().addAtListTypes(
						((Algorithm) Class.forName("visidia.algo." + className)
								.newInstance()).getListTypes());// we set the
				// list of types
				// used in the
				// chosen
				// algorithm
			}

		} catch (Exception excpt) {
			System.out.println("Problem: " + excpt);
		}
		return true;
	}

}

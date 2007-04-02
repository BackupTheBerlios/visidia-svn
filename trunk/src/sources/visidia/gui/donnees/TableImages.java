package visidia.gui.donnees;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JApplet;

public class TableImages {

	protected static Hashtable<String, java.awt.Image> tableauImages;

	public static void setTableImages(JApplet uneApplet) {
		TableImages.tableauImages = new Hashtable<String, java.awt.Image>();

		String jarAdress = new String("jar:" + uneApplet.getCodeBase()
				+ "DistributedAlgoSimulator.jar!/");

		try {
			TableImages.tableauImages.put("help", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/aide.gif"));
			TableImages.tableauImages.put("tree", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/arbre.gif"));
			TableImages.tableauImages.put("disk", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/disk.gif"));
			TableImages.tableauImages.put("duplicate", uneApplet.getImage(
					new URL(jarAdress),
					"visidia/gui/donnees/images/dupliquer.gif"));
			TableImages.tableauImages.put("go", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/go.gif"));
			TableImages.tableauImages.put("graph", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/graph.gif"));
			TableImages.tableauImages.put("image1", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image1.gif"));
			TableImages.tableauImages.put("image2", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image2.gif"));
			TableImages.tableauImages.put("image3", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image3.gif"));
			TableImages.tableauImages.put("image4", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image4.gif"));
			TableImages.tableauImages.put("image5", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image5.gif"));
			TableImages.tableauImages.put("image6", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/image6.jpg"));
			TableImages.tableauImages.put("image6bmp", uneApplet
					.getImage(new URL(jarAdress),
							"visidia/gui/donnees/images/image6.bmp"));
			TableImages.tableauImages.put("info", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/info.gif"));
			TableImages.tableauImages.put("new", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/nouveau.gif"));
			TableImages.tableauImages.put("open", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/ouvrir.gif"));
			TableImages.tableauImages.put("redo", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/redo.gif"));
			TableImages.tableauImages.put("stat", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/stat.gif"));
			TableImages.tableauImages.put("undo", uneApplet.getImage(new URL(
					jarAdress), "visidia/gui/donnees/images/undo.gif"));
			TableImages.tableauImages.put("globalClock", uneApplet.getImage(
					new URL(jarAdress),
					"visidia/gui/donnees/images/globalClock.gif"));
			TableImages.tableauImages.put("noGlobalClock", uneApplet.getImage(
					new URL(jarAdress),
					"visidia/gui/donnees/images/noGlobalClock.gif"));
		} catch (Exception e) {
			System.out.println("Problème lors du chargement des images");
			// e.printStackTrace();
		}
	}

	public static void setTableImages(Toolkit tk) {
		try {
			TableImages.tableauImages = new Hashtable<String, Image>();
			TableImages.tableauImages.put("help", tk
					.getImage("visidia/gui/donnees/images/aide.gif"));
			TableImages.tableauImages.put("tree", tk
					.getImage("visidia/gui/donnees/images/arbre.gif"));
			TableImages.tableauImages.put("disk", tk
					.getImage("visidia/gui/donnees/images/disk.gif"));
			TableImages.tableauImages.put("duplicate", tk
					.getImage("visidia/gui/donnees/images/dupliquer.gif"));
			TableImages.tableauImages.put("go", tk
					.getImage("visidia/gui/donnees/images/go.gif"));
			TableImages.tableauImages.put("graph", tk
					.getImage("visidia/gui/donnees/images/graph.gif"));
			TableImages.tableauImages.put("image1", tk
					.getImage("visidia/gui/donnees/images/image1.gif"));
			TableImages.tableauImages.put("image2", tk
					.getImage("visidia/gui/donnees/images/image2.gif"));
			TableImages.tableauImages.put("image3", tk
					.getImage("visidia/gui/donnees/images/image3.gif"));
			TableImages.tableauImages.put("image4", tk
					.getImage("visidia/gui/donnees/images/image4.gif"));
			TableImages.tableauImages.put("image5", tk
					.getImage("visidia/gui/donnees/images/image5.gif"));
			TableImages.tableauImages.put("image6", tk
					.getImage("visidia/gui/donnees/images/image6.jpg"));
			TableImages.tableauImages.put("image6bmp", tk
					.getImage("visidia/gui/donnees/images/image6.bmp"));
			TableImages.tableauImages.put("info", tk
					.getImage("visidia/gui/donnees/images/info.gif"));
			TableImages.tableauImages.put("new", tk
					.getImage("visidia/gui/donnees/images/nouveau.gif"));
			TableImages.tableauImages.put("open", tk
					.getImage("visidia/gui/donnees/images/ouvrir.gif"));
			TableImages.tableauImages.put("redo", tk
					.getImage("visidia/gui/donnees/images/redo.gif"));
			TableImages.tableauImages.put("stat", tk
					.getImage("visidia/gui/donnees/images/stat.gif"));
			TableImages.tableauImages.put("undo", tk
					.getImage("visidia/gui/donnees/images/undo.gif"));
			TableImages.tableauImages.put("globalClock", tk
					.getImage("visidia/gui/donnees/images/globalClock.gif"));
			TableImages.tableauImages.put("noGlobalClock", tk
					.getImage("visidia/gui/donnees/images/noGlobalClock.gif"));
			TableImages.tableauImages.put("homme", tk
					.getImage("visidia/gui/donnees/images/homme2.gif"));
			TableImages.tableauImages.put("miroirHomme", tk
					.getImage("visidia/gui/donnees/images/homme2miroir.gif"));
			TableImages.tableauImages.put("agentwb", tk
					.getImage("visidia/gui/donnees/images/agentwb.gif"));
			TableImages.tableauImages.put("agentkiller", tk
					.getImage("visidia/gui/donnees/images/agentkiller.gif"));
			TableImages.tableauImages.put("vertexwb", tk
					.getImage("visidia/gui/donnees/images/vertexwb.gif"));
			TableImages.tableauImages.put("vertexdefwb", tk
					.getImage("visidia/gui/donnees/images/vertexdefwb.gif"));
			TableImages.tableauImages.put("switchOff", tk
					.getImage("visidia/gui/donnees/images/switchOFF.gif"));
			TableImages.tableauImages.put("switchOn", tk
					.getImage("visidia/gui/donnees/images/switchON.gif"));
			TableImages.tableauImages.put("modeSuppression", tk
					.getImage("visidia/gui/donnees/images/modeSuppression.gif"));
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Problème lors du chargement des images");
		}
	}

	// returns the image by the key parameter

	public static Image getImage(String key) {
		if (TableImages.tableauImages.containsKey(key)) {
			return (Image) (TableImages.tableauImages.get(key));
		} else {
			System.out.println("Image " + key + " Non trouvée");
			return null;
		}
	}

}

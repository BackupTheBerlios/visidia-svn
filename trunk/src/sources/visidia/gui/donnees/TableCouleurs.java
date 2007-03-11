package visidia.gui.donnees;

import java.awt.Color;
import java.util.Hashtable;

/**
 * this class contains the Color Table for the application. There is no
 * constructor ...
 */
public class TableCouleurs {

	protected static Hashtable tableauCouleurs;

	public static void setTableCouleurs() {
		TableCouleurs.tableauCouleurs = new Hashtable();
		TableCouleurs.tableauCouleurs.put("A", Color.red);
		TableCouleurs.tableauCouleurs.put("B", new Color(239, 48, 178));
		TableCouleurs.tableauCouleurs.put("C", new Color(188, 123, 234));
		TableCouleurs.tableauCouleurs.put("D", new Color(123, 154, 234));
		TableCouleurs.tableauCouleurs.put("E", Color.yellow);
		TableCouleurs.tableauCouleurs.put("F", Color.blue);
		TableCouleurs.tableauCouleurs.put("G", new Color(217, 60, 115));
		TableCouleurs.tableauCouleurs.put("H", new Color(117, 35, 211));
		TableCouleurs.tableauCouleurs.put("I", new Color(34, 115, 58));
		TableCouleurs.tableauCouleurs.put("J", Color.magenta);
		TableCouleurs.tableauCouleurs.put("K", new Color(170, 206, 237));
		TableCouleurs.tableauCouleurs.put("L", new Color(215, 237, 92));
		TableCouleurs.tableauCouleurs.put("M", new Color(255, 113, 153));
		TableCouleurs.tableauCouleurs.put("N", Color.green);
		TableCouleurs.tableauCouleurs.put("O", new Color(161, 113, 255));
		TableCouleurs.tableauCouleurs.put("P", Color.cyan);
		TableCouleurs.tableauCouleurs.put("Q", Color.white);
		TableCouleurs.tableauCouleurs.put("R", new Color(231, 211, 22));
		TableCouleurs.tableauCouleurs.put("S", new Color(0, 95, 162));
		TableCouleurs.tableauCouleurs.put("T", new Color(255, 0, 140));
		TableCouleurs.tableauCouleurs.put("U", new Color(174, 23, 104));
		TableCouleurs.tableauCouleurs.put("V", new Color(138, 153, 207));
		TableCouleurs.tableauCouleurs.put("W", new Color(108, 155, 159));
		TableCouleurs.tableauCouleurs.put("X", new Color(164, 27, 120));
		TableCouleurs.tableauCouleurs.put("Y", new Color(98, 194, 239));
		TableCouleurs.tableauCouleurs.put("Z", new Color(145, 239, 98));

	}

	// returns the color table

	public static Hashtable getTableCouleurs() {
		return TableCouleurs.tableauCouleurs;
	}
}

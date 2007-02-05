package visidia.gui.donnees;

import java.util.*;
import java.awt.*;

/**
 * this class contains the Color Table for the application. There is
 * no constructor ...
 **/
public class TableCouleurs{

    protected static Hashtable tableauCouleurs ;

    public static void setTableCouleurs(){
	tableauCouleurs = new Hashtable();
	tableauCouleurs.put("A", Color.red);
	tableauCouleurs.put("B", new Color(239,48,178));
	tableauCouleurs.put("C", new Color(188,123,234));
	tableauCouleurs.put("D", new Color(123,154,234));
	tableauCouleurs.put("E", Color.yellow);
	tableauCouleurs.put("F", Color.blue);
	tableauCouleurs.put("G", new Color(217,60,115));
	tableauCouleurs.put("H", new Color(117,35,211));
	tableauCouleurs.put("I", new Color(34,115,58));
	tableauCouleurs.put("J", Color.magenta);
	tableauCouleurs.put("K", new Color(170,206,237));
	tableauCouleurs.put("L", new Color(215,237,92));
	tableauCouleurs.put("M", new Color(255,113,153));
	tableauCouleurs.put("N", Color.green);
	tableauCouleurs.put("O", new Color(161,113,255));
	tableauCouleurs.put("P", Color.cyan);
	tableauCouleurs.put("Q", Color.white);
	tableauCouleurs.put("R", new Color(231,211,22));
	tableauCouleurs.put("S", new Color(0,95,162));
	tableauCouleurs.put("T", new Color(255,0,140));
	tableauCouleurs.put("U", new Color(174,23,104));
	tableauCouleurs.put("V", new Color(138,153,207));
	tableauCouleurs.put("W", new Color(108,155,159));
	tableauCouleurs.put("X", new Color(164,27,120));
	tableauCouleurs.put("Y", new Color(98,194,239));
	tableauCouleurs.put("Z", new Color(145,239,98));
	
    }

   // returns the color table

    public static Hashtable getTableCouleurs(){
	return tableauCouleurs;
    }
}

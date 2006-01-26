package fr.enserb.das.gui.donnees;

import java.util.*;
import java.awt.*;

// this class contains the Color Table
// for the application
// there is no constructor ...

public class TableCouleurs{

    protected static Hashtable tableauCouleurs ;

    public static void setTableCouleurs(){
	tableauCouleurs = new Hashtable();
	tableauCouleurs.put("A", Color.red);
	tableauCouleurs.put("B", Color.green);
	tableauCouleurs.put("C", Color.white);
	tableauCouleurs.put("D", Color.yellow);
	tableauCouleurs.put("E", Color.red);
	tableauCouleurs.put("F", Color.blue);
	tableauCouleurs.put("G", Color.black);
	tableauCouleurs.put("H", Color.green);
	tableauCouleurs.put("I", Color.orange);
	tableauCouleurs.put("J", Color.magenta);
	tableauCouleurs.put("K", Color.red);
	tableauCouleurs.put("L", Color.yellow);
	tableauCouleurs.put("M", Color.blue);
	tableauCouleurs.put("N", Color.green);
	tableauCouleurs.put("O", Color.gray);
	tableauCouleurs.put("P", Color.cyan);
	tableauCouleurs.put("Q", Color.white);
	tableauCouleurs.put("R", Color.red);
	tableauCouleurs.put("S", Color.blue);
	tableauCouleurs.put("T", Color.yellow);
	tableauCouleurs.put("U", Color.gray);
	tableauCouleurs.put("V", Color.cyan);
	tableauCouleurs.put("W", Color.magenta);
	tableauCouleurs.put("X", Color.green);
	tableauCouleurs.put("Y", Color.red);
	tableauCouleurs.put("Z", Color.blue);
	
    }

   // returns the color table

    public static Hashtable getTableCouleurs(){
	return tableauCouleurs;
    }
}

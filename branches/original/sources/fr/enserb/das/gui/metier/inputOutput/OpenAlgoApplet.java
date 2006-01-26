package fr.enserb.das.gui.metier.inputOutput;

import sun.net.*;
import java.util.jar.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Enumeration;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.simulation.*;
import fr.enserb.das.algo.*;
import fr.enserb.das.gui.presentation.SommetDessin;
import fr.enserb.das.gui.presentation.boite.BoiteAlgoApplet;
import fr.enserb.das.gui.donnees.TableAlgo;

public class OpenAlgoApplet implements Serializable{
 
    public static FenetreDeSimulation fenetre;

    /** 
     *
	Open ".class" file for a simulation algorithm from the applet
	the algorithm is affected to all the vertices
    */
    public static void open(FenetreDeSimulation fenet){
	fenetre= fenet;
	BoiteAlgoApplet box = new BoiteAlgoApplet(fenetre,TableAlgo.getKeys());
	box.show();
    }

    
    
    public static void setAlgorithm(String key){
	try {
	    fenetre.getAlgorithms().putAlgorithmToAllVertices(TableAlgo.getAlgo(key));
	    fenetre. getMenuChoice(). setListTypes((TableAlgo.getAlgo(key)).getListTypes());
	    System.err.println("Changement du menu reussi\n");
	}
	catch(Exception excpt) {
	    System.out.println("Problem: " + excpt);
	}
    }

    /*
     * OPENING ALGORITHM FOR AN ENUMERATION OF VERTICES
     *
     */



    public static void openForVertices(Enumeration vertices,FenetreDeSimulation fenet){
	fenetre= fenet;
	BoiteAlgoApplet box = new BoiteAlgoApplet(fenetre,TableAlgo.getKeys(),vertices);
	box.show();
    }

    public static void setAlgorithmForVertices(String key,Enumeration e){
	try {
	    String id;
	    while (e.hasMoreElements()){
		id = ((SommetDessin)e.nextElement()).getEtiquette();
		fenetre.getAlgorithms().putAlgorithm(id, TableAlgo.getAlgo(key));
		fenetre. getMenuChoice(). addAtListTypes((TableAlgo.getAlgo(key)).getListTypes());
	    }
	}
	catch(Exception excpt) {
	    System.out.println("Problem: " + excpt);
	}
    }
    
}




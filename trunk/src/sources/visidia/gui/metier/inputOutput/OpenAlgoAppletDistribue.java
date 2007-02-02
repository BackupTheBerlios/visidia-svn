package visidia.gui.metier.inputOutput;

//import sun.net.*;
//import java.util.jar.*;
import java.io.*;
//import java.net.*;
//import javax.swing.*;
//import java.util.Enumeration;
import visidia.gui.presentation.userInterfaceSimulation.*;
//import visidia.simulation.*;
//import visidia.algo.*;
//import visidia.algoRMI.*;
//import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.boite.BoiteAlgoAppletDistribue;
import visidia.gui.donnees.TableAlgoDistribue;
//import visidia.network.*;


public class OpenAlgoAppletDistribue implements Serializable{
 
    public static FenetreDeSimulationDist fenetre;

    /** 
     *
	Open ".class" file for a simulation algorithm from the applet
	the algorithm is affected to all the vertices
    */
    
    public static void open(FenetreDeSimulationDist fenet){
	fenetre= fenet;
	BoiteAlgoAppletDistribue box = new BoiteAlgoAppletDistribue(fenetre,TableAlgoDistribue.getKeys());
	box.show();
    }
        
    public static void setAlgorithm(String key){
	try {
	    fenetre.setAlgo(TableAlgoDistribue.getAlgo(key));
	}
	catch(Exception excpt) {
	    System.out.println("Problem: " + excpt);
	}
    }
    
}


package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.*;
import java.util.Enumeration;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.simulation.*;
import visidia.gui.presentation.SommetDessin;
import java.net.URLClassLoader;
import java.net.URL;


public class OpenAlgoDistribue implements Serializable{
    
    /** Open  ".class" file for a simulation algorithm 
	the algorithm is affected to all the vertices*/
    public static void open(FenetreDeSimulationDist fenetre){
	File file_open = null; 
	JFileChooser fc = new JFileChooser("visidia/algoRMI");
	javax.swing.filechooser.FileFilter classFileFilter = new FileFilterClass();
	fc.addChoosableFileFilter(classFileFilter);
	fc.setFileFilter(classFileFilter);
      
	int returnVal = fc.showOpenDialog(fenetre);
	if(returnVal == JFileChooser.APPROVE_OPTION)
	    file_open = fc.getSelectedFile();
      
	String file_name = fc.getName(file_open);
	if (file_name == null) return; // if canceled
	int index = file_name.lastIndexOf('.');
	String className = file_name.substring(0,index);
	try {
	    if (Class.forName("visidia.algoRMI."+className).newInstance() instanceof AlgorithmDist ){
		fenetre.setTitle("Algorithm : "+className);
		fenetre.setAlgo((AlgorithmDist)Class.forName("visidia.algoRMI."+className).newInstance());
		//we set the list of types used in the chosen algorithm
		fenetre.getMenuChoice().setListTypes(((AlgorithmDist)Class.forName("visidia.algoRMI."+className).newInstance()).getListTypes());
	    }
	    
	}catch(Exception excpt) {
	    System.out.println("Problem: " + excpt);
	    excpt.printStackTrace();
	}
    }
}


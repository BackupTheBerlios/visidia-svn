package fr.enserb.das.gui.metier.inputOutput;

import java.io.*;
import javax.swing.*;
import java.util.Enumeration;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.simulation.*;
import fr.enserb.das.gui.presentation.SommetDessin;
import java.net.URLClassLoader;
import java.net.URL;


public class OpenAlgo implements Serializable{
    
    /** Open  ".class" file for a simulation algorithm 
	the algorithm is affected to all the vertices*/
  public static void open(FenetreDeSimulation fenetre){
      File file_open = null; 
      JFileChooser fc = new JFileChooser("fr/enserb/das/algo");
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
      System.out.println(className);
      try {
	  //  URL[] urlTable = new URL[1];
	  // urlTable[0] = file_open.getParentFile().toURL();
	  // URLClassLoader urlLoader = new URLClassLoader(urlTable);
	 
	  fenetre.getAlgorithms().putAlgorithmToAllVertices((Algorithm)Class.forName("fr.enserb.das.algo."+className).newInstance());
	  fenetre.getMenuChoice().setListTypes(((Algorithm)Class.forName("fr.enserb.das.algo."+className).newInstance()).getListTypes());//we set the list of types used in the chosen algorithm
	    System.out.println(Class.forName("fr.enserb.das.algo."+className).newInstance().getClass());
      }
      catch(Exception excpt) {
	  System.out.println("Problem: " + excpt);
      }
  }
    
    /** Open  ".class" file for a simulation algorithm 
	Algorithm is affected to vertex number id*/
    public static void openForVertex(Enumeration e, FenetreDeSimulation fenetre){
	
		File file_open = null;
    		JFileChooser fc = new JFileChooser("fr/enserb/das/algo");    
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
		    String id;
		    while (e.hasMoreElements()){
			id = ((SommetDessin)e.nextElement()).getEtiquette();
			fenetre.getAlgorithms().putAlgorithm(id, (Algorithm)Class.forName("fr.enserb.das.algo."+className).newInstance());
			 fenetre.getMenuChoice().addAtListTypes(((Algorithm)Class.forName("fr.enserb.das.algo."+className).newInstance()).getListTypes());//we set the list of types used in the chosen algorithm
		    }
		  
		}
		 catch(Exception excpt) {
		     System.out.println("Problem: " + excpt);
		 }
    }
    
    
}








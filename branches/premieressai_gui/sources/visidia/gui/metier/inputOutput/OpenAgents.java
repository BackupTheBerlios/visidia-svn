package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.*;
import java.util.Enumeration;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.simulation.*;
import visidia.gui.presentation.SommetDessin;
import java.net.URLClassLoader;
import java.net.URL;
import visidia.algo.*;
import visidia.rule.*;


public class OpenAgents implements Serializable{

    protected static final String dir = new String("visidia/agents");
    
    /** Open  ".class" file for agents simulation
	agents are affected to chosen vertices*/
  public static boolean open(Enumeration e, AgentsSimulationWindow window){
      File file_open = null; 
      JFileChooser fc = new JFileChooser(dir);
      javax.swing.filechooser.FileFilter classFileFilter = new FileFilterClass();
      fc.addChoosableFileFilter(classFileFilter);
      fc.setFileFilter(classFileFilter);
      
      int returnVal = fc.showOpenDialog(window);
      if(returnVal == JFileChooser.APPROVE_OPTION)
	  file_open = fc.getSelectedFile();
      
      String file_name = fc.getName(file_open);
      if (file_name == null) 
	  return false ; // if canceled
      window.mettreAJourTitreFenetre(file_name);
      
      int index = file_name.lastIndexOf('.');
      String className = file_name.substring(0,index);
      System.out.println(className);
       try {
	   /*nada
	  try {
	      int id;
	      while (enum.hasMoreElements()) {
		  //
		  window.addAgents(id,className);

	      a = (Algorithm)Class.forName("visidia.algo."+className).newInstance();
	      
	      window.getAlgorithms().putAlgorithmToAllVertices((Algorithm) a.clone());
	      window.getMenuChoice().setListTypes(((Algorithm)Class.forName("visidia.algo."+className).newInstance()).getListTypes());
	      System.out.println(Class.forName("visidia.algo."+className).newInstance().getClass());
	  } catch(Exception e) {
	      a= (SyncAlgorithm)Class.forName("visidia.algo.synchronous."+className).newInstance();
	      window.getAlgorithms().putAlgorithmToAllVertices((SyncAlgorithm) a.clone());
	      window.getMenuChoice().setListTypes(((SyncAlgorithm)Class.forName("visidia.algo.synchronous."+className).newInstance()).getListTypes());
	      System.out.println(Class.forName("visidia.algo.synchronous."+className).newInstance().getClass());
	  }
	   */
      }
      catch(Exception excpt) {
	  System.out.println("Problem: " + excpt);
      }
      return true;
  }
        
}








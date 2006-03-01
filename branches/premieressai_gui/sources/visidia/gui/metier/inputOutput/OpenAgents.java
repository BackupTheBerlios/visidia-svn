package visidia.gui.metier.inputOutput;

import java.io.*;
import java.lang.Integer;
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
	  Integer id;
	  while (e.hasMoreElements()) {
	      id = Integer.decode(((SommetDessin)e.nextElement()).getEtiquette());
	      window.addAgents(id,className);
	  }	 
      }
      catch(Exception excpt) {
	  System.out.println("Problem: " + excpt);
      }
      return true;
  }
        
}








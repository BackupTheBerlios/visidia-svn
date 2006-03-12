package visidia.gui.metier.inputOutput;

import java.io.*;
import java.lang.Integer;
import javax.swing.*;
import java.util.Enumeration;
import java.lang.reflect.Method;

import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.metier.Graphe;
import visidia.agents.agentchooser.*;

public class OpenAgentChooser implements Serializable{

    protected static final String dir = new String("visidia/agents/agentchooser");
    
  public static boolean open(AgentsSimulationWindow window){

      File file_open = null; 
      JFileChooser fc = new JFileChooser(dir);
      javax.swing.filechooser.FileFilter classFileFilter = 
          new FileFilterClass();
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
      String className = "visidia.agents.agentchooser." 
          + file_name.substring(0,index);
      System.out.println(className);
	  
      try {
          Class classChooser = Class.forName(className);
          Method methodPlace = classChooser
              .getDeclaredMethod("place", 
                                 new Class [] { AgentsSimulationWindow.class });
          methodPlace.invoke(null, window);
      } catch(Exception excpt) {
          excpt.printStackTrace();
          System.out.println("Cause " + excpt.getCause());
          throw new RuntimeException("");
      }
      return true;
  }
        
}








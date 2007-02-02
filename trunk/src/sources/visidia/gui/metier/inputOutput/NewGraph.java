package visidia.gui.metier.inputOutput;

import javax.swing.*;
//import java.io.*;

import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class NewGraph {
    
  public static void newGraphe(Editeur editeur) {
      if(editeur.graph().ordre() != 0) {
	  Object[] options = {"Save before",
			      "No need to save",
			      "Cancel"};
	  int n = JOptionPane.showOptionDialog(editeur,
					       "Do you want to save before opening a new file ?",
					       "Warning", 
					       JOptionPane.YES_NO_CANCEL_OPTION,
					       JOptionPane.WARNING_MESSAGE,
					       null,
					       options,
					       options[0]);
	  if (n == JOptionPane.YES_OPTION)
	      SaveFile.save(editeur, editeur.graph());
	  if (n == JOptionPane.CANCEL_OPTION)
	      return;
	  
      }
      
      Graphe graphe = new Graphe();
      editeur.changerVueGraphe(graphe.getVueGraphe());
      editeur.remplaceSelection(new SelectionDessin());
      editeur.setFichierEdite(null);
      editeur.pack();
      editeur.setVisible(true);
  }
    
}



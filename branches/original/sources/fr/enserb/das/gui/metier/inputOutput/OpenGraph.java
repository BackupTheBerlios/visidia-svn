package fr.enserb.das.gui.metier.inputOutput;

import java.io.*;
import javax.swing.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.*;
import fr.enserb.das.gui.metier.*;

/** this class permit to Open saved rules from directories */
public class OpenGraph implements Serializable{
    /** open a file for graph in the current editor or simulator */
    public static void open(Fenetre  fenetre){
	
	Graphe unGraphe;
	File file_open = null;
	JFileChooser fc = new JFileChooser(".");   
	javax.swing.filechooser.FileFilter graphFileFilter = new FileFilterGraph();
	fc.addChoosableFileFilter(graphFileFilter); 
	fc.setFileFilter(graphFileFilter);
	
	int returnVal = fc.showOpenDialog(fenetre);
	if(returnVal == JFileChooser.APPROVE_OPTION)
	    file_open = fc.getSelectedFile();
	
	// file name specified ?
	String file_name = fc.getName(file_open);
	if(file_name == null) {
	    return;
	}
	
	try{		
    		
	    File f = new File(fc.getCurrentDirectory(), file_name);
	    FileInputStream fI = new FileInputStream(f);
	    ObjectInputStream oI = new ObjectInputStream(fI);
	    
	    if((fenetre.getVueGraphe().getGraphe()).ordre() != 0) {
		Object[] options = {"Save and open",
				    "Open",
				    "Cancel"};
		int n = JOptionPane.showOptionDialog(fenetre,
						     "Do you want to save before opening a new file ?",
						     "Warning", 
						     JOptionPane.YES_NO_CANCEL_OPTION,
						     JOptionPane.WARNING_MESSAGE,
						     null,
						     options,
						     options[0]);
		if (n == JOptionPane.YES_OPTION)
		    SaveFile.save(fenetre, fenetre.getVueGraphe().getGraphe());
		if (n == JOptionPane.CANCEL_OPTION)
		    return;
	    }
	    
	    unGraphe = (Graphe)oI.readObject();	
	    fenetre.changerVueGraphe(unGraphe.getVueGraphe());

	    fenetre.mettreAJourTitreFenetre(f);
	    fenetre.pack();
	    fenetre.setVisible(true);
	    oI.close();
	    fI.close();	
	    
	}
	catch(OptionalDataException e){
	    System.out.println("Exception caught :" + e);
	}
	catch(ClassNotFoundException e){
	    System.out.println("Exception caught :" + e);
	}
	catch(IOException e){
	    System.out.println("Exception caught :" + e);
	}
    }
    
    
    public static void open(Fenetre fenetre,File  f){
		
	Graphe unGraphe;
	
	try{		
	    
	    FileInputStream fI = new FileInputStream(f);
	    ObjectInputStream oI = new ObjectInputStream(fI);
	    
	    
	    unGraphe = (Graphe)oI.readObject();	
	    fenetre.changerVueGraphe(unGraphe.getVueGraphe().cloner());
	    
	    fenetre.mettreAJourTitreFenetre(f);
	    fenetre.pack();
	    fenetre.setVisible(true);
	    oI.close();
	    fI.close();	
	    
	}
	catch(OptionalDataException e){
	    System.out.println("Exception caught :" + e);
	}
	catch(ClassNotFoundException e){
	    System.out.println("Exception caught :" + e);
	}
	catch(IOException e){
	    System.out.println("Exception caught :" + e);
	}
    }
    
    
    public OpenGraph(){
	
    }
}








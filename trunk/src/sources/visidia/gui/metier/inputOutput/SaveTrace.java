package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.*;
import visidia.gui.presentation.userInterfaceEdition.*;


/**
   This class permit to save a trace in a text file
**/

public class SaveTrace extends JFileChooser {
 
    public static FileOutputStream fos ;
    public static ObjectOutputStream oos ;
    
    /** The parent window(from where the saving is called) */
    protected Fenetre parent;
    
    /** a valid file with good extension */
    protected File validFile;
    
    /** Number of file names already generated */
    protected static int nombre = 0;
    
    /** the path of the current directory for saving */
    protected String path;
    
    // shall we save ?
    private boolean enregistrer = true;
  

    /** constructor to save the trace in the parent window and the path directory */  
    public SaveTrace(Fenetre parent, String path) {
	super(path);
	this.path = path;
	
	if (parent.type().equals("Editor")){
	    ((Editeur)parent).commandeRenumeroter();
	}
	
	nombre++;
	validFile = new File(path, "trace_" + Integer.toString(nombre) + ".trace");
	setSelectedFile(validFile);
	this.parent = parent;
    }
   
    /** this method permit to gave the file name given by the user
     * Here we deal with errors and warnings (existing files, extension errors ...) */
    public void approveSelection() {
	boolean save = true;
	File f = getSelectedFile();
	String s = f.getName();
	int i = s.lastIndexOf('.');
	if (i > 0 &&  i < s.length() - 1) {
	    String extension = s.substring(i+1).toLowerCase();
	    if (!extension.equals("trace")) {
		JOptionPane.showMessageDialog(this, 
					      getName(f) + " : this file has not a recognized\n"
					      + "extension. The required extension is '.trace ",
					      "Warning", 
					      JOptionPane.WARNING_MESSAGE);
		setSelectedFile(validFile);
		save = false;
		
	    } 	  
	} else {
	    if (i == -1) {
		setSelectedFile(new File(path, s + ".trace"));
		save = true;
	    } else {
		setSelectedFile(new File(path, s + "trace"));
		save = true;
	    }
	}
	
	/*	if (getSelectedFile().exists()) {
	    int overwrite = JOptionPane.showConfirmDialog(this, 
							  getName(getSelectedFile()) + 
							  " : this file aldready exists.\n"
							  + "Do you want to overwrite it ?",
							  "Warning", 
							  JOptionPane.YES_NO_OPTION);
	    if (overwrite == JOptionPane.YES_OPTION) {
		super.approveSelection();
	    } else {
		setSelectedFile(validFile);
	    }
	} else {
	    if (save) {
		super.approveSelection();
	    }
	}*/
	super.approveSelection();
    }
  
    /** Reaction when we use the "cancel" button
     * or validation of an empty file  */
    public void cancelSelection() {
	nombre--;
	if (getSelectedFile() == null) {
	    JOptionPane.showMessageDialog(this, 
					  "You must choose a file to save your trace in !",
					  "Warning", 
					  JOptionPane.WARNING_MESSAGE);
	    setSelectedFile(validFile);
	} else {      
	    super.cancelSelection();
	}
    }
  
    public static File save(Fenetre fenetre){
  	fenetre.selection.desenluminer();
	SaveTrace st = new SaveTrace(fenetre, ".");
	javax.swing.filechooser.FileFilter traceFileFilter = new FileFilterTrace();
   	st.addChoosableFileFilter(traceFileFilter);
    	st.setFileFilter(traceFileFilter);
	
    	File f = fenetre.fichier_edite();
        int returnVal = st.showSaveDialog(fenetre);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
		f = st.getSelectedFile();  
 	}
 	fenetre.selection.select();

	return f;
    }
}




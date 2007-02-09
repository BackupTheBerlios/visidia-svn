package visidia.gui.metier.inputOutput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;

/**
 * This class permit to save rules in text files
 **/

public class SaveRules extends JFileChooser {
 
  /**
	 * 
	 */
	private static final long serialVersionUID = 3415679244766739558L;
public static FileOutputStream fos ;
  public static ObjectOutputStream oos ;

    /** The parent window(from where the saving is called) */
  protected FenetreDeSimulation parent;

    /** a valid file with good extension */
  protected File validFile;
  
    /** Number of file names already generated */
  protected static int nombre = 0;

    /** the path of the current directory for saving */
  protected String path;
  

  /** constructor to save the rules in the parent window and the path directory */
  
  public SaveRules(FenetreDeSimulation parent, String path, Vector listRules) {
    super(path);
    this.path = path;
    
    nombre++;
    this.validFile = new File(path, "noname_" + Integer.toString(nombre) + ".rule");
    this.setSelectedFile(this.validFile);
    this.parent = parent;
   
  }
   
  

 /** this method permit to gave the file name given by the user
  * Here we deal with errors and warnings (existing files, extension errors ...) */

  public void approveSelection() {
    boolean save = true;
    File f = this.getSelectedFile();
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ((i > 0) &&  (i < s.length() - 1)) {
      String extension = s.substring(i+1).toLowerCase();
      if (!extension.equals("rule")) {
	JOptionPane.showMessageDialog(this, 
				      this.getName(f) + " : ths file has not a recognized\n"
				      + "extension. The required extension is '.rule ",
				      "Warning", 
				      JOptionPane.WARNING_MESSAGE);
	this.setSelectedFile(this.validFile);
	save = false;
	
      } 	  
    } else {
      if (i == -1) {
	this.setSelectedFile(new File(this.path, s + ".rule"));
	save = true;
      } else {
	this.setSelectedFile(new File(this.path, s + "rule"));
	save = true;
      }
    }
    
    
    if (this.getSelectedFile().exists()) {
      int overwrite = JOptionPane.showConfirmDialog(this, 
						    this.getName(this.getSelectedFile()) + 
						    " : this file aldready exists.\n"
						    + "Do you want to overwrite it ?",
						    "Warning", 
						    JOptionPane.YES_NO_OPTION);
      if (overwrite == JOptionPane.YES_OPTION) {
	super.approveSelection();
      } else {
	this.setSelectedFile(this.validFile);
      }
    } else {
      if (save) {
	super.approveSelection();
      }
    }
  }
  
  /** Reaction when we use the "cancel" button
    * or validation of an empty file  */
  
  public void cancelSelection() {
    nombre--;
    if (this.getSelectedFile() == null) {
      JOptionPane.showMessageDialog(this, 
				    "You must choose a file to save your rules in !",
				    "Warning", 
				    JOptionPane.WARNING_MESSAGE);
      this.setSelectedFile(this.validFile);
    } else {      
      super.cancelSelection();
    }
  }
  

    /** classic method which will be used to save the graph */
  public static void save(FenetreDeSimulation fenetre, Vector listRules) {
    
    SaveRules st = new SaveRules(fenetre, ".", listRules);
    
    File f = fenetre.fichier_edite();
    
    if(f == null) {  
      int returnVal = st.showSaveDialog(fenetre);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
	f = st.getSelectedFile();
	try{
		fos = new FileOutputStream(f);
		oos = new ObjectOutputStream(fos);
		oos.writeObject(listRules);
		oos.close();
		fos.close();

	} 
        catch(IOException e) {
      		System.out.println("Problem: " + e);
    	}
     }
    }
    else {
     try{
		fos = new FileOutputStream(f);
		oos = new ObjectOutputStream(fos);
		oos.writeObject(listRules);

		oos.close();
		fos.close();
		
     } 
        catch(IOException e) {
      		System.out.println("Problem: " + e);
    	}
    }
  }    
  public static void saveAs(FenetreDeSimulation fenetre, Vector listRules){
	  SaveRules st = new SaveRules(fenetre, ".", listRules);
    
    	File f = fenetre.fichier_rules_edite();
        int returnVal = st.showSaveDialog(fenetre);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
		f = st.getSelectedFile();
		try{
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(listRules);

			oos.close();
			fos.close();

		} 
        	catch(IOException e) {
      			System.out.println("Problem: " + e);
    		}
  
 	 }
  }
  
  
}




package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.filechooser.FileFilter;

// this class is the file filter which recognizes the graph format

public class FileFilterTrace extends FileFilter{

public FileFilterTrace(){ 
	super();}

public String getDescription(){
	return new String("*.trace");}
	
public boolean accept(File f)	{
    if(f.isDirectory()){
	return true;
    }
    
    
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ((i > 0) &&  (i < s.length() - 1)) {
      String extension = s.substring(i+1).toLowerCase();
      return (extension.equals("trace"));
    }
    return false;
}

}

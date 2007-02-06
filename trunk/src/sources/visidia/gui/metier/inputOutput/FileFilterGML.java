package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.filechooser.FileFilter;

// this class permit to recognize the gml format

public class FileFilterGML extends FileFilter{

public FileFilterGML(){ 
	super();}

public String getDescription(){
	return new String("Graph in GML format");}
	
public boolean accept(File f)	{
    if(f.isDirectory()){
	return true;
    }
    
    
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ((i > 0) &&  (i < s.length() - 1)) {
      String extension = s.substring(i+1).toLowerCase();
      return (extension.equals("gml"));
    }
    return false;
}

}

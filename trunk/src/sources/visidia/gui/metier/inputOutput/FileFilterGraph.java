package visidia.gui.metier.inputOutput;

import java.io.*;
import javax.swing.filechooser.FileFilter;

// this class is the file filter which recognizes the graph format

public class FileFilterGraph extends FileFilter{

public FileFilterGraph(){ 
	super();}

public String getDescription(){
	return new String("*.graph");}
	
public boolean accept(File f)	{
    if(f.isDirectory()){
	return true;
    }
    

    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ((i > 0) &&  (i < s.length() - 1)) {
      String extension = s.substring(i+1).toLowerCase();
      return (extension.equals("graph"));
    }
    return false;
}

}

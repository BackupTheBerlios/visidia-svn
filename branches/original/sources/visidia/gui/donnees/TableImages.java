package visidia.gui.donnees;

import java.util.*;
import java.awt.*;
import javax.swing.JApplet;
import java.net.URL;

public class TableImages{

    protected static Hashtable tableauImages;

    public static void setTableImages(JApplet uneApplet){
	tableauImages = new Hashtable();

	String jarAdress = new String("jar:"+uneApplet.getCodeBase()+"DistributedAlgoSimulator.jar!/");
	
	try{
	    tableauImages.put("help", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/aide.gif"));
	    tableauImages.put("tree", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/arbre.gif"));
	    tableauImages.put("disk", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/disk.gif"));
	    tableauImages.put("duplicate", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/dupliquer.gif"));
	    tableauImages.put("go", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/go.gif"));
	    tableauImages.put("graph", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/graph.gif"));
	    tableauImages.put("image1", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image1.gif"));
	    tableauImages.put("image2",  uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image2.gif"));
	    tableauImages.put("image3",  uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image3.gif"));
	    tableauImages.put("image4", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image4.gif"));
	    tableauImages.put("image5", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image5.gif"));
	    tableauImages.put("image6", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image6.jpg"));
	    tableauImages.put("image6bmp", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/image6.bmp"));
	    tableauImages.put("info", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/info.gif"));
	    tableauImages.put("new", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/nouveau.gif"));
	    tableauImages.put("open", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/ouvrir.gif"));
	    tableauImages.put("redo", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/redo.gif"));
	    tableauImages.put("stat", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/stat.gif"));
	    tableauImages.put("undo", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/undo.gif"));
	}catch(Exception e){}
    }

     public static void setTableImages(Toolkit tk){
	tableauImages = new Hashtable();
	tableauImages.put("help", tk.getImage("visidia/gui/donnees/images/aide.gif"));
	tableauImages.put("tree", tk.getImage("visidia/gui/donnees/images/arbre.gif"));
	tableauImages.put("disk", tk.getImage("visidia/gui/donnees/images/disk.gif"));
	tableauImages.put("duplicate", tk.getImage("visidia/gui/donnees/images/dupliquer.gif"));
	tableauImages.put("go", tk.getImage("visidia/gui/donnees/images/go.gif"));
	tableauImages.put("graph", tk.getImage("visidia/gui/donnees/images/graph.gif"));
	tableauImages.put("image1", tk.getImage("visidia/gui/donnees/images/image1.gif"));
	tableauImages.put("image2",  tk.getImage("visidia/gui/donnees/images/image2.gif"));
	tableauImages.put("image3",  tk.getImage("visidia/gui/donnees/images/image3.gif"));
	tableauImages.put("image4", tk.getImage("visidia/gui/donnees/images/image4.gif"));
	tableauImages.put("image5", tk.getImage("visidia/gui/donnees/images/image5.gif"));
	tableauImages.put("image6", tk.getImage("visidia/gui/donnees/images/image6.jpg"));
	tableauImages.put("image6bmp", tk.getImage("visidia/gui/donnees/images/image6.bmp"));
	tableauImages.put("info", tk.getImage("visidia/gui/donnees/images/info.gif"));
	tableauImages.put("new", tk.getImage("visidia/gui/donnees/images/nouveau.gif"));
	tableauImages.put("open", tk.getImage("visidia/gui/donnees/images/ouvrir.gif"));
	tableauImages.put("redo", tk.getImage("visidia/gui/donnees/images/redo.gif"));
	tableauImages.put("stat", tk.getImage("visidia/gui/donnees/images/stat.gif"));
	tableauImages.put("undo", tk.getImage("visidia/gui/donnees/images/undo.gif"));
    }




    // returns the image by the key parameter

    public static Image getImage(String key){
	if (tableauImages.containsKey(key))
	    return (Image)(tableauImages.get(key));
	else 
	    return null;
    }

}


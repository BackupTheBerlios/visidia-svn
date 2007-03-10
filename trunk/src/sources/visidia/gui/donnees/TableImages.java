package visidia.gui.donnees;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JApplet;

public class TableImages{

    protected static Hashtable<String, java.awt.Image> tableauImages;

    public static void setTableImages(JApplet uneApplet){
	tableauImages = new Hashtable<String, java.awt.Image>();

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
	    tableauImages.put("globalClock", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/globalClock.gif"));
	    tableauImages.put("noGlobalClock", uneApplet.getImage(new URL(jarAdress),"visidia/gui/donnees/images/noGlobalClock.gif"));
	}catch(Exception e){
	    System.out.println("Problème lors du chargement des images");
	    //e.printStackTrace();
	}
    }

    public static void setTableImages(Toolkit tk){
	try{
	    tableauImages = new Hashtable<String, Image>();
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
	    tableauImages.put("globalClock", tk.getImage("visidia/gui/donnees/images/globalClock.gif"));
	    tableauImages.put("noGlobalClock", tk.getImage("visidia/gui/donnees/images/noGlobalClock.gif"));
	    tableauImages.put("homme", tk.getImage("visidia/gui/donnees/images/homme2.gif"));
	    tableauImages.put("miroirHomme", tk.getImage("visidia/gui/donnees/images/homme2miroir.gif"));
            tableauImages.put("agentwb", tk.getImage("visidia/gui/donnees/images/agentwb.gif"));
            tableauImages.put("vertexwb", tk.getImage("visidia/gui/donnees/images/vertexwb.gif"));
            tableauImages.put("vertexdefwb", tk.getImage("visidia/gui/donnees/images/vertexdefwb.gif"));
	} catch (Exception e) {
	    //e.printStackTrace();
	    System.out.println("Problème lors du chargement des images");
	}
    }



    // returns the image by the key parameter

    public static Image getImage(String key){
	if (tableauImages.containsKey(key))
	    return (Image)(tableauImages.get(key));
	else {
	    System.out.println("Image "+key+" Non trouvée");
	    return null;
	}
    }
    
}


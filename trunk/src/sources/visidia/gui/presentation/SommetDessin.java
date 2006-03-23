package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import visidia.gui.metier.*;

/** the geometric shape of a vertex */
public abstract class SommetDessin extends FormeDessin {
 
    // position of the center of the vertex
    int posx; 
    int posy; 

    protected boolean drawMessage = true; 
    protected ImageIcon uneImage = null;   
    protected String monEtiquette; // label of a vertex
    protected Hashtable stateTable = new Hashtable();
    protected Hashtable wbTable = new Hashtable();

    // number displayed in the middle of a vertex
    protected String nbr = new String("0");
    
    // Constructor
    public SommetDessin(VueGraphe vg, int x, int y, String etiquette, Sommet s){
	this.vueGraphe = vg;
	monEtiquette = etiquette;
	graphObject = s;
	s.setSommetDessin(this);
	this.placer(x, y);
	vueGraphe.insererListeAffichage(this);
	stateTable.put("label","N"); // the state is saved in the table
	stateTable.put("draw messages","yes");
        wbTable.put("label","N"); 
    }

    public SommetDessin(VueGraphe vg, int x, int y, String etiquette){
        this(vg,x,y,etiquette,new Sommet(vg.getGraphe()));
    }

    // Abstracts methods
    public abstract void dessiner(Component c, Graphics g);
    public abstract void agrandir(float coef);
    public abstract float rayon(float angle);
   
    // permit to clone the vertex. 
    public Object cloner(){
	return cloner(vueGraphe);
    }

    public abstract Object cloner(VueGraphe vue);

    // Move a vertex of (dx, dy)
    public void deplacer(int dx, int dy){
	this.placer(posx + dx, posy + dy);
    }

    
    // Accessor
    public String type(){
	return new String("vertex");
    }

    public int centreX(){
	return posx;
    }

    public int centreY(){
	return posy;
    }

    public Point centre(){
	return new Point(posx,posy);}

    public String getEtiquette(){
	return monEtiquette;
    }

    public ImageIcon getImage(){
	return uneImage;
    }
    
    public String getEtat(){
	return (String)stateTable.get("label");
    }

    public Hashtable getStateTable(){
	return stateTable;}

    public Hashtable getWhiteBoardTable(){
	return wbTable;}

      
    public Sommet getSommet(){
	return ((Sommet)graphObject);
    }

   
    // Modificators
    public void setEtiquette(String etiquette){
	monEtiquette = etiquette;
    }
   
    public void placer(int x, int y){
	posx = x;
	posy = y;
	getSommet().repositionnerAretes();
    }
   
    public void setEtat(String state) {
	stateTable.put("label",state);
    }
   
    public void changerImage(ImageIcon image){
  	uneImage = image;
    }
 
    public void setSommet(Sommet s){
	graphObject = s;
    }

    // modify a value of the table
    public void setValue(String key, Object value){
	stateTable.remove(key);
	stateTable.put(key,value);
    }

    public void setWhiteBoardValue(String key, Object value){
        wbTable.remove(key);
        wbTable.put(key,value);
    }

    public String getValue(String key){
	return (String)stateTable.get(key);}

    public String getWhiteBoardValue(String key){
	return (String)wbTable.get(key);}


    // method which copy all the variable from the SommetDessin given in parameters
    public void copyAllVariable(SommetDessin s){
	this.uneImage=s.getImage();
	this.monEtiquette=s.getEtiquette();
	stateTable.put("label",s.getEtat());
	super.copyAllVariable((FormeDessin)s);
    }
    

    /**
       Make the fusion of the two vertices
    **/

    public void fusionner(SommetDessin s){
	this.getSommet().fusionner(s.getSommet());
	getVueGraphe().supprimerListeAffichage(s);
    }


    public void setDrawMessage(boolean bool){
        drawMessage = bool;
	if(drawMessage)
	    setValue("draw messages", "yes");
	else
	    setValue("draw messages", "no");
    }
	
    public boolean getDrawMessage() {
        return drawMessage;
    }

    public void setNbr(String nbr){
	this.nbr = nbr;
    }
    public String getNbr(){
	return nbr;
    }
    
}





package visidia.gui.presentation.factory;

import java.io.Serializable;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class FabriqueSommet2Cercle implements FabriqueSommet,Serializable{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -196932949835306399L;

	public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s){
	return new Sommet2Cercle(vg,x,y,label,s);
    }

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label){
	return new Sommet2Cercle(vg,x,y,label);
    }
    
    public String description(){
    	return "Two circles";}
}

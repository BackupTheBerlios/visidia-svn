package visidia.gui.presentation.factory;

import java.io.Serializable;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;

public class FabriqueSommetCircle implements FabriqueSommet, Serializable{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1074778465194624421L;

	public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s){
	return new SommetCircle(vg,x,y,label,s);
    }

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label){
	return new SommetCircle(vg,x,y,label);
    }
    
    public String description(){
    	return "Simple vertex : circle";}
}

package visidia.gui.presentation.factory;

import java.io.Serializable;

import visidia.gui.metier.Sommet;
import visidia.gui.presentation.SommetCircle;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;

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

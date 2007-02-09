package visidia.gui.presentation.factory;

import java.io.Serializable;

import visidia.gui.metier.Sommet;
import visidia.gui.presentation.SommetCarre;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;

public class FabriqueSommetCarre implements FabriqueSommet,Serializable{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 749152283586540566L;

	public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s){
	return new SommetCarre(vg,x,y,label,s);
    }

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label){
	return new SommetCarre(vg,x,y,label);
    }
    
    public String description(){
    	return "Simple vertex";}
}

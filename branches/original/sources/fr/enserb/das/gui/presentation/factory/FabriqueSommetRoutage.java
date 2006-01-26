package fr.enserb.das.gui.presentation.factory;

import java.io.Serializable;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.metier.*;

public class FabriqueSommetRoutage implements FabriqueSommet,Serializable{
 
    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label, Sommet s){
	return new SommetRoutage(vg,x,y,label,s);
    }

    public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label){
	return new SommetRoutage(vg,x,y,label);
    }
    
    public String description(){
    	return "Routing vertex type";}
}

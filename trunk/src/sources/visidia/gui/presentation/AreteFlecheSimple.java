package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.*;

/**
 * Représente le dessin d'une arête représentée par une flèche simple.
 **/
public class AreteFlecheSimple extends AreteDessin{
    
    // Variables de classe.
    
    static protected double angle_branches = 3e-1;
    static protected double longueur_branches = 20;

    // Variables d'instance.
    protected int branche1_x, branche1_y, branche2_x, branche2_y;
    //protected int mi_bran1_x, mi_bran1_y, mi_bran2_x, mi_bran2_y;
    protected boolean recalculer_branche = true;
    
    //Constructeurs.

    public AreteFlecheSimple(SommetDessin origine, SommetDessin destination, Arete arete) {
	super(origine, destination, arete);
    }
    
    public AreteFlecheSimple(SommetDessin origine, SommetDessin destination) {
	super(origine, destination);
    }
    
    
    //  Dessiner une fleche avec sa pointe sur un Graphics passe en argument.
    public void dessiner(Component c , Graphics g) {
	super.dessiner(c, g);
	if(recalculer_branche)
	    recalculer_branche();
	double theta =
	    Math.atan2((double)(destx - origx), (double)(desty - origy));

	double angle1 = theta - angle_branches, angle2 = theta + angle_branches;
	//	g.drawLine(destx, desty, branche1_x, branche1_y);
	g.drawLine((origx+destx)/2, (origy+desty)/2, ((origx+destx)/2)- ((int)Math.round(longueur_branches * Math.sin(angle1)))/2, ((origy+desty)/2)- ((int)Math.round(longueur_branches * Math.cos(angle1)))/2 );
	g.drawLine((origx+destx)/2, (origy+desty)/2, ((origx+destx)/2)- ((int)Math.round(longueur_branches * Math.sin(angle2)))/2, ((origy+desty)/2)-  ((int)Math.round(longueur_branches * Math.cos(angle2)))/2 );
	//g.drawLine(destx, desty, branche2_x, branche2_y);
    }

    public void deplacer(int dx, int dy) {
	super.deplacer(dx, dy);
	branche1_x += dx;
	branche1_y += dy;
	branche2_x += dx;
	branche2_y += dy;
	//mi_bran1_x += (dx/2);
	//mi_bran1_y += (dy/2);
	//mi_bran2_x += (dx/2);
	//mi_bran2_y += (dy/2);
    }
  

    public String forme() {
	return new String("FlecheSimple");
    }

    public void placerOrigine(int origine_x, int origine_y) {
	super.placerOrigine(origine_x, origine_y);
	recalculer_branche = true;
    }
    
    public void placerDestination(int destination_x, int destination_y) {
	super.placerDestination(destination_x, destination_y);
	recalculer_branche = true;
    }
    
    
    protected void recalculer_branche() {
	double theta =
	    Math.atan2((double)(destx - origx), (double)(desty - origy));
	double angle1 = theta - angle_branches, angle2 = theta + angle_branches;
	
	branche1_x = destx -
	    (int)Math.round(longueur_branches * Math.sin(angle1));
	branche1_y = desty -
	    (int)Math.round(longueur_branches * Math.cos(angle1));
	branche2_x = destx -
	    (int)Math.round(longueur_branches * Math.sin(angle2));
	branche2_y = desty -
	    (int)Math.round(longueur_branches * Math.cos(angle2));
	//mi_bran1_x = branche1_x-((branche1_x+origx)/2);
	//mi_bran1_y = branche1_y-((branche1_y+origy)/2);
	//mi_bran2_x = branche2_x-((branche2_x+origx)/2);
	//mi_bran2_y = branche2_y-((branche2_y+origy)/2);


	recalculer_branche = false;
  }
    
    // Duplique l'arete courante a partir des sommets origine et destination 
    // passes en parametres

    public Object cloner(SommetDessin origine, SommetDessin destination) {
	Arete a = (Arete)this.getArete().cloner(origine.getSommet(),destination.getSommet());
	AreteFlecheSimple le_clone = new AreteFlecheSimple(origine, destination, a);
	le_clone.copyAllVariable(this);
	return le_clone;
    }

    // method which copy all the variable from the AreteFlecheSimple
    // given in parameters
    public void copyAllVariable(AreteFlecheSimple a){
	super.copyAllVariable((AreteDessin)a);}

}




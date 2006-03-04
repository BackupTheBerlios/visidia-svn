package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import visidia.gui.metier.*;
import visidia.gui.presentation.boite.*;

/** represents the gemotric shape to be drawn for am element of the graph */
public abstract class FormeDessin implements Serializable{

    // vueGraphe associated to this shape
    VueGraphe vueGraphe;
 
    protected Color couleur_trait = Color.black;  // line color
    protected Color couleur_fond = Color.white; // background color
    protected boolean enlumine = false; // true if the forme is selected, color inverted
    protected boolean enlumineBis = false; // true if the forme is selected again
    protected ObjetGraphe graphObject;
   
    public abstract void deplacer(int dx, int dy);

    public abstract String type();

    public abstract String forme();

    public abstract void dessiner(Component c , Graphics g);

    public abstract boolean appartient(int x , int y); 

    public abstract boolean estDansRegion(int x1,int y1,int x2,int y2); 

    // dialog box for displaying the properties of this element
    public abstract BoiteFormeDessin proprietes(JFrame parent);

    // Modificators

    public void setVueGraph(VueGraphe graphe){
	vueGraphe = graphe;
    }
    
    public void changerCouleurTrait(Color couleurTrait) {
	couleur_trait = couleurTrait;
    }

    public void changerCouleurFond(Color couleurFond) {
	couleur_fond = couleurFond;
    }
    
    public void enluminer(boolean nouvelleValeur) {
	enlumine = nouvelleValeur;
    }

    public void enluminerBis(boolean nouvelleValeur) {
	enlumineBis = nouvelleValeur;
    }

    public void setObjetGraphe(ObjetGraphe o){
	graphObject = o;}

    public void delete(){
	vueGraphe.supprimerListeAffichage(this);
	graphObject.supprimer();
    }
    
    // Accessors
    
    public VueGraphe getVueGraphe(){
	return vueGraphe;
    }

    public ObjetGraphe getObjetGraphe(){
	return graphObject;}

    public Color couleurTrait() {
	return couleur_trait;
    }

    public Color couleurFond() {
	return couleur_fond;               
    }
    
    public boolean est_enlumine(){
	return enlumine;
    }

    public boolean est_enlumineBis(){
	return enlumineBis;
    }
    
    // method which copy all the variable from the FormeDessin given in parameters
    public void copyAllVariable(FormeDessin s){
	this.couleur_trait=s.couleur_trait;
	this.couleur_fond=s.couleur_fond;
    }
}





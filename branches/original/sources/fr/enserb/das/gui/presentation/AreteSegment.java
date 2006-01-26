package fr.enserb.das.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import fr.enserb.das.gui.presentation.boite.*;
import fr.enserb.das.gui.metier.*;


/** Représente le dessin d'une arete representee par un segment. */
public class AreteSegment extends AreteDessin{

    // Constructeur

    public AreteSegment(SommetDessin origine, SommetDessin destination, Arete arete){
	super(origine, destination, arete);
    }

    // creating a new AreteSegment with a new Arete
    public AreteSegment(SommetDessin origine, SommetDessin destination){
	super(origine, destination);
    }

    //  Dessiner l'arête sous forme de segment sur un Graphics passé en argument.
    public void dessiner(Component c , Graphics g) {
	super.dessiner(c, g);
    }
    

    // Duplique l'arête courante à partir des sommets origine et destination 
    // passés en paramètres

    public Object cloner(SommetDessin origine, SommetDessin destination) {
	Arete a = (Arete)this.getArete().cloner(origine.getSommet(),destination.getSommet());
	AreteSegment le_clone = new AreteSegment(origine, destination, a);
	le_clone.copyAllVariable(this);
	return le_clone;
    }


    // method which copy all the variable from the AreteSegment given in parameters
    public void copyAllVariable(AreteSegment a){
	super.copyAllVariable((AreteDessin)a);}

    public String forme() {
	return new String("Segment");
    }
}


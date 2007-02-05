package visidia.gui.presentation;

import java.util.*;
import visidia.gui.donnees.conteneurs.*;

/**
 * Implements a selection of elements of a graph
 * without any supposition about the relations between these elements.
 **/

public class SelectionDessin{
  // Variables d'instance
  
    /** Set of elected elements */
  protected Ensemble formes;


  // Constructor
  // Instanciation of a selection
 
    public SelectionDessin() {
	formes = new Ensemble();
    }
  
  // Methods

  // desenluminate the elements of the selection
  public void desenluminer() {
    FormeDessin elt;
    Enumeration e = formes.elements();
   
    while (e.hasMoreElements()) {
      elt = (FormeDessin)e.nextElement();
      elt.enluminer(false);
    }
   
  }

// Deselect the elements of the selection
  public void deSelect() {

    FormeDessin elt;
    Enumeration e = formes.elements();
   
    while (e.hasMoreElements()) {
      elt = (FormeDessin)e.nextElement();
      elt.enluminer(false);
    }
    formes.vider();
  }

  // Select the elements of the selection 
  public void select() {
    FormeDessin elt;
    Enumeration e = formes.elements();
    while (e.hasMoreElements()) {
      elt = (FormeDessin)e.nextElement();
      elt.enluminer(true);
    }
  }


  // Tester wether an element is in the selection
  public boolean contient(FormeDessin forme) {
    return formes.contient(forme);
  }
    
  
  // Modificators

    
  // Insert an element to the selection
  // (if this element is already in the selection, nothing happens)
  public void insererElement(FormeDessin forme) {
	formes.inserer(forme);
	forme.enluminer(true);
  }
 
  // delete an element from the selection
    public void supprimerElement(FormeDessin forme) {
	formes.supprimer(forme);
	forme.enluminer(false);
  }

  // Accessors
  public boolean estVide() {
      return (formes.estVide());
  } 

    public Enumeration elements(){
      return formes.elements();
  } 

 
	
  public int nbElements(){
      return formes.taille();
  }
      
}




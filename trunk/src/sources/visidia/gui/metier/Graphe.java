package visidia.gui.metier;

import java.util.*;
import java.io.*;
import visidia.gui.donnees.conteneurs.monde.*;
import visidia.gui.presentation.*;

/** This class correponds to a theorical graph
 * it simply contains the edges and the vertices used.
 *
 **/
public class Graphe implements Serializable {
  // Instance variables

  /** Vertex of the graph. **/
  protected Monde vertex;
  /** edges of the graph. **/
  protected Monde edges;

    /** last simulation type **/
  protected String simulationType;


    /** string expliquing the last simulation */
  protected String explication;


    /** Extension of graph files */
  protected static String extension = "graph";
    /** description of this format files */
  protected static String description = "Autograph files";

    /** VueGraphe corresponding to the associated view */
  protected VueGraphe vueGraphe;

    /** creates a new empty graph */
  public Graphe() {
    simulationType = null;
    explication = null;
    vertex = new Monde();
    edges = new Monde();
    setVueGraphe(new VueGraphe(this));
  }
  

  // modificator and accessor to the VueGraphe
    public void setVueGraphe(VueGraphe v){
	vueGraphe = v;
    }

    public VueGraphe getVueGraphe(){
    	return vueGraphe;
    }
  


  // Methodes.

  /**
   * returns the number of vertices
   **/
  public int ordre() {
    return vertex.taille();
  }

  /**
   * returns the number of edges
   **/
  public int taille() {
    return edges.taille();
  }

  /**
   * returns an enumeration of the vertices
   **/
  public Enumeration sommets() {
    return vertex.elements();
  }

  /**
   * returns an enumeration of the edges
   **/
  public Enumeration aretes() {
    return edges.elements();
  }

  /**
   * returns the file extension
   **/
  public String extension() {
    return extension;
  }
  
  /**
   * Returns the file description
   **/
  public String getFileDescription() {
    return description;
  }
}

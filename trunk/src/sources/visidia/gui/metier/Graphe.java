package visidia.gui.metier;

import java.io.Serializable;
import java.util.Enumeration;

import visidia.gui.donnees.conteneurs.monde.Monde;
import visidia.gui.presentation.VueGraphe;

/** This class correponds to a theorical graph
 * it simply contains the edges and the vertices used.
 *
 **/
public class Graphe implements Serializable {
  // Instance variables

  /**
	 * 
	 */
	private static final long serialVersionUID = -7542819897827124594L;
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
    this.simulationType = null;
    this.explication = null;
    this.vertex = new Monde();
    this.edges = new Monde();
    this.setVueGraphe(new VueGraphe(this));
  }
  

  // modificator and accessor to the VueGraphe
    public void setVueGraphe(VueGraphe v){
	this.vueGraphe = v;
    }

    public VueGraphe getVueGraphe(){
    	return this.vueGraphe;
    }
  


  // Methodes.

  /**
   * returns the number of vertices
   **/
  public int ordre() {
    return this.vertex.taille();
  }

  /**
   * returns the number of edges
   **/
  public int taille() {
    return this.edges.taille();
  }

  /**
   * returns an enumeration of the vertices
   **/
  public Enumeration sommets() {
    return this.vertex.elements();
  }

  /**
   * returns an enumeration of the edges
   **/
  public Enumeration aretes() {
    return this.edges.elements();
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

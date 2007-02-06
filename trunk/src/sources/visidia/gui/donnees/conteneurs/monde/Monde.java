package visidia.gui.donnees.conteneurs.monde;
import java.io.*;
import java.util.*;

/** 
 * Cette classe implémente une chaîne d'éléments.
 **/ 


public class Monde implements Serializable {

  // Variables d'instance.

  /**
	 * 
	 */
	private static final long serialVersionUID = -788924626663627298L;

/** Le nombre d'éléments dans le monde.*/
  protected int taille = 0;
    
  /** La position de l'élément courant dans le monde.  C'est en
   * quelque sorte un curseur.
   **/
  protected Position premier;

  // Constructeur.

  /**
   * Instancie un nouveau monde (vide).
   **/
  public Monde() {
    this.premier = new Position(this);
  }

  // Methodes.

  /**
   * Retourne le nombre d'éléments du monde.
   **/
  public int taille() {
    return this.taille;
  }

  /**
   * Ajoute l'élément un_objet dans le monde.
   **/
  public Position ajouterElement(Object un_objet) {
    this.taille++;
    return this.premier.AjouterAvant(un_objet);
  }

    /**
     * Enleve du monde 
     **/

    //    public void enleverElement();

  /**
   * Retourne une enumeration des éléments du monde.
   **/
  public Enumeration elements() {

    // "Inner class" qui implemente une enumeration
    return new Enumeration() {

      // Inner class : variable d'instance.
      
      public Position position_courante = Monde.this.premier;
      
      // Inner class : methodes.
      
      public boolean hasMoreElements() {
	return (this.position_courante.suivant != Monde.this.premier);
      }
      
      public Object nextElement() {
	if(this.position_courante.suivant == Monde.this.premier) {
	  throw new NoSuchElementException("You reached the end of the world");
	}
	this.position_courante = this.position_courante.suivant;
	return this.position_courante.element;
      }
    };
  }
}

package visidia.gui.donnees.conteneurs.monde;
import java.io.*;
import java.util.*;

/** Cette classe implemente une chaine d'elements.*/ 
public class Monde implements Serializable {

  // Variables d'instance.
  /** Le nombre d'elements dans le monde.*/
  protected int taille = 0;
  /** La position de l'element courant dans le monde.
    * C'est en quelque sorte un curseur.*/
  protected Position premier;

  // Constructeur.

  /**
   * Instancie un nouveau monde (vide).
   **/
  public Monde() {
    premier = new Position(this);
  }

  // Methodes.

  /**
   * Retourne le nombre d'elements du monde.
   **/
  public int taille() {
    return taille;
  }

  /**
   * Ajoute l'element un_objet dans le monde.
   **/
  public Position ajouterElement(Object un_objet) {
    taille++;
    return premier.AjouterAvant(un_objet);
  }

    /**
     * Enleve du monde 
     **/

    //    public void enleverElement();

  /**
   * Retourne une enumeration des elements du monde.
   **/
  public Enumeration elements() {

    // "Inner class" qui implemente une enumeration
    return new Enumeration() {

      // Inner class : variable d'instance.
      
      public Position position_courante = premier;
      
      // Inner class : methodes.
      
      public boolean hasMoreElements() {
	return (position_courante.suivant != premier);
      }
      
      public Object nextElement() {
	if(position_courante.suivant == premier) {
	  throw new NoSuchElementException("You reached the end of the world");
	}
	position_courante = position_courante.suivant;
	return position_courante.element;
      }
    };
  }
}

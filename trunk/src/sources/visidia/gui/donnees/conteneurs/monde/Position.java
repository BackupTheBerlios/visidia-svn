package visidia.gui.donnees.conteneurs.monde;
import java.io.*;


/**
 * Un objet de classe Position est en quelque sorte une référence
 * pointant sur un élément d'un monde. C'est cette classe qui 
 * encapsule l'objet situé à la "position" courante dans le monde.
 **/
public class Position implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -3456322232125828473L;
// Variables d'instance.
  /** Le monde auquel appartient cette position*/
  protected Monde un_monde;
  /** L'objet situe dans le monde a cette position.*/
  protected Object element = null;
  /** La position precedente dans le monde.*/
  protected Position precedent;
  /** La position suivante dans le monde.*/
  protected Position suivant;
  
  // Constructeurs.

  protected Position(Monde un_monde, Object element,
                     Position precedent, Position suivant) {
    this.un_monde = un_monde;
    this.element = element;
    this.precedent = precedent;
    this.suivant = suivant;
  }

  protected Position(Monde un_monde) {
    this.un_monde = un_monde;
    precedent = suivant = this;
  }
  
  // Methodes.
  /** 
   * Ajoute une position avant l'objet "element" dans le monde.
   **/
  protected Position AjouterAvant(Object element) {
    Position nouveau = new Position(un_monde, element, precedent, this);
    precedent.suivant = nouveau;
    precedent = nouveau;
    return nouveau;
  }
  
  /**
   * Retourne l'élément du monde correspondant à cette position.
   **/
  public Object lireElement() {
    return element;
  }

  /**
   * Supprime du monde l'élément correspondant à cette position
   **/
  public void supprimerElement() {
    if(un_monde != null) {
      precedent.suivant = suivant;
      suivant.precedent = precedent;
      precedent = suivant = null;
      element = null;
      un_monde.taille--;
      un_monde = null;
    }
  }
}




package fr.enserb.das.gui.donnees.conteneurs.monde;
import java.io.*;
/**
 * Un objet de classe Position est en quelque sorte une r�f�rence
 * pointant sur un �l�ment d'un monde. C'est cette classe qui 
 * encapsule l'objet situ� � la "position" courante dans le monde.
 **/
public class Position implements Serializable {

  // Variables d'instance.
  /** Le monde auquel appartient cette position*/
  protected Monde un_monde;
  /** L'objet situ� dans le monde � cette position.*/
  protected Object element = null;
  /** La position pr�c�dente dans le monde.*/
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
  /** Ajoute une position avant l'objet "element" dans le monde.*/
  protected Position AjouterAvant(Object element) {
    Position nouveau = new Position(un_monde, element, precedent, this);
    precedent.suivant = nouveau;
    precedent = nouveau;
    return nouveau;
  }
  
  /**
   * Retourne l'�l�ment du monde correspondant � cette position.
   **/
  public Object lireElement() {
    return element;
  }

  /**
   * Supprime du monde l'�l�ment corespondant � cette position
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




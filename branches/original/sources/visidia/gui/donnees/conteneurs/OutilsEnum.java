package visidia.gui.donnees.conteneurs;

import java.util.*;

/**
 * L'interface enumeration est le mecanisme standard offert par Java pour
 * passer une suite d'objets en argument d'une methode, independamment de
 * la structure de donnees qui contient ces objets.<BR>
 * Cette classe propose des methodes statiques pour manipuler les enumerations
 * comme par exemple concatener deux enumerations ou encore pour creer simplement
 * de petites enumerations sans etre oblige de passer par des stuctures de
 * donnees "lourdes" (Vector, ...).
 **/
public class OutilsEnum {

  private OutilsEnum() {}

  /**
   * Retourne une enumeration contenant pour unique element "un_objet".
   **/
  public static Enumeration creerEnumeration(final Object un_objet) {

    // "Inner class" qui implemente un singleton
    return new Enumeration() {

      // Inner class : variable d'instance.
      
      private boolean has_more = true;

      // Inner class : methodes.
      
      public boolean hasMoreElements() {
	return has_more;
      }

      public Object nextElement() {
	if(has_more) {
	  has_more = false;
	  return un_objet;
	} else
	  throw new NoSuchElementException("No more element");
      }
    };
  }

  /**
   * Retourne une enumeration contenant pour uniques elements
   * obj1 et obj2, dans cet ordre.
   **/
  public static Enumeration creerEnumeration(final Object obj1, final Object obj2) {
    Object[] un_tableau = new Object[2];
    un_tableau[0] = obj1;
    un_tableau[1] = obj2;
    return creerEnumeration(un_tableau);
  }

  /**
   * Retourne une enumeration contenant pour uniques elements
   * obj1, obj2 et obj3, dans cet ordre.
   **/
  public static Enumeration creerEnumeration(final Object obj1, final Object obj2, final Object obj3) {
    Object[] un_tableau = new Object[3];
    un_tableau[0] = obj1;
    un_tableau[1] = obj2;
    un_tableau[2] = obj3;
    return creerEnumeration(un_tableau);
  }

  /**
   * Retourne une enumeration contenant les elements d'un tableau
   * passe en argument (on a conservation de l'ordre).
   **/
  public static Enumeration creerEnumeration(final Object[] un_tableau) {

    // "Inner class" qui implemente une enumeration a partir d'un tableau

    return new Enumeration(){

      // Inner class : variable d'instance.
      
      private int indice_courant = 0;
      
      // Inner class : methodes.
    
      public boolean hasMoreElements() {
	return (indice_courant < un_tableau.length);
      }
      
      public Object nextElement() {
	if(indice_courant < un_tableau.length)
	  return un_tableau[indice_courant++];
	else
	  throw new NoSuchElementException("No more element");
      }
    };
  }

  /**
   * Cree une enumeration qui est la concatenation des deux enumerations
   * passees en argument (elements de e1 suivis de ceux de e2).<BR>
   * Les 2 enumerations sont consommees en meme temps que leur concactenation.
   **/
  public static Enumeration concatener(final Enumeration e1, final Enumeration e2) {

    // "Inner class" qui implemente la concatenation de deux enumerations.
    return new Enumeration() {

      // Inner class : variable d'instance.

      private Enumeration enumeration_courante = e1;
      
      // Inner class : methodes.
      
      public boolean hasMoreElements() {
	return (e2.hasMoreElements() ||
		((enumeration_courante != e2) &&
		 enumeration_courante.hasMoreElements()));
      }
      
      public Object nextElement() {
	if((enumeration_courante != e2) &&
	   (!enumeration_courante.hasMoreElements()))
	  enumeration_courante = e2;
	return enumeration_courante.nextElement();
      }
    };
  }

  /**
   * Cree une enumeration qui est l'union des deux enumerations
   * passees en argument (ie on supprime les doublons). L'ordre n'est
   * pas conserve.<BR>
   * Ce traitement est beaucoup plus co�teux qu'une simple concatenation.
   **/
  public static Enumeration union(final Enumeration e1, final Enumeration e2) {
    Ensemble e = new Ensemble();
    e.inserer(e1);
    e.inserer(e2);
    return e.elements();
  }
}

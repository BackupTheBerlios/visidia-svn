package fr.enserb.das.gui.donnees.conteneurs;

import java.util.*;

/**
 * L'interface �numeration est le m�canisme standard offert par Java pour
 * passer une suite d'objets en argument d'une m�thode, ind�pendamment de
 * la structure de donn�es qui contient ces objets.<BR>
 * Cette classe propose des m�thodes statiques pour manipuler les �numerations
 * comme par exemple concat�ner deux �numerations ou encore pour cr�er simplement
 * de petites �numerations sans �tre oblig� de passer par des stuctures de
 * donn�es "lourdes" (Vector, ...).
 **/
public class OutilsEnum {

  private OutilsEnum() {}

  /**
   * Retourne une �numeration contenant pour unique �l�ment "un_objet".
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
   * Retourne une �num�ration contenant pour uniques �l�ments
   * obj1 et obj2, dans cet ordre.
   **/
  public static Enumeration creerEnumeration(final Object obj1, final Object obj2) {
    Object[] un_tableau = new Object[2];
    un_tableau[0] = obj1;
    un_tableau[1] = obj2;
    return creerEnumeration(un_tableau);
  }

  /**
   * Retourne une �num�ration contenant pour uniques �l�ments
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
   * Retourne une �num�ration contenant les �l�ments d'un tableau
   * pass� en argument (on a conservation de l'ordre).
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
   * Cr�e une �num�ration qui est la concat�nation des deux �num�rations
   * pass�es en argument (�l�ments de e1 suivis de ceux de e2).<BR>
   * Les 2 �num�rations sont consomm�es en meme temps que leur concact�nation.
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
   * Cr�e une �num�ration qui est l'union des deux �num�rations
   * pass�es en argument (ie on supprime les doublons). L'ordre n'est
   * pas conserv�.<BR>
   * Ce traitement est beaucoup plus co�teux qu'une simple concat�nation.
   **/
  public static Enumeration union(final Enumeration e1, final Enumeration e2) {
    Ensemble e = new Ensemble();
    e.inserer(e1);
    e.inserer(e2);
    return e.elements();
  }
}

package fr.enserb.das.gui.donnees.conteneurs;

import java.io.*;
import java.util.*;

/**
 * Cette classe implemente un ensemble d'objets. Chacun d'entre eux n'est pr�sent qu'une fois dans l'ensemble.
 * Les m�thodes "equals" et "hashCode" des objets sont utilis�es.
 **/
public class Ensemble implements Cloneable, Serializable {

  // Variables d'instance.

  protected Hashtable table;

  // Constructeurs.

  /**
   * Cr�e un Ensemble vide.
   **/
  public Ensemble() {
    table = new Hashtable();
  }

  /**
   * Cr�e un Ensemble avec une table de hachage donnee.
   * Utilis� par clone.
   **/
  protected Ensemble(Hashtable table) {
    this.table = table;
  }

  

  // Methodes.

  /**
   * Nombre d'elements contenus dans l'ensemble.
   **/
  public int taille() {
    return table.size();
  }

  /**
   * Teste si l'ensemble est vide.
   **/
  public boolean estVide() {
    return table.isEmpty();
  }

  /**
   * Ins�re un objet dans l'ensemble. Si l'objet est d�j� pr�sent, rien ne se passe.
   * "un_objet" ne doit pas etre "null".
   **/
  public void inserer(Object un_objet) {
    // Remarque: le deuxieme argument de la methode "put" n'est pas utilise
    // On peut mettre n'importe quoi dedant
    table.put(un_objet, un_objet);
  }

  /**
   * Ins�re une �numeration d'objets dans l'ensemble. Si un de ces objet est d�j� pr�sent,
   *  rien ne se passe. Aucun objet ne doit etre "null".
   **/
  public void inserer(Enumeration e) {
    while(e.hasMoreElements())
      inserer(e.nextElement());
  }

  /**
   * Supprime un objet de l'ensemble. Si l'objet n'est pas pr�sent, rien ne se passe.
   **/
  public void supprimer(Object un_objet) {
    table.remove(un_objet);
  }

  /**
   * Supprime tous les �l�ments de l'ensemble.
   **/
  public void vider() {
    table.clear();
  }

  /**
   * Teste si un objet est pr�sent dans l'ensemble.
   **/
  public boolean contient(Object un_objet) {
    return table.containsKey(un_objet);
  }

  /**
   * Fait une copie ind�pendante. Ne clone pas les �l�ments.
   **/
  public Object clone() {
    return new Ensemble((Hashtable)table.clone());
  }

  /**
   * Retourne une �numeration de tous les �l�ments de l'ensemble.
   **/
  public Enumeration elements() {
    return table.keys();
  }

  /**
   * Ajoute tous les elements de un_ensemble a l'ensemble courant.
   **/
  public void union(Ensemble un_ensemble) {
    Enumeration e = un_ensemble.table.keys();

    while(e.hasMoreElements())
      inserer(e.nextElement());
  }

  /**
   * Teste si un_ensemble est inclus dans l'ensemble courant.
   * Les �l�ments sont compar�s avec leur m�thode equals().
   **/
  public boolean inclut(Ensemble un_ensemble) {
    Enumeration e = un_ensemble.table.keys();
    while(e.hasMoreElements())
      if(!table.containsKey(e.nextElement()))
        return false;
    return true;
  }

  /**
   * Teste l'�galit� du contenu de l'ensemble courant et de un_ensemble.
   * Les �l�ments sont compares avec leur m�thode equals().
   **/
  public boolean estEgalA(Ensemble un_ensemble) {
    return ((table.size() == un_ensemble.table.size()) &&
            inclut(un_ensemble));
  }

}

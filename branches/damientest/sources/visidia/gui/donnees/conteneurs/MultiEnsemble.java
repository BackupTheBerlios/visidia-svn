package visidia.gui.donnees.conteneurs;

import java.io.*;
import java.util.*;


/**
 * Cette classe implemente un ensemble d'objets, pouvant etre presents plusieurs fois dans l'ensemble..
 * Un compteur memorise le nombre de fois qu'un meme element a ete insere.
 * Les methodes "equals" et "hashCode" des objets sont utilisees.
 **/
public class MultiEnsemble extends Ensemble {

  // Constructeurs.

  /**
   * Cree un MultiEnsemble vide.
   **/
  public MultiEnsemble() {
    super();
  }

  /**
   * Cree un MultiEnsemble avec une table de hachage donnee.
   * Utilise par clone.
   **/
  protected MultiEnsemble(Hashtable table) {
    super(table);
  }

  // Methodes.

  /**
   * Insere un objet dans le MultiEnsemble. Si l'objet est deja present,
   * son compteur est incremente. "un_objet" ne doit pas etre "null".
   **/
  public void inserer(Object un_objet) {
    Object c = table.get(un_objet);
    if(c == null)
      table.put(un_objet, new MECompteur());
    else
      ((MECompteur)c).i++;
  }

  /**
   * Insere un objet dans le MultiEnsemble avec une cardinalite donnee.
   * Si l'objet est deja present, son compteur est incremente de la
   * cardinalite. "un_objet" ne doit pas etre "null".
   **/
  public void inserer(Object un_objet, int cardinalite) {
    //    assert(cardinalite > 0)
    Object c = table.get(un_objet);
    if(c == null)
      table.put(un_objet, new MECompteur(cardinalite));
    else
      ((MECompteur)c).i += cardinalite;
  }

  /**
   * Supprime un objet de l'ensemble. Si l'objet n'est pas present, rien ne se passe.
   * Si l'objet a ete insere plusieurs fois, son compteur est decremente.
   **/
  public void supprimer(Object un_objet) {
    MECompteur c = (MECompteur)table.get(un_objet);
    if((c != null) && (c.i != 1))
      c.i--;
    else
      table.remove(un_objet);
  }

  /**
   * Retourne la cardinalite d'un element de l'ensemble, ie le nombre de fois
   * qu'il est present dans l'ensemble.
   **/
  public int cardinalite(Object un_objet) {
    Object c = table.get(un_objet);
    if(c == null)
      return 0;
    else
      return ((MECompteur)c).i;
  }

  /**
   * Fait une copie independante. Ne clone pas les elements.
   * C'est un traitement assez couteux.
   **/
  public Object clone() {
    MultiEnsemble nouveau = new MultiEnsemble((Hashtable)table.clone());
    for(Enumeration e = table.keys(); e.hasMoreElements();) {
      Object cle = e.nextElement();
      table.put(cle, ((MECompteur)table.get(cle)).clone());
    }
    return nouveau;
  }

  /**
   * Cree un ensemble contenant les elements du MultiEnsemble courant.
   **/
  public Ensemble ensemble() {
    return new Ensemble((Hashtable)table.clone());
  }

  /**
   * Ajoute tous les elements de un_multiensemble au MultiEnsemble courant.
   **/
  public void union(MultiEnsemble un_multiensemble) {
    Enumeration e = un_multiensemble.table.keys();

    while(e.hasMoreElements()) {
      Object objet_courant = e.nextElement();
      inserer(objet_courant,
              ((MECompteur)un_multiensemble.table.get(objet_courant)).i);
    }
  }
}

// Classe privee

class MECompteur implements Cloneable ,Serializable{

  // Variable d'instance.

  int i;

  // Constructeurs.

  protected MECompteur() {
    i = 1;
  }

  protected MECompteur(int i) {
    this.i = i;
  }

  // Methodes.

  protected Object clone() {
    try {
      return super.clone();
    } catch(CloneNotSupportedException e) {
      return new MECompteur(i);
    }
  }
}

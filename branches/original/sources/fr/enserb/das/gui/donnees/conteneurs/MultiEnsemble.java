package fr.enserb.das.gui.donnees.conteneurs;

import java.io.*;
import java.util.*;


/**
 * Cette classe impl�mente un ensemble d'objets, pouvant �tre pr�sents plusieurs fois dans l'ensemble..
 * Un compteur m�morise le nombre de fois qu'un m�me �l�ment a �t� ins�r�.
 * Les m�thodes "equals" et "hashCode" des objets sont utilis�es.
 **/
public class MultiEnsemble extends Ensemble {

  // Constructeurs.

  /**
   * Cr�e un MultiEnsemble vide.
   **/
  public MultiEnsemble() {
    super();
  }

  /**
   * Cr�e un MultiEnsemble avec une table de hachage donnee.
   * Utilise par clone.
   **/
  protected MultiEnsemble(Hashtable table) {
    super(table);
  }

  // Methodes.

  /**
   * Ins�re un objet dans le MultiEnsemble. Si l'objet est d�j� present,
   * son compteur est incr�ment�. "un_objet" ne doit pas �tre "null".
   **/
  public void inserer(Object un_objet) {
    Object c = table.get(un_objet);
    if(c == null)
      table.put(un_objet, new MECompteur());
    else
      ((MECompteur)c).i++;
  }

  /**
   * Ins�re un objet dans le MultiEnsemble avec une cardinalit� donn�e.
   * Si l'objet est d�j� pr�sent, son compteur est incr�ment� de la
   * cardinalit�. "un_objet" ne doit pas �tre "null".
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
   * Supprime un objet de l'ensemble. Si l'objet n'est pas pr�sent, rien ne se passe.
   * Si l'objet a �t� ins�r� plusieurs fois, son compteur est d�cr�ment�.
   **/
  public void supprimer(Object un_objet) {
    MECompteur c = (MECompteur)table.get(un_objet);
    if((c != null) && (c.i != 1))
      c.i--;
    else
      table.remove(un_objet);
  }

  /**
   * Retourne la cardinalit� d'un �l�ment de l'ensemble, ie le nombre de fois
   * qu'il est pr�sent dans l'ensemble.
   **/
  public int cardinalite(Object un_objet) {
    Object c = table.get(un_objet);
    if(c == null)
      return 0;
    else
      return ((MECompteur)c).i;
  }

  /**
   * Fait une copie ind�pendante. Ne clone pas les �l�ments.
   * C'est un traitement assez co�teux.
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
   * Cr�e un ensemble contenant les �l�ments du MultiEnsemble courant.
   **/
  public Ensemble ensemble() {
    return new Ensemble((Hashtable)table.clone());
  }

  /**
   * Ajoute tous les �l�ments de un_multiensemble au MultiEnsemble courant.
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

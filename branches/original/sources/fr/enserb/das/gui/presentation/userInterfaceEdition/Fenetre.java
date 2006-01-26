package fr.enserb.das.gui.presentation.userInterfaceEdition;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import fr.enserb.das.gui.presentation.*;

public abstract class Fenetre extends JFrame {
 // Quelques constantes
  // Caracteristiques par defaut d'un document.
  protected static final Color COULEUR_FOND_PAR_DEFAUT = Color.lightGray;
  protected static final int DIM_X_PAR_DEFAUT = 650;
  protected static final int DIM_Y_PAR_DEFAUT = 600;

  /** Le graphe-visualisable manipulé */
  protected VueGraphe vueGraphe;
 /** La sélection est considérée comme une donnée courante */
  public SelectionDessin selection; 

 /** La couleur de fond */
  protected Color couleur_de_fond;

 /** Le fichier édité */
  protected File fichier_edite;
  /** La boite de dialogue de choix de fichier*/
  protected JFileChooser fc;

  /** ContentPane de la JFrame : le panel dans lequel on va ajouter des éléments d'interface*/
  protected JPanel content;

  /** Pane qui supporte le grapheVisu*/
  protected JScrollPane scroller;

    /** Retourne le VueGraphe édité. **/
  public VueGraphe getVueGraphe() {
    return vueGraphe;
  }
  public SelectionDessin selection() {
    return selection;
  }

  /** Retourne la selection courante.**/
  /** Retourne la couleur de fond. **/
  public Color couleur_de_fond() {
    return couleur_de_fond;
  }


  /** Retourne le fichier édité. **/
  public File fichier_edite() {
    return fichier_edite;
  }

    /** changing the edited file */
    public void setFichierEdite(File f){
	fichier_edite = f;
	mettreAJourTitreFenetre(); }
 

 /**
   * Cette méthode met à jour le titre de la fenêtre : elle est appelée
   * à chaque fois que l'on modifie la variable fichier_edite.
   **/
  protected void mettreAJourTitreFenetre() {
    String nom_fichier;
    if(fichier_edite == null)
      nom_fichier = "no title";
    else
      nom_fichier = fichier_edite.getName();
    setTitle(titre() + " [" + nom_fichier + "]");
  }


  /**
   *  Met à jour le titre de la fenêtre a partir d'un fichier:
   * on appelle cette méthode à l'ouverture d'un fichier.
   **/
  public void mettreAJourTitreFenetre(File fichier) {
    String nom_fichier;
    if(fichier == null)
      nom_fichier = "no title";
    else {
      nom_fichier = fichier.getName();
      fichier_edite = fichier;
    }
    setTitle(titre() + " [" + nom_fichier + "]");
  }
    public abstract void changerVueGraphe(VueGraphe graphe);
    protected abstract String titre();
    public abstract String type();
 
}















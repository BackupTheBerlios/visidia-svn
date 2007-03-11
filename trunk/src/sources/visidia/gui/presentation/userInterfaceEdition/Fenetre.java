package visidia.gui.presentation.userInterfaceEdition;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.VueGraphe;

public abstract class Fenetre extends JFrame {
	// Quelques constantes
	// Caracteristiques par defaut d'un document.
	protected static final Color COULEUR_FOND_PAR_DEFAUT = Color.lightGray;

	protected static final int DIM_X_PAR_DEFAUT = 800;

	protected static final int DIM_Y_PAR_DEFAUT = 600;

	/** Le graphe-visualisable manipulé */
	protected VueGraphe vueGraphe;

	/** La sélection est considerée comme une donnée courante */
	public SelectionDessin selection;

	/** La couleur de fond */
	protected Color couleur_de_fond;

	/** Le fichier édité */
	protected File fichier_edite;

	/** La boite de dialogue de choix de fichier */
	protected JFileChooser fc;

	/**
	 * ContentPane de la JFrame : le panel dans lequel on va ajouter des
	 * éléments d'interface
	 */
	protected JPanel content;

	/** Pane qui supporte le grapheVisu */
	protected JScrollPane scroller;

	/** Retourne le VueGraphe édite. * */
	public VueGraphe getVueGraphe() {
		return this.vueGraphe;
	}

	public SelectionDessin selection() {
		return this.selection;
	}

	/** Retourne la sélection courante.* */
	/** Retourne la couleur de fond. * */
	public Color couleur_de_fond() {
		return this.couleur_de_fond;
	}

	/** Retourne le fichier édite. * */
	public File fichier_edite() {
		return this.fichier_edite;
	}

	/** changing the edited file */
	public void setFichierEdite(File f) {
		this.fichier_edite = f;
		this.mettreAJourTitreFenetre();
	}

	/**
	 * Cette méthode met à jour le titre de la fenêtre : elle est appelée a
	 * chaque fois que l'on modifie la variable fichier_edite.
	 */
	protected void mettreAJourTitreFenetre() {
		String nom_fichier;
		if (this.fichier_edite == null)
			nom_fichier = "no title";
		else
			nom_fichier = this.fichier_edite.getName();
		this.setTitle(this.titre() + " [" + nom_fichier + "]");
	}

	protected void mettreAJourTitreFenetre(String nom_fichier) {
		this.setTitle(this.titre() + " [" + nom_fichier + "]");
	}

	/**
	 * Met a jour le titre de la fenêtre à partir d'un fichier: on appelle cette
	 * methode à l'ouverture d'un fichier.
	 */
	public void mettreAJourTitreFenetre(File fichier) {
		String nom_fichier;
		if (fichier == null)
			nom_fichier = "no title";
		else {
			nom_fichier = fichier.getName();
			this.fichier_edite = fichier;
		}
		this.setTitle(this.titre() + " [" + nom_fichier + "]");
	}

	public abstract void changerVueGraphe(VueGraphe graphe);

	protected abstract String titre();

	public abstract String type();

}

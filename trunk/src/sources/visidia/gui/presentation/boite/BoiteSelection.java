package visidia.gui.presentation.boite;

import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import visidia.gui.donnees.conteneurs.MultiEnsemble;

/** Cette boite affiche les caracteristiques d'une sélection
 * d'élements du graphe courant.*/
public class BoiteSelection extends Boite {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -2488576600196748587L;
//Editeur editeur;
  MultiEnsemble table_types;
  int taille;
  

  public BoiteSelection(JFrame parent, int taille_selection, MultiEnsemble table_types) {
    super(parent, "Selection properties", DISMISS_OPTION);
    //this.editeur = editeur;
    this.taille = taille_selection;
    this.table_types = table_types;
  }
 
  public static void show(JFrame parent, int taille_selection, MultiEnsemble table_types) {
    BoiteSelection b = new BoiteSelection(parent, taille_selection, table_types);
    b.showDialog();
  }
  
  public JComponent createContent() {

    JPanel mainPane = new JPanel();
    BoxLayout mainLayout = new BoxLayout(mainPane, BoxLayout.Y_AXIS);
    mainPane.setLayout(mainLayout);
    mainPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							this.taille + " selected objects, " +
							this.table_types.taille() + " type(s)"));		
    JPanel tmp = new JPanel();
    Enumeration tous_les_types = this.table_types.elements();
    while(tous_les_types.hasMoreElements()) {
      String un_type = (String)tous_les_types.nextElement();
      tmp = new JPanel(new GridLayout(1, 3));
      tmp.add(new JLabel(un_type));
      tmp.add(new JLabel(Integer.toString(this.table_types.cardinalite(un_type))));
      mainPane.add(tmp);
    }
    
    return mainPane;
  }
  
}



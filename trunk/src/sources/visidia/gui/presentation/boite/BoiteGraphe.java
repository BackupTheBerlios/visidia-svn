package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
//import java.util.*;
//import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.*;

/** Cette boite affiche les proprietes d'un graphe et de son editeur (couleur de fond, taille...)
Elle permet aussi de les modifier.
**/
public class BoiteGraphe extends Boite implements ActionListener, ChangeListener {

  /** Le bouton "Apply", qui permet d'appliquer les changements. */
  protected JButton applyButton;
  /** Le bouton de choix de la couleur de fond.*/
  protected JButton colorChooserButton;
  /** Le curseur permettant de regler la largeur du plan de travail.*/
  protected JSlider x_slider;
  /** Le curseur permettant de regler la hauteur du plan de travail.*/
  protected JSlider y_slider;
  protected JLabel colorLabel, heightLabel, widthLabel;
  
  /** L'editeur dont on affiche les proprietes.*/
  protected Editeur editeur;
  /** Un booleen qui vaut VRAI si une des valeurs affichees dans la boite est differente de la valeur effective.*/
  protected boolean modif = false;
  /** La valeur de la largeur du plan de travail affichee dans la boite.*/
  protected int new_width;
  /** La valeur de la hauteur du plan de travail affichee dans la boite.*/
  protected int new_height;
  /** La couleur du plan de travail affichee dans la boite.*/
  protected Color newColor;
  
  /** Cree une nouvelle boite.*/
  public BoiteGraphe(Editeur parent) {

    super(parent, "Document Properties", OK_CANCEL_OPTION);
    this.editeur = parent;
  }

  public static void show(Editeur parent) {
    BoiteGraphe b = new BoiteGraphe(parent);
    b.showDialog();
  }
   
  public JComponent createContent() {

    JPanel mainPane = new JPanel();
    BorderLayout mainLayout = new BorderLayout();
    mainLayout.setVgap(10);
    mainPane.setLayout(mainLayout);

    JPanel infoPane = new JPanel(new GridLayout(3, 2));
    infoPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							"Graph caracteritics"));

    //infoPane.add(new JLabel("Graph model :"));
    //infoPane.add(new JLabel(editeur.graph().type()));
    infoPane.add(new JLabel("Number of vertices :"));
    infoPane.add(new JLabel(Integer.toString(editeur.graph().ordre())));
    infoPane.add(new JLabel("Number of edges :"));
    infoPane.add(new JLabel(Integer.toString(editeur.graph().taille())));
    
    mainPane.add(infoPane, BorderLayout.NORTH);
    
    JPanel gfxPropPane = new JPanel(new GridLayout(3, 2));
    
    gfxPropPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							   "Other document properties"));

    colorLabel = new JLabel("Background color : "
			    + Integer.toString(editeur.couleur_de_fond().getRed())
			    + ", " + Integer.toString(editeur.couleur_de_fond().getBlue())	
			    + ", " + Integer.toString(editeur.couleur_de_fond().getGreen()));
    newColor = editeur.couleur_de_fond();
    colorChooserButton = new JButton("Change color");
    colorChooserButton.addActionListener(this);

    new_width = editeur.grapheVisuPanel().getPreferredSize().width;
    widthLabel = new JLabel("Desk width : "
			    + Integer.toString(new_width));
    x_slider = new JSlider(0,2 * new_width, new_width);
    x_slider.addChangeListener(this);
    
    new_height = editeur.grapheVisuPanel().getPreferredSize().height;
    heightLabel = new JLabel("Desk height : " 
			     + Integer.toString(new_height));
    y_slider = new JSlider(0,2 * new_height, new_height);
    y_slider.addChangeListener(this);
    
    gfxPropPane.add(colorLabel);
    gfxPropPane.add(colorChooserButton);
    gfxPropPane.add(widthLabel);
    gfxPropPane.add(x_slider);
    gfxPropPane.add(heightLabel);
    gfxPropPane.add(y_slider);

    mainPane.add(gfxPropPane, BorderLayout.CENTER);

    applyButton = new JButton("Apply");
    applyButton.addActionListener(this);
    applyButton.setEnabled(modif);
    
    mainPane.add(applyButton, BorderLayout.SOUTH);
    
    return mainPane;
  }
  
  /** Cette methode est appelee quand l'utilisateur clique sur le bouton <b>Choose color</b> ou sur le bouton <b>Apply</b> */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == colorChooserButton) {
      Color choosedColor = JColorChooser.showDialog(parent, 
						    "Choose color", 
						    editeur.couleur_de_fond());
      if (newColor != null) {
	modif = true;
	applyButton.setEnabled(modif);
	newColor = choosedColor;
      }
      colorLabel.setText("Background color : " 
			 + Integer.toString(newColor.getRed())
			 + ", " + Integer.toString(newColor.getBlue())	
			 + ", " + Integer.toString(newColor.getGreen()));
      colorChooserButton.setBackground(newColor);
    }
    if (e.getSource() == applyButton) {
      modif = false;
      applyButton.setEnabled(modif);
      boutonOkAppuye();
    }
  }
  
  /** Cette methode est appelee quand l'utilisateur actionne l'une des deux jauges permettant de regler la taille du plan de travail.*/
  public void stateChanged(ChangeEvent evt) {
    if (evt.getSource() == x_slider) {
      modif = true;
      applyButton.setEnabled(modif);
      new_width = x_slider.getValue();
      widthLabel.setText("Desk width : "
			 + Integer.toString(new_width));
    }
    if (evt.getSource() == y_slider) {
      modif = true;
      applyButton.setEnabled(modif);
      new_height = y_slider.getValue();
      heightLabel.setText("Desk height : "
			  + Integer.toString(new_height));
    }
  }
  
  public void boutonOkAppuye() {
    editeur.change_couleur_de_fond(newColor);
    editeur.grapheVisuPanel().setBackground(newColor);
    editeur.grapheVisuPanel().setPreferredSize(new Dimension(new_width, new_height));
    editeur.grapheVisuPanel().revalidate();
    editeur.grapheVisuPanel().repaint();
  }
}







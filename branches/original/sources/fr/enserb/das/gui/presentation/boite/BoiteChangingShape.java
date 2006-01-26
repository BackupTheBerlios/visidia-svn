package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.enserb.das.gui.presentation.*;

/**
 * This class create a box to change the shape of objects
 **/
public abstract class BoiteChangingShape implements ActionListener {

  /** La fenêtre parent : la boite sera centrée sur cette fenêtre.*/
  protected JFrame parent;
  /** Le JDialog dans lequel on va tout afficher.*/
  protected JDialog dialog;
  /** La partie de fenêtre qui permet d'afficher les caractéristiques de l'FormeDessin. */
  protected JPanel caracteristicsPane;
  /** Le bouton Ok*/
  protected JButton buttonOk;
  /** Le bouton Cancel*/
  protected JButton buttonCancel;
  /** Le bouton Apply*/
  protected JButton buttonApply;

    protected VueGraphe vueGraphe;
  /** the path directory for factories */
    protected String factoryPath = "fr/enserb/das/gui/presentation/factory";
    protected String factoryPointPath = "fr.enserb.das.gui.presentation.factory.";
    /** Ce booléen vaut VRAI si une des caractéristiques de la FormeDessin a été 
      modifiée par l'utilisateur, et FAUX sinon.*/
  protected boolean modif;
  /** Ce booléen vaut VRAI si les caractéristiques de la FormeDessin peuvent être modifiées, et FAUX sinon.*/
  protected boolean est_editable;
   
  /** Crée une nouvelle boite pour afficher les caractéristiques de "forme". 
      Ces caractéristiques seront modifiables.*/
  public BoiteChangingShape(JFrame parent, VueGraphe v) {
    this(parent,v, "Object properties");
  }
  
    /** Crée une nouvelle boite appelée "titre", associee au vueGraphe */
  public BoiteChangingShape(JFrame parent, 
			    VueGraphe v,
			    String titre) {
    
    this.dialog = new JDialog(parent, titre);
    this.parent = parent;
    this.modif = false;
    this.vueGraphe = v;
    
    this.est_editable = true;
    
    caracteristicsPane = new JPanel();
    BoxLayout caracteristicsLayout = new BoxLayout(caracteristicsPane, BoxLayout.Y_AXIS);
    caracteristicsPane.setLayout(caracteristicsLayout);

    caracteristicsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
								  "Caracteristics"));
    
    dialog.getContentPane().setLayout(new BorderLayout());
    dialog.getContentPane().add(caracteristicsPane, BorderLayout.NORTH);
        
    ajouterBoutons(est_editable);
    
  }
  
  /** Affiche la boite et la centre par rapport à "parent".*/
  public void show() {
    dialog.pack();
    dialog.show();
    dialog.setLocationRelativeTo(parent);
  }
  
   /** Ajoute un label et une liste de choix à un panel
    * @param pane le panel où on ajoute ces éléments
    * @param label le texte affiché dans le label.
    * @param choices[] un tableau qui contient les valeurs à inclure dans le liste de choix.
    * @param initial_choice la valeur initiale affichée par la liste de choix.  
    * @param editable si ce booléen vaut VRAI, la sélection de la liste de choix est modifiable
    * @return La liste de choix.
    **/	
  public JComboBox ligne_choix(JPanel pane,
			       String label,
			       Vector choices,
			       boolean editable,
			       Object initial_choice) {
    JPanel tmp = new JPanel(new GridLayout(1, 2));
    tmp.add(new JLabel(label));
    JComboBox comboBox = new JComboBox(choices);
    comboBox.setSelectedItem(initial_choice);
    comboBox.setEditable(false);
    comboBox.setEnabled(editable);
    tmp.add(comboBox);
    pane.add(tmp);
    pane.add(Box.createRigidArea(new Dimension(0, 5)));
    return comboBox;
  }
  
  /** Ajoute les boutons en bas de la boite. Suivant "est_editable", certains d'entre eux seront actifs ou non.*/
  public void ajouterBoutons(boolean est_editable) {
    JPanel buttonPane = new JPanel(new FlowLayout());
    
    buttonOk = new JButton("Ok");
    buttonOk.addActionListener(this);
    
    buttonCancel = new JButton("Cancel");
    buttonCancel.addActionListener(this);

    if (est_editable) {
      buttonApply = new JButton("Apply");
      buttonApply.addActionListener(this);
    }
    
    buttonPane.add(buttonOk);
    buttonPane.add(buttonCancel);    
    buttonPane.add(buttonApply);
    buttonApply.setEnabled(modif);
    dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
  }
  

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == buttonOk) {
      try {
        buttonOk();
        dialog.setVisible(false);
        dialog.dispose();
      } catch(NumberFormatException exception) {
        StringTokenizer st =
          new StringTokenizer(exception.getMessage(), "\n");
        int nb_lignes = st.countTokens();
  	String message = new String();
        for(int i = 0; i < nb_lignes; i++)
          message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
      }
    }
    if(e.getSource() == buttonApply) {
      try {
	buttonOk();
	parent.repaint();
	modif = false;
	buttonApply.setEnabled(false);
      } catch(NumberFormatException exception) {
	StringTokenizer st =
	  new StringTokenizer(exception.getMessage(), "\n");
	int nb_lignes = st.countTokens();
	String message = new String();
	for(int i = 0; i < nb_lignes; i++)
	  message = message + "\n" + st.nextToken();
	JOptionPane.showMessageDialog(parent,
				      message, 
				      "Warning",
				      JOptionPane.WARNING_MESSAGE);
	}
    }
    if(e.getSource() == buttonCancel) {
      dialog.setVisible(false);
      dialog.dispose();
    }
  }
  
    /** Retourne VRAI si une des caractéristiques modified */
  public void elementModified() {
    modif = true;
    buttonApply.setEnabled(modif);
  }
  
  /** Cette méthode est appelée si l'utilisateur appuie sur le bouton Ok.*/
  public abstract void buttonOk();

   /** Retourne le JDialog. */
  public JDialog dialog() {
    return dialog;
  }
  
  // returns true if the string is the name of a ".class" file
    protected boolean accept(String s)	{
    	int i = s.lastIndexOf('.');
    	if (i > 0 &&  i < s.length() - 1) {
      		String extension = s.substring(i+1).toLowerCase();
      	return (extension.equals("class"));
    }
    return false;
  }
  
  // return the name of the executable linked to this file name
  protected String nameWithoutExtension(String s)	{
    	int i = s.lastIndexOf('.');
    	return s.substring(0,i);
      	}
}

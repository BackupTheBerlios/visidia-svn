package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import visidia.gui.presentation.*;

/**
 * Cette classe cree une boite utilisee pour afficher et modifier les proprietes
 * d'une FormeDessin. Les sous-classes de cette classe la raffinent en y ajoutant de
 * nouveaux elements.
 **/
public class BoiteFormeDessin implements ActionListener {

  /** La fenetre parent : la boite sera centree sur cette fenetre.*/
  protected JFrame parent;
  /** Le JDialog dans lequel on va tout afficher.*/
  protected JDialog dialog;
  /** La partie de fenetre qui permet d'afficher les caracteristiques de l'FormeDessin. */
  protected JPanel caracteristicsPane;
  /** Le bouton Ok*/
  protected JButton buttonOk;
  /** Le bouton Cancel*/
  protected JButton buttonCancel;
  /** Le bouton Apply*/
  protected JButton buttonApply;

  /** La FormeDessin dont on affiche les caracteristiques.*/
  protected FormeDessin forme;
    protected boolean modif;
  /** Ce booleen vaut VRAI si les caracteristiques de la FormeDessin peuvent etre modifiees, et FAUX sinon.*/
  protected boolean est_editable;
  /** Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur du trait de l'ObjtVisu */
  protected LigneChoixCouleur couleur_trait;
  
  /** Cree une nouvelle boite pour afficher les caracteristiques de "forme". Ces caracteristiques seront modifiables.*/
  public BoiteFormeDessin(JFrame parent, FormeDessin forme) {
    this(parent, forme, "Object properties", true);
  }
  
  /** Cree une nouvelle boite appelee "titre" pour afficher les 
    caracteristiques de "forme". Suivant la valeur de "est_editable", 
  les caracteristiques de la FormeDessin seront modifiables.*/
  public BoiteFormeDessin(JFrame parent,
			FormeDessin forme,
		        String titre,
                        boolean est_editable) {
    
    this.dialog = new JDialog(parent, titre);
    this.parent = parent;
    this.modif = false;
    
    this.forme = forme;
    this.est_editable = est_editable;
    
    caracteristicsPane = new JPanel();
    BoxLayout caracteristicsLayout = new BoxLayout(caracteristicsPane, BoxLayout.Y_AXIS);
    caracteristicsPane.setLayout(caracteristicsLayout);

    caracteristicsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
								  "Caracteristics"));
    
    ligne_non_editable(caracteristicsPane, "Object type :", forme.type());

    couleur_trait = new LigneChoixCouleur(this,
					  "Line color (R,G,B) : ", 
					  ((FormeDessin)forme).couleurTrait().getRed(),
					  ((FormeDessin)forme).couleurTrait().getGreen(),
					  ((FormeDessin)forme).couleurTrait().getBlue(), 
					  est_editable);
    caracteristicsPane.add(couleur_trait.panel());
    
    
    dialog.getContentPane().setLayout(new BorderLayout());
    dialog.getContentPane().add(caracteristicsPane, BorderLayout.NORTH);
    
    
    ajouterBoutons(est_editable);
    
  }
  
  /** Affiche la boite et la centre par rapport a "parent".*/
  public void show(Frame parent) {
    dialog.pack();
    dialog.show();
    dialog.setLocationRelativeTo(parent);
  }
  
  /** Ajoute un bouton nomme "label" au panel "pane" *
      public JButton addButton(JPanel pane, String label) {
      JPanel tmp = new JPanel(new FlowLayout());
      JButton button = new JButton(label);
      tmp.add(button);
      button.setSize(button.getMinimumSize());
      pane.add(tmp);
      pane.add(Box.createRigidArea(new Dimension(0, 5)));
      return button;
      }
  

  /** Ajoute une ligne au panel "pane", qui permet d'afficher 
    le texte "label" et la valeur "data". La valeur "data" ne 
    sera pas modifiable.*/
    public void ligne_non_editable(JPanel pane, String label, String data) {
	JPanel tmp = new JPanel(new GridLayout(1,2));
	tmp.add(new JLabel(label));
	tmp.add(new JLabel(data));    
	pane.add(Box.createRigidArea(new Dimension(0, 5)));
	pane.add(tmp);
    }
  
  /** Ajoute un label et un champ de saisie a un panel.
    * @param pane le panel ou on ajoute ces elements
    * @param label le texte affiche dans le label.
    * @param initialValue la valeur initiale affichee dans le champ de saisie.
    * @param editable si ce booleen vaut VRAI, le champ de saisie est modifiable
    * @return Le champ de saisie.
    **/
  public JTextField ligne_editable(JPanel pane, 
				   String label, 
				   String initialValue, 
				   boolean editable) {
    JPanel tmp = new JPanel(new GridLayout(1,2));
    tmp.add(new JLabel(label));
    JTextField textField = new JTextField(initialValue, 6);
    tmp.add(textField);
    textField.setEditable(editable);
    pane.add(tmp);
    pane.add(Box.createRigidArea(new Dimension(0, 5)));
    return textField;
  }
  
  /** Ajoute un label et une liste de choix a un panel
    * @param pane le panel ou on ajoute ces elements
    * @param label le texte affiche dans le label.
    * @param choices[] un tableau qui contient les valeurs a inclure dans le liste de choix.
    * @param initial_choice la valeur initiale affichee par la liste de choix.  
    * @param editable si ce booleen vaut VRAI, la selection de la liste de choix est modifiable
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
  
  /** Retourne VRAI si une des caracteristiques de l'FormeDessin a ete modifiee, et FAUX sinon.*/ 
  public void elementModified() {
    modif = true;
    buttonApply.setEnabled(modif);
  }
  
  /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
  public void buttonOk() {
    Color trait;
    try {
	trait = new Color(couleur_trait.getRed(), couleur_trait.getGreen(), couleur_trait.getBlue());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
    }
    
    ((FormeDessin)forme).changerCouleurTrait(trait);
  }

  /** Active ou desactive le bouton de choix de couleur, suivant la valeur du booleen passe en argument. */
  public void couleurTraitSetEditable(boolean t) {
    couleur_trait.setEditable(t);
  }

  /** Retourne le JDialog. */
  public JDialog dialog() {
    return dialog;
  }
  
}


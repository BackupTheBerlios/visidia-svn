package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.enserb.das.gui.presentation.*;

/**
 * Cette classe cr�e une boite utilis�e pour afficher et modifier les propri�t�s
 * d'une FormeDessin. Les sous-classes de cette classe la raffinent en y ajoutant de
 * nouveaux �l�ments.
 **/
public class BoiteFormeDessin implements ActionListener {

  /** La fen�tre parent : la boite sera centr�e sur cette fen�tre.*/
  protected JFrame parent;
  /** Le JDialog dans lequel on va tout afficher.*/
  protected JDialog dialog;
  /** La partie de fen�tre qui permet d'afficher les caract�ristiques de l'FormeDessin. */
  protected JPanel caracteristicsPane;
  /** Le bouton Ok*/
  protected JButton buttonOk;
  /** Le bouton Cancel*/
  protected JButton buttonCancel;
  /** Le bouton Apply*/
  protected JButton buttonApply;

  /** La FormeDessin dont on affiche les caract�ristiques.*/
  protected FormeDessin forme;
    protected boolean modif;
  /** Ce bool�en vaut VRAI si les caract�ristiques de la FormeDessin peuvent �tre modifi�es, et FAUX sinon.*/
  protected boolean est_editable;
  /** Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur du trait de l'ObjtVisu */
  protected LigneChoixCouleur couleur_trait;
  
  /** Cr�e une nouvelle boite pour afficher les caract�ristiques de "forme". Ces caract�ristiques seront modifiables.*/
  public BoiteFormeDessin(JFrame parent, FormeDessin forme) {
    this(parent, forme, "Object properties", true);
  }
  
  /** Cr�e une nouvelle boite appel�e "titre" pour afficher les 
    caract�ristiques de "forme". Suivant la valeur de "est_editable", 
  les caract�ristiques de la FormeDessin seront modifiables.*/
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
  
  /** Affiche la boite et la centre par rapport � "parent".*/
  public void show(Frame parent) {
    dialog.pack();
    dialog.show();
    dialog.setLocationRelativeTo(parent);
  }
  
  /** Ajoute un bouton nomm� "label" au panel "pane" *
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
  
  /** Ajoute un label et un champ de saisie � un panel.
    * @param pane le panel o� on ajoute ces �l�ments
    * @param label le texte affich� dans le label.
    * @param initialValue la valeur initiale affich�e dans le champ de saisie.
    * @param editable si ce bool�en vaut VRAI, le champ de saisie est modifiable
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
  
  /** Ajoute un label et une liste de choix � un panel
    * @param pane le panel o� on ajoute ces �l�ments
    * @param label le texte affich� dans le label.
    * @param choices[] un tableau qui contient les valeurs � inclure dans le liste de choix.
    * @param initial_choice la valeur initiale affich�e par la liste de choix.  
    * @param editable si ce bool�en vaut VRAI, la s�lection de la liste de choix est modifiable
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
  
  /** Retourne VRAI si une des caract�ristiques de l'FormeDessin a �t� modifi�e, et FAUX sinon.*/ 
  public void elementModified() {
    modif = true;
    buttonApply.setEnabled(modif);
  }
  
  /** Cette m�thode est appel�e si l'utilisateur appuie sur le bouton Ok.*/
  public void buttonOk() {
    Color trait;
    try {
	trait = new Color(couleur_trait.getRed(), couleur_trait.getGreen(), couleur_trait.getBlue());
    } catch(NumberFormatException exception) {
	throw new NumberFormatException("Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
    }
    
    ((FormeDessin)forme).changerCouleurTrait(trait);
  }

  /** Active ou d�sactive le bouton de choix de couleur, suivant la valeur du bool�en pass� en argument. */
  public void couleurTraitSetEditable(boolean t) {
    couleur_trait.setEditable(t);
  }

  /** Retourne le JDialog. */
  public JDialog dialog() {
    return dialog;
  }
  
}


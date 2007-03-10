package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import visidia.gui.presentation.VueGraphe;

/**
 * This class create a box to change the shape of objects
 **/
public abstract class BoiteChangingShape implements ActionListener {

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

    protected VueGraphe vueGraphe;
    /** the path directory for factories */
    protected String factoryPath = "visidia/gui/presentation/factory";
    protected String factoryPointPath = "visidia.gui.presentation.factory.";
    /** Ce booleen vaut VRAI si une des caracteristiques de la FormeDessin a ete 
	modifiee par l'utilisateur, et FAUX sinon.*/
    protected boolean modif;
    /** Ce booleen vaut VRAI si les caracteristiques de la FormeDessin peuvent etre modifiees, et FAUX sinon.*/
    protected boolean est_editable;
   
    /** Cree une nouvelle boite pour afficher les caracteristiques de "forme". 
	Ces caracteristiques seront modifiables.*/
    public BoiteChangingShape(JFrame parent, VueGraphe v) {
	this(parent,v, "Object properties");
    }
  
    /** Cree une nouvelle boite appelee "titre", associee au vueGraphe */
    public BoiteChangingShape(JFrame parent, 
			      VueGraphe v,
			      String titre) {
    
	this.dialog = new JDialog(parent, titre);
	this.parent = parent;
	this.modif = false;
	this.vueGraphe = v;
    
	this.est_editable = true;
    
	this.caracteristicsPane = new JPanel();
	BoxLayout caracteristicsLayout = new BoxLayout(this.caracteristicsPane, BoxLayout.Y_AXIS);
	this.caracteristicsPane.setLayout(caracteristicsLayout);

	this.caracteristicsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
								      "Caracteristics"));
    
	this.dialog.getContentPane().setLayout(new BorderLayout());
	this.dialog.getContentPane().add(this.caracteristicsPane, BorderLayout.NORTH);
        
	this.ajouterBoutons(this.est_editable);
    
    }
  
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show() {
	this.dialog.pack();
	this.dialog.setVisible(true);
	this.dialog.setLocationRelativeTo(this.parent);
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
    
	this.buttonOk = new JButton("Ok");
	this.buttonOk.addActionListener(this);
    
	this.buttonCancel = new JButton("Cancel");
	this.buttonCancel.addActionListener(this);

	if (est_editable) {
	    this.buttonApply = new JButton("Apply");
	    this.buttonApply.addActionListener(this);
	}
    
	buttonPane.add(this.buttonOk);
	buttonPane.add(this.buttonCancel);    
	buttonPane.add(this.buttonApply);
	this.buttonApply.setEnabled(this.modif);
	this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
  

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == this.buttonOk) {
	    try {
		this.buttonOk();
		this.dialog.setVisible(false);
		this.dialog.dispose();
	    } catch(NumberFormatException exception) {
		StringTokenizer st =
		    new StringTokenizer(exception.getMessage(), "\n");
		int nb_lignes = st.countTokens();
		String message = new String();
		for(int i = 0; i < nb_lignes; i++)
		    message = message + "\n" + st.nextToken();
		JOptionPane.showMessageDialog(this.parent,
					      message, 
					      "Warning",
					      JOptionPane.WARNING_MESSAGE);
	    }
	}
	if(e.getSource() == this.buttonApply) {
	    try {
		this.buttonOk();
		this.parent.repaint();
		this.modif = false;
		this.buttonApply.setEnabled(false);
	    } catch(NumberFormatException exception) {
		StringTokenizer st =
		    new StringTokenizer(exception.getMessage(), "\n");
		int nb_lignes = st.countTokens();
		String message = new String();
		for(int i = 0; i < nb_lignes; i++)
		    message = message + "\n" + st.nextToken();
		JOptionPane.showMessageDialog(this.parent,
					      message, 
					      "Warning",
					      JOptionPane.WARNING_MESSAGE);
	    }
	}
	if(e.getSource() == this.buttonCancel) {
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
    }
  
    /** Retourne VRAI si une des caracteristiques modified */
    public void elementModified() {
	this.modif = true;
	this.buttonApply.setEnabled(this.modif);
    }
  
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public abstract void buttonOk();

    /** Retourne le JDialog. */
    public JDialog dialog() {
	return this.dialog;
    }
  
    // returns true if the string is the name of a ".class" file
    protected boolean accept(String s)	{
    	int i = s.lastIndexOf('.');
    	if ((i > 0) &&  (i < s.length() - 1)) {
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

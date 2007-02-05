package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Cette classe cree une boite de type JColorChooser **/
public class BoiteCouleur implements ActionListener {
    
    // le panel dans lequel on va creer la boite
    protected JPanel panel;
    // le label de la boite
    protected JLabel jlabel;
    // le bouton OK
    protected JButton button;
    // le titre afficher dans le label
    protected String label;

    // La composante rouge de la couleur
    protected int R;
    // La composante verte de la couleur
    protected int G;
    // La composante bleu de la couleur
    protected int B;

    // La BoiteChangementEtatArete parent
    protected BoiteChangementCouleurArete parent;

    /** Cree une nouvelle ligne
     * @param parent la BoiteChangementEtatArete parente.
     * @param label le titre affiche dans le label.
     * @param R la composante rouge de la couleur.
     * @param G la composante verte de la couleur.
     * @param B la composante bleu de la couleur.
     * @param est_editable si ce booleen est vrai, le bouton sera actif
     **/
    BoiteCouleur (BoiteChangementCouleurArete parent,
		  String label,
		  int R, int G, int B,
		  boolean est_editable) {
	
	panel = new JPanel (new GridLayout(1,2));
	this.parent = parent;
	this.label = label;
	this.R = R;
	this.G = G;
	this.B = B;
	String initialValue = new String(Integer.toString(R)+","+Integer.toString(G)+","+Integer.toString(B));
	String tmp = new String(label + initialValue);
	while (tmp.length() < 38)
	    tmp = tmp + " ";
	jlabel = new JLabel(tmp);
	panel.add(jlabel);
	button = new JButton("Change Color");
	button.addActionListener(this);
	button.setEnabled(est_editable);
	panel.add(button);
    }

    // Retourne R
    public int getRed() {
	return this.R;
    }
    
    // Retourne G
    public int getGreen() {
	return this.G;
    }

    // Retourne B
    public int getBlue() {
	return this.B;
    }

    // Active ou desactive le bouton suivant la valeur de "est_editable"
    public void setEditable (boolean est_editable) {
	button.setEnabled(est_editable);
    }
    
    // Methode appelee quand l'utilisateur appuie sur le bouton
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == button) {
	    Color choosedColor = JColorChooser.showDialog(parent.dialog(),
							  "Choose a color",
							  new Color(R, G, B));

	    if(choosedColor != null) {
		this.R = choosedColor.getRed();
		this.G = choosedColor.getGreen();
		this.B = choosedColor.getBlue();
		parent.elementModified();
	    }
	    String tmp = new String(label+Integer.toString(getRed())+","+Integer.toString(getGreen())+","+Integer.toString(getBlue()));
	    while(tmp.length() < 38)
		tmp =tmp + " ";
		jlabel.setText(tmp);
	}
    }

    // Retourne le JPanel
    public JPanel panel(){
	return panel;
    }
}

package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class EtatPanel extends JPanel implements ListSelectionListener {
    
    EtatArdoise ardoise ;
    boolean minimumSize = false;
    JList liste;
    Vector listeItems;
    
    public EtatPanel(Hashtable uneHashtable, VueEtatPanel parent,
		     String defaultValue) {
	this(uneHashtable, parent, defaultValue, false);
    }

    /**
     * - minimiumSize indicates if the panel must be the
     * smallest possible.
     */
    //PFA2003
    public EtatPanel(Hashtable uneHashtable, VueEtatPanel parent,
		     String defaultValue, boolean minimumSize) {
	this.minimumSize = minimumSize;
	ardoise = new EtatArdoise(uneHashtable, parent, minimumSize);
	listeItems = new Vector();
	liste = new JList(); 
	JScrollPane listeAvecAscenseur;
	listeItems.addElement("A");
	listeItems.addElement("B");
	listeItems.addElement("C");
	listeItems.addElement("D");
	listeItems.addElement("E");
	listeItems.addElement("F");
	listeItems.addElement("G"); 
	listeItems.addElement("H");
	listeItems.addElement("I");
	listeItems.addElement("J");
	listeItems.addElement("K");
	listeItems.addElement("L");
	listeItems.addElement("M");
	listeItems.addElement("N");
	listeItems.addElement("O");
	listeItems.addElement("P");
	listeItems.addElement("Q");
	listeItems.addElement("R");
	listeItems.addElement("S");
	listeItems.addElement("T");
	listeItems.addElement("U");
	listeItems.addElement("V");
	listeItems.addElement("W");
	listeItems.addElement("X");
	listeItems.addElement("Y");
	listeItems.addElement("Z");
	
	liste = new JList(listeItems);
	
	liste.setSelectedValue(defaultValue,true);
	ardoise.changerEtat((String)liste.getSelectedValue());
	liste.addListSelectionListener(this);
	
	setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	add(ardoise);
	listeAvecAscenseur = new JScrollPane(liste);

	
	//PFA2003
	listeAvecAscenseur.setPreferredSize(minimumSize 
					    ? new Dimension(50, 80) 
					    : new Dimension(200,80));
	add(listeAvecAscenseur);

	liste.addKeyListener(new KeyAdapter () {
		public void keyPressed(KeyEvent e) {
		    char c = e.getKeyChar();
		    if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
			String strC = "" + c;
			strC = strC.toUpperCase();
			liste.setSelectedValue(strC, true);
			liste.repaint();
		    }
		}
	    });

	setVisible(true);
    }
    

    /**
     * That method must be call when the panel is visible, in order
     * to give the focus to the list. 
     */
    public void requestFocus() {
	liste.requestFocus();
    }

    public EtatPanel(Hashtable uneHashtable , VueEtatPanel parent)
    {
	this(uneHashtable, parent, "N");
    }
    
    public void valueChanged(ListSelectionEvent evt) {
	String s = (String)((JList) evt.getSource()).getSelectedValue();
	ardoise.changerEtat(s);
	ardoise.repaint();
	ardoise.donnePere().elementModified(s);
    }
    
    public EtatArdoise ardoise(){
	return ardoise;
    }
}

class EtatArdoise extends JPanel {
    VueEtatPanel parent;	
    protected  String unEtat;
    Hashtable uneHashtable;
    //PFA2003
    boolean minimumSize = false;
  	   
    public EtatArdoise(Hashtable dictionnaire, VueEtatPanel parent, 
		       boolean minimumSize) {
	this.parent = parent ;
	this.minimumSize = minimumSize;
	uneHashtable = dictionnaire;
	setPreferredSize(minimumSize ? new Dimension(50, 50) : new Dimension(200,60));
    }
    
    public void paintComponent(Graphics g){
	super.paintComponent(g);
	if (uneHashtable.get(unEtat)!= null){
	    g.setColor((Color)uneHashtable.get(unEtat));
	    if (minimumSize)
		g.fillRect(5,5,40,40);
	    else
		g.fillRect(100,20,40,40);
	}
    }
    public void changerEtat(String etat){
 	unEtat = etat ;
 	
    }
    public String donneEtat(){
	return unEtat;
    }
    public VueEtatPanel donnePere(){
	return parent;
    }
}

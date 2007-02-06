package visidia.gui.presentation.boite;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class EtatPanelDist extends JPanel implements ListSelectionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4461954503469396999L;
	EtatArdoiseDist ardoise ;
    public EtatPanelDist(Hashtable uneHashtable , BoiteChangementEtatSommetDist parent,String defaultValue) {
        this.ardoise = new EtatArdoiseDist(uneHashtable, parent);
        Vector listeItems = new Vector();
        JList liste = new JList();
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
        this.ardoise.changerEtat((String)liste.getSelectedValue());
        liste.addListSelectionListener(this);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
        this.add(this.ardoise);
        listeAvecAscenseur = new JScrollPane(liste);
        listeAvecAscenseur.setPreferredSize(new Dimension(200,80));
        this.add(listeAvecAscenseur);
        this.setVisible(true);
    }
    
    public EtatPanelDist(Hashtable uneHashtable , BoiteChangementEtatSommetDist parent) {
        this(uneHashtable, parent, "N");
    }
    
    public void valueChanged(ListSelectionEvent evt) {
        this.ardoise.changerEtat( (String)((JList)evt.getSource()).getSelectedValue());
        this.ardoise.repaint();
        this.ardoise.donnePere().elementModified();
        
    }
    
    
    public EtatArdoiseDist ardoise(){
        return this.ardoise;
    }
    
}




class EtatArdoiseDist extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8806281112016740873L;
	BoiteChangementEtatSommetDist parent;
    protected  String unEtat;
    Hashtable uneHashtable;
    
    public EtatArdoiseDist(Hashtable dictionnaire,BoiteChangementEtatSommetDist parent){
        this.parent = parent ;
        this.uneHashtable = dictionnaire;
        this.setPreferredSize(new Dimension(200,60));
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(this.uneHashtable.get(this.unEtat)!= null){
            g.setColor((Color)this.uneHashtable.get(this.unEtat));
            g.fillRect(100,20,40,40);
        }
        
    }
    
    public void changerEtat(String etat){
        this.unEtat = etat ;
        
    }
    
    public String donneEtat(){
        return this.unEtat;
    }
    
    public BoiteChangementEtatSommetDist donnePere(){
        return this.parent;
    }
}

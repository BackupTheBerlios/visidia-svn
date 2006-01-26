package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class EtatPanel extends JPanel implements ListSelectionListener {
    
    EtatArdoise ardoise ;
    public EtatPanel(Hashtable uneHashtable , BoiteChangementEtatSommet parent,String defaultValue)
    {
	
	ardoise = new EtatArdoise(uneHashtable, parent);
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
	ardoise.changerEtat((String)liste.getSelectedValue());
	liste.addListSelectionListener(this);
	setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	add(ardoise);
	listeAvecAscenseur = new JScrollPane(liste);
	listeAvecAscenseur.setPreferredSize(new Dimension(200,80));
	add(listeAvecAscenseur);
	setVisible(true);
    }

    public EtatPanel(Hashtable uneHashtable , BoiteChangementEtatSommet parent)
    {
	this(uneHashtable, parent, "N");
    }
    
    
    public void valueChanged(ListSelectionEvent evt)
    {
	ardoise.changerEtat( (String)((JList)evt.getSource()).getSelectedValue());
	ardoise.repaint();
	ardoise.donnePere().elementModified();
	
    }
    
    
    public EtatArdoise ardoise(){
	return ardoise;
    }
    
}




class EtatArdoise extends JPanel 
{
  BoiteChangementEtatSommet parent;	
  protected  String unEtat;
  Hashtable uneHashtable;
  	   
  public EtatArdoise(Hashtable dictionnaire,BoiteChangementEtatSommet parent){
	this.parent = parent ;
	uneHashtable = dictionnaire;
	setPreferredSize(new Dimension(200,60));
  }
  public void paintComponent(Graphics g){
	super.paintComponent(g);
	if(uneHashtable.get(unEtat)!= null){
		g.setColor((Color)uneHashtable.get(unEtat));
		g.fillRect(100,20,40,40);
	}
        
  }
 public void changerEtat(String etat){
 	unEtat = etat ;
 	
 	}
public String donneEtat(){
	 return unEtat;
	}
public BoiteChangementEtatSommet donnePere(){
		return this.parent;
		}
}

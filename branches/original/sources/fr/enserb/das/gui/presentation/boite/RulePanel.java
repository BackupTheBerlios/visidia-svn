package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



public class RulePanel extends JPanel implements ItemListener  {
    protected String etatSommet1 = new String("A");
    protected String etatSommet2 = new String("A");
    protected String etatSommet3 = new String("A");
    protected String etatSommet4 = new String("A");
    protected boolean estMarquee12 = false ;
    protected boolean estMarquee34 = false;
    public final BoiteSimpleRule pere;
    
    protected JComboBox listeItemsSommet1 = new JComboBox();
    protected JComboBox listeItemsSommet2 = new JComboBox();	 
    protected JComboBox listeItemsSommet3 = new JComboBox();
    protected JComboBox listeItemsSommet4 = new JComboBox();	    	    
    protected JCheckBox boutonArete12 = new JCheckBox();
    protected JCheckBox boutonArete34 = new JCheckBox();
    public RulePanel(BoiteSimpleRule pere)
    {
	    this.pere = pere;	
	    
	  
	    JPanel panneau = new JPanel(new GridLayout(3, 2));
	    listeItemsSommet1.addItem("A");
	    listeItemsSommet1.addItem("B");
	    listeItemsSommet1.addItem("C");
	    listeItemsSommet1.addItem("D");
	    listeItemsSommet1.addItem("E");
	    listeItemsSommet1.addItem("F");
	    listeItemsSommet1.addItem("G");
	    listeItemsSommet1.addItem("H");
	    listeItemsSommet1.addItem("I");
	    listeItemsSommet1.addItem("J");
	    listeItemsSommet1.addItem("K");
	    listeItemsSommet1.addItem("L");
	    listeItemsSommet1.addItem("M");
	    listeItemsSommet1.addItem("N");
	    listeItemsSommet1.addItem("O");
	    listeItemsSommet1.addItem("P");
	    listeItemsSommet1.addItem("Q");
	    listeItemsSommet1.addItem("R");
	    listeItemsSommet1.addItem("S");
	    listeItemsSommet1.addItem("T");
	    listeItemsSommet1.addItem("U");
	    listeItemsSommet1.addItem("V");
	    listeItemsSommet1.addItem("W");
	    listeItemsSommet1.addItem("X");
	    listeItemsSommet1.addItem("Y");
	    listeItemsSommet1.addItem("Z");
	    listeItemsSommet1.addItemListener(this);
	    panneau.add(new JLabel("Sommet1"));
	    panneau.add(listeItemsSommet1);
//liste pour le sommet3

	    listeItemsSommet3.addItem("A");
	    listeItemsSommet3.addItem("B");
	    listeItemsSommet3.addItem("C");
	    listeItemsSommet3.addItem("D");
	    listeItemsSommet3.addItem("E");
	    listeItemsSommet3.addItem("F");
	    listeItemsSommet3.addItem("G");
	    listeItemsSommet3.addItem("H");
	    listeItemsSommet3.addItem("I");
	    listeItemsSommet3.addItem("J");
	    listeItemsSommet3.addItem("K");
	    listeItemsSommet3.addItem("L");
	    listeItemsSommet3.addItem("M");
	    listeItemsSommet3.addItem("N");
	    listeItemsSommet3.addItem("O");
	    listeItemsSommet3.addItem("P");
	    listeItemsSommet3.addItem("Q");
	    listeItemsSommet3.addItem("R");
	    listeItemsSommet3.addItem("S");
	    listeItemsSommet3.addItem("T");
	    listeItemsSommet3.addItem("U");
	    listeItemsSommet3.addItem("V");
	    listeItemsSommet3.addItem("W");
	    listeItemsSommet3.addItem("X");
	    listeItemsSommet3.addItem("Y");
	    listeItemsSommet3.addItem("Z");
	    listeItemsSommet3.addItemListener(this);
	    panneau.add(new JLabel("Sommet3"));
	    panneau.add(listeItemsSommet3);




//liste pour l'arete reliant Sommet1 et Sommet2	    
	    boutonArete12.addItemListener(this);
	    panneau.add(new JLabel("Arete12"));
	    panneau.add(boutonArete12);

//Liste pour l'arete reliant les sommets 3 et 4
	   
	    boutonArete34.addItemListener(this);
	    panneau.add(new JLabel("Arete34"));	    
	    panneau.add(boutonArete34);

	    
//liste pour le sommet2	    	    
	    listeItemsSommet2.addItem("A");
	    listeItemsSommet2.addItem("B");
	    listeItemsSommet2.addItem("C");
	    listeItemsSommet2.addItem("D");
	    listeItemsSommet2.addItem("E");
	    listeItemsSommet2.addItem("F");
	    listeItemsSommet2.addItem("G");
	    listeItemsSommet2.addItem("H");
	    listeItemsSommet2.addItem("I");
	    listeItemsSommet2.addItem("J");
	    listeItemsSommet2.addItem("K");
	    listeItemsSommet2.addItem("L");
	    listeItemsSommet2.addItem("M");
	    listeItemsSommet2.addItem("N");
	    listeItemsSommet2.addItem("O");
	    listeItemsSommet2.addItem("P");
	    listeItemsSommet2.addItem("Q");
	    listeItemsSommet2.addItem("R");
	    listeItemsSommet2.addItem("S");
	    listeItemsSommet2.addItem("T");
	    listeItemsSommet2.addItem("U");
	    listeItemsSommet2.addItem("V");
	    listeItemsSommet2.addItem("W");
	    listeItemsSommet2.addItem("X");
	    listeItemsSommet2.addItem("Y");
	    listeItemsSommet2.addItem("Z");
	    listeItemsSommet2.addItemListener(this);
	    panneau.add(new JLabel("Sommet2"));
	    panneau.add(listeItemsSommet2);


//liste pour le sommet 4

	    listeItemsSommet4.setName("Sommet4");
	    listeItemsSommet4.addItem("A");
	    listeItemsSommet4.addItem("B");
	    listeItemsSommet4.addItem("C");
	    listeItemsSommet4.addItem("D");
	    listeItemsSommet4.addItem("E");
	    listeItemsSommet4.addItem("F");
	    listeItemsSommet4.addItem("G");
	    listeItemsSommet4.addItem("H");
	    listeItemsSommet4.addItem("I");
	    listeItemsSommet4.addItem("J");
	    listeItemsSommet4.addItem("K");
	    listeItemsSommet4.addItem("L");
	    listeItemsSommet4.addItem("M");
	    listeItemsSommet4.addItem("N");
	    listeItemsSommet4.addItem("O");
	    listeItemsSommet4.addItem("P");
	    listeItemsSommet4.addItem("Q");
	    listeItemsSommet4.addItem("R");
	    listeItemsSommet4.addItem("S");
	    listeItemsSommet4.addItem("T");
	    listeItemsSommet4.addItem("U");
	    listeItemsSommet4.addItem("V");
	    listeItemsSommet4.addItem("W");
	    listeItemsSommet4.addItem("X");
	    listeItemsSommet4.addItem("Y");
	    listeItemsSommet4.addItem("Z");
	    listeItemsSommet4.addItemListener(this);
	    panneau.add(new JLabel("Sommet4"));	
	    panneau.add(listeItemsSommet4);
	    setLayout(new BorderLayout(5,5));
  	    add(panneau, BorderLayout.CENTER);
	    
    }
    
    public void itemStateChanged(ItemEvent evt)  
    { 
	Object source = evt.getSource();
	if(source == listeItemsSommet1)	
	    {
		etatSommet1 = new String((String)listeItemsSommet1.getSelectedItem());
		pere.elementModified();	
	    }
	else if(source == listeItemsSommet2)	
	    {
		etatSommet2 = new String((String)listeItemsSommet2.getSelectedItem());
		pere.elementModified();	
	    }
	else if(source == listeItemsSommet3)	
	    {
		etatSommet3 = new String((String)listeItemsSommet3.getSelectedItem());
		pere.elementModified();	
	    }
	else if(source == listeItemsSommet4)	
	    {
		etatSommet4 = new String((String)listeItemsSommet4.getSelectedItem());
		pere.elementModified();	
	    } 
	else if(source == boutonArete12)	
	    {
		estMarquee12 = !estMarquee12;
		pere.elementModified();	
	    }
	else if(source == boutonArete34)	
	    {
		estMarquee34 = !estMarquee34;
		pere.elementModified();	
	    }
    }
    
}

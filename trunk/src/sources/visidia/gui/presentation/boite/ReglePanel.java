package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class ReglePanel extends JPanel implements ListSelectionListener {

/**
	 * 
	 */
	private static final long serialVersionUID = 495723337179334645L;
BoutonArdoise ardoise ;

public ReglePanel(Hashtable uneHashtable , BoiteChoix parent)
{
	
    ardoise = new BoutonArdoise("A",uneHashtable, parent);
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
    liste.setSelectedIndex(0);
    ardoise.unEtat =(String)liste.getSelectedValue();
    liste.addListSelectionListener(this);
    setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
    add(ardoise);
    listeAvecAscenseur = new JScrollPane(liste);
    listeAvecAscenseur.setPreferredSize(new Dimension(200,80));
    add(listeAvecAscenseur);
    setVisible(true);
}
   
   
    public void valueChanged(ListSelectionEvent evt)
{
ardoise.unEtat = (String)((JList)evt.getSource()).getSelectedValue();
ardoise.repaint();

	
}

 
public BoutonArdoise ardoise(){
	return ardoise;
	}

	
}




class BoutonArdoise extends JPanel implements ActionListener
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -795357913763859709L;
BoiteChoix parent;	
  JButton boutonChoix;
  public String unEtat;
  Hashtable uneHashtable;	   
  public BoutonArdoise(String etat,Hashtable dictionnaire,BoiteChoix parent){
	this.parent = parent ;
	unEtat = etat;
	uneHashtable = dictionnaire;
	setPreferredSize(new Dimension(200,60));

	boutonChoix = new JButton("Change the Color");
	//boutonChoix.setBounds(50,12,40,20);
	boutonChoix.addActionListener(this);
        boutonChoix.setEnabled(true);
        add(boutonChoix);
  }
  public void paintComponent(Graphics g){
	super.paintComponent(g);
	if(uneHashtable.get(unEtat)!= null){
		g.setColor((Color)uneHashtable.get(unEtat));
		g.fillRect(85,30,30,30);
	}
        
  }
  public void actionPerformed(ActionEvent evt){
  	if(evt.getSource() == boutonChoix){

	      Color choosedColor = JColorChooser.showDialog(parent.dialog(), 
							    "Choose color", 
							    (Color)uneHashtable.get(unEtat));
	     
	      if (choosedColor != null) {
		uneHashtable.put(unEtat,choosedColor);	
                repaint();
	      }
	 }

  }

}


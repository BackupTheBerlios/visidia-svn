package visidia.gui.presentation.boite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ReglePanel extends JPanel implements ListSelectionListener {

/**
	 * 
	 */
	private static final long serialVersionUID = 495723337179334645L;
BoutonArdoise ardoise ;

public ReglePanel(Hashtable uneHashtable , BoiteChoix parent)
{
	
    this.ardoise = new BoutonArdoise("A",uneHashtable, parent);
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
    this.ardoise.unEtat =(String)liste.getSelectedValue();
    liste.addListSelectionListener(this);
    this.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
    this.add(this.ardoise);
    listeAvecAscenseur = new JScrollPane(liste);
    listeAvecAscenseur.setPreferredSize(new Dimension(200,80));
    this.add(listeAvecAscenseur);
    this.setVisible(true);
}
   
   
    public void valueChanged(ListSelectionEvent evt)
{
this.ardoise.unEtat = (String)((JList)evt.getSource()).getSelectedValue();
this.ardoise.repaint();

	
}

 
public BoutonArdoise ardoise(){
	return this.ardoise;
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
	this.unEtat = etat;
	this.uneHashtable = dictionnaire;
	this.setPreferredSize(new Dimension(200,60));

	this.boutonChoix = new JButton("Change the Color");
	//boutonChoix.setBounds(50,12,40,20);
	this.boutonChoix.addActionListener(this);
        this.boutonChoix.setEnabled(true);
        this.add(this.boutonChoix);
  }
  public void paintComponent(Graphics g){
	super.paintComponent(g);
	if(this.uneHashtable.get(this.unEtat)!= null){
		g.setColor((Color)this.uneHashtable.get(this.unEtat));
		g.fillRect(85,30,30,30);
	}
        
  }
  public void actionPerformed(ActionEvent evt){
  	if(evt.getSource() == this.boutonChoix){

	      Color choosedColor = JColorChooser.showDialog(this.parent.dialog(), 
							    "Choose color", 
							    (Color)this.uneHashtable.get(this.unEtat));
	     
	      if (choosedColor != null) {
		this.uneHashtable.put(this.unEtat,choosedColor);	
                this.repaint();
	      }
	 }

  }

}


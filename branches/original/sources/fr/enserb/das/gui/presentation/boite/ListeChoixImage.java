package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import fr.enserb.das.gui.presentation.*;

public class ListeChoixImage extends JPanel implements ListSelectionListener {
    Ardoise ardoise ;
    boolean imageChangee = false ;
    
    
    public ListeChoixImage(SommetDessin un_sommet)
    {
	ardoise = new Ardoise(un_sommet.getImage());
	Vector listeItems = new Vector();
	JList liste = new JList(); 
	JScrollPane listeAvecAscenseur;
	listeItems.addElement("no icon              ");
	listeItems.addElement("PC                   ");
	listeItems.addElement("station              ");
	listeItems.addElement("portable              ");
	listeItems.addElement("server               ");
	listeItems.addElement("mac                  ");
	
	liste = new JList(listeItems);
	liste.setSelectedIndex(0);
	ardoise.image = donneImage((String)liste.getSelectedValue());
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
	imageChangee = true;	
	ardoise.changerImage(donneImage((String)((JList)evt.getSource()).getSelectedValue()));
	ardoise.repaint();
    }
    
     ImageIcon donneImage(String s)
    {
	if(s == null || s.equals("no icon              ")) 
	    return null;
	else if (s.equals( "PC                   ")) 
	    return (new ImageIcon("fr/enserb/das/gui/donnees/images/image1.gif"));
	else if (s.equals( "station              ")) 
	    return (new ImageIcon("fr/enserb/das/gui/donnees/images/image2.gif"));
	else if (s.equals( "printer              ")) 
	    return (new ImageIcon("fr/enserb/das/gui/donnees/images/image3.gif"));
	else if (s.equals( "server               ")) 
	    return (new ImageIcon("fr/enserb/das/gui/donnees/images/image4.gif"));
	else if (s.equals( "mac                  ")) 
	    return (new ImageIcon("fr/enserb/das/gui/donnees/images/image6.jpg"));
	else return null;
	
    }
    public Ardoise ardoise(){
	return ardoise;
    }
    
    public boolean estChangee(){
	return imageChangee;
    }
}

class Ardoise extends JPanel 
{
    JLabel label = new JLabel("Choose the icon");	
    ImageIcon image = null;
    
    Ardoise(ImageIcon une_image){
	image = une_image;
	setPreferredSize(new Dimension(200,60));
	add(label,BorderLayout.WEST);
    }
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	if(image != null)
	    image.paintIcon(this,g,100,20);
        
    }
    public ImageIcon image(){
	return image;
    }
    public void changerImage(ImageIcon uneImage){
	this.image = uneImage ;
	
    }
}









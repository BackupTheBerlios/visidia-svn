package visidia.gui.metier.inputOutput;

import java.io.*;
import java.util.Enumeration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import visidia.gui.metier.*;
import visidia.gui.presentation.userInterfaceEdition.*;

/* this class permit to make the exportation of graph in GML format */

public class GMLParser extends JFileChooser implements ActionListener{

  /**
	 * 
	 */
	private static final long serialVersionUID = -2571602695974514717L;

/** The parent window */
  protected Fenetre parent;

  /** a "valid" file : with the good extension */
  protected File validFile;
  
  /** the current indentation used in the file GML */
  protected static String indentString = new String(""); 

  /** The current path for exporting. */
  protected String path;

  /** the file writer to write */
  protected static FileWriter fileWriter;
  
  /** boolean to know if we display graphics informations */
  private static boolean graphicsInfo = true;

  /* button for the graphics properties */
  JRadioButton graphicsButton;

  /** Instancies a new GMLParser object, wich will pemit to export the graph
    which is in the "parent" window, in a file of the directory "path". */
  
  public GMLParser(Fenetre parent, String path, Graphe graphe) {
    super(path);
    this.path = path;
    
    validFile = new File(path, "default.gml");
    setSelectedFile(validFile);
    this.parent = parent;
   
  }
   
  

  /** This method permit to get the name of the file chosen by the user
    * We take care here of errors and warnings (existant file, extension errors...) */

  public void approveSelection() {
    boolean save = true;
    File f = getSelectedFile();
    String s = f.getName();
    int i = s.lastIndexOf('.');
    if ((i > 0) &&  (i < s.length() - 1)) {
      String extension = s.substring(i+1).toLowerCase();
      if (!extension.equals("gml")) {
	JOptionPane.showMessageDialog(this, 
				      getName(f) + " : this file has not a recognized\n"
				      + "extension. The required extension is '.gml ",
				      "Warning", 
				      JOptionPane.WARNING_MESSAGE);
	setSelectedFile(validFile);
	save = false;
	
      } 	  
    } else {
      if (i == -1) {
	setSelectedFile(new File(path, s + ".gml"));
	save = true;
      } else {
	setSelectedFile(new File(path, s + "gml"));
	save = true;
      }
    }
    
    
    if (getSelectedFile().exists()) {
      int overwrite = JOptionPane.showConfirmDialog(this, 
						    getName(getSelectedFile()) + 
						    " : this file aldready exists.\n"
						    + "Do you want to overwrite it ?",
						    "Warning", 
						    JOptionPane.YES_NO_OPTION);
      if (overwrite == JOptionPane.YES_OPTION) {
	super.approveSelection();
      } else {
	setSelectedFile(validFile);
      }
    } else {
      if (save) {
	super.approveSelection();
      }
    }
  }
  
  /** Cancel the operation of exportation, 
    * or empty name file. */
  
  public void cancelSelection() {
    if (getSelectedFile() == null) {
      JOptionPane.showMessageDialog(this, 
				    "You must choose a file to export your graph in !",
				    "Warning", 
				    JOptionPane.WARNING_MESSAGE);
      setSelectedFile(validFile);
    } else {      
      super.cancelSelection();
    }
  }
  

  /** Exporting a graphe in GML format*/
  public static void export(Fenetre fenetre, Graphe graph) {
    
    GMLParser st = new GMLParser(fenetre, ".", graph);
    javax.swing.filechooser.FileFilter gmlFileFilter = new FileFilterGML();
    st.addChoosableFileFilter(gmlFileFilter);
    st.addTheButtons(st);
    st.setDialogTitle("Export in GML");
    st.setApproveButtonText("Export");
    st.setFileFilter(gmlFileFilter);
      
    int returnVal = st.showSaveDialog(fenetre);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
	File f = st.getSelectedFile();
	try{
	    fileWriter = new FileWriter(f);
	    creatingGMLFile(graph); // write in the object output stream
	} 
        catch(IOException e) {
	    System.out.println("Problem: " + e);
    	}
    }
  }
    
  
    // print the informations in the GML file
  private static void creatingGMLFile(Graphe graph) throws IOException
 {
     Enumeration e;
     write("graph [");
     indentMore();
     writeln("comment \"Graph make by VisDa\" ");
     // writeln("directed 1");
     e = graph.sommets();

     // for the nodes (the vertices)
     Sommet v;
     while (e.hasMoreElements())
	 {
	     v = ((Sommet)e.nextElement());
	     writeln("node [");
	     indentMore();
	     writeln("id "+v.getSommetDessin().getEtiquette());
	     if (graphicsInfo){
		writeln("graphics [");
		indentMore();
		writeln("x "+v.getSommetDessin().centreX());
		writeln("y "+v.getSommetDessin().centreY());
		indentLess();
		writeln("]");	
	     }
	     indentLess();
	     writeln("]");	     
	 }

     // for the edges
     e = graph.aretes();
     Arete edg;
     while (e.hasMoreElements())
	 {
	     edg = ((Arete)e.nextElement());
	     writeln("edge [");
	     indentMore();
	     writeln("source "+edg.origine().getSommetDessin().getEtiquette());
	     writeln("target "+edg.destination().getSommetDessin().getEtiquette());
	     if (graphicsInfo){
		writeln("graphics [");
		indentMore(); 

		writeln("Line [");
		indentMore();  

		writeln("Point [");
		indentMore();
		writeln("x "+edg.getAreteDessin().origineX());
		writeln("y "+edg.getAreteDessin().origineY());
		indentLess();
		writeln("]");

		writeln("Point [");
		indentMore();
		writeln("x "+edg.getAreteDessin().destinationX());
		writeln("y "+edg.getAreteDessin().destinationY());
		indentLess();
		writeln("]");

		indentLess();
		writeln("]"); // for line

		indentLess();
		writeln("]"); // for graphics
	     }
	     indentLess();
	     writeln("]");   // for edges
	 }
     indentLess();
     writeln("]"); // for graph

     fileWriter.close();
 } 

    // write the string in the buffer
    private static void write(String stringToWrite) throws IOException{
	fileWriter.write(stringToWrite);
	fileWriter.flush();
    }
    
    // write the string in a new line, with the rigth indentation
    private static void writeln(String stringToWrite) throws IOException{
	fileWriter.write("\n"+indentString+stringToWrite);
	fileWriter.flush();
    }


    // add some spaces to the indent string
    private static void indentMore(){
	indentString = indentString.concat("   ");
    }

    // delete some spaces to the indent string 
    private static void indentLess(){
	if (indentString.length() <3) indentString = new String("");
	else indentString = indentString.substring(0,indentString.length()-3);
    }

    public void actionPerformed(ActionEvent e){
	if (e.getSource() == graphicsButton) 
	    graphicsInfo = graphicsButton.isSelected();
    }


   private void addTheButtons(JFileChooser dialog){
	JPanel buttonPane = new JPanel(new FlowLayout());
	JLabel labelGraphics = new JLabel("Print graphics coordinates ");
	
	graphicsButton = new JRadioButton();
	graphicsButton.setSelected(graphicsInfo); // is the button selected ?
	graphicsButton.addActionListener(this);

	buttonPane.add(labelGraphics);
	buttonPane.add(graphicsButton);	    

	dialog.add(buttonPane, BorderLayout.SOUTH);
    }

}

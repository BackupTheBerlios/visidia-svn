package visidia.gui.metier.inputOutput;

import java.io.*;
import java.util.*;

import javax.swing.*;
import java.util.Enumeration;
import visidia.gui.presentation.userInterfaceSimulation.*;
import java.util.Hashtable;
import visidia.tools.LocalNodeTable;


public class OpenConfig implements Serializable{
    private static final String DEFAULT_URL_FOR_VISU = "Simulator"; 
    private Hashtable parameters = new Hashtable();
    private String visuHost;
    private String visuUrl;

    private int hostNummer=0;

    /** Open  ".config" file to set hosts for the Nodes
     */
    
    //public  void open(FenetreDeSimulationDist fenetre, VueGraphe vueGraphe){
    public void open(FenetreDeSimulationDist fenetre,int sizeOfTheGraph){
	File file_open = null; 
	JFileChooser fc = new JFileChooser("fr/enserb/das/config");
	javax.swing.filechooser.FileFilter hostFileFilter = new FileFilterConfig();
	fc.addChoosableFileFilter(hostFileFilter);
	fc.setFileFilter(hostFileFilter);
	
	int returnVal = fc.showOpenDialog(fenetre);
	if(returnVal == JFileChooser.APPROVE_OPTION)
	    file_open = fc.getSelectedFile();
	
	String file_name = fc.getName(file_open);
	if (file_name == null) return; // must implement "cancel" button action
	try{
	    FileReader fr = new FileReader(file_open);
	    BufferedReader br = new BufferedReader(fr);
	    while(br.ready()){
		String line = br.readLine();
		if (visuHost == null){
		    setVisu(line);
		}
		else{
		    handleLine(line);
		}
	    }
	    fr.close();
	    
	   
	    fenetre.setNetworkParam(createNetworkParam(sizeOfTheGraph),visuHost,visuUrl);
	
	} catch (Exception excpt) {
	    System.out.println("Problem : " + excpt);
	    excpt.printStackTrace();
	}
    }
    
    private void setVisu(String ligne){
	StringTokenizer st = new StringTokenizer(ligne);
	while (st.hasMoreTokens()) {
	    if (visuHost==null)
		visuHost=st.nextToken();
	    else if (visuUrl==null)
		visuUrl=st.nextToken();
	}
	if  (visuUrl==null)
	    visuUrl=DEFAULT_URL_FOR_VISU;
	
    }

    private void handleLine(String ligne){
	StringTokenizer st = new StringTokenizer(ligne);
	String host=null;

	// vecteur contenant les urls des noeuds locaux declare sur la machine 
	// host
	Vector vect = new Vector();
	while (st.hasMoreTokens()) {
	    if (host==null)
		host=st.nextToken();
	    else {
	        String url = st.nextToken();
		vect.addElement(url);
	    }
	}
	if(vect.isEmpty()){
	    hostNummer+=1;
	    vect.addElement(host);
	    parameters.put(host,vect);
	} else {
	    hostNummer+=vect.size();
	    parameters.put(host,vect);
	}
    }
    

    public LocalNodeTable createNetworkParam(int sizeOfTheGraph ){
	LocalNodeTable lnt = new LocalNodeTable();
	if(sizeOfTheGraph<=hostNummer){
	    int i = 0;
	    Enumeration hosts = parameters.keys();
	    while(hosts.hasMoreElements() && i<sizeOfTheGraph){
		String aHost = (String)hosts.nextElement();
		Vector localNodes = (Vector)parameters.get(aHost);
		while(! localNodes.isEmpty() && i<sizeOfTheGraph){
		    String localNode = (String)localNodes.remove(0);
		    Vector v = new Vector();
		    v.addElement(new Integer(i));
		    lnt.addLocalNode(aHost,localNode,v);
		    i++;
		}
	    }
	} else {
	    int pas = sizeOfTheGraph/hostNummer;
	    int reste = sizeOfTheGraph-hostNummer*pas;
	    Enumeration hosts = parameters.keys();
	    int current = 0;

	    while(hosts.hasMoreElements()){
		String aHost = (String)hosts.nextElement();
		Vector localNodes = (Vector)parameters.get(aHost);
		while(! localNodes.isEmpty()){
		    String localNode = (String)localNodes.remove(0);
		    if (reste>0){
			Vector vect = new Vector();
			for(int j=current; j<current+pas+1; j++)
			    vect.addElement(new Integer(j));
			
			lnt.addLocalNode(aHost,localNode,vect);
			current=current+pas+1;
			reste-=1;
		    } else {
			Vector vect = new Vector();
			for(int j=current; j<current+pas; j++)
			    vect.addElement(new Integer(j));
			lnt.addLocalNode(aHost,localNode,vect);
			current=current+pas;
		    }
		}
	    }
	}
	return lnt;
    }
}

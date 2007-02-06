package visidia.network;

import visidia.network.NodeInterfaceTry;
import visidia.network.NodeTry;
import java.rmi.*;
import java.rmi.Naming;
import java.rmi.server.*;
import java.io.*;
import java.util.*;

/** This class represents a Local Node. when distributing the simulation 
 * graph nodes over the network graph, we form local graphs for each one of 
 * network graph node. to cretae this local graphs we use the local node.
 * This is a local node of type "NodeServer".
 * see http://www.labri.fr/~derbel/these for more details
 * to sum, it creates the nodes that were assigned to this LocalNode.
 * @author DERBEL bilel
 * @version 1.0
 */

public class NodeServerImpl extends UnicastRemoteObject implements NodeServer {
   

    /**
	 * 
	 */
	private static final long serialVersionUID = 7652322589399790510L;
	//this name should be the name of the host where the nodes would run 
    public static String serverName;
    public String registryPort ;
    public String urlName="";
    
    public PrintWriter out;
    
    public Hashtable graphStub = new Hashtable();
    

    //Constructors
    public NodeServerImpl(String name, String regPort) 
	throws RemoteException{
	super();
	serverName = name;
	this.registryPort = regPort;
	
    }
    public NodeServerImpl(String name, String regPort, String urlName) throws RemoteException {
	super();
	serverName = name;
	this.registryPort = regPort;
	this.urlName = urlName;
    }

    public NodeServerImpl(String name, String regPort, String urlName, PrintWriter out) throws RemoteException {
	super();
	serverName = name;
	this.registryPort = regPort;
	this.urlName = urlName;
	this.out = out;
    }


    //Remote Method to Initilise the nodes on the the <code>serverName</code> host 
    //public Hashtable initialiser(Hashtable liste, SimpleGraph graph, String nom, String visuUrl) throws RemoteException{
    public Hashtable initialize(Vector vect, String nom, String visuUrl) throws RemoteException{
	if (this.out == null)
	    System.out.println("Noeud Local contacte");
	else
	    try {
		this.out.println("Noeud Local contacte");
		this.out.flush();
	    } catch (Exception e) {
	    }
	
	for(int i=0;i<vect.size();i++) {
	    try {
		String node = ((Integer)vect.elementAt(i)).toString();
		NodeInterfaceTry nodeSimul= new NodeTry(node,nom,visuUrl,this.registryPort);
		this.graphStub.put(node,nodeSimul);
	    } catch (Exception e) {
		if (this.out == null)
		    System.out.println(e);
		else 
		    try {
		    this.out.println(e.toString());
		    this.out.flush();
		    } catch (Exception expt) {
		    }
	    }
	}
	return this.graphStub;
    }

    public void reInitialiser() throws RemoteException {
	try {
	    Enumeration theNodes  = this.graphStub.keys();
	    while( theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeSimul = (NodeInterfaceTry)this.graphStub.get(node);
		nodeSimul.abortServer();
		//nodeSimul = null;
	    }
	}catch (Exception e) {
	    if (this.out == null)
		    System.out.println("Erreur dans la reinitialisation"+e);
		else 
		    try {
			this.out.println("Erreur dans la reinitialisation"+e.toString());
			this.out.flush();
		    } catch (Exception expt) {
		    }
	}

	this.graphStub = new Hashtable();
    }
    
    public void setUrlName(String name) throws RemoteException {
	try {
	    this.urlName = name;
	} catch (Exception e){}
    }

    public String getUrlName() throws RemoteException {
	return this.urlName ;
    }

    public String getHostName() throws RemoteException {
	return serverName;
    }
    public Vector getNodes() throws RemoteException {
	Vector vect = new Vector();
	vect.addElement(this.urlName);
	Enumeration theNodes  = this.graphStub.keys();
	while( theNodes.hasMoreElements()){
	    try {
		String node = (String)theNodes.nextElement();	    
		vect.addElement(node);
	    }catch (Exception e) {
	    }
	}
	return vect;
    }

    public void register(String visuHost, String url) throws RemoteException {
	try {
	    RegistrationThread rt = new RegistrationThread(visuHost,url,this.registryPort,this);
	    rt.start();
	} catch (Exception e){
	    if (this.out == null)
		System.out.println(e);
	    else 
		try {
		    this.out.println(e.toString());  
		    this.out.flush();
		} catch (Exception expt) {
		}
	}
    }
    
    public static final void main(String[] args) {
	PrintWriter out = new PrintWriter(System.out);
	try {
	    //PrintWriter out = new PrintWriter(System.out);
	    //DataOutputStream out = new DataOutputStream(System.out);
	    out.println(0);
	    out.flush();
	    NodeServer ns = new NodeServerImpl(args[0],args[1],args[2],out);
	    Naming.bind("rmi://"+args[0]+":"+args[1]+"/"+args[2],ns);
	} catch (Exception e){
	    out.println(e.toString());
	    out.flush();
	}
    }
}

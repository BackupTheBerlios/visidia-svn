package visidia.network;

import visidia.graph.*;
import java.rmi.*;
import java.rmi.Naming;
import java.rmi.server.*;
import java.util.*;

public interface NodeServer extends Remote {
    public Hashtable initialize(Vector vect, String visualizatorName, String visualizatorUrl) throws RemoteException;
    public void reInitialiser() throws RemoteException;
    public void setUrlName(String name) throws RemoteException;
    public String getUrlName() throws RemoteException;
    public String getHostName() throws RemoteException;
    public Vector getNodes() throws RemoteException; 
    public void register(String visuHost, String url) throws RemoteException;
}

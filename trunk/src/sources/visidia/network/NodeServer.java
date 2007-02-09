package visidia.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

public interface NodeServer extends Remote {
    public Hashtable initialize(Vector vect, String visualizatorName, String visualizatorUrl) throws RemoteException;
    public void reInitialiser() throws RemoteException;
    public void setUrlName(String name) throws RemoteException;
    public String getUrlName() throws RemoteException;
    public String getHostName() throws RemoteException;
    public Vector getNodes() throws RemoteException; 
    public void register(String visuHost, String url) throws RemoteException;
}

package visidia.network;

import java.rmi.*;
import java.rmi.registry.*;


public interface VisidiaRegistry extends Remote {
    public void showLocalNodes(int sizeOfTheGraph) throws RemoteException;
    public void init(String url, Registry reg ) throws RemoteException;
    public void register(NodeServer localNode,String host, String url) throws RemoteException;
}

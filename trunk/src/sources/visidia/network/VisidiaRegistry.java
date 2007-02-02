package visidia.network;

//import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import java.rmi.*;
import java.rmi.registry.*;
//import java.rmi.Naming;
//import java.rmi.server.*;
//import java.util.*;

public interface VisidiaRegistry extends Remote {
    public void showLocalNodes(int sizeOfTheGraph) throws RemoteException;
    public void init(String url, Registry reg ) throws RemoteException;
    public void register(NodeServer localNode,String host, String url) throws RemoteException;
}

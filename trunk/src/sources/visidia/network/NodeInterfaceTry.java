package visidia.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

import visidia.misc.MessageType;
import visidia.simulation.AlgorithmDist;
import visidia.simulation.MessagePacket;
import visidia.tools.PortTable;

public interface NodeInterfaceTry extends Remote {
    
    public Vector getMessageNumber() throws RemoteException;

    public void startServer(AlgorithmDist algo,PortTable pt, Object obj, int size) throws RemoteException;
    
    public void startRunning() throws RemoteException;

    public void receiveFromNode(MessagePacket mesgPacket) throws RemoteException;

    public void free() throws RemoteException;

    public void abortServer() throws RemoteException;

    public boolean containsAliveThreads() throws RemoteException;

    public void wedge() throws RemoteException ;

    public void unWedge() throws RemoteException ;
    
    public void setNodeProperties(Hashtable properties) throws RemoteException;

    public void setNodeDrawingMessage(boolean bool) throws RemoteException ;

    public void setMessageType(MessageType msgType, boolean state) throws RemoteException;
    
}









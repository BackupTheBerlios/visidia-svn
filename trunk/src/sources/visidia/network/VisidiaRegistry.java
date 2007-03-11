package visidia.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public interface VisidiaRegistry extends Remote {
	public void showLocalNodes(int sizeOfTheGraph) throws RemoteException;

	public void init(String url, Registry reg) throws RemoteException;

	public void register(NodeServer localNode, String host, String url)
			throws RemoteException;
}

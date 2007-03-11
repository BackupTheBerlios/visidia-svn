package visidia.network;

import java.rmi.Naming;

//*****************************************************************
// Cette classe instancie un serveur par machine
// Ce server va permettre de creer les différents serveurs de noeuds 
//*****************************************************************

public class Server {

	public static void createServer(String host, String registryPort, String url) {
		try {
			if ((registryPort != null) && (url != null)) {
				NodeServer server = new NodeServerImpl(host, registryPort);
				Naming.bind("rmi://" + host + ":" + registryPort
						+ "/NodeServer/" + url, server);
				server.setUrlName(url);
				// server.register("kangxi","Registry");
			} else if ((registryPort != null) && (url == null)) {
				NodeServer server = new NodeServerImpl(host, registryPort);
				Naming.bind("rmi://" + host + ":" + registryPort
						+ "/NodeServer/" + host, server);
				server.setUrlName(host);
				// server.register("kangxi","Registry");
			} else if ((registryPort == null) && (url != null)) {
				NodeServer server = new NodeServerImpl(host, "1099");
				Naming
						.bind("rmi://" + host + ":1099/NodeServer/" + url,
								server);
				server.setUrlName(url);
				// server.register("kangxi","Registry");
			} else if ((registryPort == null) && (url == null)) {
				NodeServer server = new NodeServerImpl(host, "1099");
				Naming.bind("rmi://" + host + ":1099/NodeServer/" + host,
						server);
				server.setUrlName(host);
				// server.register("kangxi","Registry");
			}

			System.out.println("--> demarrage du serveur sur le host " + host
					+ " effectue");
		} catch (Exception e) {
			System.out
					.println("erreur de mise dans le registre du server " + e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// int indexOfRegistry=0;
		String regPort = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-RMIregistry")) {
				// indexOfRegistry = i;
				regPort = args[i + 1];
			}
		}

		// int indexOfUrl=0;
		String urlServer = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-Name")) {
				// indexOfUrl = i;
				urlServer = args[i + 1];
			}
		}

		Server.createServer(args[0], regPort, urlServer);

	}
}

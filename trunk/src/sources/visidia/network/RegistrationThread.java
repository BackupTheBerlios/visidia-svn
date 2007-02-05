package visidia.network;

import java.rmi.*;
import java.rmi.Naming;


public class RegistrationThread extends Thread {
    
    private String visuHost;
    private String url;
    private String registryPort; 
    private NodeServerImpl master;

    public RegistrationThread(String visuHost, String url, String registryPort, NodeServerImpl master) {
	this.visuHost=visuHost;
	this.url=url;
	this.registryPort=registryPort;
	this.master=master;
    }

    public void run() {
	tryRegistration();
    }

    private void tryRegistration() {
	try {
	    if (url.equals("")){
		System.out.println("1");
		VisidiaRegistry vr = (VisidiaRegistry)Naming.lookup("rmi://"+visuHost+":"+registryPort+"/Registry");
		System.out.println("2");
		vr.register(master,master.getHostName(),master.getUrlName());
		System.out.println("Registration Complete");
	    } else {
		System.out.println("3");
		System.out.println(url);
		VisidiaRegistry vr = (VisidiaRegistry)Naming.lookup("rmi://"+visuHost+":"+registryPort+"/"+url);
		System.out.println("4");
		vr.register(master,master.getHostName(),master.getUrlName());
		System.out.println("Registration Complete");
	    }
	}  catch (java.net.MalformedURLException mue) {
	    System.out.println("!!! Error while registering the Local Node !!!");
	    System.out.println("!!! Verify the Registration URL !!!");
	    mue.printStackTrace();
	} catch (AccessException ae) {
	    System.out.println("!!! Error when contacting the registry !!!");
	    System.out.println("!!! Not Allowed Operation !!!");
	    ae.printStackTrace();
	} catch (RemoteException re) {
	    System.out.println(re);
	    //tryRegistration();
	    re.printStackTrace();
	} catch (NotBoundException nbe) {
	    System.out.println(nbe);
	    //tryRegistration();
	    nbe.printStackTrace();
	} catch (Exception other) {
	    other.printStackTrace();
	}
    }
}

package edu.utsa.tanvir.rmi.serverclass;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

import edu.utsa.tanvir.rmi.database.DatabaseManager;
import edu.utsa.tanvir.rmi.interfaces.ChatServerServant;
import edu.utsa.tanvir.rmi.server.impl.ChatServerServantImpl;
import edu.utsa.tanvir.rmi.server.threadpool.ServerSideThreadPoolManager;


public class RMIChatServer {

//	public static DatabaseManager dbM;
	private static ServerSideThreadPoolManager tpm;

	public RMIChatServer() {
		try {
			LocateRegistry.createRegistry(1099);  
			
			// Create an instance of the server object
			ChatServerServantImpl chatServerSarvant = new ChatServerServantImpl();

			System.out.println("Chat Server starting...");

			// Publish it in the RMI registry.
			Naming.rebind(ChatServerServant.LOOKUP_NAME, chatServerSarvant);

			System.out.println("Chat Server ready.");
			
			tpm = ServerSideThreadPoolManager.getThreadPoolmanager();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	public static ServerSideThreadPoolManager getThreadPoolManager() {
		return tpm;
	}
	
	public static void main(String[] args) {

		// You may want a SecurityManager for downloading of classes:
		System.setSecurityManager(new RMISecurityManager());
		RMIChatServer rmiServer = new RMIChatServer();
	}

}

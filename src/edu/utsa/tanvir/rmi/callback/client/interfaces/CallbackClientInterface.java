package edu.utsa.tanvir.rmi.callback.client.interfaces;

import java.rmi.Remote;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;


/** A statement of what the client & server must agree upon. */
public interface CallbackClientInterface extends Remote {
	
	public void alertUserMessage(Message msg) throws java.rmi.RemoteException;
	
	public void delivaryReport(Message msg) throws java.rmi.RemoteException;
	
	public void alertOfflineMessage(ArrayList<Message> allMsg) throws java.rmi.RemoteException;
	
	public void alertFriendRequest(FriendRequest msg) throws java.rmi.RemoteException;
}

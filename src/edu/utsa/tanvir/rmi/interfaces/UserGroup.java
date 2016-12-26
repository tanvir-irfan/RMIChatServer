package edu.utsa.tanvir.rmi.interfaces;

import java.rmi.Remote;


/** A statement of what the client & server must agree upon. */
public interface UserGroup extends Remote {
	
	public boolean addUser(UserAccount u) throws java.rmi.RemoteException;		//1
	
	public boolean removeUser(UserAccount u) throws java.rmi.RemoteException;					//1
}

package edu.utsa.tanvir.rmi.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.interfaces.UserAccount;
import edu.utsa.tanvir.rmi.interfaces.UserGroup;

public class UserGroupImpl extends UnicastRemoteObject implements UserGroup {
	public int groupId;
	public String groupName;
	
	public ArrayList<UserAccount> listOfUsers;
	
	UserGroupImpl () throws RemoteException {
		super();
	}
	
	UserGroupImpl (int groupId, String groupName, ArrayList<UserAccount> listOfUsers) throws RemoteException {
		this.groupId = groupId;
		this.groupName = groupName;
		this.listOfUsers = listOfUsers;
	}
	
	public boolean addUser(UserAccount u) {
		if(!listOfUsers.contains(u)) {
			return false;
		} else {
			return listOfUsers.add(u);
		}
	}
	
	public boolean removeUser(UserAccount u) {
		if(listOfUsers.contains(u)) {
			return listOfUsers.remove(u);
		} else {
			return false;
		}
	}
}

package edu.utsa.tanvir.rmi.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.database.DatabaseManager;
import edu.utsa.tanvir.rmi.interfaces.UserAccount;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.server.threadpool.ServerSideThreadPoolManager;
import edu.utsa.tanvir.rmi.serverclass.RMIChatServer;

public class UserAccountImpl extends UnicastRemoteObject implements UserAccount {

	private static UserAccountImpl userAccI;
	
	private UserAccountImpl() throws RemoteException {
		
	}
	
	public synchronized static UserAccountImpl getUserAccountImplInstance() throws RemoteException {
		if(userAccI == null) {
			userAccI = new UserAccountImpl();
		}
		
		return userAccI;
	}
	
	@Override
	public User getUser(String userName) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().getUser(userName);
	}
	
	@Override
	public User getUser(User user) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().getUser(user);
	}

	@Override
	public User viewProfile(String userName) throws RemoteException {
		return getUser(userName);
	}
	
	@Override
	public User viewProfile(User user) throws RemoteException {
		return getUser(user);
	}

	@Override
	public User updateProfile(User user) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().updateProfile(user);
	}

	@Override
	public boolean inviteFriend(FriendRequest fr) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().inviteFriend(fr);
	}
	@Override
	public boolean deleteFriend(FriendRequest fr) {
		return DatabaseManager.getDatabaseManagerInstance().deleteFriend(fr);
	}
	
	@Override
	public boolean acceptFriend(FriendRequest fr, boolean accept) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().acceptFriend(fr, accept);
	}

	@Override
	public Group getGroup(String grpName) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().getGroup(grpName);
	}

	@Override
	public boolean joinGroup(String userName, String grpName) throws RemoteException {
		return DatabaseManager.getDatabaseManagerInstance().joinGroup(userName, grpName);
		
	}
	
	@Override
	public boolean joinGroup(Group grp) throws RemoteException {
		return false;
	}

	@Override
	public boolean leaveGroup(String grpName) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean leaveGroup(Group grp) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMessage(Message msg) throws RemoteException {
		RMIChatServer.getThreadPoolManager().submitTask(msg);
		return true;
	}

	@Override
	public ArrayList<Message> readAllOfflineMessage(String userName) {
		return DatabaseManager.readAllOfflineMessage(userName);
	}

	@Override
	public ArrayList<Message> getAllMessageFrom(String me, String frnd, boolean isFrnd)
			throws RemoteException {
		return DatabaseManager.getAllMessageFrom(me,frnd, isFrnd);
	}

}

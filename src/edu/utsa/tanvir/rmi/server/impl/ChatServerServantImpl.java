package edu.utsa.tanvir.rmi.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.utsa.tanvir.rmi.callback.client.interfaces.CallbackClientInterface;
import edu.utsa.tanvir.rmi.database.DatabaseManager;
import edu.utsa.tanvir.rmi.interfaces.ChatServerServant;
import edu.utsa.tanvir.rmi.interfaces.UserAccount;
import edu.utsa.tanvir.rmi.interfaces.UserGroup;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.serverclass.RMIChatServer;

public class ChatServerServantImpl extends UnicastRemoteObject  implements ChatServerServant {
	
	
	public ChatServerServantImpl () throws RemoteException  {
		getListOfClients();
	}
	
	public static Map<String, CallbackClientInterface> getListOfClients() {
		return DatabaseManager.getListOfClients();
	}
	
	@Override
	public boolean createUserAccount(String userName, String password)
			throws RemoteException {
		boolean user = DatabaseManager.getDatabaseManagerInstance().createUserAccount(userName,password);
		return user;
	}

	@Override
	public boolean createUserAccount(User user) throws RemoteException {
		boolean success = DatabaseManager.getDatabaseManagerInstance().createUserAccount(user);
		return success;
	}
	
	@Override
	public User loginAccount(String userName, String password)
			throws RemoteException {
		User user = DatabaseManager.getDatabaseManagerInstance().loginAccount(userName,password);
		return user;
	}

	@Override
	public boolean createGroup(String userName, String grpName) throws RemoteException {
		boolean grp = DatabaseManager.getDatabaseManagerInstance().createGroup(userName,grpName);
		return grp;
	}

	@Override
	public Group findGroup(String grpName) throws RemoteException {
		return null;
	}

	@Override
	public User findUser(String userName) throws RemoteException {
		User user = DatabaseManager.getDatabaseManagerInstance().getUser(userName);
		return user;
	}

	@Override
	public UserAccount getUserAccount() throws RemoteException {
		return UserAccountImpl.getUserAccountImplInstance();
	}

	@Override
	public UserGroup getUserGroup() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean connect(String usrName, CallbackClientInterface cb)
			throws RemoteException {
		return DatabaseManager.connect(usrName, cb);
	}

	@Override
	public boolean disConnect(String usrName, CallbackClientInterface cb)
			throws RemoteException {
		return DatabaseManager.disConnect(usrName, cb);
	}

	@Override
	public ArrayList<User> getListOfUsersWhoIsNotFriend(String userName, String matchStr)
			throws RemoteException {
		ArrayList<User> allUser = DatabaseManager.getDatabaseManagerInstance().getListOfUsersWhoIsNotFriend(userName, matchStr);
		return allUser;
	}

	@Override
	public ArrayList<Group> getAllGroupUserNotMemberOf(String grpName, String userName)
			throws RemoteException {
		ArrayList<Group> allGrp = DatabaseManager.getDatabaseManagerInstance().getAllGroupUserNotMemberOf(grpName, userName);
		return allGrp;
	}

}

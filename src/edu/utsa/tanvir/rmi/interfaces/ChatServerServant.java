package edu.utsa.tanvir.rmi.interfaces;

import java.rmi.Remote;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.callback.client.interfaces.CallbackClientInterface;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.User;

public interface ChatServerServant extends Remote{
	
	public static final String LOOKUP_NAME = "ChatServerServant";
	
	public boolean createUserAccount(String userName, String password) throws java.rmi.RemoteException;	//1
	
	public boolean createUserAccount(User user) throws java.rmi.RemoteException;	//1
	
	public User loginAccount(String userName, String password) throws java.rmi.RemoteException;			//1
	
	public boolean createGroup(String userName, String grpName) throws java.rmi.RemoteException;							//1
	
	public Group findGroup(String grpName) throws java.rmi.RemoteException;								//1
	
	public User findUser(String userName) throws java.rmi.RemoteException;								//1
	
	public ArrayList<User> getListOfUsersWhoIsNotFriend(String userName, String matchStr) throws java.rmi.RemoteException;			//1
	
	public ArrayList<Group> getAllGroupUserNotMemberOf(String grpName, String userName) throws java.rmi.RemoteException;			//1
	
	public UserAccount getUserAccount() throws java.rmi.RemoteException;								//1
	
	public UserGroup getUserGroup() throws java.rmi.RemoteException;									//1
	
	public boolean connect(String usrName, CallbackClientInterface cb) throws java.rmi.RemoteException;	//1
	
	public boolean disConnect(String usrName, CallbackClientInterface cb) throws java.rmi.RemoteException;
	
}

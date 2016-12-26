package edu.utsa.tanvir.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;


/** A statement of what the client & server must agree upon. */
public interface UserAccount extends Remote {

	public User getUser(String userName) throws java.rmi.RemoteException;		//1
	
	public User getUser(User user) throws java.rmi.RemoteException;	//1
	
	public User viewProfile(String userName) throws java.rmi.RemoteException;	//1
	
	public User viewProfile(User user) throws java.rmi.RemoteException;	//1
	
	public User updateProfile(User user) throws java.rmi.RemoteException;	//1
	
	public boolean inviteFriend(FriendRequest fr) throws java.rmi.RemoteException;		//2
	
	public boolean deleteFriend(FriendRequest fr) throws java.rmi.RemoteException;		//2
	
	public boolean acceptFriend(FriendRequest fr, boolean accept) throws RemoteException;
	
	public Group getGroup(String grpName) throws java.rmi.RemoteException;		//3
	
	public boolean joinGroup(String userName, String grpName) throws java.rmi.RemoteException;		//3
	
	public boolean joinGroup(Group grp) throws java.rmi.RemoteException;		//3
	
	public boolean leaveGroup(String grpName) throws java.rmi.RemoteException;		//3
	
	public boolean leaveGroup(Group grp) throws java.rmi.RemoteException;		//3
	
	public boolean sendMessage(Message msg) throws java.rmi.RemoteException;	//5
	
	public ArrayList<Message> readAllOfflineMessage(String userName) throws java.rmi.RemoteException;	//5

	public ArrayList<Message> getAllMessageFrom(String me, String frnd, boolean isFrnd) throws java.rmi.RemoteException;;
	
}

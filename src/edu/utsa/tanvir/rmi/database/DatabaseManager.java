package edu.utsa.tanvir.rmi.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.utsa.tanvir.rmi.callback.client.interfaces.CallbackClientInterface;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.serverclass.RMIChatServer;
import edu.utsa.tanvir.rmi.utility.Constant;

public class DatabaseManager implements Serializable {

	private static DatabaseManager dbM;
	private static DataBase db;

	private static ArrayList<User> userCacheForServer;
	private static Map<String, ArrayList<FriendRequest>> mapOfAllFriendReq;

	private static Map<String, CallbackClientInterface> listOfAllOnlineClients;

	private DatabaseManager() {
		mapOfAllFriendReq = new HashMap<String, ArrayList<FriendRequest>>();

		// /////////////////////////////
		db = new DataBase();
		userCacheForServer = new ArrayList<User>();
	}

	public synchronized static DatabaseManager getDatabaseManagerInstance() {
		if (dbM == null) {
			dbM = new DatabaseManager();
		}
		return dbM;
	}

	public synchronized User loginAccount(String userName, String password) {
		User u = db.loginAccount(userName, password);
		// setting up other things that were not there in 'users' table
		// TODO later I need to set it up from database
		if (u != null) {
			updateFrndGrpAndFrndReq(u);
			updateUserInCache(u);
		}
		return u;
	}

	private void updateFrndGrpAndFrndReq(User u) {
		u.listOfGroups = getListOfGroup(u.userName);
		u.listOfFriends = getListOfFriends(u.userName);
		u.outFrndReq = getListOfFriendReq(u.userName, false);
		u.inFrndReq = getListOfFriendReq(u.userName, true);
		
		u.hasOfflineMsg = db.readAllOfflineMessageSise(u.userName) > 0;
	}

	private ArrayList<FriendRequest> getListOfFriendReq(String userName,
			boolean inReq) {
		ArrayList<FriendRequest> listOfFrnd = db.getListOfFriendReq(userName,
				inReq);
		return listOfFrnd;
	}

	private ArrayList<User> getListOfFriends(String userName) {
		ArrayList<User> listOfFrnd = db.getListOfFriends(userName);
		return listOfFrnd;
	}

	private ArrayList<Group> getListOfGroup(String userName) {
		ArrayList<Group> listOfGrp = db.getListOfGroup(userName);
		return listOfGrp;
	}

	private static synchronized void addUserToCache(User u) {
		if (!userCacheForServer.contains(u))
			userCacheForServer.add(u);
	}

	private static synchronized void removeUserFromCache(User u) {
		if (userCacheForServer.contains(u))
			userCacheForServer.remove(u);
	}

	private static synchronized void updateUserInCache(User u) {
		removeUserFromCache(u);
		addUserToCache(u);
	}

	public synchronized User getUser(String userName) {
		User user = new User();
		user.setUserName(userName);
		return getUser(user);
	}

	public synchronized User getUser(User user) {
		int userIndex = userCacheForServer.indexOf(user);
		if (userIndex == -1) {
			return null; // the user is not present in cache.
		} else {
			return userCacheForServer.get(userIndex);
		}
	}

	public static synchronized Map<String, CallbackClientInterface> getListOfClients() {
		if (listOfAllOnlineClients == null) {
			listOfAllOnlineClients = new HashMap<String, CallbackClientInterface>();
		}
		return listOfAllOnlineClients;
	}

	public static synchronized CallbackClientInterface getOnlineClient(
			String userName) {
		return listOfAllOnlineClients.get(userName);
	}

	public static boolean connect(String usrName, CallbackClientInterface cb) {
		if (listOfAllOnlineClients.get(usrName) == null) {
			listOfAllOnlineClients.put(usrName, cb);
			return true;
		} else {
			return false;
		}
	}

	public static boolean disConnect(String usrName, CallbackClientInterface cb) {
		if (listOfAllOnlineClients.get(usrName) != null) {
			listOfAllOnlineClients.remove(usrName);

			// update user cache
			User u = new User(usrName);
			removeUserFromCache(u);
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean createUserAccount(String userName,
			String password) {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		return createUserAccount(user);
	}

	public boolean createUserAccount(User u) {
		boolean success = db.createUserAccount(u);
		return success;
	}

	public synchronized User updateProfile(User user) {
		removeUserFromCache(user); // remove the old user and add the new
		user = db.updateProfile(user);
		addUserToCache(user);
		return user;
	}

	public synchronized boolean createGroup(String userName, String grpName) {
		Group grp = new Group();
		grp.groupName = userName.toUpperCase() + Constant.GROUP_NAME_SEPERATOR
				+ grpName.toUpperCase();
		grp.ownerName = userName;

		boolean success = db.createGroupOrInsertUser(grp);

		// update the user cache
		User user = userCacheForServer.get(userCacheForServer.indexOf(new User(
				userName)));
		if (user != null) {

			removeUserFromCache(user);
			user.listOfGroups.add(grp);
			addUserToCache(user);
		}

		return success;
	}

	public synchronized Group getGroup(String grpName) {
		Group grp = db.getGroup(grpName);
		return grp;
	}

	public synchronized ArrayList<Group> getAllGroupUserNotMemberOf(
			String grpName, String userName) {
		ArrayList<Group> allGrp = db.getAllGroupUserNotMemberOf(grpName,
				userName);
		return allGrp;
	}

	public synchronized boolean joinGroup(String userName, String grpName) {

		boolean success = db.joinGroup(userName, grpName);

		int index = userCacheForServer.indexOf(new User(userName));
		// u = listOfAllRegisteredUsers.get(index);
		User u = userCacheForServer.get(index);
		userCacheForServer.remove(index);

		u.listOfGroups.add(new Group(grpName));

		userCacheForServer.add(u);
		return success;
	}

	public synchronized ArrayList<User> getListOfUsersWhoIsNotFriend(
			String userName, String matchStr) {
		return db.getListOfUsersWhoIsNotFriend(userName, matchStr);
	}

	public static User getCachedUser(String userName) {
		if (userCacheForServer.contains(new User(userName))) {
			return userCacheForServer.get(userCacheForServer.indexOf(new User(
					userName)));
		} else {
			return null;
		}
	}

	// TODO NOT DONE
	public static synchronized ArrayList<Message> readAllOfflineMessage(
			String usrName) {
		ArrayList<Message> allMsg = db.readAllOfflineMessage(usrName);
		return allMsg;
	}
	
	// TODO Need to handle the BLOCKED Functionality later.
	public synchronized boolean acceptFriend(FriendRequest fr, boolean accept) {
		boolean success = db.acceptFriend(fr, accept);
		if (success) {
			// database is updated but cache is not updated yet. :-(
			makeThemMutualFriend(fr, accept);
		}
		return success;
	}

	private void makeThemMutualFriend(FriendRequest fr, boolean accept) {
		User u = new User();
		User fromU = new User(fr.fromUser);
		u = getUser(fromU);
		if (u != null) {
			updateFrndGrpAndFrndReq(u);
			updateUserInCache(u);
		}

		User toU = new User(fr.toUser);
		u = getUser(toU);
		if (u != null) {
			updateFrndGrpAndFrndReq(u);
			updateUserInCache(u);
		}

	}

	public synchronized boolean inviteFriend(FriendRequest fr) {
		//ThreadPool worker will do the job!
		fr.status = Constant.FR_SENT_TO_SERVER;
		RMIChatServer.getThreadPoolManager().submitTask(fr);
		
		//updating in the cache
		int index = userCacheForServer.indexOf(new User(fr.fromUser));
		if(index != -1) {
			User u = userCacheForServer.get(index);
			if(!u.outFrndReq.contains(fr)) {
				userCacheForServer.remove(index);
				u.outFrndReq.add(fr);
				userCacheForServer.add(u);
			}
		}
		
		index = userCacheForServer.indexOf(new User(fr.toUser));
		if(index != -1) {
			User u = userCacheForServer.get(index);
			if(!u.inFrndReq.contains(fr)) {
				userCacheForServer.remove(index);
				u.inFrndReq.add(fr);
				userCacheForServer.add(u);
			}
		}
		return true;
	}

	public boolean deleteFriend(FriendRequest fr) {
		//ThreadPool worker will do the job!
		RMIChatServer.getThreadPoolManager().submitTask(fr);

		// updating in the cache
		int index = userCacheForServer.indexOf(new User(fr.fromUser));
		if (index != -1) {
			User u = userCacheForServer.get(index);
			if (u.listOfFriends.contains(new User(fr.toUser))) {
				userCacheForServer.remove(index);
				u.listOfFriends.remove(new User(fr.toUser));
				userCacheForServer.add(u);
			}
		}
		return true;
	}
	
	public boolean saveFriendRequest(FriendRequest fr) {
		return db.saveFriendRequest(fr);
	}
	
	public boolean deleteFriendAndFrndRequest(FriendRequest fr) {
		return db.deleteFriendAndFrndRequest(fr);
	}

	public static synchronized ArrayList<Message> getAllMessageFrom(String me,
			String frnd, boolean isFrnd) {
		ArrayList<Message> allMsg = db.getAllMessageFrom(me, frnd, isFrnd);
		return allMsg;
	}

	public static synchronized boolean insertMessage(Message msg) {
		if(msg.status == Constant.MESSAGE_DELIVERED) {
			//message delivared means that user is online updating in the cache
			int index = userCacheForServer.indexOf(new User(msg.toUser));
			if(index != -1) {
				User u = userCacheForServer.get(index);
				u.hasOfflineMsg = true;
				
				userCacheForServer.remove(u);
				userCacheForServer.add(u);
			}
		}
		return db.insertMessage(msg);
	}
	
	public static synchronized boolean saveOfflineMessage(Message msg) {
		boolean success = db.saveOfflineMessage(msg);
		return success;
	}

}

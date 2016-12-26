package edu.utsa.tanvir.rmi.clientclass;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import edu.utsa.tanvir.rmi.client.impl.CallbackClientInterfaceImpl;
import edu.utsa.tanvir.rmi.client.threadpool.ClientSideThreadPoolManager;
import edu.utsa.tanvir.rmi.gui.ChatWindow;
import edu.utsa.tanvir.rmi.gui.HomePage;
import edu.utsa.tanvir.rmi.interfaces.ChatServerServant;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;

public class RMIChatClient {

	/** The local proxy for the service. */
	static ChatServerServant chatServerServant = null;
	static CallbackClientInterfaceImpl cBCIImpl = null;
	private static RMIChatClient rmiChatClient;
	private static HashMap<String, ChatWindow> allOpenChatWindow;
	private ClientSideThreadPoolManager tpm;
	public static HomePage thisHomePage;
	
	public static ChatServerServant getChatServerServant() {
		return chatServerServant;
	}

	public static void setHomePage(HomePage homePage) {
		RMIChatClient.thisHomePage = homePage;
	}

	public static void setChatServerServant(ChatServerServant chatServerServant) {
		RMIChatClient.chatServerServant = chatServerServant;
	}
	
	public ClientSideThreadPoolManager getTpm() {
		return tpm;
	}

	public void setTpm(ClientSideThreadPoolManager tpm) {
		this.tpm = tpm;
	}

	private static User me;
	protected static String msg;
	protected static String toUser;

	protected static boolean inLoggedIn = false;
	private static Scanner sc;

	public static User getMe() {
		return me;
	}

	public static void setMe(User u) {
		me = u;
	}

	public static RMIChatClient getRMIChatClientInstance() {
		if (rmiChatClient == null) {
			try {
				rmiChatClient = new RMIChatClient();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rmiChatClient;
	}

	private RMIChatClient() throws RemoteException {
		me = new User();
		try {
			System.setSecurityManager(new RMISecurityManager());
			initializeChatServerServant();
			cBCIImpl = new CallbackClientInterfaceImpl();

			tpm = ClientSideThreadPoolManager.getThreadPoolmanager();

			allOpenChatWindow = new HashMap<String, ChatWindow>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ChatServerServant initializeChatServerServant() {
		if (chatServerServant == null) {
			try {
//				String host = "//129.115.3.25:1099/";
				 String host = "//localhost:1099/";

				String fullLookUpName = host + ChatServerServant.LOOKUP_NAME;
				chatServerServant = (ChatServerServant) Naming
						.lookup(fullLookUpName);

				// chatServerServant = (ChatServerServant) Naming
				// .lookup(ChatServerServant.LOOKUP_NAME);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return chatServerServant;
	}

	public void viewProfile() {
		System.out.print(Constant.USER_NAME);
		System.out.println(me.userName);
		System.out.print(Constant.PASSWORD);
		System.out.println(me.password);

		System.out.print(Constant.FULL_NAME);
		System.out.println(me.fullName);
		System.out.print(Constant.PROFESSION);
		System.out.println(me.profession);

		System.out.print(Constant.LIVING_CITY);
		System.out.println(me.livingCity);
		System.out.print(Constant.COMPANY);
		System.out.println(me.company);

		System.out.print(Constant.COLLEGE_NAME);
		System.out.println(me.collageName);
		System.out.print(Constant.GRADUATION_YEAR);
		System.out.println(me.graduationYear);
	}

	public User login(String userName, String password) {
		try {
			me = initializeChatServerServant().loginAccount(userName, password);
			if (me != null) {
				initializeChatServerServant().connect(me.userName, cBCIImpl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return me;
	}

	public static boolean logOut() {
		boolean result = false;
		try {
			result = initializeChatServerServant().disConnect(me.userName,
					cBCIImpl);
			// closing all the open chatWindows
			closeAllOpenChatWindow();
			me = null;
		} catch (RemoteException e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public boolean createUser(User user, boolean isNew) {
		boolean success = false;
		try {
			if (isNew) { // CREATE ACCOUNT
				success = initializeChatServerServant().createUserAccount(user);
			} else { // UPDATE PROFILE
				user.setUserName(me.getUserName()); // USER NAME SHOULD NOT
													// CHANGE!
				user = initializeChatServerServant().getUserAccount()
						.updateProfile(user);
				if (user != null)
					success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public static void main(String[] args) {

		try {
			RMIChatClient rcc = new RMIChatClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static User getUser(String userName) {
		User u = null;
		try {
			u = initializeChatServerServant().findUser(userName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return u;
	}

	public static User getUser() {
		return getUser(me.userName);
	}

	public static ArrayList<User> getListOfUsersWhoIsNotFriend(String userName,
			String matchStr) {
		ArrayList<User> allUser = null;
		try {
			allUser = initializeChatServerServant()
					.getListOfUsersWhoIsNotFriend(userName, matchStr);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return allUser;
	}

	public static boolean createGroup(String userName, String grpName)
			throws RemoteException {
		boolean grp = initializeChatServerServant().createGroup(userName,
				grpName);
		return grp;
	}

	public static ArrayList<Group> getAllGroupUserNotMemberOf(String grpName,
			String userName) {
		ArrayList<Group> allGrp = null;
		try {
			allGrp = initializeChatServerServant().getAllGroupUserNotMemberOf(
					grpName, userName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return allGrp;
	}

	public static boolean joinGroup(String userName, String grpName) {
		boolean g = false;
		try {
			g = initializeChatServerServant().getUserAccount().joinGroup(
					userName, grpName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return g;
	}

	public static boolean inviteFriend(FriendRequest fr) {
		boolean result = false;
		try {
			result = initializeChatServerServant().getUserAccount()
					.inviteFriend(fr);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean deleteFriend(String myName, String friendOrGroupName, boolean isFriend, boolean isblk) {
		boolean result = false;
		try {
			
			FriendRequest fr = new FriendRequest();
			fr.fromUser = myName;
			fr.toUser = friendOrGroupName;
			fr.status = Constant.FR_DELETE;
			fr.msg = "";
			result = initializeChatServerServant().getUserAccount()
					.deleteFriend(fr);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void acceptFriend(FriendRequest fr, boolean accept) {
		try {
			initializeChatServerServant().getUserAccount().acceptFriend(fr,
					accept);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static Group getGroup(String frndOrGrpName) {
		Group g = new Group();
		try {
			g = initializeChatServerServant().getUserAccount().getGroup(
					frndOrGrpName);
		} catch (RemoteException e) {
			e.printStackTrace();
			g = null;
		}
		;
		return g;
	}

	public static boolean addNewChatWindow(String s, ChatWindow c) {
		allOpenChatWindow.put(s, c);
		return false;
	}

	public static ChatWindow getChatWindow(String fromUser) {
		return allOpenChatWindow.get(fromUser);
	}

	public static void closeAllOpenChatWindow() {

		Iterator<Entry<String, ChatWindow>> it = allOpenChatWindow.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ChatWindow> pairs = (Map.Entry<String, ChatWindow>) it
					.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			// it.remove(); // avoids a ConcurrentModificationException
			if (pairs.getValue() instanceof ChatWindow) {
				((ChatWindow) pairs.getValue()).getFrame().dispose();
			}
		}
		allOpenChatWindow.clear();
	}

	public static ArrayList<Message> getAllOfflineMessage(String userName) {
		ArrayList<Message> allOffMsg = null;
		try {
			allOffMsg = initializeChatServerServant().getUserAccount()
					.readAllOfflineMessage(userName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return allOffMsg;
	}

	public static ArrayList<Message> getAllMessageFrom(String me, String frnd,
			boolean isFrnd) {
		ArrayList<Message> allOffMsg = null;
		try {
			allOffMsg = initializeChatServerServant().getUserAccount()
					.getAllMessageFrom(me, frnd, isFrnd);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return allOffMsg;
	}

}

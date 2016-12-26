package edu.utsa.tanvir.rmi.database;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;

public class DataBase {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	// #################################################################
	private static DataBase dbM;
	private UserTable userT; // To communicate with the user table DB
	private GroupTable gTable; // To communicate with the group table DB
	private MessageTable mT; // To communicate with the message table DB
	private FriendsTable fTable; // To communicate with the message table DB

	public DataBase() {
		userT = UserTable.getUserTableInstance();
		gTable = GroupTable.getGroupTableInstance();
		mT = MessageTable.getMessageTableInstance();
		// fTable = new FriendsTable();
	}

	public synchronized static DataBase getDataBaseInstance() {
		if (dbM == null) {
			dbM = new DataBase();
		}
		return dbM;
	}

	public synchronized boolean createUserAccount(User u) {
		if (userT.existsUser(u.userName)) {
			return false;
		} else {
			return userT.insertUser(u);
		}
	}

	public synchronized User loginAccount(String userName, String password) {
		System.out.println("DataBase : loginAccount");
		if (userT.existsUser(userName)) {
			// the user is no present.
			User user = userT.getUserByUserName(userName);
			if (user.password.equalsIgnoreCase(password)) {
				// pass match
				return user;
			} else {
				// pass not match
				return null;
			}

		} else {
			// user not present
			return null;
		}
	}

	public synchronized User getUser(String userName) {
		System.out.println("DataBase : getUser");
		return userT.getUserByUserName(userName);
	}

	public synchronized User getUser(User user) {
		return userT.getUserByUserName(user.userName);
	}

	public synchronized User updateProfile(User user) {
		if (userT.existsUser(user)) {
			// remove user
			userT.deleteUserByUserName(user.userName);
			userT.insertUser(user);
			User u = userT.getUserByUserName(user.userName);
			return u;
		} else {
			return null;
		}
	}
	
	public synchronized boolean createGroupOrInsertUser(Group g) {
		return gTable.createGroupOrInsertUser(g, true);
	}
	
	public synchronized Group getGroup(String grpName) {
		return gTable.getGroup(grpName);
	}
	
	public synchronized ArrayList<Group>  getAllGroupUserNotMemberOf(String grpName, String userName) {
		return gTable.getAllGroupUserNotMemberOf(grpName, userName);
	}
	
	public ArrayList<Group> getListOfGroup(String userName) {
		ArrayList<Group> listOfGrp = gTable.getListOfGroup(userName);
		return listOfGrp;
	}
	
	public ArrayList<User> getListOfFriends(String userName) {
		ArrayList<User> listOfGrp = userT.getListOfFriends(userName);
		return listOfGrp;
	}
	
	public ArrayList<FriendRequest> getListOfFriendReq(String userName, boolean inReq) {
		ArrayList<FriendRequest> listOfGrp = userT.getListOfFriendReq(userName, inReq);
		return listOfGrp;
	}
	
	public ArrayList<User> getListOfUsersWhoIsNotFriend(String userName, String matchStr) {
		ArrayList<User> listOfGrp = userT.getListOfUsersWhoIsNotFriend(userName, matchStr);
		return listOfGrp;
	}
	
	public boolean joinGroup(String userName, String grpName) {
		boolean success = gTable.joinGroup(userName, grpName);
		return success;
	}
	
	public boolean acceptFriend(FriendRequest fr, boolean accept) {
		return userT.acceptFriend(fr, accept);
	}
	
	public synchronized ArrayList<Message> getAllMessageFrom(String me,
			String frnd, boolean isFrnd) {
		ArrayList<Message> allMsg = mT.getAllMessageFrom(me,frnd,isFrnd);
		return allMsg;
	}
	
	public boolean insertMessage(Message msg) {
		return mT.insertMessage(msg);
	}
	
	public boolean saveOfflineMessage(Message msg) {
		msg.status = Constant.MESSAGE_OFFLINE;
		return mT.updateMessageStatus(msg);
	}
	// you need to close all three to make sure
	private void close() {
		close((Closeable) resultSet);
		close((Closeable) statement);
		close((Closeable) connect);
	}

	private void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			// don't throw now as it might leave following closables in
			// undefined state
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////

	public void readDataBase() throws Exception {
		try {

			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ "user=root&password=");

			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("select * from FEEDBACK.COMMENTS");
			writeResultSet(resultSet);

			// preparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
			// parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			// remove again the insert comment
			preparedStatement = connect
					.prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
			preparedStatement.setString(1, "Test");
			preparedStatement.executeUpdate();

			resultSet = statement
					.executeQuery("select * from FEEDBACK.COMMENTS");
			writeMetaData(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// now get some metadata from the database
		System.out.println("The columns in the table are: ");
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summary = resultSet.getString("summary");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summary: " + summary);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}


	public synchronized ArrayList<Message> getMessageFor(User user) {
		System.out.println("DataBase : getMessagesFor");
		ArrayList<Message> allMsg = mT.GetAllMessagesToUser(user.userName);
		mT.DeleteMessages(allMsg); // delete all read message from serve
		return allMsg;
	}

	public synchronized boolean sendMessage(Message msg) {
		return true;
	}

	public synchronized boolean MakeItFriends(User theUser, User theFriend) {
		if (userT.existsUser(theUser) && userT.existsUser(theFriend)) {
			return (fTable.InsertFriendRealation(theUser, theFriend) && fTable
					.InsertFriendRealation(theFriend, theUser));
		} else {
			return false;
		}
	}

	public synchronized boolean UserBlockFriend(User theUser, User theFriend) {
		if (userT.existsUser(theUser) && userT.existsUser(theFriend)) {
			return fTable.BlockFriend(theUser, theFriend);
		} else {
			return false;
		}
	}

	public synchronized boolean UserUnblockFriend(User theUser, User theFriend) {
		if (userT.existsUser(theUser) && userT.existsUser(theFriend)) {
			return fTable.UnblockFriend(theUser, theFriend);
		} else {
			return false;
		}
	}

	public synchronized boolean DeleteGroup(String groupName,
			String moderatorUserName) {
		return gTable.DeleteGroup(groupName, moderatorUserName);
	}

	public synchronized boolean DeleteGroup(int groupId) {
		return gTable.DeleteGroup(groupId);
	}

	public ArrayList<Message> readAllOfflineMessage(String usrName) {
		return mT.readAllOfflineMessage(usrName);
	}
	
	public int readAllOfflineMessageSise(String usrName) {
		ArrayList<Message> allOffMsg = readAllOfflineMessage(usrName);
		if(allOffMsg != null) {
			return allOffMsg.size();
		}
		return 0;
	}

	public boolean saveFriendRequest(FriendRequest fr) {
		return userT.saveFriendRequest(fr);
	}
	
	public boolean deleteFriendAndFrndRequest(FriendRequest fr) {
		return userT.deleteFriendAndFrndRequest(fr);
	}

}
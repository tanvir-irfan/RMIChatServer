package edu.utsa.tanvir.rmi.database;

/* For the databases */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class UserTable {

	// #############################################################
	private PreparedStatement preparedStatement = null;
	private static String table = "users";
	private static Connection conn = null;
	private static Statement stmt = null;

	private UserTable() {
		conn = Utility.loadDatabase();
	}

	private static UserTable uT;

	public static UserTable getUserTableInstance() {
		if (uT == null) {
			uT = new UserTable();
		}
		return uT;
	}

	public boolean insertUser(User theUser) {
		/* Get the maximum id */
		try {
			// preparedStatements can use variables and are more efficient
			preparedStatement = conn.prepareStatement("insert into " + table
					+ " values (default, ?, ?, ?, ? , ?, ?,?, ? , ?)");
			// "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
			// parameters start with 1
			preparedStatement.setString(1, theUser.fullName);
			preparedStatement.setString(2, theUser.userName);
			preparedStatement.setString(3, theUser.password);
			preparedStatement.setString(4, theUser.profession);
			preparedStatement.setString(5, theUser.livingCity);
			preparedStatement.setString(6, theUser.company);
			preparedStatement.setString(7, theUser.collageName);
			preparedStatement.setInt(8, theUser.graduationYear);
			preparedStatement.setBoolean(9, theUser.hasOfflineMsg);
			preparedStatement.executeUpdate();

			preparedStatement.close();
			return true;

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public User getUserByUserName(String userName) {
		User localUser = null;
		try {
			String command = null;
			command = "SELECT * FROM " + table + " WHERE userName='" + userName
					+ "'";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				/*
				 * public User(int iD, String fullName, String userName, String
				 * password, String profession, String livingCity, String
				 * company, String collageName, int graduationYear, boolean
				 * hasOfflineMsg)
				 */

				localUser = new User(results.getInt(results.findColumn("ID")),
						results.getString(results.findColumn("fullName")),
						results.getString(results.findColumn("userName")),
						results.getString(results.findColumn("password")),
						results.getString(results.findColumn("profession")),
						results.getString(results.findColumn("livingCity")),
						results.getString(results.findColumn("company")),
						results.getString(results.findColumn("collageName")),
						results.getInt(results.findColumn("graduationYear")),
						results.getBoolean(results.findColumn("hasOfflineMsg")));
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			if (sqlExcept.getSQLState().equals("XSRS9")) {
				return null;
			}
			sqlExcept.printStackTrace();
		}
		return localUser;
	}

	public ArrayList<User> getListOfFriends(String userName) {
		ArrayList<User> allFrnds = new ArrayList<User>();
		try {
			String command = null;
			command = "SELECT DISTINCT friend FROM " + "friends"
					+ " WHERE me ='" + userName + "' and status = " + Constant.FR_ACCEPTED;

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			while (results.next()) {
				String usrN = results.getString(results.findColumn("friend"));
				allFrnds.add(new User(usrN));
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			if (sqlExcept.getSQLState().equals("XSRS9")) {
				return null;
			}
			sqlExcept.printStackTrace();
		}
		return allFrnds;
	}

	public ArrayList<FriendRequest> getListOfFriendReq(String userName,
			boolean inReq) {
		ArrayList<FriendRequest> allFrnds = new ArrayList<FriendRequest>();
		try {
			String command = null;
			String fromOrToUser = inReq ? "toUser" : "fromUser";

			// command = "SELECT DISTINCT friend FROM " + "friend_request"
			// + " WHERE "+fromOrToUser+" ='" + userName + "'" +
			// " and status ='" + Constant.FR_NEW +"'";
			command = "SELECT * FROM " + "friend_request" + " WHERE "
					+ fromOrToUser + " ='" + userName + "'" + " and status ='"
					+ Constant.FR_SENT_TO_SERVER + "'";

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			while (results.next()) {
				String fromUser = results.getString(results
						.findColumn("fromUser"));
				String toUser = results.getString(results.findColumn("toUser"));
				String msg = results.getString(results.findColumn("msg"));
				int status = results.getInt(results.findColumn("status"));
				// FriendRequest(String fromUser, String toUser, String msg, int
				// status) {

				FriendRequest fr = new FriendRequest(fromUser, toUser, msg,
						status);
				// there will only one request from one user.
				allFrnds.add(fr);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return allFrnds;
	}

	public ArrayList<User> getListOfUsersWhoIsNotFriend(String me,
			String matchStr) {
		ArrayList<User> allFrnds = new ArrayList<User>();
		allFrnds = getListOfFriends(me);

		ArrayList<User> allUserNotFrnd = new ArrayList<User>();
		try {
			String command = null;
			command = "SELECT DISTINCT userName FROM " + table
					+ " WHERE userName LIKE'" + matchStr + "%'";

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			while (results.next()) {
				String usrN = results.getString(results.findColumn("userName"));
				User u = new User(usrN);
				if (!allFrnds.contains(u)) {
					allUserNotFrnd.add(u);
				}
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			if (sqlExcept.getSQLState().equals("XSRS9")) {
				return null;
			}
			sqlExcept.printStackTrace();
		}
		return allUserNotFrnd;
	}

	public User getUserByUserName2(String userName) {
		User localUser = null;
		return localUser;
	}

	public ArrayList<User> getUsersByUserName(ArrayList<String> listOfUsers) {
		ArrayList<User> localUserList = new ArrayList<User>();
		for (String userName : listOfUsers) {
			localUserList.add(getUserByUserName(userName));
		}
		return localUserList;
	}

	public ArrayList<User> getAllUsers() {
		return null;
	}

	public boolean deleteUserByUserName(String userName) {
		try {
			String command = null;

			command = "DELETE FROM " + table + " WHERE userName='" + userName
					+ "'";
			stmt = conn.createStatement();
			stmt.execute(command);
			stmt.close();
			return true;

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public boolean existsUser(User theUser) {
		return existsUser(theUser.userName);
	}

	public boolean existsUser(String userName) {

		try {
			String command = null;

			command = "SELECT userName FROM " + table + " WHERE userName='"
					+ userName + "'";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				results.close();
				stmt.close();
				return true;
			} else {
				results.close();
				stmt.close();
				return false;
			}
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public boolean acceptFriend(FriendRequest fr, boolean accept) {
		int success = -1;
		try {
			String command = null;

			command = "UPDATE friend_request SET status = ? "
					+ " WHERE fromUser = ? and toUser = ?";

			preparedStatement = conn.prepareStatement(command);

			preparedStatement.setInt(1, accept ? Constant.FR_ACCEPTED
					: Constant.FR_BLOCKED);
			preparedStatement.setString(2, fr.fromUser);
			preparedStatement.setString(3, fr.toUser);

			// execute update SQL stetement
			success = preparedStatement.executeUpdate();

			preparedStatement.close();
			// now make them mutual friend
			if (accept)
				makeThemMutualFriend(fr);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return true;
	}

	private void makeThemMutualFriend(FriendRequest fr) {
		try {
			// preparedStatements can use variables and are more efficient
			preparedStatement = conn.prepareStatement("insert into "
					+ "friends" + " values (default, ?, ?, ?)");
			// parameters start with 1
			preparedStatement.setString(1, fr.fromUser);
			preparedStatement.setString(2, fr.toUser);
			preparedStatement.setInt(3, Constant.FR_ACCEPTED);
			preparedStatement.executeUpdate();

			// parameters start with 1
			preparedStatement.setString(1, fr.toUser);
			preparedStatement.setString(2, fr.fromUser);
			preparedStatement.setInt(3, Constant.FR_ACCEPTED);
			preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public boolean saveFriendRequest(FriendRequest fr) {
		try {
			String command = null;

			command = "insert into " + "friend_request"
					+ " values (default, ?, ?, ?, ?)";

			preparedStatement = conn.prepareStatement(command);

			preparedStatement.setString(1, fr.fromUser);
			preparedStatement.setString(2, fr.toUser);
			preparedStatement.setInt(3, fr.status);
			preparedStatement.setString(4, fr.msg);

			// execute update SQL stetement
			preparedStatement.executeUpdate();

			preparedStatement.close();
			// now make them mutual friend
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return true;
	}

	public boolean deleteFriendAndFrndRequest(FriendRequest fr) {
		int success = -1;
		try {
			String command = null;

			command = "UPDATE friend_request SET status = ? WHERE fromUser = ? and toUser = ?";
			preparedStatement = conn.prepareStatement(command);

			preparedStatement.setInt(1, fr.status);
			preparedStatement.setString(2, fr.fromUser);
			preparedStatement.setString(3, fr.toUser);

			// execute update SQL stetement
			int row = preparedStatement.executeUpdate();

			command = "UPDATE friends SET status = ? WHERE me = ? and friend = ?";
			preparedStatement = conn.prepareStatement(command);
			preparedStatement.setInt(1, fr.status);
			preparedStatement.setString(2, fr.fromUser);
			preparedStatement.setString(3, fr.toUser);

			// execute update SQL stetement
			row = preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return true;
	}

}

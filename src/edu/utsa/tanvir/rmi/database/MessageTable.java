package edu.utsa.tanvir.rmi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;

import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class MessageTable {

	// #############################################################
	private PreparedStatement preparedStatement = null;
	private static String table = "users";
	private static Connection conn = null;
	private static Statement stmt = null;

	private MessageTable() {
		conn = Utility.loadDatabase();
	}

	private static MessageTable mT;

	public static MessageTable getMessageTableInstance() {
		if (mT == null) {
			mT = new MessageTable();
		}
		return mT;
	}

	public boolean insertMessage(Message m) {
		try {
			// preparedStatements can use variables and are more efficient
			preparedStatement = conn
					.prepareStatement("insert into messages values (default, ?, ?, ?, ? , ?, ?,?)");
			// parameters start with 1
			preparedStatement.setString(1, m.fromUser);
			preparedStatement.setString(2, m.toUser);
			preparedStatement.setDate(3, new Date(m.timeStamp));
			preparedStatement.setBoolean(4, m.isGroupMessage);
			preparedStatement.setString(5, m.message);
			preparedStatement.setInt(6, m.status);
			preparedStatement.setString(7, m.isGroupMessage ? m.groupName
					: Constant.INVALID_GROUP_NAME);
			int row = preparedStatement.executeUpdate();

			preparedStatement.close();
			return true;

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public ArrayList<Message> getAllMessageFrom(String me, String frnd,
			boolean isFrnd) {
		ArrayList<Message> allMsg = null;
		String selectSQL = "";
		ResultSet r = null;
		// preparedStatements can use variables and are more efficient
		try {
			if (isFrnd) {
				selectSQL = "SELECT * FROM messages WHERE ((fromUser = ? and toUser = ?) or (fromUser = ? and toUser = ?)) and isGroupMessage = ?";
				// preparedStatements can use variables and are more efficient
				preparedStatement = conn.prepareStatement(selectSQL);
				// parameters start with 1
				preparedStatement.setString(1, me);
				preparedStatement.setString(2, frnd);
				preparedStatement.setString(3, frnd);
				preparedStatement.setString(4, me);
				preparedStatement.setBoolean(5, !isFrnd);
			} else {
				selectSQL = "SELECT * FROM messages WHERE groupName = ? and toUser = ? and isGroupMessage = ?";
				preparedStatement = conn.prepareStatement(selectSQL);
				// parameters start with 1
				preparedStatement.setString(1, frnd);
				preparedStatement.setString(2, me);
				preparedStatement.setBoolean(3, !isFrnd);
			}

			r = preparedStatement.executeQuery();
			while (r.next()) {
				if (allMsg == null) {
					allMsg = new ArrayList<Message>();
				}

				String fromUser = r.getString(r.findColumn("fromUser"));
				String toUser = r.getString(r.findColumn("toUser"));
				Date timeStamp = r.getDate(r.findColumn("timeStamp"));
				boolean isGroupMessage = r.getBoolean(r
						.findColumn("isGroupMessage"));
				String group = r.getString(r.findColumn("groupName"));
				String message = r.getString(r.findColumn("message"));
				int status = r.getInt(r.findColumn("status"));
				int id = r.getInt(r.findColumn("id"));

				Message m = new Message(id, fromUser, toUser,
						timeStamp.getTime(), isGroupMessage, group, message,
						status);
				allMsg.add(m);

			}

//			if (isFrnd) {
				// this will update the status of the message to DELIVERED.
				// this will only update the friend messages. I don't know
				// the logic for the GROUP :'(
				ArrayList<Message> msgForUpdate = new ArrayList<Message>();
				if (allMsg != null) {
					for (Message m : allMsg) {
						if (m.fromUser.equals(me)) {
							continue;
						}
						msgForUpdate.add(m);
					}
					updateMessageStatusInBatch(msgForUpdate);
				}
//			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allMsg;
	}

	public boolean updateMessageStatusInBatch(ArrayList<Message> allMsg) {
		if (allMsg == null)
			return false;
		if (allMsg.size() == 0)
			return false;

		try {

			String sql = "UPDATE messages SET status = ? WHERE id =?";
			// Create PrepareStatement object
			preparedStatement = conn.prepareStatement(sql);

			// Set auto-commit to false
			conn.setAutoCommit(false);

			for (Message m : allMsg) {
				// Set the variables
				preparedStatement.setInt(1, Constant.MESSAGE_DELIVERED);
				preparedStatement.setInt(2, m.id);

				// Add it to the batch
				preparedStatement.addBatch();
			}

			// Create an int[] to hold returned values
			int[] count = preparedStatement.executeBatch();

			// Explicitly commit statements to apply changes
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean updateMessageStatus(Message msg) {

		try {
			String command = null;

			command = "UPDATE messages SET status = ? WHERE id =?";

			preparedStatement = conn.prepareStatement(command);

			preparedStatement.setInt(1, msg.id);
			preparedStatement.setInt(2, msg.status);

			// execute update SQL stetement
			preparedStatement.executeUpdate();

			preparedStatement.close();
			// now make them mutual friend
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}

		return true;
	}

	public ArrayList<Message> readAllOfflineMessage(String usrName) {
		ArrayList<Message> allMsg = null;
		String selectSQL = "";
		ResultSet r = null;
		// preparedStatements can use variables and are more efficient
		try {
			selectSQL = "SELECT * FROM messages WHERE (toUser = ? and status <> ?)";
			// preparedStatements can use variables and are more efficient
			preparedStatement = conn.prepareStatement(selectSQL);
			// parameters start with 1
			preparedStatement.setString(1, usrName);
			preparedStatement.setInt(2, Constant.MESSAGE_DELIVERED);
			
			r = preparedStatement.executeQuery();
			while (r.next()) {
				if (allMsg == null) {
					allMsg = new ArrayList<Message>();
				}

				String fromUser = r.getString(r.findColumn("fromUser"));
				String toUser = r.getString(r.findColumn("toUser"));
				Date timeStamp = r.getDate(r.findColumn("timeStamp"));
				boolean isGroupMessage = r.getBoolean(r
						.findColumn("isGroupMessage"));
				String group = r.getString(r.findColumn("groupName"));
				String message = r.getString(r.findColumn("message"));
				int status = r.getInt(r.findColumn("status"));
				int id = r.getInt(r.findColumn("id"));

				Message m = new Message(id, fromUser, toUser,
						timeStamp.getTime(), isGroupMessage, group, message,
						status);
				allMsg.add(m);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allMsg;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////

	public Message NewMessage(Message theMsg) {
		try {
			int maxId = GetMessageMaxId() + 1;
			if (maxId > 0) {
				String command = null;
				command = "INSERT INTO " + table + " VALUES (" + maxId + ",'"
						+ theMsg.fromUser + "','" + theMsg.toUser + "','"
						+ theMsg.message + "','" + theMsg.timeStamp + "',"
						+ theMsg.isGroupMessage + ")";
				System.out.println(command);
				stmt = conn.createStatement();
				stmt.execute(command);
				stmt.close();
				// theMsg.setId(maxId);
				return theMsg;
			} else {
				return null;
			}

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return null;
		}
	}

	public ArrayList<Message> GetAllMessagesToUser(User user) {
		return GetAllMessagesToUser(user.userName);
	}

	public ArrayList<Message> GetAllMessagesToUser(String userName) {
		ArrayList<Message> localMsgList = new ArrayList<Message>();
		try {
			String command = null;
			command = "SELECT * FROM " + table + " WHERE receiver='" + userName
					+ "'";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			stmt.close();
		} catch (SQLException sqlExcept) {
			if (sqlExcept.getSQLState().equals("XSRS9")) {
				return null;
			}
			sqlExcept.printStackTrace();
		}
		return localMsgList;
	}

	int GetMessageMaxId() {
		try {
			int ret = -1;
			String command = null;
			command = "SELECT MAX(mId) FROM " + table;
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				ret = results.getInt(1);
			}
			stmt.close();
			return ret;
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return -1;
		}
	}

	public boolean DeleteMessage(Message theMsg) {
		// return DeleteMessage(theMsg.getId());
		return false;
	}

	public boolean DeleteMessage(int id) {
		try {
			String command = null;
			command = "DELETE FROM " + table + " WHERE mId=" + id;
			stmt = conn.createStatement();
			stmt.execute(command);
			stmt.close();
			return true;

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public boolean DeleteMessages(ArrayList<Message> msgList) {
		for (Message theMsg : msgList) {
			if (!DeleteMessage(theMsg)) {
				return false;
			}
		}
		return true;
	}

}

package edu.utsa.tanvir.rmi.database;

import java.util.ArrayList;
import java.security.acl.Owner;
/* For the databases */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.utility.Utility;

public class GroupTable {
	private static Connection conn = null;
	private static Statement stmt = null;
	private static String table = "groups";
	private PreparedStatement preparedStatement = null;

	private GroupTable() {
		conn = Utility.loadDatabase();
	}

	private static GroupTable gT;

	public static GroupTable getGroupTableInstance() {
		if (gT == null) {
			gT = new GroupTable();
		}
		return gT;
	}

	public boolean createGroupOrInsertUser(Group g, boolean isNew) {
		/* Get the maximum id */
		try {
			if (!isGroupExists(g.groupName) || !isNew) {
				// preparedStatements can use variables and are more efficient
				preparedStatement = conn.prepareStatement("insert into "
						+ table + " values (default, ?, ?, ?)");

				preparedStatement.setString(1, g.groupName);
				preparedStatement.setString(2, g.ownerName);
				
				preparedStatement.setString(3, (Utility.isValidString(g.groupMember,1)? g.groupMember : g.ownerName));
				preparedStatement.executeUpdate();

				return true;
			}
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
		return false;
	}

	public boolean isGroupExists(String groupName) {
		try {
			String command = null;
			command = "SELECT groupName FROM " + table + " WHERE groupName='"
					+ groupName + "'";

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

	public boolean isGroupExists(int groupId) {

		try {
			String command = null;
			command = "SELECT groupName FROM " + table + " WHERE groupId="
					+ groupId + "'";
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

	public Group getGroup(String grpName) {
		if (!isGroupExists(grpName)) {
			return null;
		}
		Group g = new Group(grpName);
		try {
			String command = null;
			command = "SELECT * FROM " + table + " WHERE groupName='" + grpName
					+ "'";

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);

			while (results.next()) {
				String owner = results.getString(results
						.findColumn("ownerName"));
				g.ownerName = owner;
				
				String user = results.getString(results
						.findColumn("groupMember"));

				User u = new User(user);
				if (!g.listOfUsers.contains(u)) {
					g.listOfUsers.add(u);
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

		return g;
	}

	public ArrayList<Group> getListOfGroup(String userName) {
		ArrayList<Group> listOfGrp = new ArrayList<Group>();

		try {
			String command = null;
//			command = "SELECT distinct groupName FROM " + table
//					+ " WHERE ownerName='" + userName + "'";

//			stmt = conn.createStatement();
//			ResultSet results = stmt.executeQuery(command);
//			while (results.next()) {
//				String grpName = results.getString(results
//						.findColumn("groupName"));
//				listOfGrp.add(new Group(grpName));
//			}
//
//			results.close();
//			stmt.close();

			command = null;
			command = "SELECT distinct groupName FROM " + table
					+ " WHERE groupMember='" + userName + "'";

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			while (results.next()) {
				String grpName = results.getString(results
						.findColumn("groupName"));
				listOfGrp.add(new Group(grpName));
			}

			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			if (sqlExcept.getSQLState().equals("XSRS9")) {
				return null;
			}
			sqlExcept.printStackTrace();
		}

		return listOfGrp;
	}

	public ArrayList<Group> getAllGroupUserNotMemberOf(String grpName, String userName) {
		ArrayList<Group> allGrp = null;		
		try {
			User cachedUser = DatabaseManager.getCachedUser(userName);
			ArrayList<Group> alreadyMemberGrp = cachedUser.listOfGroups; 
			
			String command = null;
			command = "SELECT DISTINCT groupName FROM " + table + " WHERE groupName like'" + grpName
					+ "%' and groupMember<>'"+userName+"'";

			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);

			while (results.next()) {
				if(allGrp == null) {
					allGrp = new ArrayList<Group>();
				}
				
				String user = results.getString(results
						.findColumn("groupName"));
				Group u = new Group(user);
				if (!allGrp.contains(u) && !alreadyMemberGrp.contains(u)) {
					allGrp.add(u);
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

		return allGrp;
	}
	
	
	public boolean joinGroup(String userName, String grpName) {
		if(!isGroupExists(grpName)) {
			return false;
		}
		Group g = getGroup(grpName);
		g.groupMember = userName;
		return createGroupOrInsertUser(g, false);
	}
	
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////

	public boolean isGroupExists(String groupName, String moderatorUserName) {
		return isGroupExists(getGroupId(groupName, moderatorUserName));
	}

	String getGroupNameWithId(int groupId) {
		String ret = null;
		try {
			String command = null;
			command = "SELECT groupName FROM " + table + " WHERE groupId="
					+ groupId;
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				ret = results.getString(1);
			}
			results.close();
			stmt.close();
			return ret;
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return null;
		}
	}

	int getGroupId(String groupName, String moderatorUserName) {
		int ret = -1;
		try {
			String command = null;
			command = "SELECT groupId FROM " + table + " WHERE groupName='"
					+ groupName + "' and moderatorUserName='"
					+ moderatorUserName + "'";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				ret = results.getInt(1);
			}
			results.close();
			stmt.close();
			return ret;
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return -1;
		}
	}

	public boolean AddUserToGroup(String groupName, User theFriend,
			User moderator) {
		try {
			Group localGroup = null;// getGroup(groupName, moderator);
			String command = null;
			command = "INSERT INTO " + table + " VALUES (" + localGroup.groupId
					+ ",'" + localGroup.groupName + "','" + theFriend.userName
					+ "','" + moderator.userName + "')";
			stmt = conn.createStatement();
			stmt.execute(command);
			stmt.close();
			return true;
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public ArrayList<User> GetGroupMembersInGroup(int groupId) {
		try {
			String command = null;
			command = "SELECT memeberUserName FROM " + table
					+ " WHERE groupId=" + groupId;
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			ArrayList<User> listOfMembers = new ArrayList<User>();
			UserTable localUserTable = UserTable.getUserTableInstance();
			while (results.next()) {
				listOfMembers.add(localUserTable.getUserByUserName2(results
						.getString(1)));
			}
			results.close();
			stmt.close();
			if (listOfMembers.isEmpty()) {
				return null;
			} else {
				return listOfMembers;
			}
		} catch (SQLException sqlExcept) {
			return null;
		}
	}

	public ArrayList<User> GetGroupMembersInGroup(String groupName,
			User moderator) {
		return GetGroupMembersInGroup(getGroupId(groupName, moderator.userName));
	}

	public ArrayList<User> GetGroupMembersInGroup(String groupName,
			String moderatorUserName) {
		return GetGroupMembersInGroup(getGroupId(groupName, moderatorUserName));
	}

	public ArrayList<Group> GetGroupsWhereUserIsMember(User member) {
		return GetGroupsWhereUserIsMember(member.userName);
	}

	public ArrayList<Group> GetGroupsWhereUserIsMember(String member) {

		// try {
		// String command = null;
		// command = "SELECT groupID, groupName FROM " +table
		// +" WHERE memberUserName='" + member +"'";
		// stmt = conn.createStatement();
		// ResultSet results = stmt.executeQuery(command);
		// ArrayList<Group> listOfGroups = new ArrayList<Group>();
		// while(results.next()) {
		// Group localGroup = new Group(results.getInt(1),
		// results.getString(2),
		// GetGroupMembersInGroup(results.getInt(1)));
		// listOfGroups.add(localGroup);
		// }
		// results.close();
		// return listOfGroups;
		// } catch(SQLException sqlExcept) {
		// sqlExcept.printStackTrace();
		return null;
		// }

	}

	public ArrayList<Group> GetGroupsModeratedBy(User moderator) {
		return GetGroupsModeratedBy(moderator.userName);
	}

	public ArrayList<Group> GetGroupsModeratedBy(String moderator) {
		// try {
		// String command = null;
		// command = "SELECT groupID, groupName FROM " +table
		// +" WHERE moderatorUserName='" + moderator +"'";
		// stmt = conn.createStatement();
		// ResultSet results = stmt.executeQuery(command);
		// ArrayList<Group> listOfGroups = new ArrayList<Group>();
		// while(results.next()) {
		// Group localGroup = new Group(results.getInt(1),
		// results.getString(2),
		// GetGroupMembersInGroup(results.getInt(1)));
		// System.out.println(localGroup.groupName);
		// listOfGroups.add(localGroup);
		// }
		// results.close();
		// return listOfGroups;
		// } catch(SQLException sqlExcept) {
		// sqlExcept.printStackTrace();
		return null;
		// }
	}

	public boolean DeleteGroup(Group group) {
		return DeleteGroup(group.groupId);
	}

	public boolean DeleteGroup(String groupName, User moderator) {
		return DeleteGroup(getGroupId(groupName, moderator.userName));
	}

	public boolean DeleteGroup(String groupName, String moderatorUserName) {
		return DeleteGroup(getGroupId(groupName, moderatorUserName));
	}

	public boolean DeleteGroup(int groupId) {
		try {
			String command = null;
			command = "DELETE FROM " + table + " WHERE groupId=" + groupId;
			stmt = conn.createStatement();
			stmt.execute(command);
			stmt.close();
			return true;

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return false;
		}
	}

	public int GetGroupsMaxId() {
		try {
			int ret = -1;
			String command = null;
			command = "SELECT MAX(groupId) FROM " + table;
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			if (results.next()) {
				ret = results.getInt(1);
			}
			results.close();
			stmt.close();
			return ret;
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			return -1;
		}
	}

}

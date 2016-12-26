package edu.utsa.tanvir.rmi.database;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import edu.utsa.tanvir.rmi.pjo.User;

public class FriendsTable {

	private static Connection conn = null;
    private static Statement stmt = null;
    private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String table = null;
    private static String dbURL = null;
    private static String host = null;
    private static String database = null;
    private static String scheme = null;
    private static String port = null;
    private static String dbType = null;
	
    public FriendsTable() {
		host = "localhost";
		port = "1527";
		scheme = "APP";
		database = "DB";
		dbType = "jdbc:derby";
		dbURL = dbType + "://" + host + ":" + port + "/" + database 
				+ ";create=true;user="+scheme;
		table = "FRIENDS";		
		LoadDatabase(dbURL);
		CreateTable(table);
	}
	
    /*****
	 * This will load the the database 
	 * @param dbURL the host + Database name 
	 *  Example: jdbc:derby://localhost:1527/DB;create=true
	 */
	 void LoadDatabase(String dbURL) {
		try {
    		Class.forName(driver).newInstance();
    		conn = DriverManager.getConnection(dbURL);
    		//System.out.println("Connection Success");
    	}catch(Exception except) {
            except.printStackTrace();
        }
		
	}
	
	/***
	 * Create the FRIENDS relation table see <details>
	 * @param table the name of the table
	 */
	 void CreateTable(String table){
    	try
        {
    		String command = "CREATE TABLE "+ 
            					table +
            					"(" + 	
            					"userName varchar(30) not null, " + 
            					"userFriend varchar(30) not null, " +
            					"status boolean not null" +
            					 ")";
            stmt = conn.createStatement();
            stmt.execute(command);
            stmt.close();
        }
        catch (SQLException sqlExcept) {
        	
        	if(sqlExcept.getSQLState().equals("X0Y32")) {
        		//System.out.println("Already created don not worried no side effects occured");
                return; // That's OK
            }
            sqlExcept.printStackTrace();
        }
	}
	 public void DropTable() {
			try
	        {
	            stmt = conn.createStatement();
	            stmt.execute("DROP TABLE "+table);
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		}
	public boolean InsertFriendRealation(User theUser, User theFriend) {
		
		try
        {
			String command = null;
            command = "INSERT INTO " + table + " VALUES ('" +
    				
    				theUser.userName + "','" +
    				theFriend.userName + "'," +
    				"TRUE" + 
                    ")";
    		stmt = conn.createStatement();
            stmt.execute(command);
            stmt.close();
            return true;
    				
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            return false;
        }
	}
	public boolean BlockFriend(User theUser, User theFriend) {
		
		try
        {
			String command = null;
			command = "UPDATE " + table + " SET status=FALSE" +
    				" WHERE userName='" + theUser.userName +
    				"' AND " +
    				"userFriend='" + theFriend.userName +
                    "'";
    		stmt = conn.createStatement();
            stmt.execute(command);
            stmt.close();
            return true;
    				
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            return false;
        } 
	}
	
	public boolean UnblockFriend(User theUser, User theFriend) {
		
		try
        {
			String command = null;
			command = "UPDATE " + table + " SET status=TRUE" +
    				" WHERE userName='" + theUser.userName +
    				"' AND " +
    				"userFriend='" + theFriend.userName +
                    "'";
    		stmt = conn.createStatement();
            stmt.execute(command);
            stmt.close();
            return true;
    				
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            return false;
        } 
	}

	public ArrayList<User> GetNonBlockFriendsOf(String userName) {
		try {
			String command = null;
			command = "SELECT userFriend FROM " + table + " WHERE userName='"+userName+"' and status=TRUE";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			//Assuming we the output
            ArrayList<User> listOfFriends = new ArrayList<User>();
			UserTable localUserTable = UserTable.getUserTableInstance();
			while (results.next()) {
				listOfFriends.add(localUserTable.getUserByUserName(results.getString(1)));
			}
			results.close();
			stmt.close();
			return listOfFriends;
		}catch(SQLException sqlExcept) {
			return null;
		}
	}
	
	public ArrayList<User> GetBlockFriendsOf(String userName) {
		try {
			String command = null;
			command = "SELECT userFriend FROM " + table + " WHERE userName='"+userName+"' and status=FALSE";
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(command);
			//Assuming we the output
            ArrayList<User> listOfFriends = new ArrayList<User>();
			UserTable localUserTable = UserTable.getUserTableInstance();
			while (results.next()) {
				listOfFriends.add(localUserTable.getUserByUserName(results.getString(1)));
			}
			results.close();
			stmt.close();
			return listOfFriends;
		}catch(SQLException sqlExcept) {
			return null;
		}
	}
	
	public int GetFriendShipStatus(User theUser, User theFriend) {
		
		try
        {
			
			String command = null;
			command = "SELECT status FROM " + table +
    				" WHERE userName='" + theUser.userName +
    				"' AND " +
    				"userFriend='" + theFriend.userName +
                    "'";
			System.out.println(command);
    		stmt = conn.createStatement();
    		ResultSet results = stmt.executeQuery(command);
    		if (results.next()) {
    			if(results.getBoolean(1)){
    				results.close();
    				stmt.close();
    				return 1;
    			}else{
    				results.close();
    				stmt.close();
    				return 0;
    			}
        	}else{
        		results.close();
				stmt.close();
				return -1;
        	}
    				
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            return -1;
        }
	}
}

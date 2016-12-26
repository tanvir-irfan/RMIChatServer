package edu.utsa.tanvir.rmi.pjo;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
	public int groupId;
	public String groupName;
	public String ownerName;
	public String groupMember;
	
	public ArrayList<User> listOfUsers;
	
	public Group() {
		this(-1, "", "", new ArrayList<User>());
	}
	
	public Group(String grpName) {
		this(-1, grpName, "" , new ArrayList<User>());
	}
	
	public Group (int groupId, String groupName, String ownerName) {
		this(groupId, groupName, ownerName, new ArrayList<User>());
	}
	
	public Group (int groupId, String groupName, String ownerName, ArrayList<User> listOfUsers) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.ownerName = ownerName;
		this.listOfUsers = listOfUsers;
	}
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<User> getListOfUsers() {
		return listOfUsers;
	}

	public void setListOfUsers(ArrayList<User> listOfUsers) {
		this.listOfUsers = listOfUsers;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Group) {
			return ((Group) obj).groupName.equalsIgnoreCase(this.groupName);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.groupName;
	}

	public boolean addUser(User u) {
		if(!listOfUsers.contains(u)) {
			return false;
		} else {
			return listOfUsers.add(u);
		}
	}
	
	public boolean removeUser(User u) {
		if(listOfUsers.contains(u)) {
			return listOfUsers.remove(u);
		} else {
			return false;
		}
	}
}
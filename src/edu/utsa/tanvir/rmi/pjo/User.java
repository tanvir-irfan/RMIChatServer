package edu.utsa.tanvir.rmi.pjo;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	public int ID;
	public String fullName;
	public String userName;
	public String password;
	public String profession;
	public String livingCity;
	public String company;
	public String collageName;
	public int graduationYear;
	public boolean hasOfflineMsg;
		
	public ArrayList<Group> listOfGroups;
	public ArrayList<User> listOfFriends;
	public ArrayList<FriendRequest> outFrndReq;
	public ArrayList<FriendRequest> inFrndReq;
	
	// user's friend is a user. :-) 
	// this will give the message with specific friend
	public ArrayList<Message> allMessageWithFriend;
	
	public User() {
		this("");
	}

	public User(String userName) {
		this.userName = userName;
		listOfFriends = new ArrayList<User>();
		listOfGroups = new ArrayList<Group>();
		inFrndReq = new ArrayList<FriendRequest>();
		outFrndReq = new ArrayList<FriendRequest>();
		
		allMessageWithFriend = new ArrayList<Message>();
	}
	
	public User(int iD, String fullName, String userName, String password,
			String profession, String livingCity, String company,
			String collageName, int graduationYear, boolean hasOfflineMsg) {
		super();
		ID = iD;
		this.fullName = fullName;
		this.userName = userName;
		this.password = password;
		this.profession = profession;
		this.livingCity = livingCity;
		this.company = company;
		this.collageName = collageName;
		this.graduationYear = graduationYear;
		this.hasOfflineMsg = hasOfflineMsg;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getLivingCity() {
		return livingCity;
	}

	public void setLivingCity(String livingCity) {
		this.livingCity = livingCity;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCollageName() {
		return collageName;
	}

	public void setCollageName(String collageName) {
		this.collageName = collageName;
	}

	public int getGraduationYear() {
		return graduationYear;
	}

	public void setGraduationYear(int graduationYear) {
		this.graduationYear = graduationYear;
	}

	public ArrayList<User> getListOfFriends() {
		return listOfFriends;
	}

	public void setListOfFriends(ArrayList<User> listOfFriends) {
		this.listOfFriends = listOfFriends;
	}

	public ArrayList<Group> getListOfGroups() {
		return listOfGroups;
	}

	public void setListOfGroups(ArrayList<Group> listOfGroups) {
		this.listOfGroups = listOfGroups;
	}

	@Override
	public String toString() {
		return this.userName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return this.userName.equals(((User) obj).userName);
		}
		return false;
	}

}

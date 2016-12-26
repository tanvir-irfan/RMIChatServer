package edu.utsa.tanvir.rmi.pjo;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendRequest implements Serializable {
	public String fromUser;
	public String toUser;
	public int status;
	public String msg;
	
	public FriendRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FriendRequest(String fromUser, String toUser, String msg, int status) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.status = status;
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "FriendRequest [fromUser=" + fromUser + ", toUser=" + toUser +"]";
	}
	@Override
	public boolean equals(Object obj) {
		return ((FriendRequest)obj).toString().equalsIgnoreCase(this.toString());
	}
	
	
}
package edu.utsa.tanvir.rmi.pjo;

import java.io.Serializable;

public class Message implements Serializable{
	public String fromUser;
	public String toUser;
	public long timeStamp;
	public boolean isGroupMessage;
	public String groupName;
	public String message;
	public int status;
	public int id;
	
	public Message() {
	}
	public Message(int id, String fromUser, String toUser, long timeStamp,
			boolean isGroupMessage, String grpName, String msg, int status) {
		super();
		this.id = id;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.timeStamp = timeStamp;
		this.isGroupMessage = isGroupMessage;
		this.groupName = grpName;
		this.message = msg;
		this.status = status;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public boolean isGroupMessage() {
		return isGroupMessage;
	}
	public void setGroupMessage(boolean isGroupMessage) {
		this.isGroupMessage = isGroupMessage;
	}
	public String getGroup() {
		return groupName;
	}
	public void setGroup(String group) {
		this.groupName = group;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "[From : " + this.fromUser + " Message : " + this.message;
	}
	@Override
	public boolean equals(Object obj) {
		//return ((Message)obj).fromUser.equals(this.fromUser) && ((Message)obj).toUser.equals(this.toUser) && ((Message)obj).message.equals(this.message);
		return ((Message)obj).fromUser.equals(this.fromUser) && ((Message)obj).toUser.equals(this.toUser) && ((Message)obj).timeStamp == this.timeStamp;
	}
	
}

package edu.utsa.tanvir.rmi.server.threadpool;

import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.utsa.tanvir.rmi.callback.client.interfaces.CallbackClientInterface;
import edu.utsa.tanvir.rmi.database.DatabaseManager;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.MyQueue;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.serverclass.RMIChatServer;
import edu.utsa.tanvir.rmi.utility.Constant;

public class ServerSideWorker<T> implements Runnable {

	private MyQueue<T> myQueue;
	private String name;
	private int workerType;

	public ServerSideWorker(MyQueue<T> myQueue, String name, int workerType) {
		this.myQueue = myQueue;
		this.name = name;
		this.workerType = workerType;
	}

	@Override
	public void run() {
//		 System.out.println(name);
		while (true) {
			switch (workerType) {
			case Constant.WORKER_NEW_MESSAGE:
			case Constant.WORKER_MESSAGE_NOT_DELIVERED:
				processMessages();
				break;
			case Constant.WORKER_MESSAGE_OFFLINE:
				processOfflineMessages();
				break;
			case Constant.WORKER_FRIEND_REQEST:
				processFriendReq();
				break;
			default:
				System.out.println("ServerSideWorker.run():default:");
			}
		}
	}

	private synchronized void processOfflineMessages() {
		// basically doing nothing!
		Message m = (Message) myQueue.dequeue();
	}

	private synchronized void processFriendReq() {
		FriendRequest fr = (FriendRequest) myQueue.dequeue();
		switch (fr.status) {
		case Constant.FR_SENT_TO_SERVER:
			synchronized (myQueue) {
				CallbackClientInterface toClient = DatabaseManager
						.getOnlineClient(fr.toUser);
				try {

					// save FR to datbase
					DatabaseManager.getDatabaseManagerInstance()
							.saveFriendRequest(fr);

					if (toClient != null) { // Recipient is online now!!
						// tell the recipient
						toClient.alertFriendRequest(fr);
						// to read the
						// Friend Request
					} else { // Recipient is NOT online now :'(
						// nothing to do, because FR is updated in database!
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			break;
		case Constant.FR_DELETE:
		case Constant.FR_BLOCKED:
			// save FR to datbase
			DatabaseManager.getDatabaseManagerInstance()
					.deleteFriendAndFrndRequest(fr);
			break;
		default:
			System.out.println("ServerSideWorker.processMessages():default:");
		}
	}

	private synchronized void processMessages() {
		Message m = (Message) myQueue.dequeue();
		switch (m.getStatus()) {
		// case Constant.MESSAGE_NEW:
		case Constant.MESSAGE_SENT_TO_SERVER:
			if (m.isGroupMessage) {
				Group g = DatabaseManager.getDatabaseManagerInstance()
						.getGroup(m.groupName);
				if (g != null && g.listOfUsers != null) {
					for (User u : g.listOfUsers) {
//						if(u.userName.equalsIgnoreCase(m.fromUser)) {
//							continue;
//						}
						m.toUser = u.userName;
						processSingleMessage(m);
					}
				}
			} else {
				processSingleMessage(m);
			}

			break;
		case Constant.MESSAGE_NOT_DELIVERED:
			// save the message for the future sending!
			m.status = Constant.MESSAGE_OFFLINE;
			DatabaseManager.insertMessage(m);
			break;
		default:
			System.out.println("ServerSideWorker.processMessages():default:");
		}
	}

	private void processSingleMessage(Message m) {
		CallbackClientInterface toClient = DatabaseManager
				.getOnlineClient(m.toUser);
		CallbackClientInterface fromClient = DatabaseManager
				.getOnlineClient(m.fromUser);
		try {
			if (toClient != null) { // Recipient is online now!!
				m.setStatus(Constant.MESSAGE_DELIVERED);

				DatabaseManager.insertMessage(m);
				// fromClient.delivaryReport(m); // tell the sender
				// about successful
				// delivary report

				toClient.alertUserMessage(m); // tell the recipient
												// to read the
												// message
				
				
			} else { // Recipient is NOT online now :'(
				m.setStatus(Constant.MESSAGE_NOT_DELIVERED);
				// fromClient.delivaryReport(m);

				RMIChatServer.getThreadPoolManager().submitTask(m);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
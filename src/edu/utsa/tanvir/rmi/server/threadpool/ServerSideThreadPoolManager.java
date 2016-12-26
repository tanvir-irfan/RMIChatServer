package edu.utsa.tanvir.rmi.server.threadpool;

import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.MyQueue;
import edu.utsa.tanvir.rmi.utility.Constant;

public class ServerSideThreadPoolManager {

	
	
	private MyQueue<Message> newMessageQ = new MyQueue<Message>();
	private MyQueue<Message> unDeliverableMessageQ = new MyQueue<Message>();
//	private MyQueue<Message> offlineMessageDelivaryQ = new MyQueue<Message>();
	
	private MyQueue<FriendRequest> friendReq = new MyQueue<FriendRequest>();
	
	private static ServerSideThreadPoolManager threadPoolManager = null;
	
	private static ServerSideThreadPoolManager getThreadPoolInstance() {
		if(threadPoolManager == null) {
			threadPoolManager = new ServerSideThreadPoolManager();
		}
		
		return threadPoolManager;
	}

	private ServerSideThreadPoolManager() {
		initAllThreads();
	}
	
	public static ServerSideThreadPoolManager getThreadPoolmanager() {
		return getThreadPoolInstance();
	}
	
	private void initAllThreads() {
		for (Integer i = 0; i < Constant.CAPACITY_NEW_MESSAGE_WORKER; i++) {
			Thread thread = new Thread(new ServerSideWorker<Message>(newMessageQ, "NEW_MESSAGE_WORKER # " + i, Constant.WORKER_NEW_MESSAGE));
			thread.start();
		}
		
		for (Integer i = 0; i < Constant.CAPACITY_UNDELIVERABLE_MESSAGE_WORKER; i++) {
			Thread thread = new Thread(new ServerSideWorker<Message>(unDeliverableMessageQ, "UNDELIVERABLE_MESSAGE_WORKER # " + i, Constant.WORKER_MESSAGE_NOT_DELIVERED));
			thread.start();
		}
		
//		for (Integer i = 0; i < Constant.CAPACITY_SEND_UNDELIVERED_MESSAGE; i++) {
//			Thread thread = new Thread(new ServerSideWorker<Message>(offlineMessageDelivaryQ, "OFFLINE_MESSAGE_WORKER # " + i, Constant.WORKER_MESSAGE_OFFLINE));
//			thread.start();
//		}
		
		for (Integer i = 0; i < Constant.CAPACITY_FRIEND_REQ_WORKER; i++) {
			Thread thread = new Thread(new ServerSideWorker<FriendRequest>(friendReq, "FRIEND_REQ__WORKER # " + i, Constant.WORKER_FRIEND_REQEST));
			thread.start();
		}
	}
	
	public void submitTask(Message msg) {
		switch(msg.getStatus()) {
		case Constant.MESSAGE_SENT_TO_SERVER:
			newMessageQ.enqueue(msg);
			break;
		case Constant.MESSAGE_NOT_DELIVERED:
			unDeliverableMessageQ.enqueue(msg);
			break;
		}
	}
	
//	public void submitTask(ArrayList<Message> allMsg) {
//		if(allMsg == null) return;
//		for(Message m: allMsg)
//			offlineMessageDelivaryQ.enqueue(m);
//	}

	public void submitTask(FriendRequest fr) {
		switch(fr.status) {
		case Constant.FR_SENT_TO_SERVER:
		case Constant.FR_DELETE:
		case Constant.FR_BLOCKED:
			friendReq.enqueue(fr);
			break;
		}
	}

}

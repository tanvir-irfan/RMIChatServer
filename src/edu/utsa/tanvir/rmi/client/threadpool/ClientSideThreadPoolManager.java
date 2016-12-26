package edu.utsa.tanvir.rmi.client.threadpool;

import java.util.ArrayList;

import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.MyQueue;
import edu.utsa.tanvir.rmi.utility.Constant;

public class ClientSideThreadPoolManager {

	private MyQueue<Message> newMessageQ = new MyQueue<Message>();

	private static ClientSideThreadPoolManager threadPoolManager = null;

	private static ClientSideThreadPoolManager getThreadPoolInstance() {
		if (threadPoolManager == null) {
			threadPoolManager = new ClientSideThreadPoolManager();
		}

		return threadPoolManager;
	}

	private ClientSideThreadPoolManager() {
		initAllThreads();
	}

	public static ClientSideThreadPoolManager getThreadPoolmanager() {
		return getThreadPoolInstance();
	}

	private void initAllThreads() {
		for (Integer i = 0; i < Constant.CAPACITY_NEW_MESSAGE_WORKER; i++) {
			Thread thread = new Thread(new ClientSideWorker<Message>(
					newMessageQ, "WORKER_MESSAGE_SEND # " + i,
					Constant.WORKER_MESSAGE_SEND));
			thread.start();
		}
	}

	public void submitTask(Message msg) {
		newMessageQ.enqueue(msg);
	}

	public void submitTask(ArrayList<Message> allMsg) {
		if (allMsg == null)
			return;
		for (Message m : allMsg)
			submitTask(m);
	}

}

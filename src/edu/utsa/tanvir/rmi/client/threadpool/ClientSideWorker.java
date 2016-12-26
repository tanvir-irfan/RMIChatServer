package edu.utsa.tanvir.rmi.client.threadpool;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.gui.ChatWindow;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.MyQueue;
import edu.utsa.tanvir.rmi.utility.Constant;

public class ClientSideWorker<T> implements Runnable {

	private MyQueue<T> myQueue;
	private String name;
	private int workerType;

	public ClientSideWorker(MyQueue<T> myQueue, String name, int workerType) {
		this.myQueue = myQueue;
		this.name = name;
		this.workerType = workerType;
	}

	@Override
	public void run() {
		while (true) {
			processMessages();
		}
	}

	private void processMessages() {
		Message m = null;
		m = (Message) myQueue.dequeue();

		switch (m.status) {
		case Constant.MESSAGE_NEW:
			try {
				synchronized (m) {
					// RMIChatClient.getRMIChatClientInstance();
					// Hope RMIChatClient is already initialized
					m.status = Constant.MESSAGE_SENT_TO_SERVER;
					RMIChatClient.getChatServerServant().getUserAccount()
							.sendMessage(m);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case Constant.MESSAGE_DELIVERED:
			synchronized (m) {
				ChatWindow c = RMIChatClient
						.getChatWindow(m.isGroupMessage ? m.groupName
								: m.fromUser);
				if (c != null) {
					System.out.println("You have some User Message from : "
							+ m.fromUser);
					c.addThisMessageToHistoryWindow(m);
				} else {
					String from = m.isGroupMessage ? m.groupName : m.fromUser;
					JOptionPane.showMessageDialog(null,
							"You have Message from " + from, "New Message!",
							JOptionPane.INFORMATION_MESSAGE);
					if(RMIChatClient.thisHomePage != null)
						RMIChatClient.thisHomePage.updateOfflineMessageB(true);
				}
			}
			break;
		}
	}
}
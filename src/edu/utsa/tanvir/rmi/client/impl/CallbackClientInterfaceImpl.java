package edu.utsa.tanvir.rmi.client.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.utsa.tanvir.rmi.callback.client.interfaces.CallbackClientInterface;
import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.utility.Constant;

public class CallbackClientInterfaceImpl extends UnicastRemoteObject implements
		CallbackClientInterface {

	public CallbackClientInterfaceImpl() throws RemoteException {
		super();
	}

	@Override
	public void alertUserMessage(Message msg) throws RemoteException {
		RMIChatClient.getRMIChatClientInstance().getTpm().submitTask(msg);
	}

	@Override
	public void delivaryReport(Message msg) throws RemoteException {
		switch (msg.getStatus()) {
		case Constant.MESSAGE_DELIVERED:
			System.out.println("Message was delivered!");
			break;
		case Constant.MESSAGE_NOT_DELIVERED:
			System.out.println("Message was NOT delivered!");
			break;
		}
	}

	@Override
	public void alertFriendRequest(FriendRequest fr) throws RemoteException {
		JOptionPane.showMessageDialog(null,
				"You have Friend Req from " + fr.fromUser, "Friend Req!",
				JOptionPane.INFORMATION_MESSAGE);
		if(RMIChatClient.thisHomePage != null)
			RMIChatClient.thisHomePage.updateFriendReqRecievedB(true);
	}

	@Override
	public void alertOfflineMessage(ArrayList<Message> allMsg)
			throws RemoteException {
		if (allMsg != null) {
			System.out.println("You were missed!!!");
			System.out.printf("%15s%s", "Person", "Message");
			for (Message m : allMsg) {
				System.out.printf("%15s%s", m.fromUser, m.message);
			}
		}

	}

}

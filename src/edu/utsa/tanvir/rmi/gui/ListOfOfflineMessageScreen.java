package edu.utsa.tanvir.rmi.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class ListOfOfflineMessageScreen {

	private JFrame frame;
	private JPanel searchResultInnerPanel;
	private boolean isFriendReqRecieved = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListOfOfflineMessageScreen window = new ListOfOfflineMessageScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndShowListOfOfflineMessageScreen(
			final boolean isInComming) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListOfOfflineMessageScreen window = new ListOfOfflineMessageScreen(
							isInComming);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ListOfOfflineMessageScreen() {
		this(false);
	}

	public ListOfOfflineMessageScreen(boolean isInComming) {
		isFriendReqRecieved = isInComming;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);

		frame = new JFrame("List Of Offline Message Screen");
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth,
				Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);

		searchResultInnerPanel = new JPanel();
		scrollPane.setViewportView(searchResultInnerPanel);
		searchResultInnerPanel.setLayout(new GridLayout(0, 3, 0, 0));

		User u = RMIChatClient.getUser();

		if (u.hasOfflineMsg) {
			// boolean remove = false;
			ArrayList<Message> allMsg = RMIChatClient
					.getAllOfflineMessage(u.userName);
			updateMessageList(allMsg);
		} else {
			JButton btnNewButton = new JButton("No Offline Message!");
			searchResultInnerPanel.add(btnNewButton);
			btnNewButton.setEnabled(false);
		}
		goBackToHomePageButton();
	}

	static HashMap<String, Message> uniqueFrndOrGrp = new HashMap<String, Message>();

	private void updateMessageList(final ArrayList<Message> listOfMsg) {
		if(listOfMsg==null) return;
		String title = "";
		for (Message msg : listOfMsg) {
			title = msg.isGroupMessage ? msg.groupName : msg.fromUser;
			if (!uniqueFrndOrGrp.containsKey(title)) {
				uniqueFrndOrGrp.put(title, msg);
			}
		}

		for (Map.Entry<String, Message> entry : uniqueFrndOrGrp.entrySet()) {
			String key = entry.getKey();
			Message m = (Message) entry.getValue();
			title = m.isGroupMessage ? m.groupName : m.fromUser;

			JButton btnNewButton = new JButton(title);
			searchResultInnerPanel.add(btnNewButton);
			btnNewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String title = ((JButton) e.getSource()).getText();

					Message m = uniqueFrndOrGrp.get(title);
					boolean isGrpWindow = m.isGroupMessage;

					// Refresh the ListOfOfflineMessageScreen
					ListOfOfflineMessageScreen.this.frame.dispose();
//					ListOfOfflineMessageScreen
//							.createAndShowListOfOfflineMessageScreen(false);

					// createAndShowFriendChatScreen(String frndOrGrpName,
					// boolean isFriendChatWindow)
					ChatWindow.createAndShowChatWindow(RMIChatClient.getMe().userName,title,
							!isGrpWindow);
					
					
					HomePage.createAndShowHomePage();

				}
			});
		}
	}

	private void goBackToHomePageButton() {
		JButton btnBackToHomePage = new JButton("Go Back");
		searchResultInnerPanel.add(btnBackToHomePage);
		btnBackToHomePage.setEnabled(true);
		btnBackToHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListOfOfflineMessageScreen.this.frame.dispose();
				RMIChatClient.setMe(RMIChatClient.getUser());
				HomePage.createAndShowHomePage();
			}
		});
	}

}

package edu.utsa.tanvir.rmi.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.GridLayout;

import javax.swing.SwingConstants;

public class SearchFriendOrGroupScreen {

	private JFrame frame;
	private static boolean isForFriend;

	private static ArrayList<User> resultUser;
	private static ArrayList<Group> resultGrp;
	private JTextField userOrGrpNameTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchFriendOrGroupScreen window = new SearchFriendOrGroupScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndShowFriendOrGroupSearchScreen(
			final ArrayList<User> resU, final ArrayList<Group> resG,
			final boolean isSearchForFriend) {
		isForFriend = isSearchForFriend;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchFriendOrGroupScreen window = new SearchFriendOrGroupScreen(
							resU, resG, isForFriend);
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
	public SearchFriendOrGroupScreen() {
		this(false);
	}

	public SearchFriendOrGroupScreen(boolean isForFriend) {
		this(null, null, isForFriend);
	}

	public SearchFriendOrGroupScreen(ArrayList<User> resU,
			ArrayList<Group> resG, boolean isForFriend) {
		// this.isForFriend = isForFriend;
		resultUser = resU;
		resultGrp = resG;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);
		frame = new JFrame("Search Friend Or Group Screen");
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth,
				Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel searchBoxPanel = new JPanel();
		frame.getContentPane().add(searchBoxPanel, BorderLayout.NORTH);
		searchBoxPanel.setLayout(new BorderLayout());

		JLabel userOrGrpNameLbl = new JLabel("Enter User or Group Name");
		searchBoxPanel.add(userOrGrpNameLbl, BorderLayout.WEST);

		userOrGrpNameTF = new JTextField();
		searchBoxPanel.add(userOrGrpNameTF, BorderLayout.CENTER);
		userOrGrpNameTF.setColumns(10);

		JButton userOrGrpNameB = new JButton("Search");
		userOrGrpNameB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String frndOrGrp = userOrGrpNameTF.getText().toString();

				if (frndOrGrp == null
						|| frndOrGrp.trim().equals("")
						|| frndOrGrp.trim().length() < Constant.MIN_CHAR_TO_START_SEARCH_FOR_FRND_OR_GRP) {
					return;
				}
				if (isForFriend) {
					resultUser = RMIChatClient
							.getListOfUsersWhoIsNotFriend(RMIChatClient.getMe().userName, frndOrGrp);
				} else {
					resultGrp = RMIChatClient
							.getAllGroupUserNotMemberOf(frndOrGrp, RMIChatClient.getMe().userName);
				}
				SearchFriendOrGroupScreen.this.frame.dispose();
				createAndShowFriendOrGroupSearchScreen(resultUser, resultGrp,
						isForFriend);
			}
		});
		searchBoxPanel.add(userOrGrpNameB, BorderLayout.EAST);

		JPanel resultBoxPanel = new JPanel();
		frame.getContentPane().add(resultBoxPanel, BorderLayout.CENTER);
		resultBoxPanel.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();
		resultBoxPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel actualResultPanel = new JPanel();
		scrollPane.setViewportView(actualResultPanel);
		actualResultPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Your Result Will Appear Here!");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		actualResultPanel.add(lblNewLabel);

		if (isForFriend) {
			if (resultUser != null && resultUser.size() > 0) {
				for (User u : resultUser) {
					String friendName = (u).toString();
					JButton btnNewButton = new JButton(friendName);
					actualResultPanel.add(btnNewButton);

					// adding the action listener.
					btnNewButton.addActionListener(frndActionListener);
				}
			}
		} else {
			if (resultGrp != null && resultGrp.size() > 0) {
				for (Group g : resultGrp) {
					String friendName = (g).toString();
					JButton btnNewButton = new JButton(friendName);
					actualResultPanel.add(btnNewButton);

					// adding the action listener.
					btnNewButton.addActionListener(grpActionListener);
				}
			}
		}
		
		JButton btnBackToHomePage = new JButton("Go Back");
		actualResultPanel.add(btnBackToHomePage);
		btnBackToHomePage.setEnabled(true);
		btnBackToHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchFriendOrGroupScreen.this.frame.dispose();
				RMIChatClient.setMe(RMIChatClient.getUser());
				HomePage.createAndShowHomePage();
			}
		});
	}

	private MyFriendClickListner frndActionListener = new MyFriendClickListner();
	private MyGroupClickListner grpActionListener = new MyGroupClickListner();

	private class MyFriendClickListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String toUser = b.getText();
			String fromUser = RMIChatClient.getMe().userName;

			FriendRequest fr = new FriendRequest(fromUser, toUser, "Hello",
					Constant.FR_NEW);

			boolean reqSent = RMIChatClient.inviteFriend(fr);

			if (reqSent) {
				JOptionPane.showMessageDialog(null,
						"Friend Request Sent Successful!", "Friend Request",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"There were some problems with sending Friend Request."
								+ " Please try again Later!", "Friend Request",
						JOptionPane.ERROR_MESSAGE);
			}
			User u = RMIChatClient.getUser(); // getting the updated
			// information from
			// SERVER
			RMIChatClient.setMe(u); // updating myself in client side!
			SearchFriendOrGroupScreen.this.frame.dispose();
			HomePage.createAndShowHomePage();
		}
	}

	private class MyGroupClickListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String grpName = b.getText();

			String userName = RMIChatClient.getMe().userName;

			boolean g = RMIChatClient.joinGroup(userName, grpName);

			if (g) {
				JOptionPane.showMessageDialog(null,
						(isForFriend ? "Friend Request Sent Successful!"
								: "Group Added Successfully"),
						(isForFriend ? "Friend Request" : "Group Add"),
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"There were some problems with "
								+ (isForFriend ? "sending Friend Request!"
										: "addin yourself in group!")
								+ " Please try again Later!",
						(isForFriend ? "Friend Request" : "Group Add"),
						JOptionPane.ERROR_MESSAGE);
			}
			User u = RMIChatClient.getUser(); // getting the updated
			// information from
			// SERVER
			RMIChatClient.setMe(u); // updating myself in client side!
			SearchFriendOrGroupScreen.this.frame.dispose();
			HomePage.createAndShowHomePage();
		}
	}
}

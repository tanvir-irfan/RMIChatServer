package edu.utsa.tanvir.rmi.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class HomePage {

	public HomePage() {
		this(null, null);
	}

	public HomePage(ArrayList<User> frndList, ArrayList<Group> grpList) {
		initialize();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public static void createAndShowHomePage() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage homePage = new HomePage();
					// homePage.frame.pack();
					homePage.frame.setVisible(true);
					RMIChatClient.setHomePage(homePage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	JFrame frame;
	JPanel rightSidePanel;
	private int northHeight = 300;
	private int southHeight = 100;

	private int centerHeight = 400;
	private int centerwidth = 500;

	private int leftWidth = 200;
	private int centerWidth = 500;
	private int rightWidth = 200;
	private JTextField fullNameTF;
	private JTextField passwordTF;
	private JTextField professionTF;
	private JTextField livingCityTF;
	private JTextField companyTF;
	private JTextField collegeNameTF;
	private JTextField graduationYearTF;
	
	JButton friendReqRecievedB;
	JButton offlineMessageB;
	/*
	 * NORHT WEST CENTER EAST SOUTH
	 */

	public JButton getFriendReqRecievedB() {
		return friendReqRecievedB;
	}

	public void setFriendReqRecievedB(JButton friendReqRecievedB) {
		this.friendReqRecievedB = friendReqRecievedB;
	}
	
	public void updateFriendReqRecievedB(boolean friendReqRecievedFlag) {
		if(rightSidePanel != null && getFriendReqRecievedB() != null) {
			getFriendReqRecievedB().setEnabled(friendReqRecievedFlag);
			rightSidePanel.revalidate();
		}
	}
	
	public JButton getOfflineMessageB() {
		return offlineMessageB;
	}

	public void setOfflineMessageB(JButton offlineMessageB) {
		this.offlineMessageB = offlineMessageB;
	}

	public void updateOfflineMessageB(boolean offlineMsgFlag) {
		if(rightSidePanel != null && getOfflineMessageB() != null) {
			getOfflineMessageB().setEnabled(offlineMsgFlag);
			rightSidePanel.revalidate();
		}
	}
	
	private void initialize() {
		// JFrame.setDefaultLookAndFeelDecorated(true);
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);

		frame = new JFrame("Home Page");
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth,
				Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		RMIChatClient.setMe(RMIChatClient.getUser());

		initializeTop();
		initializeBottom();
		initializeLeft();
		initializeCenter();
		initializeRight();
	}

	private void updateProfileInformation() {
		fullNameTF.setText(RMIChatClient.getMe().fullName);

		passwordTF.setText(RMIChatClient.getMe().password);

		professionTF.setText(RMIChatClient.getMe().profession);

		livingCityTF.setText(RMIChatClient.getMe().livingCity);

		companyTF.setText(RMIChatClient.getMe().company);

		collegeNameTF.setText(RMIChatClient.getMe().collageName);

		graduationYearTF.setText(RMIChatClient.getMe().graduationYear + "");

	}

	private void initializeCenter() {
		JPanel centerPanel = new JPanel();
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(null);

		JLabel lblFullName = new JLabel("Full Name");
		lblFullName.setBounds(135, 93, 116, 14);
		centerPanel.add(lblFullName);

		fullNameTF = new JTextField();
		fullNameTF.setBounds(286, 93, 172, 20);
		centerPanel.add(fullNameTF);
		fullNameTF.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(135, 136, 116, 14);
		centerPanel.add(lblPassword);

		passwordTF = new JTextField();
		passwordTF.setColumns(10);
		passwordTF.setBounds(286, 136, 172, 20);
		centerPanel.add(passwordTF);

		JLabel lblProfession = new JLabel("Profession");
		lblProfession.setBounds(135, 186, 116, 14);
		centerPanel.add(lblProfession);

		professionTF = new JTextField();
		professionTF.setColumns(10);
		professionTF.setBounds(286, 186, 172, 20);
		centerPanel.add(professionTF);

		JLabel lblLivingCity = new JLabel("Living City");
		lblLivingCity.setBounds(135, 227, 116, 14);
		centerPanel.add(lblLivingCity);

		livingCityTF = new JTextField();
		livingCityTF.setColumns(10);
		livingCityTF.setBounds(286, 227, 172, 20);
		centerPanel.add(livingCityTF);

		JLabel lblCompanyName = new JLabel("Company Name");
		lblCompanyName.setBounds(135, 274, 116, 14);
		centerPanel.add(lblCompanyName);

		companyTF = new JTextField();
		companyTF.setColumns(10);
		companyTF.setBounds(286, 274, 172, 20);
		centerPanel.add(companyTF);

		JLabel lblCollegeName = new JLabel("College Name");
		lblCollegeName.setBounds(135, 317, 116, 14);
		centerPanel.add(lblCollegeName);

		collegeNameTF = new JTextField();
		collegeNameTF.setColumns(10);
		collegeNameTF.setBounds(286, 317, 172, 20);
		centerPanel.add(collegeNameTF);

		JLabel lblGraduationYear = new JLabel("Graduation Year");
		lblGraduationYear.setBounds(135, 363, 116, 14);
		centerPanel.add(lblGraduationYear);

		graduationYearTF = new JTextField();
		graduationYearTF.setColumns(10);
		graduationYearTF.setBounds(286, 363, 172, 20);
		centerPanel.add(graduationYearTF);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User user = populateDataForUpdate();

				boolean success = RMIChatClient.getRMIChatClientInstance()
						.createUser(user, false); // false means that this is an
													// update!

				if (success) {
					JOptionPane.showMessageDialog(null,
							"User update successful!", "User Update",
							JOptionPane.INFORMATION_MESSAGE);
					RMIChatClient.setMe(user);
					updateProfileInformation();
				} else {
					JOptionPane.showMessageDialog(null,
							"There were some problem with user update!",
							"User Update", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnUpdate.setBounds(369, 409, 89, 23);
		centerPanel.add(btnUpdate);

		updateProfileInformation();

	}

	protected User populateDataForUpdate() {
		User user = new User();
		user.userName = RMIChatClient.getMe().userName; // user name will not
														// update!
		user.fullName = fullNameTF.getText().toString();
		user.password = passwordTF.getText().toString();
		user.profession = professionTF.getText().toString();
		user.livingCity = livingCityTF.getText().toString();
		user.company = companyTF.getText().toString();
		user.collageName = collegeNameTF.getText().toString();
		user.graduationYear = Integer.parseInt(graduationYearTF.getText()
				.toString());
		return user;
	}

	private void initializeRight() {

		rightSidePanel = new JPanel(new GridLayout(0, 1));
		rightSidePanel.setBounds(0, 0, leftWidth, centerHeight);
		Dimension d = new Dimension(leftWidth, centerHeight);
		rightSidePanel.setMinimumSize(d);
		frame.getContentPane().add(rightSidePanel, BorderLayout.EAST);

		JButton createGroupB = new JButton("Create Group");
		createGroupB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomePage.this.frame.dispose();
				CreateGroupScreen.createAndShowCreateGroupScreen();
			}
		});
		createGroupB.setBounds(0, 0, 200, 400);
		d = new Dimension(200, 400);
		createGroupB.setMinimumSize(d);

		JButton logOutB = new JButton("Log Out");
		logOutB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Are you sure to logout?", "Log Out!", dialogButton);
				if (dialogResult == JOptionPane.YES_OPTION) {
					RMIChatClient.logOut();
					HomePage.this.frame.dispose();
					LoginScreen.createAndShowLoginScreen();
				} else {
					System.out.println("No Option");
				}
			}
		});
		logOutB.setBounds(0, 0, 200, 400);
		d = new Dimension(200, 400);
		logOutB.setMinimumSize(d);

		friendReqRecievedB = new JButton("Incomming Friend Req.");
		friendReqRecievedB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "Total Friend Request = "
				// + RMIChatClient.getUser().inFrndReq.size(),
				// "Friend Request", JOptionPane.INFORMATION_MESSAGE);
				HomePage.this.frame.dispose();
				FriendRequestScreen.createAndShowFriendRequestScreen(true);

			}
		});
		friendReqRecievedB.setBounds(0, 0, 200, 400);
		d = new Dimension(200, 400);
		friendReqRecievedB.setMinimumSize(d);

		if (RMIChatClient.getMe() != null
				&& RMIChatClient.getMe().inFrndReq != null
				&& RMIChatClient.getMe().inFrndReq.size() == 0) {
			friendReqRecievedB.setEnabled(false);
		}

		JButton friendReqSentB = new JButton("Outgoing Friend Req.");
		friendReqSentB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null,
				// "Total Friend Request Sent = "
				// + RMIChatClient.getUser().outFrndReq.size(),
				// "Friend Request", JOptionPane.INFORMATION_MESSAGE);
				HomePage.this.frame.dispose();
				FriendRequestScreen.createAndShowFriendRequestScreen(false);

			}
		});
		friendReqSentB.setBounds(0, 0, 200, 400);
		d = new Dimension(200, 400);
		friendReqSentB.setMinimumSize(d);

		if (RMIChatClient.getMe() != null
				&& RMIChatClient.getMe().outFrndReq != null
				&& RMIChatClient.getMe().outFrndReq.size() == 0) {
			friendReqSentB.setEnabled(false);
		}

		offlineMessageB = new JButton("Offline Messages");
		offlineMessageB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomePage.this.frame.dispose();
				ListOfOfflineMessageScreen
						.createAndShowListOfOfflineMessageScreen(false);

			}
		});
		offlineMessageB.setBounds(0, 0, 200, 400);
		d = new Dimension(200, 400);
		offlineMessageB.setMinimumSize(d);

		if (RMIChatClient.getMe().hasOfflineMsg) {
			offlineMessageB.setText("Check Off. Messages");
		} else {
			offlineMessageB.setText("No Off. Messages");
			offlineMessageB.setEnabled(false);
		}

		rightSidePanel.add(createGroupB);
		rightSidePanel.add(logOutB);
		rightSidePanel.add(friendReqRecievedB);
		rightSidePanel.add(friendReqSentB);
		rightSidePanel.add(offlineMessageB);

	}

	private void initializeLeft() {
		// OUTER PANEL FOR FRIEND AND GROUP LIST/////////////
		JPanel outerPanelWest = new JPanel(new GridLayout(0, 1));
		outerPanelWest.setBounds(0, 0, leftWidth, centerHeight); // 200 X 400
		Dimension d = new Dimension(leftWidth, centerHeight);
		outerPanelWest.setMinimumSize(d);
		frame.getContentPane().add(outerPanelWest, BorderLayout.WEST);

		// ///////////////////////////////////////////////////
		JScrollPane friendListScrollPane = new JScrollPane();
		friendListScrollPane.setBounds(0, 0, leftWidth, centerHeight / 2); // 200
																			// X
																			// 200
		d = new Dimension(leftWidth, centerHeight / 2);
		friendListScrollPane.setMinimumSize(d);
		outerPanelWest.add(friendListScrollPane);

		JPanel friendListPanel = new JPanel(new GridLayout(0, 1));
		friendListPanel.setBounds(0, 0, leftWidth, centerHeight / 2 - 75);
		d = new Dimension(leftWidth, centerHeight / 2 - 75);
		friendListPanel.setMinimumSize(d);
		friendListScrollPane.setViewportView(friendListPanel);

		JLabel lblFriendListTitle = new JLabel(
				"##### List of your friend : #####");
		lblFriendListTitle.setVerticalAlignment(SwingConstants.TOP);
		lblFriendListTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriendListTitle.setBounds(5, 5, leftWidth, 75);
		d = new Dimension(leftWidth, 75);
		lblFriendListTitle.setMinimumSize(d);
		friendListPanel.add(lblFriendListTitle);

		String findFriend = "Find Friend";
		JButton btnfindFriend = new JButton(findFriend);
		btnfindFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomePage.this.frame.dispose();
				SearchFriendOrGroupScreen
						.createAndShowFriendOrGroupSearchScreen(null, null,
								true);
			}
		});

		friendListPanel.add(btnfindFriend);
		if (RMIChatClient.getMe().listOfFriends != null
				&& RMIChatClient.getMe().listOfFriends.size() > 0) {
			for (User u : RMIChatClient.getMe().listOfFriends) {
				String friendName = (u).toString();
				JButton btnNewButton = new JButton(friendName);
				friendListPanel.add(btnNewButton);
				btnNewButton.addActionListener(frndActionListener);
			}
		} else {
			// TODO redirect to find some friend!!
		}

		// /////////////////////////////////////////////////

		// ################################################

		JScrollPane grpListScrollPane = new JScrollPane();
		grpListScrollPane.setBounds(0, 0, leftWidth, centerHeight / 2);
		d = new Dimension(leftWidth, centerHeight / 2);
		grpListScrollPane.setMinimumSize(d);
		outerPanelWest.add(grpListScrollPane);

		JPanel groupListPanel = new JPanel(new GridLayout(0, 1));
		groupListPanel.setBounds(0, 0, leftWidth, centerHeight / 2 - 75);
		d = new Dimension(leftWidth, centerHeight / 2 - 75);
		groupListPanel.setMinimumSize(d);
		grpListScrollPane.setViewportView(groupListPanel);

		JLabel lblGroupListTitle = new JLabel(
				"##### List of your group :	#####");
		lblGroupListTitle.setVerticalAlignment(SwingConstants.TOP);
		lblGroupListTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGroupListTitle.setBounds(5, 5, leftWidth, 75);
		d = new Dimension(leftWidth, 75);
		lblGroupListTitle.setMinimumSize(d);
		groupListPanel.add(lblGroupListTitle);

		String findGroup = "Find Group";
		JButton btnfindGroup = new JButton(findGroup);
		btnfindGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomePage.this.frame.dispose();
				SearchFriendOrGroupScreen
						.createAndShowFriendOrGroupSearchScreen(null, null,
								false);
			}
		});
		groupListPanel.add(btnfindGroup);

		if (RMIChatClient.getMe().listOfGroups != null
				&& RMIChatClient.getMe().listOfGroups.size() > 0) {
			for (Group u : RMIChatClient.getUser().listOfGroups) {
				String grpName = (u).toString();
				JButton btnNewButton = new JButton(grpName);
				groupListPanel.add(btnNewButton);
				btnNewButton.addActionListener(grpActionListener);
			}
		} else {
			// TODO redirect to find some Group!!
		}
		// ################################################

	}

	private void initializeBottom() {
		JLabel lblCopyrightTanvir = new JLabel(
				"copyright @ tanvir, richard - 2014");
		lblCopyrightTanvir.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopyrightTanvir.setBounds(0, 0, Constant.windowWidth, southHeight);
		Dimension d = new Dimension(Constant.windowWidth, southHeight);
		lblCopyrightTanvir.setMinimumSize(d);
		frame.getContentPane().add(lblCopyrightTanvir, BorderLayout.SOUTH);
	}

	private void initializeTop() {
		JPanel northPanel = new JPanel();
		northPanel.setBounds(0, 0, Constant.windowWidth, northHeight); // 900 X
																		// 300
		Dimension d = new Dimension(Constant.windowWidth, northHeight);
		northPanel.setMinimumSize(d);
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);

		JLabel lblWelcome = new JLabel("Welcome "
				+ RMIChatClient.getMe().fullName + "!");
		lblWelcome.setBounds(0, 0, Constant.windowWidth, northHeight); // 900 X
																		// 300
		d = new Dimension(Constant.windowWidth, northHeight);
		lblWelcome.setMinimumSize(d);
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		northPanel.add(lblWelcome);

		Date date = new Date();

		JLabel lblDate = new JLabel("The time and date is : " + date.toString());
		lblDate.setBounds(0, 0, Constant.windowWidth, northHeight); // 900 X 300
		d = new Dimension(Constant.windowWidth, northHeight);
		lblDate.setMinimumSize(d);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		northPanel.add(lblDate);
	}

	private MyFriendClickListner frndActionListener = new MyFriendClickListner(
			true);
	private MyFriendClickListner grpActionListener = new MyFriendClickListner(
			false);

	private class MyFriendClickListner implements ActionListener {
		boolean isFriend;

		public MyFriendClickListner(boolean isFriend) {
			this.isFriend = isFriend;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String friendOrGroupName = b.getText();
			String myName = RMIChatClient.getMe().userName;

			Object[] options = { "Chat", "Delete" };
			int dialogResult = JOptionPane.showOptionDialog(null,
					"What do you want to do?", "Friend or Group Options",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

			if (dialogResult == JOptionPane.YES_OPTION) { // CHAT
				ChatWindow.createAndShowChatWindow(
						RMIChatClient.getMe().userName, friendOrGroupName,
						isFriend);
				HomePage.this.frame.dispose();
				HomePage.createAndShowHomePage();
			} else if (dialogResult == JOptionPane.NO_OPTION) {
				System.out.println("No Option");
				if(isFriend) {
					//RMIChatClient.deleteFriend(myName, friendOrGroupName, isFriend, isBlock);
					RMIChatClient.deleteFriend(myName, friendOrGroupName, isFriend, false);
					HomePage.this.frame.dispose();
					HomePage.createAndShowHomePage();
				}
			}
		}
	}

}

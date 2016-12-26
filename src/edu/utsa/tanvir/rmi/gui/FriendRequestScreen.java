package edu.utsa.tanvir.rmi.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.FriendRequest;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class FriendRequestScreen {

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
					FriendRequestScreen window = new FriendRequestScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndShowFriendRequestScreen(
			final boolean isInComming) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FriendRequestScreen window = new FriendRequestScreen(
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
	public FriendRequestScreen() {
		this(false);
	}

	public FriendRequestScreen(boolean isInComming) {
		isFriendReqRecieved = isInComming;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);
		
		frame = new JFrame("Friend Request Screen");
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

		if (this.isFriendReqRecieved) {
			updateFriendList(u.inFrndReq);
		} else {
			updateFriendList(u.outFrndReq);
		}

	}

	private void updateFriendList(ArrayList<FriendRequest> listFrndReq) {
		if (listFrndReq == null || listFrndReq.size() == 0) {
			JButton btnNewButton = new JButton("NO Friend Request!");
			searchResultInnerPanel.add(btnNewButton);
			btnNewButton.setEnabled(false);
		} else {
			for (FriendRequest f : listFrndReq) {
				JButton btnNewButton = new JButton(
						(this.isFriendReqRecieved ? f.fromUser : f.toUser));
				searchResultInnerPanel.add(btnNewButton);
				btnNewButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateFriendReqAction(e);
					}
				});
			}
		}
		//here is the options to go back to the HOMEPAGE
		goBackToHomePageButton();
	}

	private void goBackToHomePageButton() {
		JButton btnBackToHomePage = new JButton("Go Back");
		searchResultInnerPanel.add(btnBackToHomePage);
		btnBackToHomePage.setEnabled(true);
		btnBackToHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FriendRequestScreen.this.frame.dispose();
				RMIChatClient.setMe(RMIChatClient.getUser());
				HomePage.createAndShowHomePage();
			}
		});
	}
	
	private void updateFriendReqAction(ActionEvent e) {
		if (this.isFriendReqRecieved) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(
					null,
					"Do you want to accept "
							+ ((JButton) e.getSource()).getText()
							+ " as your Friend now?", "Accept Friend Request",
					dialogButton);

			FriendRequest fr = new FriendRequest();
			fr.fromUser = ((JButton) e.getSource()).getText();
			fr.toUser = RMIChatClient.getMe().userName;
			fr.status = Constant.FR_SENT_TO_SERVER_FROM_FRIEND;

			if (dialogResult == JOptionPane.YES_OPTION) {
				System.out.println("Yes Option");

				RMIChatClient.acceptFriend(fr, true);
			} else {
				//RMIChatClient.acceptFriend(fr, false);
				System.out.println("No Option");
			}

			// Refresh the FriendRequestScreen
			boolean isIncommingReq = this.isFriendReqRecieved;
			FriendRequestScreen.this.frame.dispose();
			// REFRESING Task or the Friend Request Screen is blocked for now
//			FriendRequestScreen
//					.createAndShowFriendRequestScreen(isIncommingReq);

			// Refresh the Home Page to show the updated friends.
			HomePage.createAndShowHomePage();

		} else {
			JOptionPane.showMessageDialog(null, "Please wait until "
					+ ((JButton) e.getSource()).getText()
					+ " accepts your request!", "Friend Request result",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}

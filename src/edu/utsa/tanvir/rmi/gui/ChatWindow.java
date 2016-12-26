package edu.utsa.tanvir.rmi.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

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
import edu.utsa.tanvir.rmi.pjo.Message;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;
import net.miginfocom.swing.MigLayout;

public class ChatWindow {

	public JFrame frame;
	private String me;
	private String frndOrGrpName;
	private JTextField chatMessageTF;
	private boolean isFriendChatWindow;
	private JPanel actualParticipantList;
	private JPanel actualChatHistoryPanel;

	public JFrame getFrame() {
		return frame;
	}

	public static void createAndShowChatWindow(final String me,
			final String frndOrGrpName, final boolean isFriendChatWindow) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow(me, frndOrGrpName,
							isFriendChatWindow);
					window.frame.setVisible(true);

					RMIChatClient.addNewChatWindow(frndOrGrpName, window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	public ChatWindow(final String me, final String frndOrGrpName,
			final boolean isFriendChatWindow) {
		this.me = me;
		this.frndOrGrpName = frndOrGrpName;
		this.isFriendChatWindow = isFriendChatWindow;
		RMIChatClient.setMe(RMIChatClient.getUser());
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// JFrame.setDefaultLookAndFeelDecorated(true);
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);

		frame = new JFrame(this.toString());
		frame.setBounds(Constant.xOff + Constant.windowWidth, Constant.yOff,
				Constant.windowWidth, Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel titlePannel = new JPanel();
		titlePannel.setBounds(10, 11, 664, 45);
		panel.add(titlePannel);
		titlePannel.setLayout(null);

		JLabel titleLbl = new JLabel(isFriendChatWindow ? "Friend = "
				+ frndOrGrpName : "Group Name = " + frndOrGrpName);
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setBounds(10, 11, 644, 23);
		titlePannel.add(titleLbl);

		JPanel participantTitlePanel = new JPanel();
		participantTitlePanel.setBounds(683, 11, 191, 45);
		panel.add(participantTitlePanel);
		participantTitlePanel.setLayout(null);

		JLabel participantListLbl = new JLabel("Participant List");
		participantListLbl.setHorizontalAlignment(SwingConstants.CENTER);
		participantListLbl.setBounds(10, 11, 171, 23);
		participantTitlePanel.add(participantListLbl);

		JPanel chatHistoryPanel = new JPanel();
		chatHistoryPanel.setBounds(10, 67, 664, 554);
		panel.add(chatHistoryPanel);
		chatHistoryPanel.setLayout(null);

		JScrollPane chatHistoryScrollPanel = new JScrollPane();
		chatHistoryScrollPanel.setBounds(10, 11, 644, 532);
		chatHistoryPanel.add(chatHistoryScrollPanel);

		actualChatHistoryPanel = new JPanel();
		chatHistoryScrollPanel.setViewportView(actualChatHistoryPanel);
		actualChatHistoryPanel.setLayout(new GridLayout(0, 1));

		JPanel participantListPanel = new JPanel();
		participantListPanel.setBounds(683, 67, 191, 683);
		panel.add(participantListPanel);
		participantListPanel.setLayout(null);

		JScrollPane pariticipantListScrollPanel = new JScrollPane();
		pariticipantListScrollPanel.setBounds(10, 11, 171, 661);
		participantListPanel.add(pariticipantListScrollPanel);

		actualParticipantList = new JPanel();
		pariticipantListScrollPanel.setViewportView(actualParticipantList);
		actualParticipantList.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel sendMessagePanel = new JPanel();
		sendMessagePanel.setBounds(10, 632, 664, 118);
		panel.add(sendMessagePanel);
		sendMessagePanel.setLayout(null);

		chatMessageTF = new JTextField();
		chatMessageTF.setBounds(10, 11, 502, 96);
		sendMessagePanel.add(chatMessageTF);
		chatMessageTF.setColumns(10);

		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = Utility.getValidString(chatMessageTF.getText()
						.toString());
				if (Utility.isValidString(msg, 1)) {
					Message m = new Message();

					m.status = Constant.MESSAGE_NEW;
					m.fromUser = RMIChatClient.getMe().userName;

					if (isFriendChatWindow) {
						m.isGroupMessage = false;
						m.toUser = frndOrGrpName;
						m.groupName = Constant.INVALID_GROUP_NAME;
					} else {
						// m.toUser will be set in server.
						// this message will be sent to all the group member.
						m.isGroupMessage = true;
						m.groupName = frndOrGrpName;
					}

					m.message = msg;
					m.timeStamp = System.currentTimeMillis();

					RMIChatClient.getRMIChatClientInstance().getTpm()
							.submitTask(m);
					chatMessageTF.setText(""); // clear the text field
					if(!m.isGroupMessage)
						addThisMessageToHistoryWindow(m);
				}
			}
		});
		sendBtn.setBounds(541, 48, 89, 23);
		sendMessagePanel.add(sendBtn);

		updateChatHistoryWindow();
		updateParticipantList();
	}

	public void addThisMessageToHistoryWindow(Message m) {
		
		JPanel userAndDatePart = new JPanel(null);
		userAndDatePart.setLayout(new GridLayout(0, 2));
		
		JLabel fromUserLbl = new JLabel(m.fromUser);
		fromUserLbl.setHorizontalAlignment(SwingConstants.LEADING);
		fromUserLbl.setBounds(50, 0, 100,100);
		
		String date = DateFormat.getDateInstance(DateFormat.FULL).format(
				new Date(m.timeStamp));
		
		JLabel dateLbl = new JLabel(date);
		dateLbl.setHorizontalAlignment(SwingConstants.TRAILING);
		dateLbl.setBounds(0, 0, 100,100);
		
		userAndDatePart.add(fromUserLbl);
		userAndDatePart.add(dateLbl);
		
		JPanel messagePart = new JPanel(null);
		messagePart.setLayout(new GridLayout(0, 1));
		
		JLabel msgLbl = new JLabel(m.message);
		msgLbl.setHorizontalAlignment(SwingConstants.CENTER);
		msgLbl.setBounds(0, 0, 300,100);
		
				
		messagePart.add(msgLbl);
		
		actualChatHistoryPanel.add(userAndDatePart);
		actualChatHistoryPanel.add(messagePart);
		
		actualChatHistoryPanel.revalidate();
	}

	private void updateChatHistoryWindow() {
		String me = RMIChatClient.getMe().userName;
		String frndOrGrpName = this.frndOrGrpName;

		ArrayList<Message> allMsfForThisWindow = RMIChatClient
				.getAllMessageFrom(me, frndOrGrpName, isFriendChatWindow);

		if (allMsfForThisWindow != null) {
			for (Message m : allMsfForThisWindow) {
				System.out.println(m);
				addThisMessageToHistoryWindow(m);
			}
		}

	}

	private void updateParticipantList() {
		if (isFriendChatWindow) {
			JButton me = new JButton(RMIChatClient.getMe().userName);
			actualParticipantList.add(me);

			JButton friend = new JButton(frndOrGrpName);
			actualParticipantList.add(friend);

		} else {
			Group g = RMIChatClient.getGroup(frndOrGrpName);

			for (User u : g.listOfUsers) {
				JButton participant = new JButton(u.userName);
				participant.addActionListener(frndActionListener);
				actualParticipantList.add(participant);
			}
		}
	}

	private MyFriendClickListner frndActionListener = new MyFriendClickListner();
	
	private class MyFriendClickListner implements ActionListener {

		public MyFriendClickListner() {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String friendOrGroupName = b.getText();
			String myName = RMIChatClient.getMe().userName;

			Object[] options = { "Yes", "No" };
			int dialogResult = JOptionPane.showOptionDialog(null,
					"Do you want to send Friend Request to ?" + friendOrGroupName, "Friend Request",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

			if (dialogResult == JOptionPane.YES_OPTION) { // CHAT
				FriendRequest fr = new FriendRequest(RMIChatClient.getMe().userName, friendOrGroupName, "Hello",
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
			} else if (dialogResult == JOptionPane.NO_OPTION) {
			}
		}
	}
	
	@Override
	public String toString() {
		return "[ " + me + " ==> " + frndOrGrpName + " ]";
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ChatWindow) {
			ChatWindow c = (ChatWindow) obj;
			return c.me.equalsIgnoreCase(me)
					&& c.frndOrGrpName.equalsIgnoreCase(frndOrGrpName);
		} else
			return false;
	}

}

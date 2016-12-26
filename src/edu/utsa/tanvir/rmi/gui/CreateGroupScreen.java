package edu.utsa.tanvir.rmi.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.Group;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class CreateGroupScreen {

	private JFrame frame;
	private JTextField grpNameTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateGroupScreen window = new CreateGroupScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void createAndShowCreateGroupScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateGroupScreen createGrp = new CreateGroupScreen();
					createGrp.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public CreateGroupScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);
		
		frame = new JFrame("Create Group Screen");
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth, Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblGroupName = new JLabel("Group Name");
		lblGroupName.setBounds(226, 359, 80, 14);
		frame.getContentPane().add(lblGroupName);
		
		grpNameTF = new JTextField();
		grpNameTF.setBounds(316, 356, 180, 20);
		frame.getContentPane().add(grpNameTF);
		grpNameTF.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String grpName = grpNameTF.getText().toString();
				String userName = RMIChatClient.getMe().userName;

				if(!Utility.isValidString(grpName, Constant.MIN_CHAR_TO_START_SEARCH_FOR_FRND_OR_GRP)) {
					return;
				} else {
					try {
						boolean g = RMIChatClient.createGroup( userName, grpName );
						
						if (g) {
							JOptionPane.showMessageDialog(null,
									"Group creation successful!", "User Creation",
									JOptionPane.INFORMATION_MESSAGE);
							
							User u = RMIChatClient.getUser(userName);
							RMIChatClient.setMe(u);
							CreateGroupScreen.this.frame.dispose();
							HomePage.createAndShowHomePage();
						} else {
							// TODO if user creation is failed then start over!
							// custom title, warning icon
							JOptionPane.showMessageDialog(null,
									"There were some problem with group creation!",
									"User Creation", JOptionPane.ERROR_MESSAGE);

							CreateGroupScreen.this.frame.dispose();
							CreateGroupScreen.createAndShowCreateGroupScreen();
						}
						
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			}
		});
		btnCreate.setBounds(506, 355, 89, 23);
		frame.getContentPane().add(btnCreate);
		
		JButton btnBackToHomePage = new JButton("Go Back");
		btnBackToHomePage.setBounds(615, 355, 89, 23);
		frame.getContentPane().add(btnBackToHomePage);
		
		btnBackToHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateGroupScreen.this.frame.dispose();
				RMIChatClient.setMe(RMIChatClient.getUser());
				HomePage.createAndShowHomePage();
			}
		});
		
	}
}

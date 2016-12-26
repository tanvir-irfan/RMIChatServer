package edu.utsa.tanvir.rmi.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

import java.awt.Color;

public class LoginScreen {

	static LoginScreen loginSc;
	JFrame frame;
	private JTextField passwordTF;
	private JTextField userNameTF;
	
	static User user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginSc = new LoginScreen();
					loginSc.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
		user = new User();
	}

	public static void createAndShowLoginScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginSc = new LoginScreen();
					loginSc.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Utility.setLookAndFeel(2);
		
		frame = new JFrame("Login Window");
		frame.getContentPane().setBackground(new Color(153, 204, 204));
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth, Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(315, 311, 80, 23);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(315, 345, 80, 23);
		frame.getContentPane().add(lblPassword);
		
		passwordTF = new JTextField();
		passwordTF.setBounds(405, 346, 161, 20);
		frame.getContentPane().add(passwordTF);
		passwordTF.setColumns(10);
		
		userNameTF = new JTextField();
		userNameTF.setColumns(10);
		userNameTF.setBounds(405, 312, 161, 20);
		frame.getContentPane().add(userNameTF);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user = new User();
				user.userName = userNameTF.getText().toString();
				user.password = passwordTF.getText().toString();
				
				if( user == null || user.userName == null || user.password == null) {
					return;
				}
				
				if( user.userName != null && user.password.equals("")) {
					return;
				}
				if( user.password != null && user.password.equals("")) {
					return;
				}
				
				user = RMIChatClient.getRMIChatClientInstance().login(user.userName, user.password);
				RMIChatClient.setMe(user);
				if(user == null) {
					userNameTF.setText("");
					passwordTF.setText("");
					return;
				} else {
					LoginScreen.this.frame.dispose();
					HomePage.createAndShowHomePage();
				}
			}
		});
		btnLogin.setBounds(459, 380, 107, 23);
		frame.getContentPane().add(btnLogin);
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginScreen.this.frame.dispose();
				CreateAccountScreen.createAndShowCreateAccountScreen();
			}
		});
		btnCreateAccount.setBounds(325, 379, 107, 23);
		frame.getContentPane().add(btnCreateAccount);
	}
}

package edu.utsa.tanvir.rmi.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.utsa.tanvir.rmi.clientclass.RMIChatClient;
import edu.utsa.tanvir.rmi.pjo.User;
import edu.utsa.tanvir.rmi.utility.Constant;
import edu.utsa.tanvir.rmi.utility.Utility;

public class CreateAccountScreen {

	static CreateAccountScreen createAccSc;
	JFrame frame;
	private JTextField fullNameTF;
	private JTextField professionTF;
	private JTextField livingCityTF;
	private JTextField companyTF;
	private JTextField collegeNameTF;
	private JTextField graduationYearTF;
	private JTextField userNameTF;
	private JTextField passwordTF;

	JLabel lbluserNameInstruction;
	JLabel lblPasswordInstruction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createAccSc = new CreateAccountScreen();
					createAccSc.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreateAccountScreen() {
		initialize();
	}

	public static void createAndShowCreateAccountScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					createAccSc = new CreateAccountScreen();
					createAccSc.frame.setVisible(true);
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
//		JFrame.setDefaultLookAndFeelDecorated(true);
		Utility.setLookAndFeel(Constant.WINDOW_LOOK_AND_FEEL);
		
		frame = new JFrame("Create Account Screen");
		frame.setBounds(Constant.xOff, Constant.yOff, Constant.windowWidth, Constant.windowHeight);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblFullName = new JLabel("Full Name");
		lblFullName.setHorizontalAlignment(SwingConstants.LEFT);
		lblFullName.setBounds(235, 255, 94, 14);
		frame.getContentPane().add(lblFullName);

		fullNameTF = new JTextField();
		fullNameTF.setBounds(334, 255, 135, 20);
		frame.getContentPane().add(fullNameTF);
		fullNameTF.setColumns(10);

		professionTF = new JTextField();
		professionTF.setColumns(10);
		professionTF.setBounds(334, 283, 135, 20);
		frame.getContentPane().add(professionTF);

		JLabel lblProfession = new JLabel("Profession");
		lblProfession.setHorizontalAlignment(SwingConstants.LEFT);
		lblProfession.setBounds(235, 283, 94, 14);
		frame.getContentPane().add(lblProfession);

		livingCityTF = new JTextField();
		livingCityTF.setColumns(10);
		livingCityTF.setBounds(334, 313, 135, 20);
		frame.getContentPane().add(livingCityTF);

		JLabel lblLivingCity = new JLabel("Living City");
		lblLivingCity.setHorizontalAlignment(SwingConstants.LEFT);
		lblLivingCity.setBounds(235, 313, 94, 14);
		frame.getContentPane().add(lblLivingCity);

		companyTF = new JTextField();
		companyTF.setColumns(10);
		companyTF.setBounds(334, 344, 135, 20);
		frame.getContentPane().add(companyTF);

		JLabel lblCompany = new JLabel("Company Name");
		lblCompany.setHorizontalAlignment(SwingConstants.LEFT);
		lblCompany.setBounds(235, 344, 94, 14);
		frame.getContentPane().add(lblCompany);

		collegeNameTF = new JTextField();
		collegeNameTF.setColumns(10);
		collegeNameTF.setBounds(334, 379, 135, 20);
		frame.getContentPane().add(collegeNameTF);

		JLabel lblCollege = new JLabel(" College Name");
		lblCollege.setHorizontalAlignment(SwingConstants.LEFT);
		lblCollege.setBounds(235, 379, 94, 14);
		frame.getContentPane().add(lblCollege);

		graduationYearTF = new JTextField();
		graduationYearTF.setColumns(10);
		graduationYearTF.setBounds(334, 410, 135, 20);
		frame.getContentPane().add(graduationYearTF);

		JLabel lblGraduationYear = new JLabel("Graduation Year");
		lblGraduationYear.setHorizontalAlignment(SwingConstants.LEFT);
		lblGraduationYear.setBounds(235, 410, 94, 14);
		frame.getContentPane().add(lblGraduationYear);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateAccountScreen.this.frame.dispose();
				LoginScreen.createAndShowLoginScreen();
			}
		});
		btnCancel.setBounds(380, 451, 89, 23);
		frame.getContentPane().add(btnCancel);

		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO CREATE ACCOUNT
				User user = new User();
				String str = "";

				str = userNameTF.getText().toString().trim();
				user.setUserName(str);

				str = passwordTF.getText().toString().trim();
				user.setPassword(str);

				str = fullNameTF.getText().toString().trim();
				user.setFullName(str);

				str = professionTF.getText().toString().trim();
				user.setProfession(str);

				str = livingCityTF.getText().toString().trim();
				user.setLivingCity(str);

				str = companyTF.getText().toString().trim();
				user.setCompany(str);

				str = collegeNameTF.getText().toString().trim();
				user.setCollageName(str);

				str = graduationYearTF.getText().toString().trim();
				try{
					user.setGraduationYear(Integer.parseInt(str));
				} catch(Exception ex) {
					ex.printStackTrace();
					Date d = new Date();
					user.setGraduationYear(d.getYear() + Constant.GRADUATION_YEAR_OFFSET);
				}

				if (user.getUserName() == null || user.getUserName().equals("")) {
					lbluserNameInstruction
							.setText(Constant.EMPTY_USER_NAME_WARNING);
					return;
				}
				if (user.getPassword() == null || user.getPassword().equals("")) {
					lblPasswordInstruction
							.setText(Constant.EMPTY_PASSWORD_WARNING);
					return;
				}
				// TODO need to warn user about the duplicate user!

				User existingUser = RMIChatClient.getUser(user.userName);

				if (existingUser != null) { // that means user already exists
					lbluserNameInstruction.setText(Constant.INVALID_USER_NAME);
					return;
				}

				boolean success = RMIChatClient.getRMIChatClientInstance().createUser(
						user, true);

				if (success) {
					JOptionPane.showMessageDialog(null,
							"User creation successful!", "User Creation",
							JOptionPane.INFORMATION_MESSAGE);
					CreateAccountScreen.this.frame.dispose();
					LoginScreen.createAndShowLoginScreen();
				} else {
					// TODO if user creation is failed then start over!
					// custom title, warning icon
					JOptionPane.showMessageDialog(null,
							"There were some problem with user creation!",
							"User Creation", JOptionPane.ERROR_MESSAGE);

					CreateAccountScreen.this.frame.dispose();
					CreateAccountScreen.createAndShowCreateAccountScreen();
				}
			}
		});
		btnCreate.setBounds(281, 453, 89, 23);
		frame.getContentPane().add(btnCreate);

		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setHorizontalAlignment(SwingConstants.LEFT);
		lblUserName.setBounds(235, 194, 94, 14);
		frame.getContentPane().add(lblUserName);

		userNameTF = new JTextField();
		userNameTF.setColumns(10);
		userNameTF.setBounds(334, 194, 135, 20);
		frame.getContentPane().add(userNameTF);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(235, 222, 94, 14);
		frame.getContentPane().add(lblPassword);

		passwordTF = new JTextField();
		passwordTF.setColumns(10);
		passwordTF.setBounds(334, 222, 135, 20);
		frame.getContentPane().add(passwordTF);

		lbluserNameInstruction = new JLabel(Constant.EMPTY_USER_NAME);
		lbluserNameInstruction.setBounds(479, 194, 170, 14);
		frame.getContentPane().add(lbluserNameInstruction);

		lblPasswordInstruction = new JLabel(Constant.EMPTY_PASSWORD);
		lblPasswordInstruction.setBounds(479, 222, 170, 14);
		frame.getContentPane().add(lblPasswordInstruction);
		
		JLabel lblGraduationYearWarning = new JLabel("Pleaes put integer value here. e.g. 2019");
		lblGraduationYearWarning.setBounds(479, 410, 209, 14);
		frame.getContentPane().add(lblGraduationYearWarning);
	}

	public void fillUpProfileWithUserInformation(User u) {

		userNameTF.setText(u.userName);

		fullNameTF.setText(u.fullName);

		professionTF = new JTextField(u.profession);

		livingCityTF = new JTextField(u.livingCity);

		companyTF = new JTextField(u.company);

		collegeNameTF = new JTextField(u.collageName);

		graduationYearTF = new JTextField(u.graduationYear);
	}

}

package edu.utsa.tanvir.rmi.utility;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Utility {
	public static boolean isValidString(String str, int minLength) {
		if (str == null || str.trim().equals(""))
			return false;
		str = str.trim();

		if (str.length() >= minLength) {
			return true;
		} else {
			return false;
		}
	}

	public static String getValidString(String str) {
		str = str.trim();
		return str;
	}

	public static void setLookAndFeel(int style) {
		switch (style) {
		case 1:
			JFrame.setDefaultLookAndFeelDecorated(true);
			break;
		case 2:
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	/*****
	 * This will load the the database
	 */
	public static Connection loadDatabase() {
		// ########################################################
		// this will load the MySQL driver, each DB has its own driver
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// setup the connection with the DB.
			conn = DriverManager.getConnection(Constant.CON_DRIVER_URL, "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;

	}
	
	public static void main(String[] s) {
		String str = "      tanvir     ";
		System.out.println(str + " = " + Utility.getValidString(str));

		str = "      tanvir     ";
		System.out.println(str + " = " + Utility.isValidString(str, 0));

		str = "           ";
		System.out.println(str + " = " + Utility.isValidString(str, 0));

		str = "      tanvir     ";
		System.out.println(str + " = " + Utility.isValidString(str, 6));

		str = "      tanvir     ";
		System.out.println(str + " = " + Utility.isValidString(str, 4));

		str = "      tanvir     ";
		System.out.println(str + " = " + Utility.isValidString(str, 7));
		
		str = "";
		System.out.println(str + " = " + Utility.isValidString(str, 1));
		str = "";
		System.out.println(str + " = " + Utility.isValidString(str, 1));

	}

}

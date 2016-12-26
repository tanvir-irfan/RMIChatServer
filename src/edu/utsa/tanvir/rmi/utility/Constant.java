package edu.utsa.tanvir.rmi.utility;

public final class Constant {
	public static final int TASK_MAGIC_ADD = 0;
	public static final int TASK_MAGIC_SUB = TASK_MAGIC_ADD + 1;
	public static final int TASK_MAGIC_MUL = TASK_MAGIC_SUB + 1;
	public static final int TASK_MAGIC_SORT = TASK_MAGIC_MUL + 1;

	public static final int WORKER_TYPE_ADD = 1;
	public static final int WORKER_TYPE_SUB = WORKER_TYPE_ADD + 1;
	public static final int WORKER_TYPE_MUL = WORKER_TYPE_SUB + 1;
	public static final int WORKER_TYPE_SORT = WORKER_TYPE_MUL + 1;
	public static final int WORKER_TYPE_REQUEST_HANDLER = WORKER_TYPE_SORT + 1;

	public static final int DEFAULT_PORT = 10000;
	public static final String DEFAULT_HOST = "localhost";
	public static final int INVALID_PORT = -1;

	public static final int NUMBER_OF_OPERATION_PER_CLIENT = 1000;
	public static final int NUMBER_OF_THREAD_PER_CLIENT = 5;

	// public static final float FLOAT_MIN = Float.MIN_VALUE;
	// public static final float FLOAT_MAX = Float.MAX_VALUE;

	public static final float FLOAT_MIN = -1000000;
	public static final float FLOAT_MAX = 1000000;

	public static final int K_MIN = 500;
	public static final int K_MAX = 1000;

	public static final int SORT_K_MIN = 100;
	public static final int SORT_K_MAX = 1000;

	public static final int SORT_INT_MIN = 1;
	public static final int SORT_INT_MAX = 1000000;

	public static final int INDEX_OPERATION_TYPE = 0;
	public static final int INDEX_FIRST_DATA = 1;
	public static final int INDEX_SECOND_DATA = 2;
	public static final int INDEX_K = 3;

	public static final int INDEX_SORT_K = 1;
	public static final int INDEX_SORT_INTEGERS = 2;

	public static final String INPUT_SPLITTER = "#";
	public static final String SORT_INTEGER_SPLITTER = " ";

	public static final int SERVER_REPORTING_INTERVAL = 1000;

	public static final int CONNECTION_CHECK = 1;

	// #############################################################
	public static final int FR_NEW = 0;
	public static final int FR_SENT_TO_SERVER = FR_NEW + 1;
	public static final int FR_SENT_TO_FRIEND = FR_SENT_TO_SERVER + 1;
	public static final int FR_SENT_TO_SERVER_FROM_FRIEND = FR_SENT_TO_FRIEND + 1;
	public static final int FR_SENT_TO_CLIENT_FROM_SERVER = FR_SENT_TO_SERVER_FROM_FRIEND + 1;
	
	public static final int FR_ACCEPTED = FR_SENT_TO_CLIENT_FROM_SERVER + 1;
	public static final int FR_BLOCKED = FR_ACCEPTED + 1;
	public static final int FR_DELETE = FR_BLOCKED + 1;

	// These three status is used by client!
	public static final int MESSAGE_NEW = 0;
	public static final int MESSAGE_SENT_TO_SERVER = MESSAGE_NEW + 1;
	public static final int MESSAGE_SENT_TO_CLIENT_FROM_SERVER = MESSAGE_SENT_TO_SERVER + 1;
	// These two status is used by server!
	public static final int MESSAGE_DELIVERED = MESSAGE_SENT_TO_CLIENT_FROM_SERVER + 1;
	public static final int MESSAGE_NOT_DELIVERED = MESSAGE_DELIVERED + 1;
	public static final int MESSAGE_OFFLINE = MESSAGE_NOT_DELIVERED + 1;

	public static final int WORKER_NEW_MESSAGE = 0;
	public static final int WORKER_MESSAGE_NOT_DELIVERED = MESSAGE_NEW + 1;
	public static final int WORKER_MESSAGE_OFFLINE = WORKER_MESSAGE_NOT_DELIVERED + 1;
	public static final int WORKER_MESSAGE_SEND = WORKER_MESSAGE_OFFLINE + 1;

	public static final int WORKER_FRIEND_REQEST = WORKER_NEW_MESSAGE + 100;

	public static final int CAPACITY_NEW_MESSAGE_WORKER = 5;
	public static final int CAPACITY_UNDELIVERABLE_MESSAGE_WORKER = 5;
	public static final int CAPACITY_SEND_UNDELIVERED_MESSAGE = 3;
	public static final int CAPACITY_FRIEND_REQ_WORKER = 3;

	public static final String GO_UP = "Go Up : -1";

	public static final String EXIT = "Exit : 0";

	public static final String LOGIN = "Log In : 1";
	public static final String CREATE_USER = "Create User : 2";

	public static final String VIEW_PROFILE = "View Profile : 1";
	public static final String UPDATE_PROFILE = "Update Profile : 2";
	public static final String SEND_USER_MESSAGE = "Send Message to User : 3";

	public static final int MENU_EXIT = 0;
	public static final int LEVEL_UP = -1;
	public static final int MENU_ROOT = 1;

	public static final int SEL_LOG_IN = 1;
	public static final int SEL_CREATE_USER = SEL_LOG_IN + 1;

	public static final int SEL_VIEW_PROFILE = SEL_CREATE_USER + 1;
	public static final int SEL_UPDATE_PROFILE = SEL_VIEW_PROFILE + 1;
	public static final int SEL_SEND_USER_MESSAGE = SEL_UPDATE_PROFILE + 1;

	public static final String USER_NAME = "User Name: ";
	public static final String PASSWORD = "Password: ";
	public static final String FULL_NAME = "Full Name: ";
	public static final String PROFESSION = "Profession: ";
	public static final String LIVING_CITY = "Living City: ";
	public static final String COMPANY = "Company: ";
	public static final String COLLEGE_NAME = "College Name: ";
	public static final String GRADUATION_YEAR = "Graduation Year: ";

	public static final String EMPTY_USER_NAME = "User Name must not be empty!";
	public static final String EMPTY_USER_NAME_WARNING = "What is your user name, again?";

	public static final String EMPTY_PASSWORD = "Password must not be empty!";
	public static final String EMPTY_PASSWORD_WARNING = "What is your password, again?";
	public static final String INVALID_USER_NAME = "This Name is already taken!";

	public static final int MIN_CHAR_TO_START_SEARCH_FOR_FRND_OR_GRP = 4;

	public static final String GROUP_NAME_SEPERATOR = " ";

	public static final int GRADUATION_YEAR_OFFSET = 4;

	public static final int xOff = 50;
	public static final int yOff = 50;

	public static final int windowWidth = 900;
	public static final int windowHeight = 800;

	public static final int WINDOW_LOOK_AND_FEEL = 800;
	public static final String INVALID_GROUP_NAME = "INVALID_GROUP";

	public static String CON_DRIVER_URL = "jdbc:mysql://localhost/rmi_chat_server";
}

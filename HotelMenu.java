import java.util.Scanner;
import java.util.*;


/**
 *The class HotelMenu has the responsibility of logging in the user.
 *
 *@author Vilius Drumsta
 */
public class HotelMenu {
	private Scanner in;
	private UserFileHandler userFH = new UserFileHandler("users.csv");
	private ArrayList<User> userAL = new ArrayList<User>();
	
	/**
   * Constructor for creating Scanner object.
   */
	public HotelMenu() {
		in = new Scanner(System.in);
	}
	
	/**
   * Presents the user with a menu for him to select the user type and letting him log in.
   */
	public void run() {
		boolean more, loggedIn;
		int privilege = -1; // 0 = customer, 1 = personnel, 2 =  supervisor
		more = true;
		loggedIn = false;
		
		while (more) {
			System.out.println("Choose your user type:");
			System.out.println("S)Supervisor  P)Personnel  C)Customer  Q)Quit");
			String command = in.nextLine().toUpperCase();
			
			if (command.equals("C")) {
				privilege = 0;
				loggedIn = logIn(privilege);
			}
			else if (command.equals("P")) {
				privilege = 1;
				loggedIn = logIn(privilege);
			}
			else if (command.equals("S")) {
				privilege = 2;
				loggedIn = logIn(privilege);
			}
			else if (command.equals("Q")) {
				more = false;
			}
			
			if (loggedIn) {
				Options options = new Options(privilege);
				options.showOptions();
				loggedIn = false;
			}
		}
	}
	
	private boolean logIn(int userType) {
		int menuStage;
		String username, password;
		boolean more = true;
		menuStage = 0;
		username = password = "";
		
		userAL = userFH.readUserFile();
		while(more) {
			switch(menuStage) {
				case 0:
					System.out.println("Enter your username.");
					username = in.nextLine();
					
					menuStage++;
					break;
				case 1:
					System.out.println("Enter your password.");
					password = in.nextLine();
					
					menuStage++;
					more = false;
					break;
				default:
					System.out.println("Error");
					menuStage = 0;
					break;
			}
		}
		
		User currentUser = new User(userType, username, password);
		for (int i = 0; i < userAL.size(); i++) {
			if (currentUser.equals(userAL.get(i))) {
				return true;
			}
		}
		
		System.out.println("Invalid username/password");
		return false;
	}
}
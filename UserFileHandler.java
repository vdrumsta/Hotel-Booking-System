import java.util.*;
import java.text.ParseException;
import java.io.*;

/**
 *The class UserFileHandler has the responsibility of reading to the user file.
 *
 *@author Vilius Drumsta
 */
public class UserFileHandler {
	private String filename;
	
	/**
   * Constructor for creating new UserFileHandler objects.
   * @param  filename the name of the users.csv file.
   */
	public UserFileHandler(String filename) {
		this.filename = filename;
	}
	
	/**
   * This method returns the ArrayList of users that are read from the file.
   * @return ArrayList User  ArrayList of users.
   */
	public ArrayList<User> readUserFile() {
		ArrayList<User> userAL = new ArrayList<User>();
		ArrayList<String> userStringAL = new ArrayList<String>();	// User info will be read into this
		try {
			String line = "";
			File usersFile = new File(filename);
			
			// if file doesn't exist create it
			if (!usersFile.exists()) {
				usersFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(usersFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				userStringAL.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		for (int i = 0; i < userStringAL.size(); i++) {
			String[] temp = userStringAL.get(i).split(",");
			
			userAL.add(new User(Integer.parseInt(temp[0]), temp[1], temp[2]));
		}
		
		return userAL;
	}
}
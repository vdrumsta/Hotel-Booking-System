import java.util.*;
import java.text.ParseException;
import java.io.*;

/**
 *The class CheckInFileHandler has the responsibility of reading and writing from checkins file.
 *
 *@author Vilius Drumsta
 */
public class CheckInFileHandler {
	String filename;
	
	/**
   * Constructor for creating a new CheckInFileHandler.
   * @param  filename The name for the checkins.csv file.
   */
	public CheckInFileHandler(String filename) {
		this.filename = filename;
	}
	
	/**
   * Writes a Check-In into the checkins file.
   * @param currentCheckIn the check-in that will be added to the file.
   */
	public void addCheckIn(CheckIn currentCheckIn) {
		ArrayList<String> checkInAL = readCheckInsFile();
		
		String tempCheckInInfo = currentCheckIn.toString();
		checkInAL.add(tempCheckInInfo); // add currentCheckIn to cancellationAL
		
		updateCheckInFile(checkInAL);
	}
	
	/**
   * Writes a ArrayList String into checkins file.
   * @param checkInAL The ArrayList String that will be written into the checkins file.
   */
	public void updateCheckInFile(ArrayList<String> checkInAL) {
		try {	// incase it fails to open or read from the file
			String line = "";
			File checkInsFile = new File(filename);
			
			FileWriter fw = new FileWriter(checkInsFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < checkInAL.size(); i++) {
				bw.write(checkInAL.get(i));
				bw.write("\n");
			}
			
			bw.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	/**
   * Returns the an ArrayList String of all the check-ins in the file
   * @return checkInAL the ArrayList String that holds all the check-in String info.
   */
	public ArrayList<String> readCheckInsFile() {
		ArrayList<String> checkInAL = new ArrayList<String>();	// check-in info will be read into this
		try {
			String line = "";
			File checkInFile = new File(filename);
			
			// if file doesn't exist create it
			if (!checkInFile.exists()) {
				checkInFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(checkInFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				checkInAL.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		return checkInAL;
	}
}
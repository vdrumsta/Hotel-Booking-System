import java.util.*;
import java.text.ParseException;
import java.io.*;

/**
 *The class CheckOutFileHandler has the responsibility of reading and writing from checkouts file.
 *
 *@author Vilius Drumsta
 */
public class CheckOutFileHandler {
	String filename;
	
	/**
   * Constructor for creating a new CheckOutFileHandler.
   * @param  filename The name for the checkouts.csv file.
   */
	public CheckOutFileHandler(String filename) {
		this.filename = filename;
	}
	
	/**
   * Writes a Check-Out into the checkouts file.
   * @param currentCheckOut the check-out that will be added to the file.
   */
	public void addCheckOut(CheckOut currentCheckOut) {
		ArrayList<String> checkOutAL = readCheckOutsFile();
		
		String tempCheckOutInfo = currentCheckOut.toString();
		checkOutAL.add(tempCheckOutInfo); // add currentCheckOut to checkOutAL
		
		updateCheckOutFile(checkOutAL);
	}
	
	/**
   * Writes a ArrayList String into checkouts file.
   * @param checkOutAL The ArrayList String that will be written into the checkouts file.
   */
	public void updateCheckOutFile(ArrayList<String> checkOutAL) {
		try {	// incase it fails to open or read from the file
			String line = "";
			File checkOutsFile = new File(filename);
			
			FileWriter fw = new FileWriter(checkOutsFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < checkOutAL.size(); i++) {
				bw.write(checkOutAL.get(i));
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
   * Returns the an ArrayList String of all the check-outs in the file
   * @return checkOutAL the ArrayList String that holds all the check-out String info.
   */
	public ArrayList<String> readCheckOutsFile() {
		ArrayList<String> checkOutAL = new ArrayList<String>();	// check-out info will be read into this
		try {
			String line = "";
			File checkOutsFile = new File(filename);
			
			// if file doesn't exist create it
			if (!checkOutsFile.exists()) {
				checkOutsFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(checkOutsFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				checkOutAL.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		return checkOutAL;
	}
}
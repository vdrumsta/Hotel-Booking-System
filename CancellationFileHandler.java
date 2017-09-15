import java.util.*;
import java.text.ParseException;
import java.io.*;

/**
 *The class CancellationFileHandler has the responsibility of reading and writing from cancellations file.
 *
 *@author Vilius Drumsta
 */
public class CancellationFileHandler {
	String filename;
	
	/**
   * Constructor for creating a new CancellationFileHandler.
   * @param  filename The name for the cancellations.csv file.
   */
	public CancellationFileHandler(String filename) {
		this.filename = filename;
	}
	
	/**
   * Writes a cancellation into the cancellations file.
   * @param currentCancellation the cancellation that will be added to the file.
   */
	public void addCancellation(Cancellation currentCancellation) {
		ArrayList<String> cancellationAL = readCancellationsFile();
		
		String tempCancelInfo = currentCancellation.toString();
		cancellationAL.add(tempCancelInfo); // add currentCancellation to cancellationAL
		
		
		
		
		try {	// incase it fails to open or read from the file
			String line = "";
			File cancellationsFile = new File(filename);
			
			FileWriter fw = new FileWriter(cancellationsFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < cancellationAL.size(); i++) {
				bw.write(cancellationAL.get(i));
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
   * Returns the an ArrayList String of all the cancellations in the file
   * @return  cancellationAL the ArrayList String that holds all the cancellation String info.
   */
	public ArrayList<String> readCancellationsFile() {
		ArrayList<String> cancellationAL = new ArrayList<String>();	// cancellation info will be read into this
		try {
			String line = "";
			File cancellationFile = new File(filename);
			
			// if file doesn't exist create it
			if (!cancellationFile.exists()) {
				cancellationFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(cancellationFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				cancellationAL.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		return cancellationAL;
	}
}
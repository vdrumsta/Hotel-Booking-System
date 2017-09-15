import java.util.*;
import java.text.ParseException;
import java.io.*;

/**
 *The class ReservationFileHandler has the responsibility of reading and writing from reservations file.
 *
 *@author Vilius Drumsta
 */
public class ReservationFileHandler {
	private String filename;
	
	/**
   * Constructor for creating a new ReservationFileHandler.
   * @param  filename The name for the reservations.csv file.
   */
	public ReservationFileHandler(String filename) {
		this.filename = filename;
	}
	
	/**
   * Writes a reservation into the reservations file.
   * @param currentReservation the reservation that will be added to the file.
   */
	public void addReservation(Reservation currentReservation) {
		ArrayList<String> reservationAL = readReservationsFile();
		ArrayList<Room> tempRooms;
		
		// add currentReservation to reservationAL
		String tempResInfo = currentReservation.getHotel() + ",";
		tempResInfo += currentReservation.getNumber() + ",";
		tempResInfo += currentReservation.getName() + ",";
		tempResInfo += currentReservation.getType() + ",";
		tempResInfo += currentReservation.getNights() + ",";
		tempResInfo += currentReservation.getDeposit() + ",";
		tempResInfo += currentReservation.getTotalCost() + ",";
		tempResInfo += currentReservation.getTotalCost() - currentReservation.getDeposit() + ","; // amount left to pay
		tempResInfo += currentReservation.getDate() + ",";
		
		tempRooms = currentReservation.getRooms();
		for (int i = 0; i < tempRooms.size(); i++) {
			tempResInfo += tempRooms.get(i).getRoomType() + ".";
			tempResInfo += tempRooms.get(i).getNumAdults() + ".";
			tempResInfo += tempRooms.get(i).getNumChildren() + ".";
			if (tempRooms.get(i).getBreakfastIncluded()) {
				tempResInfo += "t";
			} else {
				tempResInfo += "f";
			}
			
			if (i != (tempRooms.size() - 1)) {
				tempResInfo += "+";
			}
		}
		
		reservationAL.add(tempResInfo);
		updateReservationsFile(reservationAL);
	}
	
	/**
   * Writes a ArrayList String into reservations file.
   * @param reservationAL The ArrayList String that will be written into the reservations file.
   */
	public void updateReservationsFile(ArrayList<String> reservationAL) {
		try {	// incase it fails to open or read from the file
			String line = "";
			File hotelsFile = new File(filename);
			
			FileWriter fw = new FileWriter(hotelsFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < reservationAL.size(); i++) {
				bw.write(reservationAL.get(i));
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
   * Returns the an ArrayList String of all the reservations in the file
   * @return  reservationAL the ArrayList String that holds all the reservation String info.
   */
	public ArrayList<String> readReservationsFile() {
		ArrayList<String> reservationAL = new ArrayList<String>();	// reservation info will be read into this
		try {
			String line = "";
			File hotelsFile = new File(filename);
			
			// if file doesn't exist create it
			if (!hotelsFile.exists()) {
				hotelsFile.createNewFile();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(hotelsFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				reservationAL.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		return reservationAL;
	}
}
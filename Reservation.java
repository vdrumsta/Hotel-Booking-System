import java.util.ArrayList;

/**
 *The class Reservation has the responsibility defining and holding information about a Reservation.
 *
 *@author Vilius Drumsta
 */
public class Reservation {
	private String hotel;
	private int number;
  private String name;
	private String type;
  private int numNights;
  private double deposit;
	private double totalCost;
  private String date;
	private ArrayList<Room> rooms;
	
	/**
   * Constructor for creating a new Reservation objects.
   * @param hotel the type of hotel reservation is made for.
	 * @param number The id the reservation.
	 * @param name The name of the reservation.
	 * @param type The type of the reservation
	 * @param numNights The number of nights that the user will be staying.
	 * @param deposit The amount deposited.
	 * @param totalCost The total cost of the stay.
	 * @param date The date of the stay.
	 * @param rooms All rooms which are reserved.
   */
	Reservation(String hotel, int number, String name, String type, int numNights, double deposit, double totalCost, String date, ArrayList<Room> rooms) {
    this.hotel = hotel;
		this.number = number;
		this.name = name;
    this.type = type;
		this.numNights = numNights;
		this.deposit = deposit;
		this.totalCost = totalCost;
    this.date = date;
		this.rooms = rooms;
  }
	
	/**
   * This method returns the hotel for which the reservation was made.
   * @return hotel The hotel for which the reservation was made.
   */
	public String getHotel() {
		return hotel;
	}
	
	/**
   * This method returns the id of the reservation.
   * @return number The id of the reservation.
   */
	public int getNumber() {
    return number;
  }
	
	/**
   * This method returns the name of the reservation.
   * @return name The name of the reservation.
   */
	public String getName() {
		return name;
	}
	
	/**
   * This method returns the type of the reservation.
   * @return type The type of the reservation.
   */
	public String getType() {
		return type;
	}

	/**
   * This method returns the number of nights that the user will be staying.
   * @return numNights The number of nights of the reservation.
   */
	public int getNights() {
		return numNights;
	}
	
	/**
   * This method returns the amount paid for the deposit.
   * @return deposit The deposit for the reservation.
   */
	public double getDeposit() {
		return deposit;
	}
	
	/**
   * This method returns the amount left to pay for the reservation.
   * @return totalCost The total cost left to pay for the reservation.
   */
	public double getTotalCost() {
		return totalCost;
	}
	
	/**
   * This method returns the The date of the stay.
   * @return type The The date of the stay.
   */
  public String getDate() {
    return date;
  }
	
	/**
   * This method returns the The ArrayList of all the rooms reserved for the reservation.
   * @return type The The rooms reserved for the reservation.
   */
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	/**
   * This method returns all the info of the Reservation seperated by a comma.
   * @return result The result seperated by a comma.
   */
	public String toString() {
		String result = "";
		result += hotel + ",";
		result += number + ",";
		result += name + ",";
    result += type + ",";
		result += numNights + ",";
		result += deposit + ",";
		result += totalCost + ",";
    result += date + ",";
		
		for (int i = 0; i < rooms.size(); i++) {
			result += rooms.get(i).toString();
			if (i != (rooms.size() - 1)) {
				result += "+";
			}
		}
		
		return result;
	}
	
	/**
   * This method returns a boolean value of whether or not 2 reservations values have the same id.
	 * @param number The id value that is compared.
   * @return result The boolean value of whether or not the ids are equal.
   */
	public boolean equals(int number) {
		if (this.number == number) {
			return true;
		} else {
			return false;
		}
	}
}
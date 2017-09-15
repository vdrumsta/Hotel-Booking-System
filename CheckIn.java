/**
 *The class CheckIn has the responsibility of defining and holding information about a Check-In.
 *
 *@author Vilius Drumsta
 */
public class CheckIn {
	private int number;
	private String date;
	
	/**
   * Constructor for creating a new CheckIn objects.
   * @param number The reservation ID for which to check in.
	 * @param date The date when checking in.
   */
	public CheckIn(int number, String date) {
		this.number = number;
		this.date = date;
	}
	
	/**
   * This method returns the id of the reservation that was check in for.
   * @return number number of the reservation that was checked in for.
   */
	public int getNumber() {
		return number;
	}
	
	/**
   * This method returns the date when check-in was done.
   * @return date The date when the the check-in was done.
   */
	public String getDate() {
		return date;
	}
	
	/**
   * This method returns all the info of the check-in seperated by a comma.
   * @return result The result seperated by a comma.
   */
	public String toString() {
		String result = "";
		result += number + ",";
		result += date;

		return result;
	}
	
	/**
   * This method returns a boolean value of whether or not 2 check-in values have the same id.
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
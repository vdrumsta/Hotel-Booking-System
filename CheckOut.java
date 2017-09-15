/**
 *The class CheckOut has the responsibility of defining and holding information about a Check-Out.
 *
 *@author Vilius Drumsta
 */
public class CheckOut {
	private int number;
	private String date;
	
	/**
   * Constructor for creating a new CheckOut objects.
   * @param number The reservation ID for which to check out.
	 * @param date The date when checking out.
   */
	public CheckOut(int number, String date) {
		this.number = number;
		this.date = date;
	}
	
	/**
   * This method returns the id of the reservation that was checked out for.
   * @return number number of the reservation that was checked out for.
   */
	public int getNumber() {
		return number;
	}
	
	/**
   * This method returns the date when check-out was done.
   * @return date The date when the the check-out was done.
   */
	public String getDate() {
		return date;
	}
	
	/**
   * This method returns all the info of the check-out seperated by a comma.
   * @return result The result seperated by a comma.
   */
	public String toString() {
		String result = "";
		result += number + ",";
		result += date;

		return result;
	}
	
	/**
   * This method returns a boolean value of whether or not 2 check-out values have the same id.
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
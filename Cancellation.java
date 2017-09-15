/**
 *The class Cancellation has the responsibility defining and holding information about a Cancellation.
 *
 *@author Vilius Drumsta
 */
public class Cancellation {
	private int number;
	private String date;
	private boolean refunded;
	
	/**
   * Constructor for creating a new Cancellation objects.
   * @param number The reservation ID that will be cancelled.
	 * @param date The date when the cancellation is done.
	 * @param refunded The boolean whether the money was refunded.
   */
	public Cancellation(int number, String date, boolean refunded) {
		this.number = number;
		this.date = date;
		this.refunded = refunded;
	}
	
	/**
   * This method returns the id of the reservation that was cancelled.
   * @return number The id of the reservation that was cancelled.
   */
	public int getNumber() {
		return number;
	}
	
	/**
   * This method returns the date when cancellation was done.
   * @return date The date when the the cancellation was done.
   */
	public String getDate() {
		return date;
	}
	
	/**
   * This method returns the boolean of whether the cancellation refunded the money.
   * @return refunded The boolean of whether the cancellation refunded the money.
   */
	public boolean getRefunded() {
		return refunded;
	}
	
	/**
   * This method returns all the info of the Cancellation seperated by a comma.
   * @return result The result seperated by a comma.
   */
	public String toString() {
		String result = "";
		result += number + ",";
		result += date + ",";
		
		if (refunded) {
			result += "t";
		} else {
			result += "f";
		}
		
		return result;
	}
	
	/**
   * This method returns a boolean value of whether or not 2 cancellation values have the same id.
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
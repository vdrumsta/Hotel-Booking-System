/**
 *The class Room has the responsibility of defining a room and storing the associated information.
 *
 *@author Vilius Drumsta
 */
public class Room {
	private String type;
  private int numAdults;
  private int numChildren;
  private boolean breakfastIncluded;
	
	/**
   * Constructor that creates an object of Room type.
   * @param type The type that the room is.
	 * @param numAdults The number of adults that will be staying in this room.
	 * @param numChildren The number of children that will be staying in this room.
	 * @param breakfastIncluded The boolean of whether or not breakfast is included.
   */
	Room(String type, int numAdults, int numChildren, boolean breakfastIncluded) {
    this.type              = type;
    this.numAdults         = numAdults;
    this.numChildren       = numChildren;
    this.breakfastIncluded = breakfastIncluded;
  }
	
	/**
   * This method returns the type of the room.
   * @return type the type of the room.
   */
	public String getRoomType() {
    return type;
  }

	/**
   * This method returns the number of adults that are staying in this room.
   * @return numAdults The number of adults.
   */
  public int getNumAdults() {
    return numAdults;
  }

	/**
   * This method returns the number of children that are staying in this room.
   * @return numChildren The number of children.
   */
  public int getNumChildren() {
    return numChildren;
  }

	/**
   * This method returns the boolean of whether or not breakfast is included.
   * @return breakfastIncluded The boolean of whether or not breakfast was included.
   */
  public boolean getBreakfastIncluded() {
    return breakfastIncluded;
  }
	
	/**
   * This method returns all the info of the Room seperated by a comma.
   * @return result The result seperated by a comma.
   */
	public String toString() {
		String result = "";
		result += type + ".";
		result += numAdults + ".";
		result += numChildren + ".";
		result += breakfastIncluded ? "t" : "f";
		
		return result;
	}
}
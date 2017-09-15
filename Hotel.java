import java.util.*;

/**
 *The class Hotel has the responsibility of defining a hotel and storing the associated information.
 *
 *@author Vilius Drumsta
 */
public class Hotel {
	private Scanner in;
	private String hotelType;
	private String rooms;
	private ArrayList<ArrayList<String>> roomInfoAL;
	
	/**
   * Constructor that creates an object of Hotel type.
   */
	public Hotel() {
		in = new Scanner(System.in);
	}
	
	/**
   * Constructor that creates an object of Hotel type.
   * @param hotelType the type of the hotel.
   */
	public Hotel(String hotelType) {
		in = new Scanner(System.in);
		this.hotelType = hotelType;
		roomInfoAL = new ArrayList<ArrayList<String>>();
	}
	
	/**
   * This method adds information about a room to the hotel.
   * @param roomInfo The information about the room to be added.
   */
	public void addRoom(String[] roomInfo) {
		roomInfoAL.add(new ArrayList<String>());
		for (int i = 0; i < roomInfo.length; i++) {
			roomInfoAL.get(roomInfoAL.size() - 1).add(roomInfo[i]);
		}
	}
	
	/**
   * This method returns the type of hotel
   * @return hotelType the type of the hotel.
   */
	public String getHotelType() {
		return hotelType;
	}
	
	/**
   * Returns the ArrayList of ArrayList String info of all rooms.
   * @return roomInfo the type of the hotel.
   */
	public ArrayList<ArrayList<String>> getHotelInfo() {
		return roomInfoAL;
	}
	
	/**
   * Returns the ArrayList String room info for a particular index.
   * @param roomIndex The index of the room info requested.
   * @return the room info at the given index.
   */
	public ArrayList<String> getRoomInfo(int roomIndex) {
		return roomInfoAL.get(roomIndex);
	}
	
	/**
   * Reduces the room amount of specified name by 1
   * @param roomName the name of the room whose amount will be reduced
   */
	public void reduceRoomAmount(String roomName) {
		for (int i = 0; i < roomInfoAL.size(); i++) {
			// if room name matches, reduce by 1
			if (roomInfoAL.get(i).get(0).equals(roomName)) {
				roomInfoAL.get(i).set(1, Integer.toString(Integer.parseInt(roomInfoAL.get(i).get(1)) - 1));
			}
		}
	}
}
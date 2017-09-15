import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 *The class Options has the responsibility of letting the user select an operation and then carrying it out
 *
 *@author Vilius Drumsta
 */
public class Options {
	private Scanner in;
	private Calendar currentDate;
	private ArrayList<Hotel> hotelAL = new ArrayList<Hotel>();
	private ArrayList<ArrayList<String>> tableHeadingsAL = new ArrayList<ArrayList<String>>();
	private ArrayList<Reservation> reservationAL = new ArrayList<Reservation>();
	private ArrayList<Cancellation> cancelAL = new ArrayList<Cancellation>();
	private ArrayList<CheckIn> checkInAL = new ArrayList<CheckIn>();
	private ArrayList<CheckOut> checkOutAL = new ArrayList<CheckOut>();
	private CheckInFileHandler checkInFH;
	private CheckOutFileHandler checkOutFH;
	private ReservationFileHandler resFH;
	private CancellationFileHandler cancelFH;
	private int privileges;
	
	/**
   * Constructor for creating Options objects. Sets the privileges to the lowest.
   */
	public Options() {
		this(0); // call options method with minimum privileges
	}
	
	/**
   * Constructor for creating Options objects.
   * @param privileges The level of privileges which let the user access appropriate commands.
   */
	public Options(int privileges) {
		in = new Scanner(System.in);
		currentDate = new GregorianCalendar();
		resFH = new ReservationFileHandler("reservations.csv");
		cancelFH = new CancellationFileHandler("cancellations.csv");
		checkInFH = new CheckInFileHandler("checkins.csv");
		checkOutFH = new CheckOutFileHandler("checkouts.csv");
		createReservationAL();
		createCancelAL();
		createCheckInAL();
		createCheckOutAL();
		this.privileges = privileges;
	}
	
	/**
   * Presents the user with a menu of commands and calls his desired command.
   */
	public void showOptions() {
		boolean more = true;
		while(more) {
			String result, command;
			result = "Options error";
			command = "";
			
			// display appropriate options
			if (privileges >= 0) {
				result = "Q)Quit R)Reserve  C)Cancel";
			}
			if (privileges >= 1) {
				result += "  I)Check-in  O)Check-out";
			}
			if (privileges >= 2) {
				result += "  A)Analysis";
			}
			System.out.println(result);
			
			// Intake appropriate options
			command = in.nextLine().toUpperCase();
			
			// Call the appropriate command
			if (command.equals("R")) {
				reserve(privileges);
			}
			else if (command.equals("C")) {
				cancel();
			}
			else if (command.equals("I") && privileges >= 1) {
				checkIn();
			}
			else if (command.equals("O") && privileges >= 1) {
				checkOut();
			}
			else if (command.equals("A") && privileges >= 2) {
				analysis();
			} else if (command.equals("Q")) {
				more = false;
			}
		}
	}
	
	// takes in an id from the user and checks if it exists in one of the reservations
	private int intakeValidId() {
		int resIndex = -1;	// -1 = quit, 0+ = reservationAL index
		boolean more = true;
		String pattern = "[0-9]{1,}"; // valid number ID pattern
		
		while (more) {
			System.out.println("Enter a reservation number please. Enter -1 to go back to the menu.");
			String userInput = in.nextLine();
			
			if (userInput.matches(pattern)) {
				boolean found = false;
				
				int idNum = Integer.parseInt(userInput);
				
				// loop through reservationAL to find the index that matches the id number
				for (int i = 0; i < reservationAL.size() && !found; i++) {
					if (idNum == reservationAL.get(i).getNumber()) {
						resIndex = i;
						found = true;
						more = false;
					}
				}
				
				if(!found) {
					System.out.println("This reservation does not exist.");
				}
			} else if (userInput.equals("-1")) {
				more = false;
			} else {
				System.out.println("Invalid input. Please try again.");
			}
		}
		return resIndex;
	}
	
	// writes analysis to a file
	private void analysis() {
		int menuStage, interval, analysisType, day, month, year, daysInterval, monthsInterval, weeksInterval;
		int[] intervalSpans = new int[3];
		int[] spansLengths = {1, 7, 30};
		boolean more, valid;
		String userInput, date, filename, hotelType, result;
		Calendar startDate, endDate;
		
		result = hotelType = "";
		day = month = year = analysisType = interval = 0;
		menuStage = 0;
		more = true;
		filename = "";
		startDate = new GregorianCalendar();
		endDate = new GregorianCalendar();
		
		while(more) {
			switch(menuStage) {
				case 0:	// get interval type
					String[] intervals = {"Daily", "Weekly (7 days)", "Monthly (30 days)"};
					String pattern = "[1-" + intervals.length + "]";
					
					// display intervals
					System.out.println("Select analysis intervals.");
					for (int i = 0; i < intervals.length; i++) {
						System.out.println((i + 1) + ". " + intervals[i]);
					}
					userInput = in.nextLine();	// intake choice
					
					if (userInput.matches(pattern)) {	// check if it's a number
						interval = Integer.parseInt(userInput) - 1;
						
						menuStage++;
					}
					break;
				case 1:	// get start date
					System.out.println("From which date do you want to start the analysis? e.g. 20/12/2017");
					date = in.nextLine();
					pattern = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}";
					valid = false;
					
					// validate date
					if (isValidDate(date)) {	// check if it's in the right format dd/mm/yyyy
						String[] dateString = date.split("/");
						day = Integer.parseInt(dateString[0]);
						month = Integer.parseInt(dateString[1]);
						year = Integer.parseInt(dateString[2]);
						
						startDate = new GregorianCalendar(year, month - 1, day);
						valid = true;
					} else {
						valid = false;
					}
					
					if (!valid) {
						System.out.println("Invalid date");
					} else {
						filename += day + "-" + month + "-" + year + " to ";
						menuStage++;
					}
					break;
				case 2:	// get start date
					System.out.println("Until which date do you want to do the analysis? e.g. 20/12/2017");
					date = in.nextLine();
					pattern = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}";
					valid = false;
					
					// validate date
					if (isValidDate(date)) {	// check if it's in the right format dd/mm/yyyy
						String[] dateString = date.split("/");
						day = Integer.parseInt(dateString[0]);
						month = Integer.parseInt(dateString[1]);
						year = Integer.parseInt(dateString[2]);
						
						endDate = new GregorianCalendar(year, month - 1, day);
						if (endDate.before(startDate)) {
							System.out.print("Your end date must be older than the start date. ");
							valid = false;
						} else {
							valid = true;
						}
					} else {
						valid = false;
					}
					
					if (!valid) {
						System.out.println("Invalid date");
					} else {
						filename += day + "-" + month + "-" + year;
						menuStage++;
					}
					break;
				case 3:
					readHotelsFile();
					pattern = "[1-" + hotelAL.size() + "]{1}";
					
					System.out.println("Which hotel would you like to analyse?");
					for (int i = 0; i < hotelAL.size(); i++) {	// print out the hotel star options
						System.out.println((i + 1) + ". " + hotelAL.get(i).getHotelType());
					}
					
					userInput = in.nextLine();	// user selects hotel
					if (userInput.matches(pattern)) {
						int hotelTableIndex;
						
						hotelTableIndex = Integer.parseInt(userInput) - 1;				// index in the table - 1 (for use in hotelAL to get the appropriate room info)
						hotelType = hotelAL.get(hotelTableIndex).getHotelType();	// stores the hotel type
						//int tableIndex = Integer.parseInt(hotelType.substring(0,1));							// stores the star of the hotel type as a number
						
						menuStage++;
					}	
					break;
				case 4:	// get analysis type. occupancy/rates
					String[] analysisTypes = {"Occupancy Figures", "Rates"};
					pattern = "[1-" + analysisTypes.length + "]";
					
					// display analysis types
					System.out.println("What sort of analysis would you like to see?");
					for (int i = 0; i < analysisTypes.length; i++) {
						System.out.println((i + 1) + ". " + analysisTypes[i]);
					}
					userInput = in.nextLine();	// intake choice
					
					if (userInput.matches(pattern)) {	// check if it's a number
						analysisType = Integer.parseInt(userInput) - 1;
						
						filename += " " + analysisTypes[analysisType] + " Analysis.csv";
						menuStage++;
						more = false;
					}
					break;
				default:
					System.out.println("Error");
					menuStage = 0;
					break;
			}
		}
		
		// get difference of milliseconds between 2 dates
		long end = endDate.getTimeInMillis();
    long start = startDate.getTimeInMillis();
    intervalSpans[0] = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
		
		double[] spans;
		
		// get number of cycles there will have to be done for appropriate intervals and create an array of that size
		switch (interval) {
			case 0:
				spans = new double[intervalSpans[0]];
				System.out.println("spans.length = " + spans.length);
				break;
			case 1:
				// calculate how many weeks are in the span
				intervalSpans[1] = intervalSpans[0] / 7;
				if (intervalSpans[0] % 7 > 0) {
					intervalSpans[1]++;
				}
				
				spans = new double[intervalSpans[1]];
				break;
			case 2:
					// calculate how many months are in the span
					intervalSpans[2] = intervalSpans[0] / 30;
					if (intervalSpans[0] % 30 > 0) {
						intervalSpans[2]++;
					}
					
					spans = new double[intervalSpans[2]];
				break;
			default:
				spans = new double[1];
				System.out.println("Error");
				break;
		}
		
		for (int i = 0; i < spans.length; i++) {
			double revenue = 0;
			int adults, children;
			adults = children = 0;
			
			day = startDate.get(Calendar.DAY_OF_MONTH);
			month = startDate.get(Calendar.MONTH) + 1;
			year = startDate.get(Calendar.YEAR);
			result += day + "/" + month + "/" + year + ",";
			
			if (i != spans.length - 1) {
				int daysToAdd = 1;
				if (interval == 1) {
					daysToAdd = 6;
				} else if (interval == 2) {
					daysToAdd = 29;
				}
				
				Calendar tempDate = (Calendar) startDate.clone();
				tempDate.set(Calendar.DAY_OF_MONTH, tempDate.get(Calendar.DAY_OF_MONTH) + daysToAdd);
				
				day = tempDate.get(Calendar.DAY_OF_MONTH);
				month = tempDate.get(Calendar.MONTH) + 1;
				year = tempDate.get(Calendar.YEAR);
				result += day + "/" + month + "/" + year + ",";
			} else {
				day = endDate.get(Calendar.DAY_OF_MONTH);
				month = endDate.get(Calendar.MONTH) + 1;
				year = endDate.get(Calendar.YEAR);
				result += day + "/" + month + "/" + year + ",";
			}
			
			for (int j = 0; j < spansLengths[interval]; j++) {
				for (int k = 0; k < reservationAL.size(); k++) {
					boolean cancelled = false;
					
					valid = false;
					day = startDate.get(Calendar.DAY_OF_MONTH);
					month = startDate.get(Calendar.MONTH) + 1;
					year = startDate.get(Calendar.YEAR);
					
					date = day + "/" + month + "/" + year;
					
					// check if dates are matching
					String resDate = reservationAL.get(k).getDate();
					if (resDate.equals(date)) {
						valid = true;
					}
					
					// check if the hotel is the same type
					if (!hotelType.equals(reservationAL.get(k).getHotel())) {
						valid = false;
					}
					
					// check if the reservation has been cancelled
					for (int l = 0; l < cancelAL.size() && valid; l++) {
						if (cancelAL.get(l).getNumber() == reservationAL.get(l).getNumber()) {
							cancelled = true;
						}
					}
					
					if (valid) {
						if (analysisType == 0 && !cancelled) {					// occupancy analysis
							ArrayList<Room> rooms = reservationAL.get(k).getRooms();
							
							for (int l = 0; l < rooms.size(); l++) {
								adults += rooms.get(l).getNumAdults();
								//System.out.println(reservationAL.get(k).getNumber() + ", adults = " + rooms.get(l).getNumAdults());
								children += rooms.get(l).getNumChildren();
								//System.out.println(reservationAL.get(k).getNumber() + ", children = " + rooms.get(l).getNumChildren());
							}
						} else if (analysisType == 1) {		// rates analysis
							if (cancelled) {
								revenue += (reservationAL.get(k).getTotalCost() - reservationAL.get(k).getDeposit());
							} else {
								revenue += reservationAL.get(k).getTotalCost();
							}
						}
					}
				}
				startDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH) + 1);
			}
			if (analysisType == 0) {
				result += adults + "," + children;	
			} else {
				result += revenue;
			}
			result += "\n";			
		}
		
		// write to file
		try {	// incase it fails to open or read from the file
			String line = "";
			File analysisFile = new File(filename);
			
			FileWriter fw = new FileWriter(analysisFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("From,To,");
			
			if (analysisType == 0) {
				bw.write("Adults,Children\n");
			} else if (analysisType == 1) {
				bw.write("Revenue\n");
			}
			
			bw.write(result);
			
			bw.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private void checkOut() {
		boolean more = true;
		while (more) {
			int resIndex = intakeValidId();
					
			if (resIndex >= 0) {
				int numOfNights = reservationAL.get(resIndex).getNights();
				int checkOutNum = reservationAL.get(resIndex).getNumber();
				boolean valid, dateValid, numValid, checkedIn;
				currentDate = new GregorianCalendar();	// current date and time
				
				dateValid = numValid = valid = true;
				
				// get the departure date
				String[] temp = reservationAL.get(resIndex).getDate().split("/");
				
				Calendar depDate = new GregorianCalendar(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[0]));
				
				Calendar depDateMod = (Calendar) depDate.clone();
				depDateMod.set(Calendar.DAY_OF_MONTH, depDateMod.get(Calendar.DAY_OF_MONTH) + numOfNights);
				depDateMod.set(Calendar.HOUR_OF_DAY, 12);					// check out time is 12 o'clock
				
				
				if (currentDate.before(depDate)) {						// if current date is before the morning of dep date, then it's an invalid check out
					valid = false;
					dateValid = false;
				} else if (currentDate.after(depDateMod)) {		// if current date is after dep date, then it's an invalid check out
					valid = false;
					dateValid = false;
				} else if (!checkForCheckInDuplicate(checkOutNum)) { // check if this reservation was checked in
					valid = false;
					checkedIn = false;
				} else if (checkForCheckOutDuplicate(checkOutNum)) {
					valid = false;
					numValid = false;
				}
				
				// make a check-out
				if (valid) {
					String date = currentDate.get(Calendar.DAY_OF_MONTH) + "/" + (currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.YEAR);	// turns the Calendar date into a String date
					checkOutAL.add(new CheckOut(checkOutNum, date));							// add cancellation to the cancelAL
					checkOutFH.addCheckOut(checkOutAL.get(checkOutAL.size() - 1));	// write the cancellation to file
					System.out.println("Your check-out has been recorded.");
					
					more = false;
				} else {
					System.out.print("Invalid input. ");
					if (!dateValid) {
						System.out.println("You can only check-out on the morning of your departure.");
					} else if (!numValid) {
						System.out.println("Your check-out is already made.");
					}
				}
			} else {
				more = false;
			}
		}
	}
	
	private void checkIn() {
		boolean more = true;
		while (more) {
			int resIndex = intakeValidId();
					
			if (resIndex >= 0) {
				int numOfNights = reservationAL.get(resIndex).getNights();
				int checkInNum = reservationAL.get(resIndex).getNumber();
				boolean valid, dateValid, numValid, dateOld;
				currentDate = new GregorianCalendar();	// current date and time
				
				dateValid = numValid = valid = true;
				
				// get the reservations date
				String[] temp = reservationAL.get(resIndex).getDate().split("/");
				Calendar resDate = new GregorianCalendar(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[0]));
				
				Calendar resDateMod = (Calendar) resDate.clone();
				resDateMod.set(Calendar.DAY_OF_MONTH, resDateMod.get(Calendar.DAY_OF_MONTH) + numOfNights);
				
				if (currentDate.before(resDate)) {						// if current date is before res date, then it's an invalid check in
					valid = false;
					dateValid = false;
				} else if (currentDate.after(resDateMod)) {		// if it's after the departure date, then it's an invalid check in
					valid = false;
					dateValid = false;
				} else if (checkForCheckInDuplicate(checkInNum)) { // check if there's no identical check-in number
					valid = false;
					numValid = false;
				}
				
				// make a check-in
				if (valid) {
					String date = currentDate.get(Calendar.DAY_OF_MONTH) + "/" + (currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.YEAR);	// turns the Calendar date into a String date
					checkInAL.add(new CheckIn(checkInNum, date));		// add cancellation to the cancelAL
					checkInFH.addCheckIn(checkInAL.get(checkInAL.size() - 1));	// write the cancellation to file
					System.out.println("Your check-in has been recorded.");
					
					more = false;
				} else {
					System.out.print("Invalid input. ");
					if (!dateValid) {
						System.out.println("You can only check-in during your stay.");
					} else if (!numValid) {
						System.out.println("Your check-in is already made.");
					}
				}
			} else {
				more = false;
			}
		}
	}
	
	private void cancel() {
		boolean more = true;
		while (more) {
			int resIndex = intakeValidId();
					
			if (resIndex >= 0) {
				int cancelNum = reservationAL.get(resIndex).getNumber();
				boolean valid, dateValid, numValid;
				currentDate = new GregorianCalendar();
				
				dateValid = numValid = valid = true;
				
				// get the reservations date
				String[] temp = reservationAL.get(resIndex).getDate().split("/");
				Calendar resDateMod = new GregorianCalendar(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));
				
				// check if the current date is not older than the reservation date
				if (currentDate.after(resDateMod)) {
					valid = false;
					dateValid = false;
				}
				
				// check if there's no identical cancellation number
				if (checkForCancelDuplicate(cancelNum)) {
					valid = false;
					numValid = false;
				}
				
				// make a cancellation
				if (valid) {
					boolean refunded = false;
					resDateMod.set(Calendar.DAY_OF_MONTH, resDateMod.get(Calendar.DAY_OF_MONTH) - 2);
					
					// if cancellation date is 48 hrs before check-in and type is standard then refund the money
					if (currentDate.before(resDateMod) && (reservationAL.get(resIndex).getType().equals("S"))) {
						refunded = true;
						System.out.println("You cancelled your reservation and your money has been refunded.");
					} else {
						System.out.println("You cancelled your reservation.");
					}
					
					String date = currentDate.get(Calendar.DAY_OF_MONTH) + "/" + (currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.YEAR);	// turns the Calendar date into a String date
					cancelAL.add(new Cancellation(cancelNum, date, refunded));		// add cancellation to the cancelAL
					cancelFH.addCancellation(cancelAL.get(cancelAL.size() - 1));	// write the cancellation to file
					more = false;
				} else {
					System.out.print("Invalid reservation. ");
					if (!dateValid) {
						System.out.println("Your reservation is ongoing.");
					} else if (!numValid) {
						System.out.println("Your cancellation is already made.");
					}
				}
			} else {
				more = false;
			}
		}
	}
	
	/**
   * Intakes the appropriate information needed for a reservation and writes it to a file.
   * @param privileges which indicate if the user can set a discount.
   */
	public void reserve(int privileges) {
		int menuStage, nights, day, month, year, resNumber;
		boolean valid, more;
		double deposit, totalCost;
		String date, reservationType, hotelType, pattern, resName;
		Calendar arrivalDate = new GregorianCalendar();
		ArrayList<Room> roomAL = new ArrayList<Room>();
		menuStage = 0;
		nights = day = month = year = resNumber = 0;
		totalCost = 0;
		deposit = 0;
		more = true;
		reservationType = "";
		resName = "";
		hotelType = "";
		
		// Keep looping until all info is taken from the user
		while (more) {
			switch (menuStage) {
				case 0:	// get reservation type
					String[] customerTypes = {"S", "AP"};
					pattern = "[1-2]";
					String userInput;
					valid = false;
					
					System.out.println("What reservation type would you like to make?");
					for (int i = 0; i < customerTypes.length; i++) {
						System.out.println((i + 1) + ". " + customerTypes[i]);
					}
					
					userInput = in.nextLine();
					if (userInput.matches(pattern)) {
						menuStage++;
						reservationType = customerTypes[Integer.parseInt(userInput) - 1]; // place the appropriate customer type
					} else {
						System.out.println("Invalid input. Type in a number choice");
					}
					break;
				case 1:	// get arrival date 
					System.out.println("When is your arrival date? e.g. 20/12/2016");
					date = in.nextLine();
					valid = false;
					
					// validate date
					if (isValidDate(date)) {	// check if it's in the right format dd/mm/yyyy
						String[] dateString = date.split("/");
						day = Integer.parseInt(dateString[0]);
						month = Integer.parseInt(dateString[1]);
						year = Integer.parseInt(dateString[2]);
						arrivalDate = new GregorianCalendar(year, month - 1, day);
						currentDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH) - 1);
						if (currentDate.before(arrivalDate)) {	// check if the current date is before the arrival date
							
							valid = true;
						}
					} else {
						valid = false;
					}
					
					if (!valid) {
						System.out.println("Invalid date");
					} else {
						menuStage++;
					}
					break;
				case 2:	// get number of nights
					System.out.println("How many nights are you staying?");
					String nightsString = in.nextLine();
					pattern = "[0-9]{1,}";
					
					if (nightsString.matches(pattern)) {	// check if it's a number
						nights = Integer.parseInt(nightsString);
						if (nights >= 1) {
							menuStage++;	// proceed if it's at least 1 nights
						} else {
							System.out.println("Incorrect input. You must stay at least 1 night.");
						}
					}
					break;
				case 3:	// display table and let them pick a room
					readHotelsFile();
					pattern = "[1-" + hotelAL.size() + "]{1}";
					
					
			
					System.out.println("Which hotel would you like to stay at?");
					for (int i = 0; i < hotelAL.size(); i++) {	// print out the hotel star options
						System.out.println((i + 1) + ". " + hotelAL.get(i).getHotelType());
					}
					
					userInput = in.nextLine();	// user selects hotel
					if (userInput.matches(pattern)) {
						int hotelTableIndex;
						boolean firstTime, moreRooms;
						firstTime = true;
						moreRooms = true;
						valid = false;
						
						hotelTableIndex = Integer.parseInt(userInput) - 1;				// index in the table - 1 (for use in hotelAL to get the appropriate room info)
						hotelType = hotelAL.get(hotelTableIndex).getHotelType();	// stores the hotel type
						int tableIndex = Integer.parseInt(hotelType.substring(0,1));							// stores the star of the hotel type as a number
						
						pattern = "[1-" + hotelAL.get(hotelTableIndex).getHotelInfo().size() + "]{1}";	// pattern: between 1 and num of different room types
						
						// let the user select as many rooms as he wants until he enters -1 (must select at least 1)
						while (moreRooms) {
							boolean roomLeft = true;
							
							readHotelsFile();
							updateRoomAvailability(arrivalDate, nights, hotelTableIndex);
							printTable(tableIndex);		// displays the table
							
							if (firstTime) {	// first time the user is shown the message, he's not informed of -1 to quit
								System.out.println("Select which room you would like.");
							} else {
								System.out.println("Select which room you would like. Enter -1 to stop selecting rooms.");
							}
							
							userInput = in.nextLine();
							if (userInput.matches(pattern)) {
								String occAdultPatern, occChildPattern, secondaryUserInput;
								boolean breakfast, askMoreAboutRoom;
								int roomTableIndex, subMenuStage, minAdults, minChildren, maxAdults, maxChildren, occAdult, occChild;
								roomTableIndex = Integer.parseInt(userInput) - 1;
								subMenuStage = 0;
								occAdult = occChild = 0;
								askMoreAboutRoom = true;
								breakfast = false;
								ArrayList<String> currentRoomInfo = hotelAL.get(hotelTableIndex).getRoomInfo(roomTableIndex); // retrieve the selected room info								
								
								if(Integer.parseInt(currentRoomInfo.get(1)) > 0) {	// only allowed to choose this room if there's more than 0 Number of Rooms
									String[] temp = currentRoomInfo.get(2).split("\\+");	// split min children 
									minAdults = Integer.parseInt(temp[0]);
									minChildren = Integer.parseInt(temp[1]);
									
									temp = currentRoomInfo.get(3).split("\\+");	// split max children 
									maxAdults = Integer.parseInt(temp[0]);
									maxChildren = Integer.parseInt(temp[1]);
									
									occAdultPatern = "[" + minAdults + "-" + maxAdults + "]";
									occChildPattern = "[" + minChildren + "-" + maxChildren + "]";
									
									firstTime = false;
									
									while (askMoreAboutRoom) {
										if (subMenuStage == 0) {	// intake adult occupancy
											System.out.println("How many adults will be staying? Min is " + minAdults + ", Max is " + maxAdults);
											secondaryUserInput = in.nextLine();
											
											if (secondaryUserInput.matches(occAdultPatern)) {
												occAdult = Integer.parseInt(secondaryUserInput);
												
												subMenuStage++;
											} else {
												System.out.println("Incorrect input. Enter a number between " + minAdults + " and " + maxAdults);
											}
										} else if (subMenuStage == 1) {	// intake children occupancy
											if (maxChildren != 0) {	
												System.out.println("How many children will be staying? Min is " + minChildren + ", Max is " + maxChildren);
												
												secondaryUserInput = in.nextLine();
											
												if (secondaryUserInput.matches(occChildPattern)) {
													occChild = Integer.parseInt(secondaryUserInput);
													
													subMenuStage++;
												} else {
													System.out.println("Incorrect input. Enter a number between " + minChildren + " and " + maxChildren);
												}
											}	else {	// skips this stage automatically if children max limit is 0
												subMenuStage++;
											}
										}	else if (subMenuStage == 2) {
											System.out.println("Would you like breakfast included? 10 euro charge.");
											System.out.println("1. Yes\n2. No");
											
											secondaryUserInput = in.nextLine();
											if (secondaryUserInput.equals("1")) {
												breakfast = true;
												if (reservationType.equals("AP")) {
													totalCost += (10 * nights) * 0.95; // apply a 5% discount
												} else {
													totalCost += 10 * nights;
												}
												
												askMoreAboutRoom = false;
											} else if (secondaryUserInput.equals("2")) {
												breakfast = false;
												askMoreAboutRoom = false;
											} else {
												System.out.println("Incorrect input. Enter a number between 1 and 2");
											}
										}
									}
									
									double[] currentRates = new double[7];
									
									int dayOfWeek = arrivalDate.get(Calendar.DAY_OF_WEEK) - 2; // we get day of week index. minus 2, cause starts on sunday
									dayOfWeek = dayOfWeek == -1 ? 6 : dayOfWeek; // if -1 then it is Sunday. Sunday's index is 6
									
									// read in rates for the appropriate room
									for (int i = 4; i < currentRoomInfo.size(); i++) { // start at 4, cause that's where rates column begins
										currentRates[i - 4] = Integer.parseInt(currentRoomInfo.get(i));
									}
									
									// loop through rates as many nights the person is staying
									for (int i = 0; i < nights; i++) {
										if (reservationType.equals("AP")) {
											totalCost += 0.95 * currentRates[((dayOfWeek + i) % 7)]; // % 7, cause we want it to reset to 0 once it reaches 7. Apply 5% discount
										} else {
											totalCost += currentRates[((dayOfWeek + i) % 7)]; // % 7, cause we want it to reset to 0 once it reaches 7
										}
									}
									System.out.println("Cost so far is " + totalCost + " euros.");
									
									roomAL.add(new Room(currentRoomInfo.get(0), occAdult, occChild, breakfast));
								} else {
									System.out.println("Sorry, there are no more rooms of this type available. Please choose another room.");
								}
								
							}	else if (!firstTime && userInput.equals("-1")) {	// no more rooms
								moreRooms = false;
								menuStage++;
							} else {
								System.out.println("Invalid input. Type in a number choice.");
							}
						}
					} else {
						System.out.println("Invalid input. Type in a number choice");
					}
					
					break;
				case 4: // ask for Reservation Number
					pattern = "[0-9]{1,}";
					System.out.println("Enter your reservation number.");
					userInput = in.nextLine();
					
					if (userInput.matches(pattern)) {
						boolean unique = true;
						
						resNumber = Integer.parseInt(userInput); // check if duplicate doesn't exist
						
						if (checkForResDuplicate(resNumber)) {
							unique = false;
						}
						
						if (unique) {
							menuStage++;
						} else {
							System.out.println("This reservation number already exists.");
						}
						
					} else {
						System.out.println("Incorrect input. Enter a number please");
					}
					break;
				case 5: // ask for Reservation Name
					System.out.println("Enter your reservation name.");
					userInput = in.nextLine();
					resName = userInput;	// check if duplicate doesn't exist
					
					menuStage++;
					break;
				case 6:	// apply a discount
					if (privileges == 2) {
						pattern = "\\d+\\.?(\\d+)*";
						
						System.out.println("Your total cost is " + totalCost + " euros. Enter a discount. (0-100)");
						userInput = in.nextLine();
						
						if (userInput.matches(pattern)) {
							double tempDiscount = Double.parseDouble(userInput);
							if (tempDiscount >= 0.0 && tempDiscount <= 100.0) {
								totalCost *= (1 - (tempDiscount / 100));
								menuStage++;
							} else {
								System.out.println("Invalid input enter a number from 1 to 100");
							}
						} else {
							System.out.println("Invalid input enter a number from 1 to 100");
						}
					} else {
						menuStage++;
					}
					
					break;
				case 7:
					pattern = "\\d+\\.?(\\d+)*";
						
					System.out.println("Your total cost is " + totalCost + " euros. Enter a deposit.");
					userInput = in.nextLine();	// get the deposit
					
					if (userInput.matches(pattern)) {
						double tempDeposit = Double.parseDouble(userInput);
						if (tempDeposit >= 0.0 && tempDeposit <= totalCost) {	// make sure it's less than total cost but more than 0
							deposit = tempDeposit;
							menuStage++;	
							more = false;	// stop asking about the reservation
						} else {
							System.out.println("Invalid input. Enter a number from 0 to " + totalCost);
						}
					} else {
						System.out.println("Invalid input enter a number from 0 to " + totalCost);
					}
					break;
				default:	// if something goes wrong, brings back to step 1
					System.out.println("Error");
					menuStage = 0;
					break;
			}
		}
		String arrivalDateString = arrivalDate.get(Calendar.DAY_OF_MONTH) + "/" + (arrivalDate.get(Calendar.MONTH) + 1) + "/" + arrivalDate.get(Calendar.YEAR);
		
		reservationAL.add(new Reservation(hotelType, resNumber, resName, reservationType, nights, deposit, totalCost, arrivalDateString, roomAL));
		resFH.addReservation(reservationAL.get(reservationAL.size() - 1));	// adds the reservation to a file
		System.out.println("Your reservation has been recorded!");
	}
	
	// updates hotelAL num of rooms based on the arrival date of the user staying
	private void updateRoomAvailability(Calendar currArrDate, int currNights, int hotelTableIndex) {
		int currDepDay = currArrDate.get(Calendar.DAY_OF_MONTH) + currNights;
		
		// create the current departure date by adding 
		Calendar currDepDate = new GregorianCalendar(currArrDate.get(Calendar.YEAR), (currArrDate.get(Calendar.MONTH) + 1), currDepDay);
		
		for (int i = 0; i < reservationAL.size(); i++) {
			// conver the String into day, month, year
			String[] tempDate = reservationAL.get(i).getDate().split("/");
			int resArrDay, resArrMonth, resArrYear;
			
			resArrDay = Integer.parseInt(tempDate[0]);
			resArrMonth = Integer.parseInt(tempDate[1]);
			resArrYear = Integer.parseInt(tempDate[2]);
			
			// create a date for a previously made reservation
			Calendar resArrDate = new GregorianCalendar(resArrYear, resArrMonth, resArrDay);
			Calendar resDepDate = new GregorianCalendar(resArrYear, resArrMonth, resArrDay);
			
			// check if the hotels match and the date which is being used for current reservation is between any previous reservation dates
			//System.out.println(reservationAL.get(i).getHotel().equals(hotelAL.get(hotelTableIndex).getHotelType()));
			//System.out.println((resArrDate.after(currArrDate) && resArrDate.before(currDepDate)));
			//System.out.println((resDepDate.after(currArrDate) && resDepDate.before(currDepDate)));
			if (reservationAL.get(i).getHotel().equals(hotelAL.get(hotelTableIndex).getHotelType())
					&& ((resArrDate.after(currArrDate) && resArrDate.before(currDepDate))
					|| (resDepDate.after(currArrDate) && resDepDate.before(currDepDate)))) {
						ArrayList<Room> roomAL = reservationAL.get(i).getRooms();
						for (int j = 0; j < roomAL.size(); j++) {
							hotelAL.get(hotelTableIndex).reduceRoomAmount(roomAL.get(j).getRoomType());
						}
					}
		}
	}

	private void createReservationAL() {
		ArrayList<String> prevRes = resFH.readReservationsFile();
		Calendar oldDateThreshold = new GregorianCalendar();
		oldDateThreshold.set(Calendar.YEAR, oldDateThreshold.get(Calendar.YEAR) - 7);
		
		// read individual reservation info
		for(int i = 0; i < prevRes.size(); i++) {
			String[] temp = prevRes.get(i).split(",");
			String hotel, name, type, date;
			int number, numNights;
			double deposit, totalCost;
			boolean old = false;
			ArrayList<Room> roomAL = new ArrayList<Room>();
			
			hotel = temp[0];
			number = Integer.parseInt(temp[1]);
			name = temp[2];
			type = temp[3];
			numNights = Integer.parseInt(temp[4]);
			deposit = Double.parseDouble(temp[5]);
			totalCost = Double.parseDouble(temp[6]);
			date = temp[7];		// skips temp[7] because that's only needed for analysis
			
			// check if the date is older than 7 years
			String[] tempDate = date.split("/");
			Calendar resDate = new GregorianCalendar(Integer.parseInt(tempDate[2]), Integer.parseInt(tempDate[1]) - 1, Integer.parseInt(tempDate[0])); 
			
			// read individual room info
			String[] temp2 = temp[8].split("\\+");
			for (int j = 0; j < temp2.length; j++) {
				String[] temp3 = temp2[j].split("\\.");
				
				String roomType;
				int numAdults, numChildren;
				boolean breakfast;
				
				roomType = temp3[0];
				numAdults = Integer.parseInt(temp3[1]);
				numChildren = Integer.parseInt(temp3[2]);
				if (temp[3].equals("t")) {
					breakfast = true;
				} else {
					breakfast = false;
				}
				
				roomAL.add(new Room(roomType, numAdults, numChildren, breakfast));
			}
			
			// if the reservation date is older than 7 years don't add it
			if (resDate.after(oldDateThreshold)) {
				reservationAL.add(new Reservation(hotel, number, name, type, numNights, deposit, totalCost, date, roomAL));
			}
		}
		
		ArrayList<String> reservationStringAL = new ArrayList<String>();
		
		// put the reservationAL into a String ArrayList
		for (int i = 0; i < reservationAL.size(); i++) {
			reservationStringAL.add(reservationAL.get(i).toString());
		}
		resFH.updateReservationsFile(reservationStringAL);
	}
	
	private void createCancelAL() {
		ArrayList<String> prevCancel = cancelFH.readCancellationsFile();
		
		// read individual reservation info
		for(int i = 0; i < prevCancel.size(); i++) {
			String[] temp = prevCancel.get(i).split(",");
			
			String date = temp[1];
			int number = Integer.parseInt(temp[0]);
			boolean refunded = false;
			
			if (temp[2].equals("t")) {
				refunded = true;
			}
			
			cancelAL.add(new Cancellation(number, date, refunded));
		}
	}
	
	private void createCheckInAL() {
		ArrayList<String> prevCheckIn = checkInFH.readCheckInsFile();
		Calendar oldDateThreshold = new GregorianCalendar();
		oldDateThreshold.set(Calendar.YEAR, oldDateThreshold.get(Calendar.YEAR) - 7);
		
		// read individual reservation info
		for(int i = 0; i < prevCheckIn.size(); i++) {
			String[] temp = prevCheckIn.get(i).split(",");
			
			String date = temp[1];
			int number = Integer.parseInt(temp[0]);
			
			// check if the date is older than 7 years
			String[] tempDate = date.split("/");
			Calendar checkInDate = new GregorianCalendar(Integer.parseInt(tempDate[2]), Integer.parseInt(tempDate[1]) - 1, Integer.parseInt(tempDate[0])); 
			
			// if the reservation date is older than 7 years don't add it
			if (checkInDate.after(oldDateThreshold)) {
				checkInAL.add(new CheckIn(number, date));
			}
		}
	}
	
	private void createCheckOutAL() {
		ArrayList<String> prevCheckOut = checkOutFH.readCheckOutsFile();
		Calendar oldDateThreshold = new GregorianCalendar();
		oldDateThreshold.set(Calendar.YEAR, oldDateThreshold.get(Calendar.YEAR) - 7);
		
		// read individual reservation info
		for(int i = 0; i < prevCheckOut.size(); i++) {
			String[] temp = prevCheckOut.get(i).split(",");
			
			String date = temp[1];
			int number = Integer.parseInt(temp[0]);
			
			// check if the date is older than 7 years
			String[] tempDate = date.split("/");
			Calendar checkOutDate = new GregorianCalendar(Integer.parseInt(tempDate[2]), Integer.parseInt(tempDate[1]) - 1, Integer.parseInt(tempDate[0])); 
			
			// if the reservation date is older than 7 years don't add it
			if (checkOutDate.after(oldDateThreshold)) {
				checkOutAL.add(new CheckOut(number, date));
			}
		}
		
		ArrayList<String> checkOutStringAL = new ArrayList<String>();
		
		// put the checkOutAL into a String ArrayList
		for (int i = 0; i < checkOutAL.size(); i++) {
			checkOutStringAL.add(checkOutAL.get(i).toString());
		}
		checkOutFH.updateCheckOutFile(checkOutStringAL);
	}
	
	// reads from hotels file and puts the info into hotelAL
	private void readHotelsFile() {
		hotelAL.clear();
		tableHeadingsAL.clear();
		try {	// incase it fails to open or read from the file
			String line;
			int counter = 0;
			File hotelsFile = new File("l4Hotels.csv");
			BufferedReader br = new BufferedReader(new FileReader(hotelsFile));
			
			while ((line = br.readLine()) != null) { // read the info 
				if (counter >= 2) {	// if not headings
					String[] temp = line.split(",");
					if (temp[0].contains("star")) { // Create a new hotel and add the room info
						hotelAL.add(new Hotel(temp[0])); // create new #-star hotel
					}
					
					String[] roomTempInfo = new String[11]; // create an array without the "#-star" element
					for (int i = 1; i < temp.length; i++) {
						roomTempInfo[i - 1] = temp[i];	// reads only the room info without the "#-star"
					}
					hotelAL.get(hotelAL.size() - 1).addRoom(roomTempInfo); // adds room info to the latest added hotel
				} else { // if headings
					String[] temp = line.split(",");
					tableHeadingsAL.add(new ArrayList<String>()); 	// holds headings in 2D ArrayList
					for (int i = 0; i < temp.length; i++) {
						tableHeadingsAL.get(tableHeadingsAL.size() - 1).add(temp[i]);	// reads in headings
					}
					
					// change headings' name to make them shorter
					tableHeadingsAL.get(0).set(2, "No. of Rooms");
					tableHeadingsAL.get(0).set(3, "Occ. Min");
					tableHeadingsAL.get(0).set(4, "Occ. Max");
				}
				counter++;
			}
			br.close();
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private void printArrayList(ArrayList<String> al) {
		for (int i = 0; i < al.size(); i++) {
			System.out.println(i + "; " + al.get(i));
		}
	}
	
	private void printAll() {
		for (int i = 0; i < hotelAL.size(); i++) {
			System.out.println(hotelAL.get(i).getHotelType());
			ArrayList<ArrayList<String>> currentHotelAL = hotelAL.get(i).getHotelInfo();
			for (int j = 0; j < currentHotelAL.size(); j++) {
				for (int k = 0; k < currentHotelAL.get(j).size(); k++) {
					System.out.print(currentHotelAL.get(j).get(k) + "\t|");
				}
				System.out.println();
			}
		}
	}
	
	private void printTable(int star) {
		String starName = "" + star + "-star";
		int starIndex = 0;
		
		// find appropriate index for the hotel type
		for (int i = 0; i < hotelAL.size(); i++) {
			if (hotelAL.get(i).getHotelType().equals(starName)) {
				starIndex = i;
			}
		}
		
		// print headings
		for (int i = 0; i < tableHeadingsAL.size(); i++) {
			for (int j = 0; j < tableHeadingsAL.get(i).size(); j++) {
				if (j > 4) {
					System.out.printf("%-5s|", tableHeadingsAL.get(i).get(j));		// rates column
				} else if (j == 0 && i == 0) {
					System.out.printf("%-11s|", "Options");												// top left column
				} else if (j == 0) {
					System.out.printf("%-11s|", tableHeadingsAL.get(i).get(j));		// row 2, first column. It's empty
				} else if (j == 1) {
					System.out.printf("%-16s|", tableHeadingsAL.get(i).get(j));		// room type column
				} else {
					System.out.printf("%-13s|", tableHeadingsAL.get(i).get(j));		// every other columns
				}
			}
			System.out.println();
		}
		
		// print out the rooms
		ArrayList<ArrayList<String>> currentHotelAL = hotelAL.get(starIndex).getHotelInfo();
		for (int i = 0; i < currentHotelAL.size(); i++) {
			System.out.printf("%-11s|", ((i + 1) + "."));										// first column list option numbers
			for (int j = 0; j < currentHotelAL.get(i).size(); j++) {				// prints out rooms contents
				if (j >= 4) {
					System.out.printf("%-5s|", currentHotelAL.get(i).get(j));		// rates columns with prices for every day of the week
				} else if (j == 0) {
					System.out.printf("%-16s|", currentHotelAL.get(i).get(j));	// room type column
				} else {
					System.out.printf("%-13s|", currentHotelAL.get(i).get(j));	// every other column
				}
			}
			System.out.println();
		}
	}
	
	// returns true if the first element of the arraylist is equal to the number
	private boolean checkForCancelDuplicate(int number) {
		for (int i = 0; i < cancelAL.size(); i++) {
			if (cancelAL.get(i).equals(number)) {
				return true;
			}
		}
		
		return false;
	}
	
	// returns true if the first element of the arraylist is equal to the number
	private boolean checkForCheckInDuplicate(int number) {
		for (int i = 0; i < checkInAL.size(); i++) {
			if (checkInAL.get(i).equals(number)) {
				return true;
			}
		}
		
		return false;
	}
	
	// returns true if the first element of the arraylist is equal to the number
	private boolean checkForCheckOutDuplicate(int number) {
		for (int i = 0; i < checkOutAL.size(); i++) {
			if (checkOutAL.get(i).equals(number)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkForResDuplicate(int number) {
		for (int i = 0; i < reservationAL.size(); i++) {
			if (reservationAL.get(i).equals(number)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
   * Checks if the date is in correct format
   * @param inDate The date in a string format that is to be checked.
	 * @return The boolean if the date is in a valid format.
   */
	public boolean isValidDate(String inDate) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(inDate.trim());
    } catch (ParseException pe) {
      return false;
    }
    return true;
  }
}
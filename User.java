/**
 *The class User defines and stores the user info.
 *
 *@author Vilius Drumsta
 */
public class User {
	private int type;
  private String name;
  private String password;
	
	/**
   * Constructor to create a new user object.
   * @param   type The type of user(customer,desk-admin or supervisor).
   * @param   name The name of the user.
   * @param   password The password for the given user.
   */
	public User(int type, String name, String password) {
    this.type = type;
    this.name = name;
    this.password = password;
  }
	
	/**
   * This method returns the type of user.
   * @return type The type of user.
   */
	public int getType(){
    return type;
  }
	
	/**
   * This method returns the name of the user.
   * @return name The name of the user.
   */
	public String getName(){
    return name;
  }
	
	/**
   * This method returns the password of the user.
   * @return password The user's password.
   */
	public String getPassword(){
    return password;
  }
	
	/**
   * This method compares two user passwords.
   * @param  user The user that we are comparing with.
   * @return The boolean value of whether or not the two user passwords are equal.
   */
	public boolean equals(User user) {
		if (type == user.getType() && name.equals(user.getName()) && password.equals(user.getPassword())) {
			return true;
		} else {
			return false;
		}
	}
}
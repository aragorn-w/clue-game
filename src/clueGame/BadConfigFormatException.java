/*
 * Class: BadConfigFormatException
 *
 * Purpose: The BadConfigFormatException class is an exception that is thrown when the setup or layout config file is invalid.
 *
 * Responsibilities: This custom checked exception is responsible for throwing an exception when the setup or layout config file is invalid.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: March 25, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

public class BadConfigFormatException extends Exception {
    private static final long serialVersionUID = -8436274110449011956L;

	public BadConfigFormatException() {
        super("Either the setup or layout config file is invalid.");
    }

    public BadConfigFormatException(String message) {
        super(message);
    }
}
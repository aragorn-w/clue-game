/*
 * Enum: DoorDirection
 *
 * Purpose: The DoorDirection enum is used to represent the direction of a door on a board cell.
 *
 * Responsibilities: The DoorDirection enum is responsible for storing the possible directions of a door on a board cell and providing a method to get the direction of a door based on a character. The DoorDirection enum is also responsible for throwing an exception if the character is not a valid door direction. The DoorDirection enum is used by the BoardCell class to store the direction of a door on a board cell. The DoorDirection enum is also used by the Board class to load the board configuration from a file.
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

public enum DoorDirection {
	UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

	private final char value;

	DoorDirection(char value) {
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public static DoorDirection getDirection(char value) throws BadConfigFormatException {
		for (DoorDirection direction : DoorDirection.values()) {
			if (direction.value == value) {
				return direction;
			}
		}

		// If the character is not a valid door direction, throw an exception since the config file must be invalid
		throw new BadConfigFormatException("Invalid door direction");
	}
}
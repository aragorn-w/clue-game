/*
 * Enum: DoorDirection
 * 
 * Purpose: The DoorDirection enum is used to represent the direction of a door on a board cell.
 * 
 * Responsibilities: The DoorDirection enum is responsible for storing the possible directions of a door on a board cell and providing a method to get the direction of a door based on a character.
 * 
 * Authors: Aragorn Wang, Anya Streit
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
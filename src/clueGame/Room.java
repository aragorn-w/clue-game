/*
 * Class: Room
 *
 * Purpose: The Room class is used to store information about a room in the Clue game.
 *
 * Responsibilities: The Room class is responsible for storing the name of the room, the center cell of the room, and the label cell of the room.
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

public class Room {
	public static final char LABEL_MARKER = '#';
	public static final char CENTER_MARKER = '*';

	private final String name;

	private BoardCell centerCell;
	private BoardCell labelCell;

	public Room(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
}
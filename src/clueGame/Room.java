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

import java.awt.Font;
import java.awt.Graphics;

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

	public void drawLabel(Graphics graphics, int width, int height) {
		if (labelCell == null) {
			return;
		}

		int pixelCol = labelCell.getCol() * width + BoardPanel.ROOM_LABEL_TEXT_PADDING;
		int pixelRow = labelCell.getRow() * height + BoardPanel.ROOM_LABEL_FONT_PX_SIZE;

		graphics.setColor(BoardPanel.LABEL_TEXT_COLOR);

		Font oldFont = graphics.getFont();
		graphics.setFont(graphics.getFont().deriveFont(BoardPanel.ROOM_LABEL_FONT_PX_SIZE).deriveFont(Font.BOLD));
		graphics.drawString(name, pixelCol, pixelRow);
		graphics.setFont(oldFont);
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
/*
 * Class: BoardPanel
 *
 * Purpose: The BoardPanel class is a JPanel that represents the game board in the Clue game.
 *
 * Responsibilities: The BoardPanel class is responsible for displaying the game board and handling user interactions with the board.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 14, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	public static final Color
		ROOM_COLOR = Color.LIGHT_GRAY,
		WALKWAY_COLOR = Color.YELLOW,
		UNUSED_COLOR = Color.BLACK,
		LABEL_TEXT_COLOR = Color.BLUE,
		WALKWAY_CELL_BORDER_COLOR = Color.BLACK,
		DOORWAY_COLOR = Color.BLUE,
		TARGET_COLOR = Color.CYAN;
		
	
	public static final int
		DOORWAY_THICKNESS_CELL_PERCENT = 20,
		ROOM_LABEL_FONT_PX_SIZE = 14,
		ROOM_LABEL_TEXT_PADDING = 5;

	public BoardPanel() {
		super();

	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		int cellWidth = getWidth() / Board.getInstance().getNumColumns();
		int cellHeight = getHeight() / Board.getInstance().getNumRows();

		for (int row = 0; row < Board.getInstance().getNumRows(); row++) {
			for (int col = 0; col < Board.getInstance().getNumColumns(); col++) {
				BoardCell cell = Board.getInstance().getCell(row, col);
				cell.draw(graphics, cellWidth, cellHeight);
			}
		}

		for (int row = 0; row < Board.getInstance().getNumRows(); row++) {
			for (int col = 0; col < Board.getInstance().getNumColumns(); col++) {
				BoardCell cell = Board.getInstance().getCell(row, col);
				cell.drawDoorway(graphics, cellWidth, cellHeight);
			}
		}

		for (Room room : Board.getInstance().getRooms()) {
			room.drawLabel(graphics, cellWidth, cellHeight);
		}

		for (Player player : Board.getInstance().getPlayers()) {
			player.draw(graphics, cellWidth, cellHeight);
		}
	}
}
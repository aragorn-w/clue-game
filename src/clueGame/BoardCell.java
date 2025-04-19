/*
 * Class: BoardCell
 *
 * Purpose: The BoardCell class represents a single cell on the game board. It contains information about the cell's
 *
 * Responsibilities: The BoardCell class is responsible for storing information about a single cell on the game board, including its row and column, the initial of the room it is in, whether it is a doorway, the direction of the doorway, whether it is a room label, whether it is a room center, the secret passage in the room, and the cells adjacent to it. It is also responsible for providing access to this information.
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

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private final int row;
	private final int col;

	private final char initial;

	private boolean
		isRoomLabel,
		isRoomCenter,
		isWalkway,
		isRoom,
		isOccupied;
	
	private char secretPassage;

	private DoorDirection doorDirection;

	private final Set<BoardCell> adjList;

	public BoardCell(int row, int col, char initial) throws BadConfigFormatException {
		super();
		this.row = row;
		this.col = col;
		this.initial = initial;
		this.adjList = new HashSet<>();
	}

	public void draw(Graphics graphics, int width, int height) {
		int pixelCol = col * width;
		int pixelRow = row * height;

		if (isRoom) {
			if (!ClueGame.getInstance().getHumanTurnFinished() && Board.getInstance().getTargets().contains(this)) {
				graphics.setColor(BoardPanel.TARGET_COLOR);
				graphics.fillRect(pixelCol, pixelRow, width, height);
			} else {
				graphics.setColor(BoardPanel.ROOM_COLOR);
				graphics.fillRect(pixelCol, pixelRow, width, height);
			}
			
		} else if (isWalkway) {
			
			if (!ClueGame.getInstance().getHumanTurnFinished() && Board.getInstance().getTargets().contains(this)) {
				graphics.setColor(BoardPanel.TARGET_COLOR);
				graphics.fillRect(pixelCol, pixelRow, width, height);
			} else {
				graphics.setColor(BoardPanel.WALKWAY_COLOR);
				graphics.fillRect(pixelCol, pixelRow, width, height);
			}

			graphics.setColor(BoardPanel.WALKWAY_CELL_BORDER_COLOR);
			graphics.drawRect(pixelCol, pixelRow, width, height);
		}
	}

	public void drawDoorway(Graphics graphics, int width, int height) {
		if (doorDirection == null) {
			return;
		}

		int pixelCol = col * width;
		int pixelRow = row * height;

		graphics.setColor(BoardPanel.DOORWAY_COLOR);

		int doorwayThickness = (int) (width * BoardPanel.DOORWAY_THICKNESS_CELL_PERCENT / 100.0);
		switch (doorDirection) {
			case UP -> {
				graphics.fillRect(pixelCol, pixelRow - doorwayThickness, width, doorwayThickness);
			}
			case RIGHT -> {
				graphics.fillRect(pixelCol + width, pixelRow, doorwayThickness, height);
			}
			case DOWN -> {
				graphics.fillRect(pixelCol, pixelRow + height, width, doorwayThickness);
			}
			case LEFT -> {
				graphics.fillRect(pixelCol - doorwayThickness, pixelRow, doorwayThickness, height);
			}
			default -> {
				throw new IllegalStateException("Unexpected value: " + doorDirection);
			}
		}
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public char getInitial() {
		return initial;
	}

	public boolean isDoorway() {
		return doorDirection != null;
	}

	public boolean isRoomLabel() {
		return isRoomLabel;
	}

	public void setIsRoomLabel(boolean isRoomLabel) {
		this.isRoomLabel = isRoomLabel;
	}

	public boolean isRoomCenter() {
		return isRoomCenter;
	}

	public void setIsRoomCenter(boolean isRoomCenter) {
		this.isRoomCenter = isRoomCenter;
	}

	public boolean isWalkway() {
		return isWalkway;
	}

	public void setIsWalkway(boolean isWalkway) {
		this.isWalkway = isWalkway;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setIsRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public boolean isSecretPassage() {
		return secretPassage != 0;
	}

	public char getSecretPassage() {
		return secretPassage;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public void addAdj(BoardCell cell) {
		adjList.add(cell);
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}
}
/*
 * Class: TestBoardCell
 *
 * Purpose: This class is used to create a cell for the board. It is used to calculate the targets for the player to move to.
 *
 * Responsibilites: TestBoardCell is responsible for representing a cell on the board and whether or not it is a room and whether it is occupied.
 * 
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 5, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
    @SuppressWarnings("unused")
	private final int row;
	@SuppressWarnings("unused")
	private final int col;

	private final Set<TestBoardCell> adjCells = new HashSet<>();

	private boolean isRoom = false;
	private boolean occupied = false;

	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public void addAdjacency(TestBoardCell cell) {
		adjCells.add(cell);
	}

	public Set<TestBoardCell> getAdjList() {
		return adjCells;
	}

	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isOccupied() {
		return occupied;
	}
}
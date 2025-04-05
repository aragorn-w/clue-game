/*
 * Class: TestBoard
 *
 * Purpose: This class is used to create a board for the game. It is used to calculate the targets for the player to move to.
 *
 * Responsibilites: TestBoard is responsible for creating the board, noting each cell's adjacency list, and calculating the targets for the player to move to.
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestBoard {
	public final static int NUM_ROWS = 4;
	public final static int NUM_COLS = 4;

	private final List<List<TestBoardCell>> grid;

	private Set<TestBoardCell> targets;

	private Set<TestBoardCell> visited;

	public TestBoard() {
		super();

		grid = new ArrayList<>();

	    for (int row = 0; row < NUM_ROWS; row++) {
	        List<TestBoardCell> gridRow = new ArrayList<>();
	        for (int col = 0; col < NUM_COLS; col++) {
	            gridRow.add(new TestBoardCell(row, col));  // Assuming TestBoardCell has a constructor
	        }
	        grid.add(gridRow);
	    }

		// Calculate each cell's adjacency list
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int col = 0; col < NUM_COLS; col++) {
				TestBoardCell cell = grid.get(row).get(col);
				if (row > 0) {
					cell.addAdjacency(grid.get(row - 1).get(col));
				}
				if (row < NUM_ROWS - 1) {
					cell.addAdjacency(grid.get(row + 1).get(col));
				}
				if (col > 0) {
					cell.addAdjacency(grid.get(row).get(col - 1));
				}
				if (col < NUM_COLS - 1) {
					cell.addAdjacency(grid.get(row).get(col + 1));
				}
			}
		}
	}

	public TestBoardCell getCell(int row, int col) {
		return grid.get(row).get(col);
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}

	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visited = new HashSet<>();
		targets = new HashSet<>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}

	private void findAllTargets(TestBoardCell startCell, int pathLength) {
		for (TestBoardCell adjCell: startCell.getAdjList()) {
			// If the cell is a room, add it to the targets and return early since the player can't move through rooms
			if (startCell.isRoom()) {
				targets.add(startCell);
				return;
			}

			if (visited.contains(adjCell)) {
				continue;
			}

			visited.add(adjCell);

			// We only add the cell to the targets if it is not occupied since the player can't move to or through an occupied cell
			if (!adjCell.isOccupied()) {
				if (pathLength == 1) {
					targets.add(adjCell);
				} else {
					findAllTargets(adjCell, pathLength - 1);
				}
			}

			visited.remove(adjCell);
		}
	}
}
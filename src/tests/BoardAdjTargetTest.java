/*
 * Class: BoardAdjTargetTest
 *
 * Purpose: Tests adjacency lists and target lists from the actual game board
 *
 * Responsibilities: Inits board from data files, and sets up board to test various edge cases then resets board to inital state
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 5, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package tests;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
	}

	// test adjacencies from room center
	// this includes test cases for normal rooms, rooms with multiple doors, and rooms with secret passages
	// These cells are light orange on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms() {
		// test room with two doorways and secret passage
		Set<BoardCell> testList = board.getAdjList(18, 2);
		assertEquals(3, testList.size());
		// doors from room
		assertTrue(testList.contains(board.getCell(17, 2)));
		assertTrue(testList.contains(board.getCell(18, 6)));
		// secret passage
		assertTrue(testList.contains(board.getCell(1, 23)));

		// test room with 1 door and secret passage
		testList = board.getAdjList(1, 23);
		assertEquals(2, testList.size());
		// door from room
		assertTrue(testList.contains(board.getCell(2, 23)));
		// secret passage
		assertTrue(testList.contains(board.getCell(18, 2)));

		// test room with 1 door
		testList = board.getAdjList(18, 24);
		assertEquals(1, testList.size());
		// door from room
		assertTrue(testList.contains(board.getCell(17, 24)));
	}

	// Test door locations include room location and walkways
	// These cells are dark orange on the planning spreadsheet
	@Test
	public void testAdjacencyDoor() {
		// doorway with 4 adjacencies
		Set<BoardCell> testList = board.getAdjList(2, 10);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(1, 10)));
		assertTrue(testList.contains(board.getCell(2, 9)));
		assertTrue(testList.contains(board.getCell(2, 11)));
		assertTrue(testList.contains(board.getCell(3, 10)));

		// doorway adjacent to room and empty cell
		// this also tests door not directly adjacent to room center
		testList = board.getAdjList(18, 13);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(18, 17)));
		assertTrue(testList.contains(board.getCell(17, 13)));

		// doorway with 4 adjacencies
		testList = board.getAdjList(2, 23);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(2, 22)));
		assertTrue(testList.contains(board.getCell(2, 24)));
		assertTrue(testList.contains(board.getCell(1, 23)));
		assertTrue(testList.contains(board.getCell(3, 23)));
	}

	// Test walkway adjacencies with a few edge cases
	// These cells are dark blue on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways() {
		// test bottom of board, 1 adjacency
		Set<BoardCell> testList = board.getAdjList(19, 20);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(18, 20)));

		// test near door but not adjacent
		testList = board.getAdjList(16, 11);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 10)));
		assertTrue(testList.contains(board.getCell(16, 12)));
		assertTrue(testList.contains(board.getCell(15, 11)));
		assertTrue(testList.contains(board.getCell(17, 11)));

		// test adjacent to door
		testList = board.getAdjList(16, 2);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 1)));
		assertTrue(testList.contains(board.getCell(16, 3)));
		assertTrue(testList.contains(board.getCell(15, 2)));
		assertTrue(testList.contains(board.getCell(17, 2)));

		// test next to empty space
		testList = board.getAdjList(9, 10);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(8, 10)));
		assertTrue(testList.contains(board.getCell(10, 10)));
		assertTrue(testList.contains(board.getCell(9, 9)));
	}

	// test coming out of room with varying roll amounts
	// These cells are light blue on the planning spreadsheet
	@Test
	public void testTargetsInLibrary() {
		// roll = 1
		board.calcTargets(board.getCell(9, 27), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(9, 24)));

		// roll = 2
		board.calcTargets(board.getCell(9, 27), 2);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(8, 24)));
		assertTrue(targets.contains(board.getCell(10, 24)));
		assertTrue(targets.contains(board.getCell(9, 23)));

		// roll = 3
		board.calcTargets(board.getCell(9, 27), 3);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(8, 25)));
		assertTrue(targets.contains(board.getCell(7, 24)));
		assertTrue(targets.contains(board.getCell(8, 23)));
		assertTrue(targets.contains(board.getCell(9, 22)));
		assertTrue(targets.contains(board.getCell(10, 23)));
		assertTrue(targets.contains(board.getCell(11, 24)));
	}

	// test room with multiple doors
	// These cells are light blue on the planning spreadsheet
	@Test
	public void testTargetsInCurbRoom() {
		// roll = 1
		board.calcTargets(board.getCell(18, 17), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(18, 13)));
		assertTrue(targets.contains(board.getCell(17, 17)));

		// roll = 2
		board.calcTargets(board.getCell(18, 17), 2);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(17, 13)));
		assertTrue(targets.contains(board.getCell(17, 16)));
		assertTrue(targets.contains(board.getCell(17, 18)));
		assertTrue(targets.contains(board.getCell(16, 17)));

		// roll = 3
		board.calcTargets(board.getCell(18, 17), 3);
		targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(17, 12)));
		assertTrue(targets.contains(board.getCell(16, 13)));
		assertTrue(targets.contains(board.getCell(17, 15)));
		assertTrue(targets.contains(board.getCell(15, 17)));
		assertTrue(targets.contains(board.getCell(16, 18)));
	}

	// tests targets from door, also sees if room is an option
	// These cells are light blue on the planning spreadsheet
	@Test
	public void testTargetsInDoor() {
		// roll = 1
		board.calcTargets(board.getCell(2, 17), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1, 17)));
		assertTrue(targets.contains(board.getCell(2, 16)));
		assertTrue(targets.contains(board.getCell(2, 18)));
		assertTrue(targets.contains(board.getCell(3, 17)));

		// roll = 2
		board.calcTargets(board.getCell(2, 17), 2);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(1, 17)));
		assertTrue(targets.contains(board.getCell(2, 15)));
		assertTrue(targets.contains(board.getCell(4, 17)));
		assertTrue(targets.contains(board.getCell(2, 19)));
		assertTrue(targets.contains(board.getCell(3, 18)));
	}

	// test targets from walkway
	// These cells are light blue on the planning spreadsheet
	@Test
	public void testTargetsInWalkway1() {
		// roll = 1
		board.calcTargets(board.getCell(0, 7), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(0, 6)));

		// roll = 3
		board.calcTargets(board.getCell(0, 7), 3);
		targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(1, 5)));
		assertTrue(targets.contains(board.getCell(2, 6)));

		// roll = 4
		board.calcTargets(board.getCell(0, 7), 4);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 5)));
		assertTrue(targets.contains(board.getCell(3, 6)));
		assertTrue(targets.contains(board.getCell(2, 7)));
	}

	// test walkway in the open
	// These cells are light blue on the planning spreadsheet
	@Test
	public void testTargetsInWalkway2() {
		// roll = 1
		board.calcTargets(board.getCell(8, 4), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(8, 5)));
		assertTrue(targets.contains(board.getCell(8, 3)));
		assertTrue(targets.contains(board.getCell(9, 4)));
		assertTrue(targets.contains(board.getCell(7, 4)));

		// roll = 2
		board.calcTargets(board.getCell(8, 4), 2);
		targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(8, 2)));
		assertTrue(targets.contains(board.getCell(8, 6)));
		assertTrue(targets.contains(board.getCell(6, 4)));
		assertTrue(targets.contains(board.getCell(10, 4)));
		assertTrue(targets.contains(board.getCell(9, 3)));

		// roll = 3
		board.calcTargets(board.getCell(8, 4), 3);
		targets = board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(8, 1)));
		assertTrue(targets.contains(board.getCell(8, 7)));
		assertTrue(targets.contains(board.getCell(5, 4)));
		assertTrue(targets.contains(board.getCell(11, 4)));
		assertTrue(targets.contains(board.getCell(10, 3)));
		assertTrue(targets.contains(board.getCell(7, 6)));
		assertTrue(targets.contains(board.getCell(8, 5)));
		assertTrue(targets.contains(board.getCell(8, 3)));
	}

	// test walkway target when
	// These cells are light blue on the planning spreadsheet
	// blocked cells are red on the planning sheet
	@Test
	public void testTargetsOccupied() {
		// roll = 2, blocked 1 down
		board.getCell(8, 20).setOccupied(true);
		board.calcTargets(board.getCell(7, 20), 2);
		board.getCell(8, 20).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(7, 18)));
		assertTrue(targets.contains(board.getCell(6, 19)));
		assertTrue(targets.contains(board.getCell(7, 22	)));
		assertTrue(targets.contains(board.getCell(8, 19)));
		assertFalse(targets.contains(board.getCell(9, 20)));

		// roll = 1, test room blocked
		// we still want to be able to get into the room
		board.getCell(1, 17).setOccupied(true);
		board.calcTargets(board.getCell(2, 17), 1);
		board.getCell(1, 17).setOccupied(false);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(2, 16)));
		assertTrue(targets.contains(board.getCell(2, 18)));
		assertTrue(targets.contains(board.getCell(1, 17)));
		assertTrue(targets.contains(board.getCell(3, 17)));

		// check leaving a room with a blocked doorway
		// this one has no possible moves since only doorway is blocked
		board.getCell(17, 10).setOccupied(true);
		board.calcTargets(board.getCell(18, 10), 3);
		board.getCell(17, 10).setOccupied(false);
		targets = board.getTargets();
		assertEquals(0, targets.size());
	}
}
/*
 * Authors: Aragorn Wang, Anya Streit
 *
 * Class: BoardTestsExp
 *
 * Purpose: This class tests the adjacency and target generation of the TestBoard class.
 *
 * Responsibilites: BoardTestsExp tests the adjacency and target generation of the TestBoard class by creating a TestBoard object and testing the adjacency and target generation of the cells on the board.
 */

package tests;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

public class BoardTestsExp {
	private static TestBoard board;

	@BeforeAll
	public static void setUp() {
		board = new TestBoard();
	}

	@Test
	public void testTopLeftAdj() {
		// Test Top Left Corner (4x4)
		TestBoardCell testBoardCell = board.getCell(0, 0);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		assertTrue(adj.contains(board.getCell(0, 1)));
		assertTrue(adj.contains(board.getCell(1, 0)));
		assertEquals(2, adj.size());
	}

	@Test
	public void testBottomRightAdj() {
		// Test Bottom Right Corner
		TestBoardCell testBoardCell = board.getCell(3, 3);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		assertTrue(adj.contains(board.getCell(2, 3)));
		assertTrue(adj.contains(board.getCell(3, 2)));
		assertEquals(2, adj.size());
	}

	@Test
	public void testLeftSideAdj() {
		// Left side
		TestBoardCell testBoardCell = board.getCell(2, 0);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		assertTrue(adj.contains(board.getCell(1, 0)));
		assertTrue(adj.contains(board.getCell(2, 1)));
		assertTrue(adj.contains(board.getCell(3, 0)));
		assertEquals(3, adj.size());
	}

	@Test
	public void testRightSideAdj() {
		// Right side
		TestBoardCell testBoardCell = board.getCell(2, 3);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		assertTrue(adj.contains(board.getCell(1, 3)));
		assertTrue(adj.contains(board.getCell(2, 2)));
		assertTrue(adj.contains(board.getCell(3, 3)));
		assertEquals(3, adj.size());
	}

	@Test
	public void testMiddleAdj() {
		TestBoardCell testBoardCell = board.getCell(2, 2);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		assertTrue(adj.contains(board.getCell(2, 3)));
		assertTrue(adj.contains(board.getCell(3, 2)));
		assertTrue(adj.contains(board.getCell(2, 1)));
		assertTrue(adj.contains(board.getCell(1, 2)));
		assertEquals(4, adj.size());
	}

	@Test
	public void testMakeTargetsNormal() {
		// test normal targets with a roll of 2.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertEquals(3, targets.size());
	}

	@Test
	public void testMakeTargetsSpecial() {
		// test targets when there is a room and an occupied cell
		// this should be 1 less than the previous since one of the cells is occupied and the other is a room (a valid target)
		board.getCell(2, 0).setOccupied(true);
		board.getCell(0, 2).setRoom(true);
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertEquals(2, targets.size());
	}

	@Test
	public void testOneStep() {
		// test normal targets with a roll of 1.
		TestBoardCell testBoardCell = board.getCell(3, 3);
		board.calcTargets(testBoardCell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertEquals(2, targets.size());
	}

	@Test
	public void testThreeSteps() {
		// test normal targets with a roll of 3.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertEquals(6, targets.size());
	}

	@Test
	public void testMaxRoll() {
		// test normal targets with a roll of 6.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertEquals(7, targets.size());
	}
}
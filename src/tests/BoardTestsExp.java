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

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import experiment.TestBoard;
import experiment.TestBoardCell;

public class BoardTestsExp {
	private TestBoard board;
	
	@Before
	public void setUp() {
		board = new TestBoard();
	}
	
	@Test
	public void testTopLeftAdj() {
		// Test Top Left Corner (4x4)
		TestBoardCell testBoardCell = board.getCell(0, 0);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		Assert.assertTrue(adj.contains(board.getCell(0, 1)));
		Assert.assertTrue(adj.contains(board.getCell(1, 0)));
		Assert.assertEquals(2, adj.size());
	}
	
	@Test
	public void testBottomRightAdj() {
		// Test Bottom Right Corner
		TestBoardCell testBoardCell = board.getCell(3, 3);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		Assert.assertTrue(adj.contains(board.getCell(2, 3)));
		Assert.assertTrue(adj.contains(board.getCell(3, 2)));
		Assert.assertEquals(2, adj.size());
	}

	@Test
	public void testLeftSideAdj() {
		// Left side
		TestBoardCell testBoardCell = board.getCell(2, 0);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		Assert.assertTrue(adj.contains(board.getCell(1, 0)));
		Assert.assertTrue(adj.contains(board.getCell(2, 1)));
		Assert.assertTrue(adj.contains(board.getCell(3, 0)));
		Assert.assertEquals(3, adj.size());	
	}

	@Test
	public void testRightSideAdj() {
		// Right side
		TestBoardCell testBoardCell = board.getCell(2, 3);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		Assert.assertTrue(adj.contains(board.getCell(1, 3)));
		Assert.assertTrue(adj.contains(board.getCell(2, 2)));
		Assert.assertTrue(adj.contains(board.getCell(3, 3)));
		Assert.assertEquals(3, adj.size());
	}

	@Test
	public void testMiddleAdj() {
		TestBoardCell testBoardCell = board.getCell(2, 2);
		Set<TestBoardCell> adj = testBoardCell.getAdjList();
		Assert.assertTrue(adj.contains(board.getCell(2, 3)));
		Assert.assertTrue(adj.contains(board.getCell(3, 2)));
		Assert.assertTrue(adj.contains(board.getCell(2, 1)));
		Assert.assertTrue(adj.contains(board.getCell(1, 2)));
		Assert.assertEquals(4, adj.size());
	}
	
	@Test
	public void testMakeTargetsNormal() {
		// test normal targets with a roll of 2.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertEquals(3, targets.size());
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
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertEquals(2, targets.size());
	}
	
	@Test
	public void testOneStep() {
		// test normal targets with a roll of 1.
		TestBoardCell testBoardCell = board.getCell(3, 3);
		board.calcTargets(testBoardCell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(2, targets.size());
	}

	@Test
	public void testThreeSteps() {
		// test normal targets with a roll of 3.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertEquals(6, targets.size());
	}
	
	@Test
	public void testMaxRoll() {
		// test normal targets with a roll of 6.
		TestBoardCell testBoardCell = board.getCell(0, 0);
		board.calcTargets(testBoardCell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertEquals(7, targets.size());
	}
}
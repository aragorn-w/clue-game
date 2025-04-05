/*
 * Class: GameSetupTests
 *
 * Purpose: This class will test that the players and cards are setup correctly.
 *
 * Responsibilities: This class ensures people and weapons are loaded properly, Player is setup properly, the deck of cards is setup properly. 
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Player;
import clueGame.Solution;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.CardType;

public class GameSetupTests {
	// Same constants as before
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 30;

	public static final int ANSWER_SIZE = 3;
	public static final int DECK_SIZE = 21;

	public static final int NUM_PLAYERS = 6;
	public static final int NUM_WEAPONS = 6;
	public static final int NUM_ROOMS = 9;

	private static Board board;
		
	// Same setUp as FileInitTests
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.dealCards();
	}
	
	@Test
	public void testPlayers() {
		// Make sure players ArrayList size is correct
		assertEquals(NUM_PLAYERS, board.getPlayers().size());
		
		Player human = board.getPlayers().get(0);
		Player computer = board.getPlayers().get(1);
		
		// Test name + colors assignment
		assertEquals("Hans Wolfeschlegelsteinhausenbergerdorff", human.getName());
		assertEquals("Rick Roelle", computer.getName());
		assertEquals(new Color(184, 143, 64), human.getColor());
		
		// Test start location
		assertEquals(0, human.getRow());
		assertEquals(7, human.getColumn());
		
		// Test type of players
		assertTrue(human instanceof HumanPlayer);
		assertTrue(computer instanceof ComputerPlayer);
	}
	
	@Test
	public void testPlayerCards() {
		// Ensure player card count is in the expected range and make sure all cards were dealt properly
		int numCardsDealt = 0;
		int minHandSize = (int) Math.floor((DECK_SIZE - ANSWER_SIZE) / NUM_PLAYERS);
		int maxHandSize = (int) Math.ceil((DECK_SIZE - ANSWER_SIZE) / NUM_PLAYERS);
		for (Player player : board.getPlayers()) {
			assertTrue(player.getHand().size() >= minHandSize && player.getHand().size() <= maxHandSize);
			numCardsDealt += player.getHand().size();
		}

		assertEquals(DECK_SIZE - ANSWER_SIZE, numCardsDealt);
	}
	
	@Test
	public void testAnswerDeals() {
		Solution solution = board.getTheAnswer();
		// Ensure card types are correct 
		assertEquals(CardType.ROOM, solution.getRoomCard().getType());
		assertEquals(CardType.PERSON, solution.getPersonCard().getType());
		assertEquals(CardType.WEAPON, solution.getWeaponCard().getType());
	}
}
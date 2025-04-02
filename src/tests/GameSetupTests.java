/*
 * Class: GameSetupTests
 *
 * Purpose: This class will test that the players and cards are setup correctly.
 *
 * Responsibilities: This class ensures people and weapons are loaded properly, Player is setup properly, the deck of cards is setup properly. 
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 1, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

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
	public static final int NUM_CARDS = 21;

	private static Board board;
	
	private static ArrayList<Player> players;
	
	// Same setUp as FileInitTests
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		players = board.getPlayers();
	}
	
	@Test
	public void testPlayers() {
		// Make sure players ArrayList size is correct
		assertEquals(6, players.size());
		
		Player human = players.get(0);
		Player computer = players.get(1);
		
		// Test name + colors assignment
		assertEquals("Hans Wolfeschlegelsteinhausenbergerdorff", human.getName());
		assertEquals("Rick Roelle", computer.getName());
		assertEquals(new Color(184, 143, 64), human.getColor());
		
		// Test start location
		assertEquals(0, human.getRow());
		assertEquals(7, human.getCol());
		
		// Test type of players
		assertTrue(human instanceof HumanPlayer);
		assertTrue(computer instanceof ComputerPlayer);
	}
	
	@Test
	public void testPlayerCards() {
		// Ensure player card count is in the expected range and make sure all cards were dealt properly
		int cardSum = 0;
		for (Player player : players) {
			assertTrue(player.getCards().size() > 2 && player.getCards().size() < 5);
			cardSum += player.getCards().size();
		}
		// - 3 to account for answer cards
		assertEquals(NUM_CARDS - 3, cardSum);
	}
	
	@Test
	public void testAnswerDeals() {
		Solution solution = board.getSolution();
		// ensure card types are correct 
		assertEquals(CardType.ROOM, solution.getRoomCard().getType());
		assertEquals(CardType.PERSON, solution.getPersonCard().getType());
		assertEquals(CardType.WEAPON, solution.getWeaponCard().getType());
	}
}
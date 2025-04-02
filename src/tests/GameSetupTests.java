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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

public class GameSetupTests {
	// Same constants as before
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 30;

	private static Board board;
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
}
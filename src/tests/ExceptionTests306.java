/*
 * Class: ExceptionTests306
 *
 * Purpose: This class tests that exceptions are thrown appropriately when loading config files.
 *
 * Responsibilites: ExceptionTests tests that exceptions are thrown when loading config files that are not formatted correctly by asserting that the number of columns in the layout file is consistent, that the room specified in the layout file is in the legend, and that the setup file is formatted correctly.
 *
 * Authors: Spring 2025 CSCI 306 Staff
 * 
 * Date Last Edited: April 5, 2025
 * 
 * Collaborators: Aragorn Wang, Anya Streit
 * 
 * Sources: None
 */

package tests;

/*
 * This program tests that, when loading config files, exceptions
 * are thrown appropriately.
 */

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;

public class ExceptionTests306 {
	// Test that an exception is thrown for a layout file that does not
	// have the same number of columns for each row
	@Test
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			// Note that we are using a LOCAL Board variable, because each
			// test will load different files
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayoutBadColumns306.csv", "data/ClueSetup306.txt");
			// Instead of initialize, we call the two load functions directly.
			// This is necessary because initialize contains a try-catch.
			board.loadSetupConfig();
			// This one should throw an exception
			board.loadLayoutConfig();
		});
	}

	// Test that an exception is thrown for a Layout file that specifies
	// a room that is not in the legend.
	@Test
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayoutBadRoom306.csv", "data/ClueSetup306.txt");
			board.loadSetupConfig();
			board.loadLayoutConfig();
		});
	}

	// Test that an exception is thrown for a bad format Setup file
	@Test
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayout306.csv", "data/ClueSetupBadFormat306.txt");
			board.loadSetupConfig();
			board.loadLayoutConfig();
		});
	}
}
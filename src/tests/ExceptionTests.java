/*
 * Class: ExceptionTests
 *
 * Purpose: This class tests that exceptions are thrown appropriately when loading config files.
 *
 * Responsibilites: ExceptionTests tests that exceptions are thrown when loading config files that are not formatted correctly by asserting that the number of columns in the layout file is consistent, that the room specified in the layout file is in the legend, and that the setup file is formatted correctly.
 *
 * Authors: Aragorn Wang, Anya Streit
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

public class ExceptionTests {
	// Test that an exception is thrown for a layout file that does not
	// have the same number of columns for each row (rightmost column is deleted)
	@Test
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			// Note that we are using a LOCAL Board variable, because each
			// test will load different files
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayoutBadColumns.csv", "data/ClueSetup.txt");
			// Instead of initialize, we call the two load functions directly.
			// This is necessary because initialize contains a try-catch.
			board.loadSetupConfig();
			// This one should throw an exception
			board.loadLayoutConfig();
		});
	}

	// Test that an exception is thrown for a Layout file that specifies
	// a room that is not in the legend. (room "J", which is not in the legend)
	@Test
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayoutBadRoom.csv", "data/ClueSetup.txt");
			board.loadSetupConfig();
			board.loadLayoutConfig();
		});
	}

	// Test that an exception is thrown for a bad format Setup file (first line starts with "Boom", not "Room")
	@Test
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		assertThrows(BadConfigFormatException.class, () -> {
			Board board = Board.getInstance();
			board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetupBadFormat.txt");
			board.loadSetupConfig();
			board.loadLayoutConfig();
		});
	}

}

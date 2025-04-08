/*
 * Class: ClueGame
 *
 * Purpose: The ClueGame class is the main class for the Clue game. It initializes the game and sets up the GUI.
 *
 * Responsibilities: The ClueGame class is responsible for creating the main window of the game and setting up the game board.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 8, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	public ClueGame() {
		super();
	}

	public static void main(String[] args) {
		Board.getInstance().setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		Board.getInstance().initialize();
		Board.getInstance().dealCards();

		ClueGame game = new ClueGame();
		game.setSize(800, 600);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setTitle("Clue Game");
		game.setVisible(true);
	}
}
/*
 * Class: HumanPlayer
 *
 * Purpose: The HumanPlayer class extends the Player class and represents a human player in the game. It inherits properties and methods from the Player class and can be used to manage the player's actions and state during the game.
 *
 * Responsibilities: The HumanPlayer class is responsible for representing a human player in the game. It inherits properties and methods from the Player class and can be used to manage the player's actions and state during the game.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 5, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

public class HumanPlayer extends Player {
	public HumanPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}
}
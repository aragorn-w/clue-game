/*
 * Class: ComputerPlayer
 *
 * Purpose: The ComputerPlayer class extends the Player class and represents a computer-controlled player in the game of Clue. It inherits the properties and methods of the Player class and can be used to implement AI behavior for the computer player.
 *
 * Responsibilities: The ComputerPlayer class is responsible for managing the computer player's name, color, and position on the board. It can also implement AI logic for making decisions during the game.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 2, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

public class ComputerPlayer extends Player {	
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

}
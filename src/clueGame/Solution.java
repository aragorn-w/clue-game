/*
 * Class: Solution
 *
 * Purpose: The Solution class is meant to represent the solution to the game of Clue. It contains the room, person, and weapon cards that make up the solution.
 *
 * Responsibilities: The Solution class is responsible for storing the room, person, and weapon cards that make up the solution to the game of Clue. It provides methods to get and set these cards.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 1, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

public class Solution {
	private Card roomCard;
	private Card personCard;
	private Card weaponCard;

	public Solution() {
		super();
	}

	public Card getRoomCard() {
		return roomCard;
	}

	public void setRoomCard(Card roomCard) {
		this.roomCard = roomCard;
	}

	public Card getPersonCard() {
		return personCard;
	}

	public void setPersonCard(Card personCard) {
		this.personCard = personCard;
	}

	public Card getWeaponCard() {
		return weaponCard;
	}

	public void setWeaponCard(Card weaponCard) {
		this.weaponCard = weaponCard;
	}
}
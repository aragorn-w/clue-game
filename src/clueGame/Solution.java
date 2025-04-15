/*
 * Class: Solution
 *
 * Purpose: The Solution class is meant to represent the solution to the game of Clue. It contains the room, person, and weapon cards that make up the solution.
 *
 * Responsibilities: The Solution class is responsible for storing the room, person, and weapon cards that make up the solution to the game of Clue. It provides methods to get and set these cards.
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

import java.util.HashSet;
import java.util.Set;

public class Solution {
	private Card
		roomCard,
		personCard,
		weaponCard;

	public Solution() {
		super();
	}

	public Solution(Card roomCard, Card personCard, Card weaponCard) {
		super();
		this.roomCard = roomCard;
		this.personCard = personCard;
		this.weaponCard = weaponCard;
	}

	public Solution(Solution solution) {
		super();
		this.roomCard = solution.roomCard;
		this.personCard = solution.personCard;
		this.weaponCard = solution.weaponCard;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Solution)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Solution other = (Solution) obj;
		return roomCard.equals(other.roomCard)
			&& personCard.equals(other.personCard)
			&& weaponCard.equals(other.weaponCard);
	}

	@Override
	public String toString() {
		return "SOLUTION: " + roomCard + ", " + personCard + ", " + weaponCard;
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

	public Set<Card> getCardSet() {
		Set<Card> cardList = new HashSet<>();
		cardList.add(roomCard);
		cardList.add(personCard);
		cardList.add(weaponCard);
		return cardList;
	}

	public void setAnyCard(Card card) {
		switch (card.getType()) {
			case ROOM -> setRoomCard(card);
			case PERSON -> setPersonCard(card);
			case WEAPON -> setWeaponCard(card);
			default -> throw new IllegalArgumentException("Invalid card type");
		}
	}
}
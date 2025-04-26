/*
 * Class: Card
 *
 * Purpose: The Card class represents a card in the game. It contains the name of the card and its type (PERSON, WEAPON, or ROOM). The Card class is used to represent the cards in the game and is used in conjunction with the Board class to manage the game state.
 *
 * Responsibilities: The Card clas is responsible for storing the name and type of the card, as well as providing methods to access this information. The Card class also provides a method to compare two cards for equality.
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

import java.util.Objects;

public class Card {
	private String name;
	private CardType type;

	public Card(String cardName, CardType type) {
		super();
		this.name = cardName;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Card)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Card card = (Card) obj;
		return card.name.equals(name) && card.type == type;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(name, type);
	}

	@Override
	public String toString() {
		return name + " " + type;
	}

	public String getName() {
		return name;
	}
	
	public CardType getType() {
		return type;
	}
}
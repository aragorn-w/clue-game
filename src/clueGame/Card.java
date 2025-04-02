/*
 * Class: Card
 *
 * Purpose: The Card class represents a card in the game. It contains the name of the card and its type (PERSON, WEAPON, or ROOM). The Card class is used to represent the cards in the game and is used in conjunction with the Board class to manage the game state.
 *
 * Responsibilities: The Card clas is responsible for storing the name and type of the card, as well as providing methods to access this information. The Card class also provides a method to compare two cards for equality.
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

public class Card {
	private String cardName;
	private CardType type;

	public Card(String cardName, CardType type) {
		super();
		this.cardName = cardName;
		this.type = type;
	}
	
	public boolean equals(Card target) {
		return target.cardName.equals(cardName);
	}

	public String getName() {
		return cardName;
	}
	
	public CardType getType() {
		return type;
	}
}
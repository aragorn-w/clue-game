// TODO: Add class header string here (make similar to Board's header doc)

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
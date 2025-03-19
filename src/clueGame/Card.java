// TODO: Add class header string here (make similar to Board's header doc)

package clueGame;

public class Card {
	private String cardName;

	public boolean equals(Card target) {
		return target.cardName.equals(cardName);
	}
}
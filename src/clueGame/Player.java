// TODO: Add class header string here (make similar to Board's header doc)

package clueGame;

import java.util.ArrayList;
import java.awt.Color;

public abstract class Player {
	private String name;
	private Color color;

	private int row;
	private int column;

	private ArrayList<Card> cards;

	public abstract void updateHand(Card card);
}
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
	
	public Player(String name, String color, int row, int column) {
		cards = new ArrayList<Card>();
		this.name = name;
		this.row = row;
		this.column = column;
		switch(color) {
			case "Gold" -> {
				this.color = new Color(184,143,64);
			}
			case "Blue" -> {
				this.color = new Color(103,121,191);
			}
			case "Green" -> {
				this.color = new Color(112,168,69);
			}
			case "Pink" -> {
				this.color = new Color(195,92,164);
			}
			case "Teal" -> {
				this.color = new Color(137,224,2);
			}
			case "Red" -> {
				this.color = new Color(203,88,76);
			}
		}
	}

	public void updateHand(Card card) {
		cards.add(card);
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return column;
	}	
	
	public ArrayList<Card> getCards() {
		return cards;
	}
}
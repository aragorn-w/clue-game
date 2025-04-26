/*
 * Class: Player
 *
 * Purpose: The Player class represents a player in the game of Clue. It stores the player's name, color, and current position on the board. The Player class also maintains a hand of cards that the player has collected during the game.
 *
 * Responsibilities: The Player class is responsible for storing and managing the player's name, color, position, and hand of cards. It provides methods to update the player's hand and retrieve information about the player.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 14, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Player {
	private static final Color
		GOLD = new Color(184, 143, 64),
		BLUE = new Color(103, 121, 191),
		GREEN = new Color(112, 168, 69),
		PINK = new Color(195, 92, 164),
		TEAL = new Color(137, 224, 2),
		RED = new Color(203, 88, 76);

	private String name;
	private Color color;

	private int row;
	private int column;

	private Set<Card> hand;
	private Set<Card> seenCards;
	
	private boolean dragged = false;
	
	public Player(String name, String color, int row, int column) {
		super();
		this.hand = new HashSet<Card>();
		this.seenCards = new HashSet<>();
		this.name = name;
		this.row = row;
		this.column = column;
		switch (color) {
			case "Gold" -> this.color = GOLD;
			case "Blue" -> this.color = BLUE;
			case "Green" -> this.color = GREEN;
			case "Pink" -> this.color = PINK;
			case "Teal" -> this.color = TEAL;
			case "Red" -> this.color = RED;
		}
	}

	public void draw(Graphics graphics, int width, int height) {
		int pixelCol = column * width;
		int pixelRow = row * height;

		graphics.setColor(color);
		graphics.fillOval(pixelCol, pixelRow, width, height);
	}

	public boolean updateHand(Card card) {
		return hand.add(card);
	}

	public boolean removeFromHand(Card card) {
		return hand.remove(card);
	}

	public boolean updateSeen(Card seenCard) {
		return seenCards.add(seenCard);
	}

	public boolean removeFromSeen(Card card) {
		return seenCards.remove(card);
	}

	public Card disproveSuggestion(Solution suggestion) {
		Set<Card> matchingCards = new HashSet<>(hand);
		// Retain all will do a set intersection between the two sets
		matchingCards.retainAll(suggestion.getCardSet());
		List<Card> matchingCardsList = new ArrayList<>(matchingCards);
		if (!matchingCardsList.isEmpty()) {
			return matchingCardsList.get((int) (Math.random() * (matchingCardsList.size())));
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Player)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Player otherPlayer = (Player) obj;
		return name.equals(otherPlayer.name);
	}

	@Override
	public String toString() {
		return name + " " + color + " " + row + " " + column;
	}
	
	public void move(BoardCell cell) {
		Board.getInstance().getCell(row, column).setOccupied(false);
		cell.setOccupied(true);
		row = cell.getRow();
		column = cell.getCol();
	}

	public boolean isDragged() {
		return dragged;
	}

	public void setDragged(boolean dragged) {
		this.dragged = dragged;
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
	
	public int getColumn() {
		return column;
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public Card getAnyHandCard() {
		if (hand.isEmpty()) {
			return null;
		}
		return hand.iterator().next();
	}

	public Set<Card> getSeenCards() {
		return seenCards;
	}

	public Card getAnySeenCard() {
		if (seenCards.isEmpty()) {
			return null;
		}
		return seenCards.iterator().next();
	}
}
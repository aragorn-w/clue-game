/*
 * Class: ComputerPlayer
 *
 * Purpose: The ComputerPlayer class extends the Player class and represents a computer-controlled player in the game of Clue. It inherits the properties and methods of the Player class and can be used to implement AI behavior for the computer player.
 *
 * Responsibilities: The ComputerPlayer class is responsible for managing the computer player's name, color, and position on the board. It can also implement AI logic for making decisions during the game.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ComputerPlayer extends Player {	
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		List<Card> validWeaponCards = new ArrayList<>();
		List<Card> validPersonCards = new ArrayList<>();
		for (Card card: Board.getInstance().getTotalDeck()) {
			if (getSeenCards().contains(card)) {
				continue;
			}

			switch (card.getType()) {
				case WEAPON -> validWeaponCards.add(card);
				case PERSON -> validPersonCards.add(card);
				default -> {}
			}
		}

		BoardCell currentCell = Board.getInstance().getCell(getRow(), getColumn());
		Card roomCard = Board.getInstance().getRoomCard(currentCell);
		Card weaponCard = validWeaponCards.get((int) (Math.random() * validWeaponCards.size()));
		Card personCard = validPersonCards.get((int) (Math.random() * validPersonCards.size()));
		
		return new Solution(roomCard, personCard, weaponCard);
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		// If a target is in a room and the room is not in that player's seen list,
		// select the room (or if multiple rooms select randomly).
		List<BoardCell> roomTargets = new ArrayList<>();
		for (BoardCell target: targets) {
			if (target.isRoom()) {
				if (!getSeenCards().contains(Board.getInstance().getRoomCard(target))) {
					return target;
				}
				roomTargets.add(target);
			}
		}

		if (!roomTargets.isEmpty()) {
			return roomTargets.get((int) (Math.random() * roomTargets.size()));
		}

		// Otherwise, select a target randomly from the target list.
		List<BoardCell> targetList = new ArrayList<>(targets);
		return targetList.get((int) (Math.random() * targetList.size()));
	}
}
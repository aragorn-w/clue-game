/*
 * Class: ComputerPlayer
 *
 * Purpose: The ComputerPlayer class extends the Player class and represents a computer-controlled player in the game of Clue. It inherits the properties and methods of the Player class and can be used to implement AI behavior for the computer player.
 *
 * Responsibilities: The ComputerPlayer class is responsible for managing the computer player's name, color, and position on the board. It can also implement AI logic for making decisions during the game.
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 27, 2025
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
	private final static double CHOOSE_UNSEEN_CHANCE = 0.5;
	private final static double CHOOSE_SEEN_CHANCE = 0.2;
	
	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
	}

	public Solution createSuggestion() {
		Board board = Board.getInstance();
		List<Card> validWeaponCards = new ArrayList<>();
		List<Card> validPersonCards = new ArrayList<>();
		for (Card card : board.getTotalDeck()) {
			if (getSeenCards().contains(card)) {
				continue;
			}

			switch (card.getType()) {
				case WEAPON -> validWeaponCards.add(card);
				case PERSON -> validPersonCards.add(card);
				default -> {}
			}
		}

		BoardCell currentCell = board.getCell(getRow(), getColumn());
		Card roomCard = board.getRoomCard(currentCell);
		Card personCard = validPersonCards.get((int) (Math.random() * validPersonCards.size()));
		Card weaponCard = validWeaponCards.get((int) (Math.random() * validWeaponCards.size()));
		
		return new Solution(roomCard, personCard, weaponCard);
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		// If a target is in a room and the room is not in that player's seen list,
		// select the room (or if multiple rooms select randomly).
		Board board = Board.getInstance();
		List<BoardCell> roomTargets = new ArrayList<>();
		for (BoardCell target : targets) {
			if (target.isRoom()) {
				if (!getSeenCards().contains(board.getRoomCard(target)) && Math.random() < CHOOSE_UNSEEN_CHANCE) {
					return target;
				}
				roomTargets.add(target);
			}
		}

		if (!roomTargets.isEmpty() && Math.random() < CHOOSE_SEEN_CHANCE) {
			return roomTargets.get((int) (Math.random() * roomTargets.size()));
		}

		// Otherwise, select a target randomly from the target list.
		List<BoardCell> targetList = new ArrayList<>(targets);
		if (!targetList.isEmpty()) {
			return targetList.get((int) (Math.random() * targetList.size()) % targetList.size());
		}
		return board.getCell(super.getRow(), super.getColumn());
	}
	
	public int makeAccusation() {
		if (super.getSeenCards().size() == Board.getInstance().getNonAnswerCards().size()) return 1;
		return -1;
	}
}
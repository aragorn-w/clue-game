/*
 * Class: ComputerAITest
 *
 * Purpose: 
 *
 * Responsibilities: 
 *
 * Authors: Aragorn Wang, Anya Streit
 * 
 * Date Last Edited: April 5, 2025
 * 
 * Collaborators: None
 * 
 * Sources: None
 */

package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.dealCards();
	}

	// Computer player suggestion tests
	@Test
	public void testComputerSuggestion() {
		// Test that room matches current room
		ComputerPlayer computerPlayer = null;
		for (Player player : board.getPlayers()) {
			if (!player.equals(board.getHumanPlayer())) {
				computerPlayer = (ComputerPlayer) player;
				break;
			}
		}
		assertNotEquals(computerPlayer, null);
		BoardCell currentCell = board.getCell(computerPlayer.getRow(), computerPlayer.getColumn());
		Card currentRoomCard = board.getRoomCard(currentCell);
		Solution suggestion = computerPlayer.createSuggestion();
		assertEquals(currentRoomCard, suggestion.getRoomCard());

		// If only one weapon is not seen, it's selected.
		// And if only one person is not seen, it's selected
		Card firstUnseenWeapon = null;
		Card firstUnseenPerson = null;
		for (Card card : board.getTotalDeck()) {
			if (computerPlayer.getSeenCards().contains(card)) {
				continue;
			}
			// Processes unseen cards
			switch (card.getType()) {
				case WEAPON -> {
					if (firstUnseenWeapon == null) {
						firstUnseenWeapon = card;
					} else {
						computerPlayer.updateSeen(card);
					}
				}
				case PERSON -> {
					if (firstUnseenPerson == null) {
						firstUnseenPerson = card;
					} else {
						computerPlayer.updateSeen(card);
					}
				}
				default -> {}
			}
		}
		assertNotEquals(firstUnseenWeapon, null);
		assertNotEquals(firstUnseenPerson, null);
		Solution computerSuggestion = computerPlayer.createSuggestion();
		assertEquals(computerSuggestion.getWeaponCard(), firstUnseenWeapon);
		assertEquals(computerSuggestion.getPersonCard(), firstUnseenPerson);

		// If multiple weapons are not seen, one is randomly selected.
		// And if multiple people are not seen, one is randomly selected
		Set<Card> unseenWeapons = new HashSet<>();
		Set<Card> unseenPeople = new HashSet<>();
		// Add the one card of each non-room type we've already seen so far
		assertTrue(unseenWeapons.add(firstUnseenWeapon));
		assertTrue(unseenPeople.add(firstUnseenPerson));
		// Add one more card of each non-room type so that we have multiple
		// unseen cards of each non-room type to test suggestion random selection for
		Card secondUnseenWeapon = null;
		for (Card seenCard : computerPlayer.getSeenCards()) {
			if (seenCard.getType() == CardType.WEAPON) {
				secondUnseenWeapon = seenCard;
				assertTrue(computerPlayer.removeFromSeen(secondUnseenWeapon));
				break;
			}
		}
		assertTrue(unseenWeapons.add(secondUnseenWeapon));

		Card secondUnseenPerson = null;
		for (Card seenCard : computerPlayer.getSeenCards()) {
			if (seenCard.getType() == CardType.PERSON) {
				secondUnseenPerson = seenCard;
				assertTrue(computerPlayer.removeFromSeen(secondUnseenPerson));
				break;
			}
		}
		assertTrue(unseenPeople.add(secondUnseenPerson));

		// If after 999 random selections of trying to find both of the two cards
		// for each type, we don't find both, then we are extremely unlucky.
		// If this fails, run test 10 more times to see if it fails 10 times in a row.
		// If it somehow still fails 10 times in a row, then most likely our
		// implementation is the real reason for failing this test, instead of just
		// being ungodly unlucky.
		Set<Card> selectedWeapons = new HashSet<>();
		Set<Card> selectedPeople = new HashSet<>();
		for (int i = 0; i < 999; i++) {
			computerSuggestion = computerPlayer.createSuggestion();
			selectedWeapons.add(computerSuggestion.getWeaponCard());
			selectedPeople.add(computerSuggestion.getPersonCard());
		}
		assertTrue(selectedWeapons.containsAll(unseenWeapons));
		assertTrue(selectedPeople.containsAll(unseenPeople));
	}

	// Computer player selects target tests
	@Test
	public void testComputerSelectTarget() {
		ComputerPlayer computerPlayer = null;
		for (Player player : board.getPlayers()) {
			if (!player.equals(board.getHumanPlayer())) {
				computerPlayer = (ComputerPlayer) player;
				break;
			}
		}
		assertNotEquals(computerPlayer, null);

		// If no rooms in list, select random target
		Set<BoardCell> targets = new HashSet<>();
		// Add three walkways to the target list (no rooms)
		targets.addAll(Set.of(
			board.getCell(2, 0),
			board.getCell(3, 1),
			board.getCell(4, 0))
		);
		Set<BoardCell> selectedTargets = new HashSet<>();
		// Similar reasoning for the 999 random selections as in the suggestion test
		for (int i = 0; i < 999; i++) {;
			selectedTargets.add(computerPlayer.selectTarget(targets));
		}
		selectedTargets.removeAll(targets);
		assertTrue(selectedTargets.isEmpty());

		// If unseen room in list, select it
		targets.clear();
		// Add center of unseen room (Curb Room) and two walkways to the target list
		BoardCell unseenRoomCell = board.getCell(1, 2);
		Card unseenRoomCard = board.getRoomCard(unseenRoomCell);
		targets.addAll(Set.of(
			unseenRoomCell,
			board.getCell(2, 1),
			board.getCell(2, 3))
		);
		computerPlayer = null;
		for (Player player : board.getPlayers()) {
			if (!player.equals(board.getHumanPlayer())
			&& !player.getSeenCards().contains(unseenRoomCard)) {
				computerPlayer = (ComputerPlayer) player;
				break;
			}
		}
		assertNotEquals(computerPlayer, null);
		assertEquals(computerPlayer.selectTarget(targets), unseenRoomCell);

		// Otherwise, select random target (including the room(s))
		computerPlayer.updateSeen(unseenRoomCard);
		// Similar reasoning for the 999 random selections as in the suggestion test
		for (int i = 0; i < 999; i++) {
			selectedTargets.add(computerPlayer.selectTarget(targets));
		}
		selectedTargets.removeAll(targets);
		assertTrue(selectedTargets.isEmpty());
	}
}
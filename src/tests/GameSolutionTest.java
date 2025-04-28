/*
 * Class: GameSolutionTest
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

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.dealCards();
	}

	// Accusation tests
	@Test
	public void testAccusation() {
		// Test solution that is correct
		Solution testSolution = new Solution(board.getTheAnswer());
		assertTrue(board.checkAccusation(testSolution));
		
		// Test solution that has wrong person
		testSolution.setPersonCard(new Card("Wrong Person", CardType.PERSON));
		assertFalse(board.checkAccusation(testSolution));

		// Test solution that has wrong weapon
		testSolution.setWeaponCard(new Card("Wrong Weapon", CardType.WEAPON));
		assertFalse(board.checkAccusation(testSolution));

		// Test solution that has wrong room
		testSolution.setRoomCard(new Card("Wrong Room", CardType.ROOM));
		assertFalse(board.checkAccusation(testSolution));
	}

	// Player suggestion disproval tests
	@Test
	public void testDisproval() {
		// If player only has one matching card, it should be returned
		Card answerWeaponCard = board.getTheAnswer().getWeaponCard();
		for (Player player : board.getPlayers()) {
			player.updateHand(answerWeaponCard);
			assertEquals(player.disproveSuggestion(board.getTheAnswer()), answerWeaponCard);
		}

		// If any player has more than one matching card, it should return random matching card
		Card answerPersonCard = board.getTheAnswer().getPersonCard();
		Set<Card> matchingCards = new HashSet<>();
		for (Player player : board.getPlayers()) {
			player.updateHand(answerPersonCard);
			// If after 999 random selections of trying to find both of the two possible
			// disproving cards, we don't find both, then we are extremely unlucky.
			// If this fails, run test 10 more times to see if it fails 10 times in a row.
			// If it somehow still fails 10 times in a row, then most likely our
			// implementation
			// is the real reason for failing this test, instead of just being ungodly
			// unlucky.
			for (int numRandomTries = 0; numRandomTries < 999; numRandomTries++) {
				matchingCards.add(player.disproveSuggestion(board.getTheAnswer()));
			}
			assertTrue(matchingCards.contains(answerWeaponCard));
			assertTrue(matchingCards.contains(answerPersonCard));

			player.removeFromHand(answerWeaponCard);
			player.removeFromHand(answerPersonCard);
		}

		// If player has no matching cards, it should return null
		for (Player player : board.getPlayers()) {
			assertEquals(player.disproveSuggestion(board.getTheAnswer()), null);
		}
	}

	// Suggestion handling tests
	@Test
	public void testHandleSuggestion() {
		// Suggestion that no one can disprove should return null
		for (Player player : board.getPlayers()) {
			// Other players should not be able to disprove
			assertEquals(board.handleSuggestion(player, board.getTheAnswer()), null);
			// Suggesting player also should not be able to disprove
			assertEquals(player.disproveSuggestion(board.getTheAnswer()), null);
		}

		// Suggestion that only suggesting player can disprove should return null
		Solution originalAnswer = board.getTheAnswer();
		for (Player player : board.getPlayers()) {
			Solution newAnswer = new Solution(originalAnswer);
			Card handCard = player.getHand().iterator().next();
			switch (handCard.getType()) {
				case WEAPON -> newAnswer.setWeaponCard(handCard);
				case PERSON -> newAnswer.setPersonCard(handCard);
				case ROOM -> newAnswer.setRoomCard(handCard);
				default -> throw new IllegalArgumentException("Unexpected value: " + handCard.getType());
			}
			board.setTheAnswer(newAnswer);
			// Player should be able to disprove their own suggestion
			assertNotEquals(player.disproveSuggestion(newAnswer), null);
			// Other players should not be able to disprove
			assertEquals(board.handleSuggestion(player, newAnswer), null);
		}
		// Reset board's actual answer since we want to replace one of the original
		// solution's cards with a human player's hand card
		board.setTheAnswer(originalAnswer);

		// Suggestion that only human can disprove should return disproving card
		Solution humanSuggestion = new Solution(originalAnswer);
		Card handCard = board.getHumanPlayer().getHand().iterator().next();
		switch (handCard.getType()) {
			case WEAPON -> humanSuggestion.setWeaponCard(handCard);
			case PERSON -> humanSuggestion.setPersonCard(handCard);
			case ROOM -> humanSuggestion.setRoomCard(handCard);
			default -> throw new IllegalArgumentException("Unexpected value: " + handCard.getType());
		}
		// Human player should be able to disprove their own suggestion
		assertEquals(board.getHumanPlayer().disproveSuggestion(humanSuggestion), handCard);
		// When handling any computer player's suggestion, we should return the
		// disproving card since the human player (who is not suggesting) is the only one
		// who can disprove
		for (Player computerPlayer : board.getPlayers()) {
			if (!computerPlayer.equals(board.getHumanPlayer())) {
				assertEquals(board.handleSuggestion(computerPlayer, humanSuggestion), handCard);
				assertEquals(computerPlayer.disproveSuggestion(humanSuggestion), null);
			}
		}

		// Suggestion that 2 players can disprove should return first disproving player's disproving card
		Solution twoPersonSuggestion = new Solution(board.getTheAnswer());
		Player firstPlayer = board.getPlayers().get(0);
		// Use second player in list so that we can show at least one player
		// who can't disprove beforehand. AKA the second player should be the one who first disproves
		Player secondPlayer = board.getPlayers().get(1);
		Card disprovingCardOne = secondPlayer.getAnyHandCard();
		Card disprovingCardTwo = null;
		int playersProcessed = 0;
		int otherPlayerIdx = 1;
		boolean foundSecondCard = false;
		while (!foundSecondCard && playersProcessed < board.getPlayers().size() - 1) {
			otherPlayerIdx = (otherPlayerIdx + 1) % board.getPlayers().size();
			for (Card card : board.getPlayers().get(otherPlayerIdx).getHand()) {
				if (card.getType() != disprovingCardOne.getType()) {
					disprovingCardTwo = card;
					foundSecondCard = true;
					break;
				}
			}
			playersProcessed++;
		}
		// The second disproving card can not come from the first player since we want
		// to show that the turn-based suggestion disproval returns the second player's
		// disproving card, not the first nor third (or fourth or fifth or sixth) player's
		Player otherPlayer = board.getPlayers().get(otherPlayerIdx);
		assertNotEquals(otherPlayer, firstPlayer);
		assertNotEquals(disprovingCardTwo, null);
		twoPersonSuggestion.setAnyCard(disprovingCardOne);
		twoPersonSuggestion.setAnyCard(disprovingCardTwo);
		
		assertEquals(board.handleSuggestion(firstPlayer, twoPersonSuggestion), disprovingCardOne);
	}
}
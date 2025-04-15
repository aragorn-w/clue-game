/*
 * Class: CardsPanel
 *
 * Purpose: The CardsPanel class' purpose is to create a panel that displays the cards that the player has seen and the cards that are in their hand. The CardsPanel class is responsible for creating the layout of the panel, adding the cards to the panel, and updating the panel when new cards are seen.
 *
 * Responsibilities: The CardsPanel class is responsible for creating the layout of the panel, adding the cards to the panel, and updating the panel when new cards are seen. It also provides methods to add new cards to the panel and update the display of the cards.
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

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CardsPanel extends JPanel {
	private JPanel
		seenPeopleCardsPanel,
		seenRoomCardsPanel,
		seenWeaponCardsPanel;
	
	public CardsPanel() {
		super();
		setLayout(new GridLayout(3, 1));
		setBorder(BorderFactory.createTitledBorder("Known Cards"));

		// Top, middle, and bottom row panels
		JPanel peopleCardsPanel = new JPanel();
		peopleCardsPanel.setLayout(new GridLayout(0, 1));
		peopleCardsPanel.setBorder(BorderFactory.createTitledBorder("People"));
		JPanel roomCardsPanel = new JPanel();
		roomCardsPanel.setLayout(new GridLayout(0, 1));
		roomCardsPanel.setBorder(BorderFactory.createTitledBorder("Rooms"));
		JPanel weaponCardsPanel = new JPanel();
		weaponCardsPanel.setLayout(new GridLayout(0, 1));
		weaponCardsPanel.setBorder(BorderFactory.createTitledBorder("Weapons"));
		add(peopleCardsPanel);
		add(roomCardsPanel);
		add(weaponCardsPanel);

		// Top row panel's components
		addCardSetToPanel(peopleCardsPanel, true, CardType.PERSON);
		addCardSetToPanel(peopleCardsPanel, false, CardType.PERSON);

		// Middle row panel's components
		addCardSetToPanel(roomCardsPanel, true, CardType.ROOM);
		addCardSetToPanel(roomCardsPanel, false, CardType.ROOM);

		// Bottom row panel's components
		addCardSetToPanel(weaponCardsPanel, true, CardType.WEAPON);
		addCardSetToPanel(weaponCardsPanel, false, CardType.WEAPON);
	}

	private void addCardSetToPanel(JPanel panel, boolean isHand, CardType type) {
		Set<Card> cardSet = null;
		String cardSetLabel = null;
		Player humanPlayer = Board.getInstance().getHumanPlayer();
		if (isHand) {
			cardSet = humanPlayer.getHand();
			cardSetLabel = "In Hand:";
		} else {
			cardSet = humanPlayer.getSeenCards();
			cardSetLabel = "Seen:";
		}

		JPanel cardSetPanel = new JPanel();
		switch (type) {
			case PERSON -> seenPeopleCardsPanel = cardSetPanel;
			case ROOM -> seenRoomCardsPanel = cardSetPanel;
			case WEAPON -> seenWeaponCardsPanel = cardSetPanel;
			default -> throw new IllegalArgumentException("Invalid card type: " + type);
		}
		cardSetPanel.setLayout(new GridLayout(0, 1));

		cardSetPanel.add(new JLabel(cardSetLabel));
		for (Card card: getCardsOfType(cardSet, type)) {
			JTextField cardText = new JTextField(card.getName());
			cardText.setEditable(false);
			cardText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			configureSeenCardText(cardText, card);
			// Overrides the background color if the card is in the hand
			// since otherwise, for seen cards, the text field customization is
			// implemented identically as when adding a seen card after we've
			// created the CardsPanel
			if (isHand) {
				cardText.setBackground(humanPlayer.getColor());
			}
			
			cardSetPanel.add(cardText);
		}
		panel.add(cardSetPanel);
	}

	private void configureSeenCardText(JTextField cardText, Card card) {
		cardText.setEditable(false);
		cardText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		for (Player player: Board.getInstance().getPlayers()) {
			if (player.getSeenCards().contains(card)) {
				cardText.setBackground(player.getColor());
				break;
			}
		}
	}

	private Set<Card> getCardsOfType(Set<Card> cards, CardType type) {
		Set<Card> wantedCards = new HashSet<>();
		for (Card card: cards) {
			if (card.getType() == type) {
				wantedCards.add(card);
			}
		}
		return wantedCards;
	}

	public void addSeenPersonCard(Card card) {
		updatePanel(seenPeopleCardsPanel, card);
	}

	public void addSeenRoomCard(Card card) {
		updatePanel(seenRoomCardsPanel, card);
	}

	public void addSeenWeaponCard(Card card) {
		updatePanel(seenWeaponCardsPanel, card);
	}

	private void updatePanel(JPanel panel, Card card) {
		JTextField newCardText = new JTextField(card.getName());
		configureSeenCardText(newCardText, card);
		panel.add(newCardText);
		panel.revalidate();
		panel.repaint();
	}

	public static void main(String[] args) {
		Board.getInstance().setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		Board.getInstance().initialize();
		Board.getInstance().dealCards();

		JFrame frame = new JFrame();
		frame.setSize(180, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CardsPanel cardsPanel = new CardsPanel();
		frame.setContentPane(cardsPanel);
		frame.setVisible(true);

		Player humanPlayer = Board.getInstance().getHumanPlayer();
		Player computerPlayer = null;
		for (Player player : Board.getInstance().getPlayers()) {
			if (player != humanPlayer) {
				computerPlayer = player;
				break;
			}
		}
		assert computerPlayer != null;
		
		// Test updaters after human player sees new cards of different types

		Card newPersonCard = new Card("Miss Scarlet", CardType.PERSON);
		humanPlayer.updateSeen(newPersonCard);
		cardsPanel.addSeenPersonCard(newPersonCard);
		
		Card newRoomCard = new Card("Dining Room", CardType.ROOM);
		humanPlayer.updateSeen(newRoomCard);
		cardsPanel.addSeenRoomCard(newRoomCard);
		
		Card newWeaponCard = new Card("Revolver", CardType.WEAPON);
		humanPlayer.updateSeen(newWeaponCard);
		cardsPanel.addSeenWeaponCard(newWeaponCard);

		// Test updaters after a computer player sees new cards of different types

		Card computerPersonCard = new Card("Professor Plum", CardType.PERSON);
		computerPlayer.updateSeen(computerPersonCard);
		cardsPanel.addSeenPersonCard(computerPersonCard);

		Card computerRoomCard = new Card("Kitchen", CardType.ROOM);
		computerPlayer.updateSeen(computerRoomCard);
		cardsPanel.addSeenRoomCard(computerRoomCard);
		
		Card computerWeaponCard = new Card("Acid", CardType.WEAPON);
		computerPlayer.updateSeen(computerWeaponCard);
		cardsPanel.addSeenWeaponCard(computerWeaponCard);
	}
}
/*
 * Class: ClueGame
 *
 * Purpose: The ClueGame class is the main class for the Clue game. It initializes the game and sets up the GUI.
 *
 * Responsibilities: The ClueGame class is responsible for creating the main window of the game and setting up the game board.
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame {
	private static ClueGame theInstance;
	
	private static Clip moveSoundClip;
	
	private int playerTurnIndex;
	private boolean humanTurnFinished;
	
	private static final int
		GAME_WINDOW_WIDTH = 1100,
		GAME_WINDOW_HEIGHT = 700,
		CARDS_PANEL_WIDTH_PERCENT = 20,
		GAME_CONTROL_PANEL_HEIGHT_PERCENT = 16;

	private CardsPanel cardsPanel;
	
	private BoardPanel boardPanel;
	
	private GameControlPanel gameControlPanel;

	public ClueGame() {
		super();
		setLayout(new BorderLayout());

		setSize(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");

		boardPanel = new BoardPanel();
		boardPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				clickBoard(event);
			}
		});
		cardsPanel = new CardsPanel();
		cardsPanel.setPreferredSize(
			new Dimension(
				(int) (GAME_WINDOW_WIDTH * CARDS_PANEL_WIDTH_PERCENT / 100),
				getHeight()
			)
		);
		gameControlPanel = new GameControlPanel();
		gameControlPanel.setPreferredSize(
			new Dimension(
				getWidth(),
				(int) (GAME_WINDOW_HEIGHT * GAME_CONTROL_PANEL_HEIGHT_PERCENT / 100)
			)
		);

		add(boardPanel, BorderLayout.CENTER);
		add(cardsPanel, BorderLayout.EAST);
		add(gameControlPanel, BorderLayout.SOUTH);

		setVisible(true);
		
		playerTurnIndex = 0;
		humanTurnFinished = false;
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();

		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		board.initialize();
		board.dealCards();
		
		Player player = board.getHumanPlayer();

		int roll = (int) (Math.random() * 6) + 1;
		board.calcTargets(board.getCell(player.getRow(), player.getColumn()), roll);
		theInstance.gameControlPanel.setTurnText(player, roll);

		// Loop elevator music, forever, as the background music
		try {
			AudioInputStream backgroundAudioStream = AudioSystem.getAudioInputStream(
				new File("data/ElevatorMusic.wav")
			);
			Clip backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(backgroundAudioStream);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

			AudioInputStream clickToMoveAudioStream = AudioSystem.getAudioInputStream(
				new File("data/MoveSound.wav")
			);
			moveSoundClip = AudioSystem.getClip();
			moveSoundClip.open(clickToMoveAudioStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException exception) {
			System.err.println("Background music or move sound failed to load: " + exception.getMessage());
		}

		JOptionPane.showMessageDialog(
			null,
			"Welcome to Clue! You are " + board.getHumanPlayer().getName() + ".",
			"ClueGame",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	public void nextTurn() {
		if (!humanTurnFinished) {
			JOptionPane.showMessageDialog(
				null,
				"Please finish your turn.",
				"ERROR",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		
		Board board = Board.getInstance();
		
		playerTurnIndex = (playerTurnIndex + 1) % board.getPlayers().size();
		Player player = board.getPlayers().get(playerTurnIndex);
		int roll = (int) (Math.random() * 6) + 1;
		board.calcTargets(board.getCell(player.getRow(), player.getColumn()), roll);
		player.setDragged(false);
		theInstance.gameControlPanel.setTurnText(player, roll);
		if (playerTurnIndex == 0) {
			gameControlPanel.setSuggestion(null);
			humanTurnFinished = false;			
		} else {
			// If it is not human player's turn
			int accusationResult = ((ComputerPlayer) player).makeAccusation();
			if (accusationResult != -1){
				this.handleAccusation(accusationResult);
			}
			BoardCell target = ((ComputerPlayer) player).selectTarget(board.getTargets());
			player.move(target);
			if (target.isRoom()) {
				Solution suggestion = ((ComputerPlayer) player).createSuggestion();
				gameControlPanel.setSuggestion(suggestion);
				board.getPlayerFromCard(suggestion.getPersonCard()).move(target);
				board.getPlayerFromCard(suggestion.getPersonCard()).setDragged(true);
				boardPanel.repaint();
			} else {
				gameControlPanel.setSuggestion(null);
			}
		}

		boardPanel.repaint();
	}
	
	public void makeAccusation() {
		if (humanTurnFinished) {
			JOptionPane.showMessageDialog(
				null,
				"It is not your turn.",
				"ERROR",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		Board board = Board.getInstance();
		ArrayList<Card> rooms = new ArrayList<>();
		ArrayList<Card> people = new ArrayList<>();
		ArrayList<Card> weapons = new ArrayList<>();
		for (Card card : board.getNonAnswerCards()) {
			switch(card.getType()) {
				case ROOM -> rooms.add(card);
				case WEAPON -> weapons.add(card);
				case PERSON -> people.add(card);
				default -> {}
			}
		}
		for (Card card : board.getTheAnswer().getCardSet()) {
			switch(card.getType()) {
				case ROOM -> rooms.add(card);
				case WEAPON -> weapons.add(card);
				case PERSON -> people.add(card);
				default -> {}
			}
		}
		
		// Create the combo boxes
		JComboBox<Card> room = new JComboBox<>(rooms.toArray(new Card[0]));
		JComboBox<Card> person = new JComboBox<>(people.toArray(new Card[0]));
		JComboBox<Card> weapon = new JComboBox<>(weapons.toArray(new Card[0]));

		// Create a panel and add the combo boxes
		JPanel panel = new JPanel(new GridLayout(3, 2, 0, 5));
		panel.add(new JLabel("Room: "));
		panel.add(room);
		panel.add(new JLabel("Person: "));
		panel.add(person);
		panel.add(new JLabel("Weapon: "));
		panel.add(weapon);

		// Show the dialog
		int result = JOptionPane.showConfirmDialog(
			null,
			panel, 
			"Make Accusation",
			JOptionPane.OK_CANCEL_OPTION
		);

		if (result == JOptionPane.OK_OPTION) {
			Solution submitted = new Solution(
				(Card) room.getSelectedItem(),
				(Card) person.getSelectedItem(),
				(Card) weapon.getSelectedItem()
			);
			this.handleAccusation(board.checkAccusation(submitted) ? 1 : 0);
		}
	}
	
	private void handleAccusation(int accusationResult) {
		Board board = Board.getInstance();
		Player player = board.getPlayers().get(playerTurnIndex);
		if (playerTurnIndex == 0) {
			if (accusationResult == 1) {
				JOptionPane.showMessageDialog(
					null,
					"You solved the Clue Mystery!",
					"You Win!",
					JOptionPane.INFORMATION_MESSAGE
				);
			} else {
				JOptionPane.showMessageDialog(
					null,
					"That was not the correct answer. The solution was " + board.getTheAnswer(),
					"Game Over",
					JOptionPane.INFORMATION_MESSAGE
				);
			}
		} else {
			if (accusationResult == 1) {
				JOptionPane.showMessageDialog(
					null,
					player.getName() + " got the Solution! It was " + board.getTheAnswer(),
					"You Lose :(",
					JOptionPane.INFORMATION_MESSAGE
				);
			} else {
				JOptionPane.showMessageDialog(
					null,
					player.getName() + " messed up. The solution was " + board.getTheAnswer(),
					"I guess you win.",
					JOptionPane.INFORMATION_MESSAGE
				);
			}
		}
		System.exit(0);
	}

	public void clickBoard(MouseEvent event) {
		if (humanTurnFinished) {
			JOptionPane.showMessageDialog(null, "It is not your turn.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Board board = Board.getInstance();
		
		int x = event.getX();
		int y = event.getY();
		
		int col = x / (boardPanel.getWidth() / board.getNumColumns());
		int row = y / (boardPanel.getHeight() / board.getNumRows());

		BoardCell clickedCell = board.getCell(row, col);
		if (!board.getTargets().contains(clickedCell)) {
			JOptionPane.showMessageDialog(
					null,
					"Please select a valid cell.",
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		board.getHumanPlayer().move(clickedCell);
		humanTurnFinished = true;

		if (moveSoundClip != null) {
			moveSoundClip.stop();
			moveSoundClip.setFramePosition(0);
			moveSoundClip.start();
		}
		
		boardPanel.repaint();
		if (clickedCell.isRoomCenter()) {
			ArrayList<Card> people = new ArrayList<>();
			ArrayList<Card> weapons = new ArrayList<>();
			for (Card card : board.getNonAnswerCards()) {
				switch(card.getType()) {
					case WEAPON -> weapons.add(card);
					case PERSON -> people.add(card);
					default -> {}
				}
			}
			for (Card card : board.getTheAnswer().getCardSet()) {
				switch(card.getType()) {
					case WEAPON -> weapons.add(card);
					case PERSON -> people.add(card);
					default -> {}
				}
			}
			
			// Create the combo boxes
			JComboBox<Card> room = new JComboBox<>(new Card[]{board.getRoomCard(clickedCell)});
			JComboBox<Card> person = new JComboBox<>(people.toArray(new Card[0]));
			JComboBox<Card> weapon = new JComboBox<>(weapons.toArray(new Card[0]));

			// Create a panel and add the combo boxes
			JPanel panel = new JPanel(new GridLayout(3, 2, 0, 5));
			panel.add(new JLabel("Room: "));
			panel.add(room);
			panel.add(new JLabel("Person: "));
			panel.add(person);
			panel.add(new JLabel("Weapon: "));
			panel.add(weapon);

			// Show the dialog
			int result = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Make Suggestion",
				JOptionPane.OK_CANCEL_OPTION
			);

			if (result == JOptionPane.OK_OPTION) {
				Solution submitted = new Solution(
					(Card) room.getSelectedItem(),
					(Card) person.getSelectedItem(),
					(Card) weapon.getSelectedItem()
				);
				Card card = gameControlPanel.setSuggestion(submitted);
				board.getPlayerFromCard(submitted.getPersonCard()).move(clickedCell);
				board.getPlayerFromCard(submitted.getPersonCard()).setDragged(true);
				boardPanel.repaint();
				if (card != null) {
					cardsPanel.addCard(card);
				}
			}
		}
	}
	
	public static ClueGame getInstance() {
		if (theInstance == null) {
			theInstance = new ClueGame();
		}
		return theInstance;
	}

	public int getPlayerTurnIndex() {
		return playerTurnIndex;
	}
	
	public boolean getHumanTurnFinished() {
		return humanTurnFinished;
	}
}
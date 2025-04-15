/*
 * Class: ClueGame
 *
 * Purpose: The ClueGame class is the main class for the Clue game. It initializes the game and sets up the GUI.
 *
 * Responsibilities: The ClueGame class is responsible for creating the main window of the game and setting up the game board.
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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame {
	private static final int
		GAME_WINDOW_WIDTH = 1100,
		GAME_WINDOW_HEIGHT = 700,
		CARDS_PANEL_WIDTH_PERCENT = 20,
		GAME_CONTROL_PANEL_HEIGHT_PERCENT = 16;

	private JPanel
		boardPanel,
		cardsPanel,
		gameControlPanel;

	public ClueGame() {
		super();
		setLayout(new BorderLayout());

		setSize(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");

		boardPanel = new BoardPanel();
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
	}

	public static void main(String[] args) {
		Board.getInstance().setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		Board.getInstance().initialize();
		Board.getInstance().dealCards();

		ClueGame game = new ClueGame();
	}
}
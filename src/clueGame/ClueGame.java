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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame {
	
	private static ClueGame theInstance;
	
	private int playerTurnIndex;
	private boolean humanTurnFinished;
	
	private static final int
		GAME_WINDOW_WIDTH = 1100,
		GAME_WINDOW_HEIGHT = 700,
		CARDS_PANEL_WIDTH_PERCENT = 20,
		GAME_CONTROL_PANEL_HEIGHT_PERCENT = 16;

	private JPanel cardsPanel;
	
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
		Board.getInstance().setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
		Board.getInstance().initialize();
		Board.getInstance().dealCards();

		Board board = Board.getInstance();
		Player player = board.getHumanPlayer();
		int roll = (int)(Math.random() * 6) + 1;
		board.calcTargets(
			board.getCell(player.getRow(), player.getColumn()),
			roll
		);
		
		theInstance = new ClueGame();
		
		JOptionPane.showMessageDialog(null, "Welcome to Clue! You are " + Board.getInstance().getHumanPlayer().getName() + ".", "ClueGame", JOptionPane.INFORMATION_MESSAGE);
		
		
		
		
	}
	
	public void nextTurn() {
		if (!humanTurnFinished) {
			JOptionPane.showMessageDialog(null, "Please finish your turn.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Board board = Board.getInstance();
		
		playerTurnIndex = (playerTurnIndex + 1) % board.getPlayers().size();
		Player player = board.getPlayers().get(playerTurnIndex);
		int roll = (int)(Math.random() * 6) + 1;
		board.calcTargets(
			board.getCell(player.getRow(), player.getColumn()),
			roll
		);
		theInstance.gameControlPanel.setTurnText(player, roll);
		if (playerTurnIndex == 0) {
			gameControlPanel.setSuggestion(null);
			humanTurnFinished = false;			
		} else {
			// If it is not human player's turn
			((ComputerPlayer)player).doAccusation();
			BoardCell target = ((ComputerPlayer)player).selectTarget(board.getTargets());
			player.move(target);
			Solution suggestion = ((ComputerPlayer)player).createSuggestion();
			gameControlPanel.setSuggestion(suggestion);
		}

		boardPanel.repaint();
	}
	
	public void clickBoard(MouseEvent event) {
		if (humanTurnFinished) {
			JOptionPane.showMessageDialog(null, "It is not your turn.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
        int x = event.getX();
        int y = event.getY();
        
        int col = x / (boardPanel.getWidth() / Board.getInstance().getNumColumns());
        int row = y / (boardPanel.getHeight() / Board.getInstance().getNumRows());

        BoardCell clickedCell = Board.getInstance().getCell(row, col);
        if (Board.getInstance().getTargets().contains(clickedCell)) {
        	Board.getInstance().getHumanPlayer().move(clickedCell);
        	humanTurnFinished = true;
        	
        	boardPanel.repaint();
        } else {
        	JOptionPane.showMessageDialog(null, "Please select a valid cell.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
        }
	}
	
	public static ClueGame getInstance() {
		return theInstance;
	}

	public int getPlayerTurnIndex() {
		return playerTurnIndex;
	}
	
	public boolean getHumanTurnFinished() {
		return humanTurnFinished;
	}
	

	

	
	
}
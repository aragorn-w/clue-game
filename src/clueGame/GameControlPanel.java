/*
 * Class: GameControlPanel
 *
 * Purpose: The GameControlPanel class' purpose is to create a panel that displays the current player's turn, the roll of the dice, the current guess, and the result of the guess. The GameControlPanel class is responsible for creating the layout of the panel, adding the components to the panel, and updating the panel when new information is available.
 *
 * Responsibilities: The GameControlPanel class is responsible for creating the layout of the panel, adding the components to the panel, and updating the panel when new information is available. It also provides methods to set the text of the various components in the panel.
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameControlPanel extends JPanel {
	private static final int
		PLAYER_TURN_TEXT_LENGTH = 20,
		ROLL_TEXT_LENGTH = 10,
		GUESS_TEXT_LENGTH = 30,
		GUESS_RESULT_TEXT_LENGTH = 30;

	private JTextField
		playerTurnText,
		rollText,
		guessText,
		guessResultText;

	public GameControlPanel() {
		super();
		setLayout(new GridLayout(2, 0));

		// Top and bottom row panels
		JPanel turnAndActionPanel = new JPanel(new GridLayout(1, 4));
		JPanel guessStatusPanel = new JPanel(new GridLayout(0, 2));
		add(turnAndActionPanel);
		add(guessStatusPanel);
		
		// Top row panel's components
		JPanel playerTurnPanel = new JPanel();
		playerTurnPanel.setLayout(new BoxLayout(playerTurnPanel, BoxLayout.Y_AXIS));
		JPanel rollPanel = new JPanel();
		rollPanel.setLayout(new BoxLayout(rollPanel, BoxLayout.X_AXIS));
		JButton makeAccusationButton = new JButton("Make Accusation");
		makeAccusationButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		makeAccusationButton.setBackground(Color.LIGHT_GRAY);
		makeAccusationButton.setOpaque(true);
		makeAccusationButton.setBorderPainted(true);
		JButton nextTurnButton = new JButton("NEXT!");
		nextTurnButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nextTurnButton.setBackground(Color.LIGHT_GRAY);
		nextTurnButton.setOpaque(true);
		nextTurnButton.setBorderPainted(true);
		turnAndActionPanel.add(playerTurnPanel);
		turnAndActionPanel.add(rollPanel);
		turnAndActionPanel.add(makeAccusationButton);
		turnAndActionPanel.add(nextTurnButton);

		// Top row panel's leftmost component's components
		playerTurnPanel.add(new JLabel("Whose turn?"));
		playerTurnText = new JTextField(PLAYER_TURN_TEXT_LENGTH);
		playerTurnText.setEditable(false);
		playerTurnPanel.add(playerTurnText);

		// Top row panel's 2nd leftmost component's components
		rollPanel.add(new JLabel("Roll:"));
		rollText = new JTextField(ROLL_TEXT_LENGTH);
		rollText.setEditable(false);
		rollPanel.add(rollText);

		// Bottom row panel's components
		JPanel guessPanel = new JPanel(new GridLayout(1, 0));
		guessPanel.setBorder(BorderFactory.createTitledBorder("Guess"));
		JPanel guessResultPanel = new JPanel(new GridLayout(1, 0));
		guessResultPanel.setBorder(BorderFactory.createTitledBorder("Guess Result"));
		guessStatusPanel.add(guessPanel);
		guessStatusPanel.add(guessResultPanel);

		// Bottom row panel's left component's components
		guessText = new JTextField(GUESS_TEXT_LENGTH);
		guessText.setEditable(false);
		guessPanel.add(guessText);

		// Bottom row panel's right component's components
		guessResultText = new JTextField(GUESS_RESULT_TEXT_LENGTH);
		guessResultText.setEditable(false);
		guessResultPanel.add(guessResultText);
	}

	public void setTurnText(Player player, int roll) {
		playerTurnText.setText(player.getName());
		playerTurnText.setBackground(player.getColor());
		rollText.setText(String.valueOf(roll));
	}

	public void setGuessText(String guess) {
		guessText.setText(guess);
	}

	public void setGuessResultText(String string) {
		guessResultText.setText(string);
	}

	public static void main(String[] args) {
		// Overall panel
		JFrame frame = new JFrame();
		frame.setSize(750, 180);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameControlPanel gameControlPanel = new GameControlPanel();
		frame.setContentPane(gameControlPanel);
		frame.setVisible(true);

		// Test setters for panel
		gameControlPanel.setTurnText(new ComputerPlayer("Fanny Wanter", "Gold", 0, 7), 10);
		gameControlPanel.setGuessText("I have no guess!");
		gameControlPanel.setGuessResultText("So you have nothing?");
	}
}